package net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.rms.RecordStore;
import log.Log;
import screen.BrowserScreen;
import utils.ArrayList;
import zincfish.zinclib.GlobalVar;
import zincfish.zinclib.NetLib.CallBackFunction;
import zincfish.zincparser.xmlparser.ParserException;
import zincfish.zincscript.ZincScript;
import zincfish.zincwidget.SNSImageComponent;
import com.mediawoz.akebono.corerenderer.CRImage;
import data.Friend;
import data.FriendData;
import data.Group;

public class NetController {

	private static Net net = Net.getInstance();

	private Log log = Log.getLog("NetController");

	private NetController() {
		netImgTable = new Hashtable();
	}

	private static class SingletonHolder {
		static final NetController uniqueInstance = new NetController();
	}

	public static NetController getInstance() {
		return SingletonHolder.uniqueInstance;
	}

	public synchronized void processSuccessResp(Respond resp) {
		// final int INTERVAL = 300;

		// while (true) {

		// ----- 处理net响应 ----------//
		// if (!net.isEmptyRespond()) {
		// Respond resp = net.getRespond();
		int respondType = resp.ResponseState;
		try {
			// "notInstant"是在发送非即时请求时带的参数,用解包的协议处理
			if (NQ.SUCCESS == respondType) {
				if ("notInstant".equals(resp.request.getObj())) {
					handleNotInstantResp(resp);
				} else if ("PostDiary".equals(resp.request.getObj())) {
					handlePostDiary(resp);
				} else if ("UploadIMG".equals(resp.request.getObj())) {
					handleUploadIMG(resp, true);
				} else if ("FTP".equals(resp.request.getObj())) {
					processFTP(resp, true);
				} else if (isImgRep(resp)) {
					handleImg(resp, true);
				} else {

					log.debug("handleInstantResp===>" + respondType);
					// 即时请求的响应,用以前的协议处理
					handleInstantResp(resp);

				}
			} else {
				if (isImgRep(resp)) {
					log.debug("respondType===" + respondType);
					handleImg(resp, false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (isImgRep(resp)) {
				handleImg(resp, false);
			}
			// this.start();
			// continue;
		}
		// }
		// try {
		// Thread.sleep(INTERVAL);
		// } catch (Exception e) {
		// log.error(e, "run()--" + e.getMessage());
		// }
		// }

	}

	private void dealwithUtfBom(DataInputStream dis) throws IOException {
		dis.mark(10);

		byte a = dis.readByte();
		byte b = dis.readByte();
		byte c = dis.readByte();
		// contains utf bom
		if (a == (byte) 0xef && b == (byte) 0xbb && c == (byte) 0xbf) {
			log.debug("xml contains BOM!");
		} else {
			// not contains utf bom
			dis.reset();
		}
	}

	public static String friendTime = null;

	private void handleUpdateFriends(Respond resp, byte[] data) {
		String oldTime = ((String) resp.request.getObj())
				.substring("UPDATE-FRIENDS_".length());
		log.debug("RmsTime==" + oldTime);
		byte[] respDate = data;
		RecordStore rs = null;
		try {

			// 解析xml会重置数据
			FriendData.getInstance().parseXMLData(respDate);
			String netTime = FriendData.getInstance().getTime();
			log.debug("netTime==" + netTime);
			// 从网络获取的时间戳和RMS里的时间戳不一样,说明朋友列表需要更新
			if (oldTime != null && !oldTime.equals(netTime)) {
				FriendData.getInstance().saveToRMS();
				friendTime = netTime;
			} else {
				friendTime = oldTime;
				// 从RMS读取的朋友数据
				FriendData rmsFriendData = FriendData.getInstance()
						.readFromRMS();
				// 不更新,还是用原来的朋友列表
				FriendData.setInstance(rmsFriendData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BrowserScreen.getInstance().showWarningPop(
					"更新好友列表出错!" + e.getMessage());
			// TODO: handle exception
		} finally {
			try {
				if (rs != null) {
					rs.closeRecordStore();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private boolean isImgRep(Respond resp) {
		return resp.request.getObj() instanceof String
				&& ((String) resp.request.getObj()).startsWith("GetImg");
	}

	private boolean isUpdateFriends(String respStr) {
		return respStr.indexOf("operator=") < 0
				&& respStr.indexOf("frienddata") >= 0
				&& respStr.indexOf("time") >= 0;
	}

	private boolean isFriendsOpr(String respStr) {
		return respStr.indexOf("operator=") >= 0;
		// return resp.request.getObj() instanceof String
		// && ((String) resp.request.getObj()).startsWith("FRIENDS-OPR");
	}

	/**
	 * 放入所有的imgCompents,在处理图片方便定位对应的图片
	 */
	public Hashtable netImgTable;

	protected void processFTP(Respond resp, boolean isSuccess) {
		BrowserScreen.getInstance().loadUnit(resp.ResponseData, null);
	}

	/**
	 * 处理图片 上传
	 * 
	 * @param resp
	 */
	private void handleUploadIMG(Respond resp, boolean isSuccess) {

		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new ByteArrayInputStream(
					resp.ResponseData));
			byte RspStatus = dis.readByte();
			// if (RspStatus == 1) {
			// SNSLoadingComponent.loading = false;
			// }
			System.out.println("handleUploadIMG--RspStatus--->" + RspStatus);
			// for(int i = 0; i < piclist.size(); i++) {
			// short len = is.readShort();
			// byte[] url = new byte[len];
			// is.read(url);
			// System.out.println("url--->" + new String(url));
			// }
			// int msglen = is.readInt();
			// byte[] msgbt = new byte[msglen];
			// is.read(msgbt);
			// System.out.println("msgbt--->" + new String(msgbt));
		} catch (Exception e) {
			System.out.println("====handleImg 抛异常了============");
			e.printStackTrace();
		} finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理图片
	 * 
	 * @param resp
	 */
	private void handleImg(Respond resp, boolean isSuccess) {
		try {
			String imgKey = (String) resp.request.getObj();
			SNSImageComponent imgCom = (SNSImageComponent) netImgTable
					.get(imgKey);
			log.debug(isSuccess + "<---->" + imgKey
					+ "========handleImg response==========" + imgCom);
			netImgTable.remove(imgKey); // 图片组件已过期 移除该组件
			// log.debug("========handleImg response===imgCom=======" + imgKey);
			if (imgCom == null) {
				return;
			}
			if (isSuccess) {
				// log.debug(imgKey +
				// "========handleImg response=====setNetImgOK=====");
				ByteArrayInputStream bis = new ByteArrayInputStream(
						resp.ResponseData);
				CRImage img = CRImage.createImage(bis);
				imgCom.setNetImgOK(img);
			} else {
				// log.debug(imgKey +
				// "========handleImg response=====setNetImgFail=====");
				// BrowserScreen.getInstance().showWarningPop("取网络图片出错!");
				imgCom.setNetImgFail();
			}
		} catch (Exception e) {
			// System.out.println("====handleImg 抛异常了============");
			e.printStackTrace();
		}
	}

	private void handlePostDiary(Respond resp) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				resp.ResponseData));
		try {
			byte RspStatus = dis.readByte();
			if (RspStatus == 0x01) {
				// 成功
				byte DiaryContentLen = dis.readByte();
				byte[] DiaryContentUrlBytes = new byte[DiaryContentLen];
				dis.read(DiaryContentUrlBytes);
				net.addRequest(new String(DiaryContentUrlBytes, "utf-8"), null,
						false, false, true);
				log.debug("DiaryContentUrlBytes==="
						+ new String(DiaryContentUrlBytes, "utf-8"));
			} else if (RspStatus == 0x02) {
				// 对话超时过期
				byte MsgLen = dis.readByte();
				byte[] MsgConBytes = new byte[MsgLen];
				dis.read(MsgConBytes);
				BrowserScreen.getInstance().showWarningPop(
						new String(MsgConBytes, "utf-8"));
			} else if (RspStatus == 0x03) {
				// 缺少权限
				byte MsgLen = dis.readByte();
				byte[] MsgConBytes = new byte[MsgLen];
				dis.read(MsgConBytes);
				BrowserScreen.getInstance().showWarningPop(
						new String(MsgConBytes, "utf-8"));
			} else if (RspStatus == 0x04) {
				// 其他错误
				byte MsgLen = dis.readByte();
				byte[] MsgConBytes = new byte[MsgLen];
				dis.read(MsgConBytes);
				BrowserScreen.getInstance().showWarningPop(
						new String(MsgConBytes, "utf-8"));
			} else if (RspStatus == 0x0F) {
				// 未知错误
				byte MsgLen = dis.readByte();
				byte[] MsgConBytes = new byte[MsgLen];
				dis.read(MsgConBytes);
				BrowserScreen.getInstance().showWarningPop(
						new String(MsgConBytes, "utf-8"));
			}
		} catch (Exception e) {
			BrowserScreen.getInstance().showWarningPop("网络错误!");
			e.printStackTrace();
		} finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleInstantResp(Respond respond) {
		int respondType = respond.ResponseState;
		CallBackFunction callBackFuntion = null;
		ArrayList callBackParams = null;
		String callBack = null;

		if (respond.request.getObj() instanceof CallBackFunction) {
			callBackFuntion = (CallBackFunction) respond.request.getObj();
			callBack = callBackFuntion.callback;
			callBackParams = callBackFuntion.callBackParams;
		}

		log.debug("==========!net.isEmptyRespond()=====respondType======"
				+ respondType);
		DataInputStream dis = null;
		try {

			if (NQ.SUCCESS == respondType) {

				byte[] data = respond.ResponseData;
				dis = new DataInputStream(new ByteArrayInputStream(data));

				byte RspStatus = dis.readByte();
				log.debug("RspStatus==" + RspStatus);
				// 返回成功-[直接获取XML]
				if (RspStatus == 1) {

					int MsgLen = 0;

					// read length
					// 由于服务器的byte(0, 255) 是无符号的 而java客户端的byte(-128, 127)是有符号的
					int b1 = (dis.readByte() + 256) % 256; // 低位
					int b2 = (dis.readByte() + 256) % 256;
					int b3 = (dis.readByte() + 256) % 256;
					int b4 = (dis.readByte() + 256) % 256; // 高位
					// log.debug("<-->" + b1 + " " + b2 + " " + b3 + " " + b4);
					log.debug(Integer.toHexString(RspStatus) + "<-->"
							+ Integer.toHexString(b1) + " "
							+ Integer.toHexString(b2) + " "
							+ Integer.toHexString(b3) + " "
							+ Integer.toHexString(b4));
					// MsgLen = b4 <<24 + b3 <<16 + b2<<8 + b1; 错误的移位方式
					MsgLen = (b4 << 24) + (b3 << 16) + (b2 << 8) + b1;

					// deal with Utf-Bom
					dealwithUtfBom(dis); // TODO test for FTP

					byte[] MsgConBytes = new byte[MsgLen];
					dis.read(MsgConBytes);
					String cont = new String(MsgConBytes, "utf-8");

					log.debug("===MsgCon==" + cont);

					if (isUpdateFriends(cont)) {/* 好友更新 */
						handleUpdateFriends(respond, MsgConBytes);
					} else if (isFriendsOpr(cont)) {/* 好友操作 */
						handleFriendsOperator(MsgConBytes);
					} else {/* 其他,直接跳转页面 */
						BrowserScreen.getInstance().loadUnit(MsgConBytes, null);
						// 回调
						if (callBack != null) {
							if (callBackParams != null) {
								ZincScript.getZincScript().callFunction(
										callBack, callBackParams);
							} else {
								ZincScript.getZincScript()
										.executeDynamicScript(callBack);
							}

						}
					}

					return;
				}
				// 返回成功-[进一步获取XML数据的URL]
				else if (RspStatus == 2) {
					int MsgLen = dis.readByte();
					log.debug("MsgLen==" + MsgLen);
					byte[] MsgConBytes = new byte[512];
					dis.read(MsgConBytes);
					log.debug("MsgCon==" + new String(MsgConBytes, "utf-8"));
					// 再次请求,但是不要返回
					net.addRequest(new String(MsgConBytes, "utf-8"), null,
							false, false, true);
				}
				// 返回错误-[错误信息]
				else {

					int MsgLen = dis.readByte();
					log.debug("MsgLen==" + MsgLen);
					byte[] MsgConBytes = new byte[512];
					dis.read(MsgConBytes);
					log.debug("MsgCon==" + new String(MsgConBytes, "utf-8"));

					// 回调
					if (callBack != null) {
						if (callBackParams != null) {
							ZincScript.getZincScript().callFunction(callBack,
									callBackParams);
						} else {
							ZincScript.getZincScript().executeDynamicScript(
									callBack);
						}

					}
					// TODO 显示错误信息
					return;
				}

			} else {
				// TODO 提示修改不成功
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void handleNotInstantResp(Respond resp) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				resp.ResponseData));
		try {
			byte SegNum = dis.readByte();
			log.debug("SegNum==" + SegNum);

			for (int i = 0; i < SegNum; i++) {
				short SegFormat = dis.readShort();
				log.debug("SegFormat==" + SegFormat);

				if (SegFormat == 0x4001) {
					// XML
					handleXML(dis);
				} else if (SegFormat == 0x4002) {
					// 取XML的URL
					handleXmlUrl(dis);
				} else if (SegFormat == 0x4003) {
					// 提示信息
					handleHint(dis);
				} else if (SegFormat == 0x4004) {
					// 消息
					handleMsg(dis);
				} else if (SegFormat == 0x4005) {
					// 操作结果返回
					// 一个心跳包中进行了多个操作，将这些操作的结果放到下面的格式中。客户端将这些信息提示给用户。一个该格式的包体，对应一个操作。
					handleRespStatus(dis);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleRespStatus(DataInputStream dis) throws IOException {
		byte Success = dis.readByte();
		log.debug("Success==" + Success);

		short NameLen = dis.readShort();
		byte[] NameBytes = new byte[NameLen];
		dis.read(NameBytes);
		String Name = new String(NameBytes, "utf-8");
		log.debug("Name==" + Name);

		short InfoLen = dis.readShort();
		byte[] InfoBytes = new byte[InfoLen];
		dis.read(InfoBytes);
		String Info = new String(InfoBytes, "utf-8");
		log.debug("Info==" + Info);
		// 返回成功
		if (Success == 1) {

		}
		// 返回失败
		else if (Success == 0) {
			// TODO 提示,重试
		}
	}

	private void handleMsg(DataInputStream dis) throws IOException {
		// 存储在内存中 {@link config.GlobalVar}

		// 新消息总数
		short TotleNum = dis.readShort();
		log.debug("TotleNum==" + TotleNum);
		// 新短信息数目
		short RecNum = dis.readShort();
		GlobalVar.short_msg = RecNum;
		log.debug("RecNum==" + RecNum);
		// 新系统消息数目
		short SysNum = dis.readShort();
		GlobalVar.sys_msg = SysNum;
		log.debug("SysNum==" + SysNum);
		// 新留言板/回复数目
		short wordNum = dis.readShort();
		GlobalVar.leave_words = wordNum;
		log.debug("wordNum==" + wordNum);
		// 新留言板回复数新目
		// short wordhfNum = dis.readShort();
		// log.debug("wordhfNum==" + wordhfNum);
		// 新评论/回复数目
		short CommentNum = dis.readShort();
		GlobalVar.comment = CommentNum;
		log.debug("CommentNUm==" + CommentNum);
		// 新评论回复数目
		// short CommenthfNum = dis.readShort();
		// log.debug("CommenthfNum==" + CommenthfNum);

		// TODO 在UI更新消息,提醒
	}

	private void handleHint(DataInputStream dis) throws IOException {
		// 提示信息的长度
		int HintLen = dis.readInt();
		log.debug("HintLen==" + HintLen);

		byte[] MsgConBytes = new byte[HintLen];
		// 提示信息的内容
		dis.read(MsgConBytes);
		String MsgCon = new String(MsgConBytes, "utf-8");
		log.debug("MsgCon==" + MsgCon);
		BrowserScreen.getInstance().showWarningPop(MsgCon);
	}

	private void handleXmlUrl(DataInputStream dis) throws IOException {
		// url的长度
		int URLLen = dis.readInt();
		log.debug("URLLen==" + URLLen);
		// url的内容
		byte[] MsgConBytes = new byte[URLLen];
		dis.read(MsgConBytes);
		log.debug("MsgCon==" + new String(MsgConBytes, "utf-8"));
		// 再次请求,但是不要返回
		net.addRequest(new String(MsgConBytes, "utf-8"), null, false, false,
				true);
	}

	private void handleXML(DataInputStream dis) throws IOException {
		// Xml的长度
		int XmlLen = dis.readInt();
		log.debug("XmlLen==" + XmlLen);
		// dealwithUtfBom(dis);
		// Xml的内容
		byte[] MsgConBytes = new byte[1024 * 4];
		dis.read(MsgConBytes);
		String msgCon = new String(MsgConBytes, "utf-8");
		log.debug("MsgCon==" + msgCon);
		if (msgCon.startsWith("<frienddata")) {/* 好友操作,不加载页面,而是更新好友数据 */
			handleFriendsOperator(MsgConBytes);
		} else {
			// 加载页面
			BrowserScreen.getInstance().loadUnit(MsgConBytes, null);
		}

	}

	private void handleFriendsOperator(byte[] MsgConBytes) throws IOException {
		String msgCon = new String(MsgConBytes, "utf-8");
		log.debug("MsgCon==" + msgCon);
		if (msgCon.indexOf("id=\"errpage_body\"") >= 0) {
			// 跳到错误页面
			BrowserScreen.getInstance().loadUnit(MsgConBytes, null);
		} else {
			// 因为重新解析xml会更新内存中的好友数据,所以备份一份
			FriendData backupFriData = FriendData.getInstance().deepCopy();
			ArrayList bFList = backupFriData.frienddatalist;
			ArrayList bGList = backupFriData.groupdatalist;

			// 处理好友操作

			try {
				FriendData.getInstance().parseXMLData(MsgConBytes);

			} catch (ParserException e) {
				e.printStackTrace();
				BrowserScreen.getInstance().showWarningPop(
						"保存好友列表出错:" + e.getMessage());
			}
			FriendData oprData = FriendData.getInstance();

			ArrayList oFList = oprData.frienddatalist;
			ArrayList oGList = oprData.groupdatalist;
			//
			for (int i = 0; i < oFList.size(); i++) {
				Friend friend = (Friend) oFList.get(i);
				log.debug("operator friend:" + friend.getOperator() + "===mid:"
						+ friend.getMid());
				if ("del".equals(friend.getOperator())) {

					// TODO 测试一下,貌似还删不掉
					for (int j = i; j < bFList.size(); j++) {
						if (bFList.get(j).equals(friend)) {
							log.debug("removing friend:" + friend.getMid());
							bFList.remove(bFList.get(j));
						}
					}
				} else if ("add".equals(friend.getOperator())) {// TODO
					// 字符串"add"?
					friend.setOperator(null);
					bFList.add(friend);
				} else if ("move".equals(friend.getOperator())) {// TODO
					// 字符串"move"?

				}
			}

			for (int i = 0; i < oGList.size(); i++) {
				Group group = (Group) oGList.get(i);
				if ("del".equals(group.getOperator())) {
					for (int j = i; j < bGList.size(); j++) {
						if (bGList.get(j).equals(oGList.get(i))) {
							bGList.remove(bGList.get(j));
						}
					}
				} else if ("add".equals(group.getOperator())) {// TODO 字符串"add"?
					group.setOperator(null);
					bGList.add(group);
				} else if ("move".equals(group.getOperator())) {// TODO
					// 字符串"move"?

				}
			}
			// reset friends data from back up data
			FriendData.setInstance(backupFriData);

			// 保存
			try {
				FriendData.getInstance().saveToRMS();
			} catch (Exception e) {
				e.printStackTrace();
				BrowserScreen.getInstance().showWarningPop(
						"保存好友列表出错:" + e.getMessage());
			}
		}

	}

}
