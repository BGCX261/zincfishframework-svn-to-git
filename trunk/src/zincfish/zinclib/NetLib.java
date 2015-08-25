package zincfish.zinclib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.rms.RecordStore;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import log.Log;
import net.NQ;
import net.Net;
import net.NetController;
import net.Respond;
import screen.BrowserScreen;
import utils.ArrayList;
import utils.DOMUtil;
import utils.StringUtil;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincscript.Array;
import zincfish.zincscript.ZSException;
import zincfish.zincscript.ZincScript;
import data.Friend;
import data.FriendData;
import data.Group;
import data.UploadIMG;

public class NetLib extends AbstractLib {
	private static final int FUNCTION_NUM = 1;

	private static final String SEND = "_zsnSend";
	private static final byte SEND_CODE = 1;
	private static final String POP = "_zsnPopDlg";
	private static final byte POP_CODE = 4;
	private static final String DELETE_CURRENT_DOM = "_zsnDeleteCurrentDom";
	private static final byte DELETE_CURRENT_DOM_CODE = 5;
	private static final String OPEN_DRAFT = "_zsnOpenDraft";
	private static final byte OPEN_DRAFT_CODE = 6;

	private static final String WAP_REG = "_zsnRegister";
	private static final byte WAP_REG_CODE = 100;
	private static final String MODIFY_PSW = "_zsnChangePwd";
	private static final byte MODIFY_PSW_CODE = 101;
	private static final String MMS_REG = "_zsnMMSReg";
	private static final byte MMS_REG_CODE = 102;
	private static final String LOGIN = "_zsnLogin";
	private static final byte LOGIN_CODE = 103;
	private static final String SAVA_INFO = "_zsnSaveInfo";
	private static final byte SAVA_INFO_CODE = 104;
	private static final String GET_GUID = "_zsnGetGuid";
	private static final byte GET_GUID_CODE = 105;

	private static final String GET_PHONE_NUM = "_zsnGetPhoneNum";
	private static final byte GET_PHONE_NUM_CODE = 120;
	private static final String GET_PWD = "_zsnGetPwd";
	private static final byte GET_PWD_CODE = 121;
	private static final String GET_LOGIN = "_zsnGetloginStatus";
	private static final byte GET_LOGIN_CODE = 122;
	private static final String GET_REMENBER_NUM = "_zsnPwdRemberStatus";
	private static final byte GET_REMENBER_NUM_CODE = 123;

	private static final String SHOW_CONTENT = "_zsnShowcontent";
	private static final byte SHOW_CONTENT_CODE = 106;
	private static final String SEND_DATA = "_zsnSendData";
	private static final byte SEND_DATA_CODE = 107;
	private static final String GET_STATUS = "_zsnGetLoginSuccessStatus";
	private static final byte GET_STATUS_CODE = 108;
	private static final String LOGIN_FAILUER_INFO = "_zsnLoginFailureInfo";
	private static final byte LOGIN_FAILUER_INFO_CODE = 109;
	private static final String SEND_LATER = "_zsnSendLater";
	private static final byte SEND_LATER_CODE = 110;
	private static final String POST_DIARY = "_zsnPostDiary";
	private static final byte POST_DIARY_CODE = 111;

	private static final String POST_COMMENT = "_zsnPostComment";
	private static final byte POST_COMMENT_CODE = 51;
	private static final String GET_SIDS = "_zsnGetSids";
	private static final byte GET_SIDS_CODE = 52;
	private static final String GET_MIDS = "_zsnGetMids";
	private static final byte GET_MIDS_CODE = 53;
	private static final String GET_FRINDS_LIST = "_zsnGetFriendsList";
	private static final byte GET_FRINDS_LIST_CODE = 54;
	private static final String GET_GROUP_LIST = "_zsnGetGroupsList";
	private static final byte GET_GROUP_LIST_CODE = 55;
	private static final String GET_FRINDS_LIST_SIZE = "_zsnGetFriendsListSize";
	private static final byte GET_FRINDS_LIST_SIZE_CODE = 56;
	private static final String UPDATA_FRIENDS_LIST = "_zsnUpdateFriendsList";
	private static final byte UPDATA_FRIENDS_LIST_CODE = 57;
	private static final String GET_GROUP_LIST_SIZE = "_zsnGetGroupsListSize";
	private static final byte GET_GROUP_LIST_SIZE_CODE = 58;
	private static final String GET_FRIEND_TIME = "_zsnGetFriendTime";
	private static final byte GET_FRIEND_TIME_CODE = 59;
	private static final String SEND_NOW = "_zsnSendNow";
	private static final byte SEND_NOW_CODE = 60;
	private static final String OPEN_CUR_NODE = "_zsnOpenCurNode";
	private static final byte OPEN_CUR_NODE_CODE = 61;
	private static final String SELETE_PHOTO = "_zsnSeletePhoto";
	private static final byte SELETE_PHOTO_CODE = 62;
	private static final String POST_PHOTO = "_zsnPostPhoto";
	private static final byte POST_PHOTO_CODE = 63;
	private static final String STOP_POST_PHOTO = "_zsnStopPostPhoto";
	private static final byte STOP_POST_PHOTO_CODE = 64;
	private static final String ENTRY_CAMERA_SCREEN = "_zsnEntryCameraScreen";
	private static final byte ENTRY_CAMERA_SCREEN_CODE = 65;

	// FTP 测试
	private static final String FTP_SEND = "_zsnFTPSend";
	private static final byte FTP_SEND_CODE = 0x71;

	private static Log log = Log.getLog("NetLib");

	/**
	 * 所有库都采用单例模式，确保内存中已有一份库的实例
	 */
	private static NetLib instance = null;

	private Net net = null;

	public static NetLib getInstance() {
		if (instance == null) {
			instance = new NetLib();
			instance.createFunctionMap();
		}
		return instance;
	}

	private NetLib() {
		super();
		net = Net.getInstance();
	}

	public Object callFunction(String name, ArrayList params)
			throws ZSException {
		Byte code = (Byte) functionMap.get(name);
		log.debug("zsn--callFunction===>" + name + "==params size=="
				+ params.size());
		if (code != null) {
			switch (code.byteValue()) {
			case SHOW_CONTENT_CODE:
				sendURL(params, 0);
				return null;
			case SEND_CODE:
				sendURL(params, 1);
				return null;
			case FTP_SEND_CODE:
				_zsnFTPSend(params);
				return null;
			case SEND_DATA_CODE:
				sendURL(params, 2);
				return null;
			case SEND_LATER_CODE:
				sendURL(params, 3);
				return null;
			case POP_CODE:
				_zsnPopDlg(params);
				return null;
			case DELETE_CURRENT_DOM_CODE:
				// _zsnDeleteCurrentDom(params);
				return null;
			case OPEN_DRAFT_CODE:
				_zsnOpenDraft(params);
				return null;
			case WAP_REG_CODE:
				_zsnWapReg(params);
				return null;
			case MODIFY_PSW_CODE:
				_zsnModifyPassword(params);
			case MMS_REG_CODE:
				_zsnMmsReg(params);
				return null;
			case LOGIN_CODE:
				_zsnLogin(params);
				return null;
			case SAVA_INFO_CODE:
				_zsnSaveInfo(params);
				return null;
			case GET_GUID_CODE:
				return _zsnGetGuid();
			case GET_PHONE_NUM_CODE:
				return _zsnGetPhoneNum();
			case GET_PWD_CODE:
				return _zsnGetPwd();
			case GET_FRINDS_LIST_CODE:
				return _zsnGetFriendsList(params);
			case GET_FRINDS_LIST_SIZE_CODE:
				return _zsnGetFriendsListSize(params);
			case GET_GROUP_LIST_SIZE_CODE:
				return _zsnGetGroupListSize();
			case GET_GROUP_LIST_CODE:
				return _zsnGetGroupsList();
			case UPDATA_FRIENDS_LIST_CODE:
				_zsnUpdateFriendsList();
				return null;
			case GET_LOGIN_CODE:
				return _zsnGetloginStatus();
			case GET_REMENBER_NUM_CODE:
				return _zsnGetRemberStatus();
			case GET_STATUS_CODE:
				return _zsnGetLoginSuccessStatus();
			case LOGIN_FAILUER_INFO_CODE:
				return _zsnGetLoginFailureInfo();
			case GET_SIDS_CODE:
				return _zsnGetSids();
			case GET_MIDS_CODE:
				return _zsnGetMids();
			case GET_FRIEND_TIME_CODE:
				return _zsnGetFriendTime();
			case POST_DIARY_CODE:
				_zsnPostDiary(params);
				return null;
			case POST_COMMENT_CODE:
				_zsnPostComment(params);
				return null;
			case SEND_NOW_CODE:
				_zsnSendNow(params);
				return null;
			case SELETE_PHOTO_CODE:
				_zsnSeletePhoto(params);
				return null;
			case POST_PHOTO_CODE:
				_zsnPostPhoto(params);
				return null;
			case STOP_POST_PHOTO_CODE:
				_zsnStopPostPhoto(params);
				return null;
			case ENTRY_CAMERA_SCREEN_CODE:
				_zsnEntryCameraScreen(params);
				return null;
			}
			return null;
		} else {
			throw new ZSException("函数" + name + "不存在");
		}
	}

	private String _zsnGetSids() {
		// TODO Auto-generated method stub
		return null;
	}

	private String _zsnGetMids() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 立即发送网络数据
	 * 
	 * @param params
	 */
	private void _zsnSendNow(ArrayList params) {

	}

	/**
	 * @param params
	 */
	private void _zsnSeletePhoto(ArrayList params) {
		if (params == null || params.size() == 0) {
			return;
		}
		String textfieldid = (String) params.get(0);
		BrowserScreen.getInstance().loadUnit("/widgets/album/upload_local.xml",
				null);
		AbstractSNSDOM filedom = DOMUtil.getDOMByID(BrowserScreen.getInstance()
				.getBuffer().getCurrentBuffer(), "localfile");
		if (filedom != null) {
			filedom.setSubAttributeValue("domid", textfieldid);
			filedom.setSubAttributeValue("domattr", "value");
			filedom.setSubAttributeValue("unitid", "AlbumUpload_unit");
		}
	}

	/**
	 * @param params
	 */
	private void _zsnEntryCameraScreen(ArrayList params) {
		// String snapcallback = (String) params.get(0);
		// BrowserScreen.getInstance().loadUnit("/widgets/album/album_camera.xml",
		// null);
		// AbstractDOM cameradom =
		// DOMUtil.getDOMByID(BrowserScreen.getInstance().getBuffer()
		// .getCurrentBuffer(), "localfile");
		// if(cameradom != null) {
		// cameradom.setSubAttributeValue("domid", "phtext1");
		// cameradom.setSubAttributeValue("domattr", "value");
		// cameradom.setSubAttributeValue("unitid", "album_unit");
		// }

		// SNSMIDlet.display.setCurrent(nextDisplayable);
		// FileDOM filedom = new FileDOM();
		// filedom.domid = "phtext1";
		// filedom.domattr = "value";
		// filedom.unitid = "album_unit";
		// CRDisplay.getLcduiDisplayable()
		// SNSMIDlet.DISPLAY.setCurrent(new CameraCanvas(filedom));
	}

	/**
	 * @param params
	 */
	private void _zsnStopPostPhoto(ArrayList params) {
		if (params == null || params.size() < 2) {
			return;
		}
	}

	/**
	 * @param params
	 */
	private void _zsnPostPhoto(ArrayList params) {
		if (params == null || params.size() < 2) {
			return;
		}
		String url = (String) params.get(0);
		String callback = (String) params.get(1);
		int length = 2;
		ArrayList piclist = new ArrayList(); // 存储图片信息列表
		while (params.size() >= length + 5) {
			String picpath = (String) params.get(length); // 路径
			String picname = (String) params.get(length + 1); // 名称
			String picdescribe = (String) params.get(length + 2); // 描述
			int picalbum = StringUtil.Str2Int((String) params.get(length + 3)); // 专辑
			String picfri = (String) params.get(length + 4); // 好友
			length += 5;
			if (picpath != null && !picpath.trim().equals("")) {
				UploadIMG upload = new UploadIMG();
				upload.picpath = picpath;
				upload.picname = picname;
				upload.picdescribe = picdescribe;
				upload.picalbum = picalbum;
				upload.picfri = picfri;
				piclist.add(upload);
			}
		}
		if (piclist.size() > 0) {
			new Thread(new NetUploadIMG(url, callback, piclist)).start();
		}
	}

	private class NetUploadIMG implements Runnable {
		ArrayList piclist;
		String url;
		String callback;

		public NetUploadIMG(String url, String callback, ArrayList piclist) {
			this.piclist = piclist;
			this.url = url;
			this.callback = callback;
		}

		public byte[] getBytes(int len) {
			byte[] by = new byte[4];
			by[3] = (byte) (len - ((len >> 8) << 8));
			by[2] = (byte) (len >> 8 - ((len >> 16) << 8));
			by[1] = (byte) (len >> 16 - ((len >> 24) << 8));
			by[0] = (byte) (len >> 24);
			return by;
		}

		// 加载本地图片
		public void run() {
			try {
				byte upCode = 0x01;
				if (piclist == null || piclist.size() == 0) {
					return;
				}
				// 如果url没有guid参数,则自动加上guid
				if (url.indexOf("guid=") < 0) {
					url += ((url.indexOf("?") < 0 ? "?" : "&") + "guid=" + guid);
				}
				System.out.println("<------------->" + url);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] header = new byte[3];
				header[0] = upCode; // 第一种方式上传
				header[1] = 0x01; // 0x01 包含图片数据 0x02 不包含图片数据
				header[2] = (byte) piclist.size(); // 图片数量
				baos.write(header);
				for (int i = 0; i < piclist.size(); i++) {
					UploadIMG upload = (UploadIMG) piclist.get(i);
					if (upload.picname != null && upload.picname.length() > 0) {
						baos
								.write(getBytes(upload.picname
										.getBytes("UTF-8").length));
						baos.write(upload.picname.getBytes("UTF-8"));
					} else {
						baos.write(getBytes(0x0));
					}
					System.out.println(upload.picpath + "<--->"
							+ (upload.getPicname().getBytes().length)
							+ "<-getPicname->" + upload.getPicname() + "<-|->");
					if (upload.picdescribe != null
							&& upload.picdescribe.length() > 0) {
						baos.write(getBytes(upload.picdescribe
								.getBytes("UTF-8").length));
						baos.write(upload.picdescribe.getBytes("UTF-8"));
					} else {
						baos.write(getBytes(0x0));
					}
					baos.write(getBytes(upload.picalbum));
					if (upload.picfri != null && upload.picfri.length() > 0) {
						baos
								.write(getBytes(upload.picfri.getBytes("UTF-8").length));
						baos.write(upload.picfri.getBytes("UTF-8"));
					} else {
						baos.write(getBytes(0x0));
					}
					FileConnection filecon = (FileConnection) Connector.open(
							upload.picpath, Connector.READ_WRITE);
					DataInputStream dis = filecon.openDataInputStream();
					ByteArrayOutputStream fbaos = new ByteArrayOutputStream();
					byte[] bt = new byte[1024];
					int len = 0;
					while ((len = dis.read(bt)) > -1) {
						fbaos.write(bt, 0, len);
					}
					bt = fbaos.toByteArray();
					fbaos.close();
					dis.close();
					filecon.close();
					System.out.println(upload.picpath + "<-IMG byte size--->"
							+ bt.length);
					baos.write(getBytes(bt.length));
					baos.write(bt);
				}
				header = baos.toByteArray();
				baos.close();
				// net.addRequest(url, header, "UploadIMG",
				// false, false, true);

				HttpConnection httpcon = (HttpConnection) Connector.open(url,
						Connector.READ_WRITE);// .open(url);
				httpcon.setRequestProperty("User-Agent",
						"Profile/MIDP-2.0 Configuration/CLDC-1.0");

				httpcon.setRequestMethod(HttpConnection.POST);
				OutputStream dos = httpcon.openOutputStream();
				dos.write(header);
				System.out.println("<--响应代码:-->" + httpcon.getResponseCode());
				DataInputStream is = httpcon.openDataInputStream();
				byte RspStatus = is.readByte();
				if (RspStatus == 1) {
					//SNSLoadingComponent.loading = false;
				}
				System.out
						.println(piclist.size() + "RspStatus--->" + RspStatus);

				if (upCode != 0x01) {
					for (int i = 0; i < piclist.size(); i++) {
						int length = 0;
						int b1 = (is.readByte() + 256) % 256; // 低位
						int b2 = (is.readByte() + 256) % 256;
						length = (b2 << 8) + b1;
						byte[] url = new byte[length];
						is.read(url);
						System.out.println("url--->" + new String(url));
					}
				}
				System.out.println("--开始读返回信息---");
				int msglen = 0;
				int b1 = (is.readByte() + 256) % 256; // 低位
				int b2 = (is.readByte() + 256) % 256;
				int b3 = (is.readByte() + 256) % 256;
				int b4 = (is.readByte() + 256) % 256; // 高位
				System.out.println(b1 + " " + b2 + " " + b3 + " " + b4);
				msglen = (b4 << 24) + (b3 << 16) + (b2 << 8) + b1;
				byte[] msgbt = new byte[msglen];
				System.out.println("msglen--->" + msglen);
				is.read(msgbt);
				System.out.println("msgbt--->" + new String(msgbt));
				httpcon.close();
				is.close();
				ZincScript.getZincScript().callFunction(callback,
						new ArrayList());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 发表评论
	 * 
	 * @param params
	 *            <li>[0]Sids:日记ID <li>[0]Mids:该日记隶属的用户的mids,加密后的用户手机号 <li>
	 *            [0]Contents:评论内容
	 */
	private void _zsnPostComment(ArrayList params) {
		if (params != null && params.size() < 3) {
			return;
		}

		String Sids = (String) params.get(0);
		String Mids = (String) params.get(1);
		String Contents = (String) params.get(2);
		final String URL = "http://test.3g.cn/sns/Interface/CommentSend.aspx?zujian=2&sids="
				+ Sids + "&mids=" + Mids + "&contents=" + Contents;
		net.addRequest(URL, null, false, false, false);
	}

	/**
	 * <code>_zsnGetFriendsList</code>联网更新好友列表的数据,保存在本地.
	 */
	private void _zsnUpdateFriendsList() {
		final String URL = "http://test.3g.cn/sns/interface/FriList.aspx?guid=13560237826&time=";

		// 打开RMS,查询时间戳
		try {

			RecordStore rs = RecordStore.openRecordStore(FriendData.RMS_NAME,
					true);
			if (rs != null && rs.getNumRecords() > 0) {
				FriendData rmsFData = FriendData.getInstance().readFromRMS();
				String time = rmsFData.getTime();
				net.addRequest(URL + time, "UPDATE-FRIENDS_" + time, false,
						false, true);
			} else {
				// RMS中没有数据,说明是第一次更新,用当前时间作为时间戳
				net.addRequest(URL + getCurTime(), "UPDATE-FRIENDS_"
						+ getCurTime(), false, false, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getCurTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String date = calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.MONDAY);
		String time = calendar.get(Calendar.HOUR) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND);
		return date + time;
	}

	private Array _zsnGetFriendsList(ArrayList prams) {
		String groupID = "0";
		if (prams != null && prams.size() >= 1) {
			groupID = (String) prams.get(0);
		}

		Array temp = new Array(2);
		FriendData fData = FriendData.getInstance();
		ArrayList fList = fData.getFriendByGroup(groupID);
		log.debug("fList.size()=====================" + fList.size());
		for (int i = 0; i < fList.size(); i++) {
			Friend friend = (Friend) fList.get(i);
			// 第一维
			final Integer dimemtion1 = new Integer(i);
			// 朋友MID
			temp.setValue(new ArrayList() {
				{
					add(dimemtion1);
					add(new Integer(0));
				}
			}, friend.getMid());
			// 朋友Name
			temp.setValue(new ArrayList() {
				{
					add(dimemtion1);
					add(new Integer(1));
				}
			}, friend.getName());
			// 朋友Group
			temp.setValue(new ArrayList() {
				{
					add(dimemtion1);
					add(new Integer(2));
				}
			}, friend.getGroup());
			// 朋友Description
			temp.setValue(new ArrayList() {
				{
					add(dimemtion1);
					add(new Integer(3));
				}
			}, friend.getDescript());
		}

		return temp;
	}

	private Array _zsnGetGroupsList() {
		Array temp = new Array(2);
		FriendData gData = FriendData.getInstance();
		ArrayList fList = gData.getGroupdatalist();

		for (int i = 0; i < fList.size(); i++) {
			Group group = (Group) fList.get(i);
			// 第一维
			final Integer dimemtion1 = new Integer(i);
			// 组ID
			temp.setValue(new ArrayList() {
				{
					add(dimemtion1);
					add(new Integer(0));
				}
			}, group.getId());
			// 组Name
			temp.setValue(new ArrayList() {
				{
					add(dimemtion1);
					add(new Integer(1));
				}
			}, group.getName());
			// 朋友Description
			temp.setValue(new ArrayList() {
				{
					add(dimemtion1);
					add(new Integer(2));
				}
			}, group.getDescript());
		}

		return temp;
	}

	private Integer _zsnGetFriendsListSize(ArrayList params) {
		String groupID = "0";
		if (params != null && params.size() >= 1) {
			groupID = (String) params.get(0);
		}

		if (FriendData.getInstance() != null) {
			return new Integer(FriendData.getInstance().getFriendByGroup(
					groupID).size());
		}
		return new Integer(0);
	}

	private Integer _zsnGetGroupListSize() {
		if (FriendData.getInstance() != null) {
			return new Integer(FriendData.getInstance().getGroupdatalist()
					.size());
		}
		return new Integer(0);
	}

	protected void createFunctionMap() {
		if (functionMap == null)
			functionMap = new Hashtable(FUNCTION_NUM);
		functionMap.put(SEND, new Byte(SEND_CODE));
		functionMap.put(SHOW_CONTENT, new Byte(SHOW_CONTENT_CODE));
		functionMap.put(SEND_DATA, new Byte(SEND_DATA_CODE));
		functionMap.put(POP, new Byte(POP_CODE));
		functionMap.put(DELETE_CURRENT_DOM, new Byte(DELETE_CURRENT_DOM_CODE));
		functionMap.put(OPEN_DRAFT, new Byte(OPEN_DRAFT_CODE));
		functionMap.put(WAP_REG, new Byte(WAP_REG_CODE));
		functionMap.put(MODIFY_PSW, new Byte(MODIFY_PSW_CODE));
		functionMap.put(MMS_REG, new Byte(MMS_REG_CODE));
		functionMap.put(LOGIN, new Byte(LOGIN_CODE));
		functionMap.put(SAVA_INFO, new Byte(SAVA_INFO_CODE));
		functionMap.put(GET_GUID, new Byte(GET_GUID_CODE));
		functionMap.put(GET_PHONE_NUM, new Byte(GET_PHONE_NUM_CODE));
		functionMap.put(GET_PWD, new Byte(GET_PWD_CODE));
		functionMap.put(GET_LOGIN, new Byte(GET_LOGIN_CODE));
		functionMap.put(GET_REMENBER_NUM, new Byte(GET_REMENBER_NUM_CODE));
		functionMap.put(GET_STATUS, new Byte(GET_STATUS_CODE));
		functionMap.put(LOGIN_FAILUER_INFO, new Byte(LOGIN_FAILUER_INFO_CODE));
		functionMap.put(SEND_LATER, new Byte(SEND_LATER_CODE));
		functionMap.put(POST_DIARY, new Byte(POST_DIARY_CODE));
		functionMap.put(POST_COMMENT, new Byte(POST_COMMENT_CODE));
		functionMap.put(GET_SIDS, new Byte(GET_SIDS_CODE));
		functionMap.put(GET_MIDS, new Byte(GET_MIDS_CODE));
		functionMap.put(GET_FRINDS_LIST, new Byte(GET_FRINDS_LIST_CODE));
		functionMap.put(GET_FRINDS_LIST_SIZE, new Byte(
				GET_FRINDS_LIST_SIZE_CODE));
		functionMap
				.put(UPDATA_FRIENDS_LIST, new Byte(UPDATA_FRIENDS_LIST_CODE));
		functionMap.put(GET_GROUP_LIST, new Byte(GET_GROUP_LIST_CODE));
		functionMap
				.put(GET_GROUP_LIST_SIZE, new Byte(GET_GROUP_LIST_SIZE_CODE));
		functionMap.put(GET_FRIEND_TIME, new Byte(GET_FRIEND_TIME_CODE));
		functionMap.put(SEND_NOW, new Byte(SEND_NOW_CODE));
		functionMap.put(OPEN_CUR_NODE, new Byte(OPEN_CUR_NODE_CODE));
		functionMap.put(SELETE_PHOTO, new Byte(SELETE_PHOTO_CODE));
		functionMap.put(POST_PHOTO, new Byte(POST_PHOTO_CODE));
		functionMap.put(STOP_POST_PHOTO, new Byte(STOP_POST_PHOTO_CODE));
		functionMap
				.put(ENTRY_CAMERA_SCREEN, new Byte(ENTRY_CAMERA_SCREEN_CODE));

		functionMap.put(FTP_SEND, new Byte(FTP_SEND_CODE));
	}

	/**
	 * 发表日记
	 * 
	 * @param params
	 *            <li>[0]url <li>[1]title文本框id <li>[2]Privacy <li>[3]FriendStr
	 *            <li>[0]TitleStr <li>[1]Content <li>[2]Privacy <li>[3]FriendStr
	 */
	private void _zsnPostDiary(final ArrayList params) {
		final String URL = "http://mob.3g.cn/u123342/diary/edit.aspx?did=11231456647&guid="
				+ guid;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {

			int TotalLen = 0;
			/*** 所有上传的数据 ***/
			// Time:上传时间
			int Time = (int) System.currentTimeMillis();
			TotalLen += 4;
			// TitleStr:日记标题
			String TitleStr = (String) params.get(0);
			// TitleLen:
			int TitleLen = TitleStr.getBytes().length;
			TotalLen += (4 + TitleLen);

			// Content:日记正文
			String Content = (String) params.get(1);
			// ContentLen:
			int ContentLen = TitleStr.getBytes().length;
			TotalLen += (4 + ContentLen);

			// Privacy:查看权限
			byte Privacy = Byte.parseByte((String) params.get(2));
			TotalLen++;
			// FriendStr:提到字符串xxxxxx;xxxxxx;形式(好友限制20个)
			String FriendStr = (String) params.get(3);
			// FriendStrLen:提到朋友 字符串长度
			int FriendStrLen = FriendStr.getBytes().length;
			// UploadMethod:附件上传方法(0x00 和正文一起上传 0x01 断点上传)
			TotalLen += (4 + FriendStrLen);

			// 附件
			ArrayList attachList = getAttachList();
			// AttachNum:
			byte AttachNum = (byte) attachList.size();
			TotalLen++;

			for (int i = 0; i < attachList.size(); i++) {
				Attatch attatch = (Attatch) attachList.get(i);
				// 附件文件名
				String AttachName = attatch.AttachName;
				// 附件文件名长度
				byte AttachNameLen = (byte) AttachName.getBytes().length;
				// AttachData
				byte[] AttachData = attatch.AttachData;
				// 附件数据长度
				int AttachDataLen = AttachData.length;

				TotalLen += (1 + AttachNameLen + 1 + AttachDataLen + 4);

			}

			/*** 写数据 ***/
			// 当前包是正文包
			dos.writeByte(1);
			// TotalLen
			dos.writeInt(TotalLen);
			dos.writeInt(Time);
			dos.writeInt(TitleLen);
			dos.writeUTF(TitleStr);
			dos.writeInt(ContentLen);
			dos.writeUTF(Content);
			dos.writeByte(Privacy);
			dos.writeInt(FriendStrLen);
			dos.writeUTF(FriendStr);

			// 0x00 和正文一起上传 0x01 断点上传
			byte UploadMethod = 0x00;
			dos.writeByte(UploadMethod);
			dos.writeByte(AttachNum);

			for (int i = 0; i < attachList.size(); i++) {
				Attatch attatch = (Attatch) attachList.get(i);
				// 附件类型:0x01 图片0x02 音频0x03 视频 0x04 附件
				byte AttachType = attatch.AttachType;
				// 附件文件名
				String AttachName = attatch.AttachName;
				// 附件文件名长度
				byte AttachNameLen = (byte) AttachName.getBytes().length;
				// AttachData
				byte[] AttachData = attatch.AttachData;
				// 附件数据长度
				int AttachDataLen = AttachData.length;

				dos.writeByte(AttachType);
				dos.writeByte(AttachNameLen);
				dos.writeUTF(AttachName);
				dos.writeInt(AttachDataLen);
				dos.write(AttachData);

			}
			/*****/
			baos.flush();
			dos.flush();

			byte[] data = baos.toByteArray();

			net.addRequest(URL, data, "PostDiary", false, false, true);

			baos.close();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String _zsnGetFriendTime() {
		return NetController.friendTime;
	}

	private class Attatch {
		public byte AttachType;
		public String AttachName;
		public byte[] AttachData;
	}

	private ArrayList getAttachList() {
		ArrayList temp = new ArrayList();
		return temp;
	}

	protected void _zsnFTPSend(ArrayList params) {
		// System.out.println("_zsnFTPSend---->" + params.get(0));
		net.addRequest((String) params.get(0), "FTP", false, false, true);
	}

	public static String currentURL = null;

	/**
	 * 根据URL请求页面,是_zsnShowcontent(),_zsnSend(),_zsnSendData()这3个方法的通用方法
	 * 
	 * @param params
	 *            <p>
	 *            _zsnShowcontent() :
	 *            <li>1个参数：本地或网络xml
	 *            </p>
	 * <br>
	 *            <p>
	 *            _zsnSend()
	 *            <li>2个参数： 本地或网络xml
	 *            <li>3个参数： 本地或网络xml 回调函数
	 *            <li>4个或3个以上参数： 本地或网络xml+ 回调函数 + 回调参数
	 *            </p>
	 * <br>
	 *            <p>
	 *            _zsnSendData()
	 *            <li>1个参数： 网络xml
	 *            <li>2个参数： 网络xml + 回调函数
	 *            <li>3个参数： 网络xml + 上传字符串 + 上传方式:int(type==0是post请求,其他是get请求)
	 *            <li>4个参数： 网络xml + 上传字符串 + 上传方式:int + 回调函数
	 *            </p>
	 * <br>
	 *            <p>
	 *            _zsnSendLater(string url)
	 *            <li>1个参数： 网络xml
	 *            </p>
	 * @param function
	 *            用整型标志调用哪个方法: <li>0: _zsnShowcontent <li>1: _zsnSend <li>2:
	 *            _zsnSendData
	 */
	private void sendURL(final ArrayList params, final int function) {
		/*----- 请求-------*/
		// FIXME 测试不将url换成小写
		String url = ((String) params.get(0));// .toLowerCase();
		boolean isInstant = true;

		log.debug("params.size()" + params.size());
		log.debug("url-----" + url);

		if (url.startsWith("http://")) {
			url = StringUtil.handUrl(currentURL, url);
			// 如果url没有guid参数,则自动加上guid
			if (url.indexOf("guid=") < 0) {
				url += ((url.indexOf("?") < 0 ? "?" : "&") + "guid=" + guid);
			}
		}

		String callBack = null;

		ArrayList callBackParams = null;

		CallBackFunction callBackFunction = null;

		log.debug("sendURL====>>url==" + url);
		// _zsnShowcontent()
		// 1个参数：本地或网络xml + isInstant
		if (params.size() == 1 && function == 0) {
			// if (url.toLowerCase().indexOf("http://") >= 0) {
			net.addRequest(url, null, false, false, isInstant);
			// } else {
			// BrowserScreen.getInstance().loadUnit(url, null);
			// }

		}
		// _zsnSend()
		// 1个参数： 本地或网络xml
		// 2个参数： 本地或网络xml + 回调函数
		// 3个或3个以上参数： 本地或网络xml + 回调函数 + 回调参数
		else if (params.size() >= 1 && function == 1) {
			if (params.size() >= 2) {
				callBack = (String) params.get(1);
				params.remove(0);
				params.remove(0);
				callBackParams = params;
				if (callBackParams.size() == 0) {
					callBackParams = null;
				}
			}
			log.debug("sendURL==function == 1==>>url==" + url);
			// if (url.toLowerCase().indexOf("http://") >= 0) {
			callBackFunction = new CallBackFunction(callBack, callBackParams);
			net.addRequest(url, callBackFunction, false, false, isInstant);
			// } else {
			// log.debug("sendURL=加载本地===>>url==" + url);
			// BrowserScreen.getInstance().getZinc().reset();
			// BrowserScreen.getInstance().loadUnit(url, null);
			// try {
			// // 回调
			// if (callBack != null) {
			// if (callBackParams != null) {
			// ZincScript.getZincScript().callFunction(callBack,
			// callBackParams);
			// } else {
			// ZincScript.getZincScript().callFunction(callBack);
			// }
			//
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
		}
		// _zsnSendData()
		// 1个参数： 网络xml
		// 2个参数： 网络xml + 回调函数
		// 3个参数： 网络xml + 上传字符串 + 上传方式
		// 4个参数： 网络xml + 上传字符串 + 上传方式(type==0是post请求,其他是get请求) + 回调函数
		else if (function == 2) {
			// 提交表单的请求,传入所有表单的父ID
			if (params.size() == 1) {
				net.addRequest(url, null, false, false, isInstant);
			} else if (params.size() == 2) {
				callBackFunction = new CallBackFunction((String) params.get(1),
						null);
				net.addRequest(url, callBackFunction, false, false, isInstant);
			} else {
				String netString = (String) params.get(1);
				// type==0是post请求,其他是get请求
				int type = ((Integer) params.get(2)).intValue();
				if (params.size() == 4) {
					callBack = (String) params.get(3);
				}
				// POST请求
				if (type == 1) {
					byte[] data = netString.getBytes();
					callBackFunction = new CallBackFunction((String) params
							.get(1), null);
					net.addRequest(url, data, callBackFunction, false, false,
							isInstant);
					data = null;
				} else if (type == 0) {
					// GET请求
					StringBuffer sb = new StringBuffer();
					sb.append(url);
					if (url.indexOf("?") < 0) {
						sb.append('?');
					} else {
						sb.append("&");
					}

					sb.append(netString);
					net.addRequest(sb.toString(), null, callBack, false, false,
							isInstant);
					sb = null;
				}
			}

		}
		// _zsnSendLater(string url)
		else if (function == 3) {
			isInstant = false;
			net.addRequest(url, null, false, false, isInstant);
		}
		// 处理响应
		// handleInstantResp(callBack, callBackParams);
	}

	public class CallBackFunction {
		public String callback;
		public ArrayList callBackParams;

		public CallBackFunction(String callback, ArrayList callBackParams) {
			this.callback = callback;
			this.callBackParams = callBackParams;
		}
	}

	private final void _zsnPopDlg(ArrayList params) {
		String msg = (String) params.get(0);
		BrowserScreen.getInstance().showWarningPop(msg);
		msg = null;
	}

	private final void _zsnOpenDraft(ArrayList params) {

	}

	/**
	 * 用户id(手机号)
	 */
	public static String userID;
	private static String mmsRegPhoneNum;
	/**
	 * 登录以后的临时ID
	 */
	public static String guid = "13560237826";
	private final String LOGIN_URL = "http://61.145.124.35/MobileHeart/Passprot.aspx";

	/**
	 * 通过WAP注册
	 * 
	 * @param params
	 * @return
	 */

	private final void _zsnWapReg(ArrayList params) {

		final String url = LOGIN_URL;
		String imei = getIMEI();
		log.debug("imei=====" + imei);
		if (imei == null) {
			// TODO 处理获取不到imei的情况
			// BrowserScreen.getInstance()
			// .loadUnit("/widgets/login/mms.xml", null);
			return;
		}

		// 请求注册类型URL：
		net.addRequest(url + "?cmd=1&imei=" + imei, "getRegURLs", false, false,
				true);

		// 处理响应
		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (!net.isEmptyRespond()) {
						DataInputStream dis = null;
						try {
							Respond respond = net.getRespond();
							// 非即时请求,忽略
							if ("notInstant".equals(respond.request.getObj())) {
								net.addResponse(respond);
								continue;
							}

							int respondType = respond.ResponseState;

							String regURL = "";

							if (NQ.SUCCESS == respondType) {
								// url3响应不处理
								if ("url3".equals((String) respond.request
										.getObj())) {
									continue;
								}

								byte[] data = respond.ResponseData;
								dis = new DataInputStream(
										new ByteArrayInputStream(data));
								int PackageLength = dis.readInt();
								log.debug("PackageLength==" + PackageLength);
								int Command = dis.readShort();
								log.debug("Command==" + Command);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								// 响应是"请求注册类型URL"
								if ("getRegURLs"
										.equals((String) respond.request
												.getObj())) {
									int RegUrlCount = dis.readByte();
									log.debug("RegUrlCount==" + RegUrlCount);
									for (int i = 0; i < RegUrlCount; i++) {
										int RegUrlType = dis.readByte();
										log.debug("RegUrlType==" + RegUrlType);
										short URLLen = dis.readShort();
										log.debug("URLLen==" + URLLen);
										byte[] urlBytes = new byte[URLLen];
										dis.read(urlBytes);
										String URLCon = new String(urlBytes,
												"utf-8");
										log.debug("URLCon==" + URLCon);
										if (i == 2) {
											regURL = URLCon;
										}
										if (i == 1) {
											mmsRegPhoneNum = URLCon.substring(
													0, 10);
										}
										net.addRequest(regURL, "wap2Reg",
												false, false, true);
									}
									// WAP2注册
								} else if ("wap2Reg"
										.equals((String) respond.request
												.getObj())) {
									int RspStatus = dis.readByte();
									log.debug("RspStatus==" + RspStatus);

									int url3Length = dis.readShort();
									log.debug("url3Length==" + url3Length);
									byte[] url3Bytes = new byte[url3Length];
									dis.read(url3Bytes);
									String Url3 = new String(url3Bytes, "utf-8");
									log.debug("URL3==" + Url3);

									int url4Length = dis.readShort();
									log.debug("url4Length==" + url4Length);
									byte[] url4Bytes = new byte[url4Length];
									dis.read(url4Bytes);
									String Url4 = new String(url4Bytes, "utf-8");
									log.debug("URL4==" + Url4);

									net.addRequest(Url3, "url3", false, false,
											true);

									Thread.sleep(10000);
									net.addRequest(Url4, "url4", false, false,
											true);

								} else if ("url4"
										.equals((String) respond.request
												.getObj())) {

									int RspStatus = dis.readByte();
									log.debug("RspStatus==" + RspStatus);
									int MobLen = dis.readShort();
									log.debug("MobLen==" + MobLen);
									byte[] MobConBytes = new byte[MobLen];
									dis.read(MobConBytes);
									String MobCon = new String(MobConBytes,
											"utf-8");
									log.debug("MobCon==" + MobCon);

									// XXX 测试,模拟注册成功
									// RspStatus = 0;

									// 注册不成功
									if (RspStatus != 0) {
										// TODO 注册不成功
										// BrowserScreen.getInstance().loadUnit(
										// "/widgets/login/mms.xml", null);
									} else {
										// FIXME 测试
										// userID = "15800009459";
										// String password = "888888";// XXX

										// 注册成功
										userID = MobCon;

										BrowserScreen
												.getInstance()
												.loadUnit(
														"/widgets/login/modifyPswd.xml",
														null);

									}
									if (dis != null) {
										try {
											dis.close();
										} catch (IOException e) {
											e.printStackTrace();
										} finally {
											net.clearRequest();
										}
									}
									return;
								}

							}
						} catch (Exception e) {

							if (dis != null) {
								try {
									dis.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								} finally {
									net.clearRequest();
								}
							}
							e.printStackTrace();

							// TODO 短信注册
							// BrowserScreen.getInstance().loadUnit(
							// "/widgets/login/mms.xml", null);
							return;
						}

					}

				}
			}
		}.start();

	}

	/**
	 * 修改密码
	 * 
	 * @param params
	 *            带两个参数的ArrayList: <br>
	 *            <li>[0]:新密码:
	 */
	private void _zsnModifyPassword(ArrayList params) {
		if (params == null || params.size() < 1) {
			return;
		}
		final String url = LOGIN_URL;

		final String newPassword = (String) params.get(0);
		// TODO test
		userID = "15800009459";
		if (userID == null) {
			return;
		}
		// TODO 密码在将来可能需要md5加密
		String oldPasswordMD5 = userID.substring(5);
		String newPasswordMD5 = newPassword;
		// 修改密码
		net.addRequest(url + "?cmd=6&uid=" + userID + "&opass="
				+ oldPasswordMD5 + "&npass=" + newPasswordMD5, "modifyPsw",
				false, false, true);
		// 处理响应
		new Thread() {
			public void run() {
				DataInputStream dis = null;
				try {

					while (true) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if (!net.isEmptyRespond()) {

							Respond respond = net.getRespond();
							// 非即时请求,忽略
							if ("notInstant".equals(respond.request.getObj())) {
								net.addResponse(respond);
								continue;
							}

							int respondType = respond.ResponseState;

							if (NQ.SUCCESS == respondType) {
								byte[] data = respond.ResponseData;
								dis = new DataInputStream(
										new ByteArrayInputStream(data));
								int PackageLength = dis.readInt();
								log.debug("PackageLength==" + PackageLength);
								int Command = dis.readShort();
								log.debug("Command==" + Command);
								System.out
										.print("(String) respond.request.getObj()====>");
								log.debug((String) respond.request.getObj());

								// 修改密码
								if ("modifyPsw".equals((String) respond.request
										.getObj())) {
									int RspStatus = dis.readByte();
									log.debug("RspStatus==" + RspStatus);
									int MsgLen = dis.readShort();
									log.debug("MsgLen==" + MsgLen);
									byte[] MsgConBytes = new byte[MsgLen];
									dis.read(MsgConBytes);
									String MsgCon = new String(MsgConBytes,
											"utf-8");
									log.debug("MsgCon==" + MsgCon);
									// 关闭资源
									if (dis != null) {
										try {
											dis.close();
										} catch (IOException e) {
											e.printStackTrace();
										} finally {
											net.clearRequest();
										}
									}
									if (RspStatus == 0) {
										BrowserScreen.getInstance()
												.showWarningPop(
														"修改密码成功,3秒后自动调转到首页..");
										BrowserScreen.getInstance().loadUnit(
												"/widgets/main/mainpage.xml",
												null);
									} else {
										BrowserScreen.getInstance()
												.showWarningPop(MsgCon);
									}

									return;
								}

							}
						}
					}
				} catch (Exception e) {
					if (dis != null) {
						try {
							dis.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						} finally {
							net.clearRequest();
						}
					}
					e.printStackTrace();
				}

			}

		}.start();
	}

	private void _zsnMmsReg(ArrayList params) {
		if (params == null || params.size() == 0 || mmsRegPhoneNum == null) {
			return;
		}

		// WheelLoadingComponent hourglass =
		// WheelLoadingComponent.getInstance();
		// BrowserScreen.getInstance().setHourglass(hourglass);
		// BrowserScreen.getInstance().setEnabledMode(false);

		log.debug("sendMMS===>" + mmsRegPhoneNum);
		// 发短信
		boolean results = sendMMS("imei=" + getIMEI() + "&ua=nokian70",
				mmsRegPhoneNum);

		final String url = (String) params.get(0);

		// 处理响应
		new Thread() {
			public void run() {
				try {
					Thread.sleep(120000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// 请求注册类型URL：
				net.addRequest(url + "?cmd=1&imei=" + getIMEI(), "mmsReg",
						false, false, true);

				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (!net.isEmptyRespond()) {
						DataInputStream dis = null;
						try {
							Respond respond = net.getRespond();
							// 非即时请求,忽略
							if ("notInstant".equals(respond.request.getObj())) {
								net.addResponse(respond);
								continue;
							}

							int respondType = respond.ResponseState;

							if (NQ.SUCCESS == respondType) {
								byte[] data = respond.ResponseData;
								dis = new DataInputStream(
										new ByteArrayInputStream(data));
								int PackageLength = dis.readInt();
								log.debug("PackageLength==" + PackageLength);
								int Command = dis.readShort();
								log.debug("Command==" + Command);
								// 响应是"请求注册类型URL"
								if ("mmsReg".equals((String) respond.request
										.getObj())) {
									int RspStatus = dis.readByte();
									log.debug("RspStatus==" + RspStatus);
									int MobLen = dis.readShort();
									log.debug("MobLen==" + MobLen);
									byte[] MobConBytes = new byte[MobLen];
									dis.read(MobConBytes);
									String MobCon = new String(MobConBytes,
											"utf-8");
									log.debug("MobCon==" + MobCon);

									// // FIXME test
									// RspStatus = 0;
									// 注册不成功
									if (RspStatus != 0) {

										ArrayList params = new ArrayList();
										params.add("msgText");
										params.add("text");
										params.add("注册不成功,请尝试重新注册");
										DOMLib.getInstance().callFunction(
												"_zsdSetAttValue", params);
										DOMLib.getInstance().callFunction(
												"_zsdRefresh", null);

									} else {
										// 注册成功
										userID = MobCon;

										BrowserScreen
												.getInstance()
												.loadUnit(
														"/widgets/login/modify-psw.xml",
														null);

										ArrayList params = new ArrayList();
										params.add("phonenum");
										params.add("text");
										params.add("账号：" + MobCon);
										DOMLib.getInstance().callFunction(
												"_zsdSetAttValue", params);
										DOMLib.getInstance().callFunction(
												"_zsdRefresh", null);
									}
									if (dis != null) {
										try {
											dis.close();
										} catch (IOException e) {
											e.printStackTrace();
										} finally {
											net.clearRequest();
										}
									}

									// BrowserScreen.getInstance().setHourglass(
									// null);
									// BrowserScreen.getInstance().setEnabledMode(
									// true);

									return;
								}
							}
						} catch (Exception e) {
							if (dis != null) {
								try {
									dis.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								} finally {
									net.clearRequest();
								}
							}
							e.printStackTrace();
						}

					}

				}
			}
		}.start();
	}

	private static int status;
	private String LoginFailureInfo;

	/**
	 * @param params
	 *            <li>[0]UID <li>[1]PASSWORD <li>[2]登录成功回调函数<li>[3]登录失败回调函数
	 */
	private void _zsnLogin(ArrayList params) {
		status = 0;
		LoginFailureInfo = "";
		if (params == null || params.size() < 1) {
			return;
		}
		final String url = "http://61.145.124.35/MobileHeart/Passprot.aspx";
		final String uid = (String) params.get(0);
		final String password = (String) params.get(1);
		final String callback = (String) params.get(2);

		log.debug("uid==========" + uid);

		// TODO 密码在将来可能需要md5加密
		final String passwordMD5 = password;

		// 处理响应
		new Thread() {

			public void run() {
				net.addRequest(url + "?cmd=5&ver=" + getVersion() + "&uid="
						+ uid + "&pass=" + passwordMD5, "login", false, false,
						true);

				DataInputStream dis = null;
				try {

					while (true) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if (!net.isEmptyRespond()) {

							Respond respond = net.getRespond();
							// 非即时请求,忽略
							if ("notInstant".equals(respond.request.getObj())) {
								net.addResponse(respond);
								continue;
							}

							int respondType = respond.ResponseState;

							if (NQ.SUCCESS == respondType) {
								byte[] data = respond.ResponseData;
								dis = new DataInputStream(
										new ByteArrayInputStream(data));
								int PackageLength = dis.readInt();
								log.debug("PackageLength==" + PackageLength);
								int Command = dis.readShort();
								log.debug("Command==" + Command);

								if ("login".equals((String) respond.request
										.getObj())) {
									int RspStatus = dis.readByte();
									log.debug("RspStatus==" + RspStatus);
									int MsgLen = dis.readShort();
									log.debug("MsgLen==" + MsgLen);
									byte[] MsgConBytes = new byte[MsgLen];
									dis.read(MsgConBytes);
									String MsgCon = new String(MsgConBytes,
											"utf-8");
									log.debug("MsgCon==" + MsgCon);
									LoginFailureInfo = MsgCon;
									// 关闭资源
									if (dis != null) {
										try {
											dis.close();
										} catch (IOException e) {
											e.printStackTrace();
										} finally {
											net.clearRequest();
										}
									}
									// FIXME 测试
									// RspStatus = 0;
									// 登录成功
									if (RspStatus == 0) {
										log.debug("login ok...");
										userID = uid;

										guid = MsgCon;
										status = 1;
										// DOMLib.getInstance().callFunction(
										// successCallback, null);
										// onLoginSuccess(uid, passwordMD5);

									} else {
										status = 2;
										// ArrayList params = new ArrayList();
										// params.add("loginInfo");
										// params.add("text");
										// params.add(MsgCon);
										// DOMLib.getInstance().callFunction(
										// "_zsdSetAttValue", params);
										// DOMLib.getInstance().callFunction(
										// "_zsdRefresh", null);
									}
									ZincScript.getZincScript().callFunction(
											callback, null);
									return;
								}

							}
						}
					}
				} catch (Exception e) {
					if (dis != null) {
						try {
							dis.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						} finally {
							net.clearRequest();
						}
					}
					e.printStackTrace();
				}

			}

		}.start();
	}

	/**
	 * 保存修改的个人信息
	 * 
	 * @param params
	 *            <li>[0] URL(不带参数) <li>[1] 用户名 <li>[2] 性别 <li>[3] 临时guid
	 */
	private void _zsnSaveInfo(ArrayList params) {
		if (params == null || params.size() < 1) {
			return;
		}
		final String url = (String) params.get(0);
		final String username = (String) params.get(1);
		final String sex = (String) params.get(2);
		final String city = (String) params.get(3);
		final String guid = this.guid;

		// 处理响应
		new Thread() {
			public void run() {
				// http://test.3g.cn/sns/interface/RegisterTrue.aspx?username=&sex=1&city=xxx&guid=xxx
				try {
					net.addRequest(url, ("username=" + username + "&sex=" + sex
							+ "&city=" + city + "&guid=" + guid)
							.getBytes("utf-8"), "saveInfo", false, false, true);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (!net.isEmptyRespond()) {

						Respond respond = net.getRespond();
						// 非即时请求,忽略
						if ("notInstant".equals(respond.request.getObj())) {
							net.addResponse(respond);
							continue;
						}

						int respondType = respond.ResponseState;

						if (NQ.SUCCESS == respondType) {
							// TODO 提示修改成功
							BrowserScreen.getInstance().loadUnit(
									"/widgets/main/mainpage.xml", null);
						} else {
							// TODO 提示修改不成功
						}

						return;

					}
				}
			}

		}.start();
	}

	private String _zsnGetLoginFailureInfo() {
		return LoginFailureInfo;
	}

	/**
	 * 得到手机的imei
	 * 
	 * @return
	 */
	private String getIMEI() {
		String imei = "";
		// moto
		imei = System.getProperty("IMEI");
		if (imei == null || "".equals(imei)) {
			imei = System.getProperty("phone.IMEI");
		}
		if (imei == null || "".equals(imei)) {
			imei = System.getProperty("com.motorola.IMEI");
		}
		// SIEMENS
		if (imei == null || "".equals(imei)) {
			imei = System.getProperty("com.siemens.IMEI");
		}

		// NOKIA
		if (imei == null || "".equals(imei)) {
			imei = System.getProperty("com.nokia.mid.imei");
		}
		if (imei == null || "".equals(imei)) {
			imei = System.getProperty("com.nokia.IMEI");
		}

		// sonyericsson
		if (imei == null || "".equals(imei)) {
			imei = System.getProperty("com.sonyericsson.imei");
		}
		if (imei == null || "".equals(imei)) {
			imei = System.getProperty("com.sonyericsson.IMEI");
		}
		// samsung
		if (imei == null || "".equals(imei)) {
			imei = System.getProperty("com.samsung.imei");
		}
		if (imei == null || "".equals(imei)) {
			log.debug("==can not get imei!~");

			return String.valueOf(System.currentTimeMillis());
		}
		// 只提取数字,如"IMEI 00460101-501594-5-00" --> "00460101501594500"
		StringBuffer imeiNum = new StringBuffer();
		boolean isHeadZero = true;
		for (int i = 0; i < imei.length(); i++) {

			char c = imei.charAt(i);
			if (c >= '0' && c <= '9') {
				isHeadZero &= (c == '0');
				if (!isHeadZero) {
					imeiNum.append(c);
				}
			}

		}
		imei = imeiNum.toString();
		// if(imei.indexOf(' ')>0){
		// imei = imei.replace(' ', '_');
		// }

		return imei;
	}

	private String _zsnGetGuid() {

		return guid;
	}

	/**
	 * 给指定号码发送短信息
	 * 
	 * @param content
	 *            短信息内容
	 * @param phoneNumber
	 *            手机号码
	 * @return 发送成功返回true，否则返回false
	 */

	private boolean sendMMS(String content, String phoneNumber) {

		// 返回值
		boolean result = true;
		try {
			// 地址

			String address = "sms://+" + phoneNumber;

			// 建立连接

			MessageConnection conn = (MessageConnection) Connector
					.open(address);
			// 设置短信息类型为文本，短信息有文本和二进制两种类型
			TextMessage msg = (TextMessage) conn
					.newMessage(MessageConnection.TEXT_MESSAGE);
			// 设置信息内容
			msg.setPayloadText(content);
			// 发送
			conn.send(msg);
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			// 未处理
		}
		return result;
	}

	private String getVersion() {
		return "1.0";
	}

	private String phoneNum;
	private String password;
	private String loginStatus;
	private String rememberStatus;

	private String _zsnGetPhoneNum() throws ZSException {
		if (phoneNum == null) {
			ArrayList params1 = new ArrayList();
			params1.add("logininfo");
			StrLib.getInstance().callFunction("_zsrOpenRecord", params1);
			int rsNum = ((Integer) StrLib.getInstance().callFunction(
					"_zsrGetRecordsNum", null)).intValue();

			if (rsNum > 0) {
				ArrayList params2 = new ArrayList();
				params2.add(new Integer(1));
				phoneNum = (String) StrLib.getInstance().callFunction(
						"_zsrGetString", params2);

				ArrayList params3 = new ArrayList();
				params3.add(new Integer(2));
				password = (String) StrLib.getInstance().callFunction(
						"_zsrGetString", params3);

				ArrayList params4 = new ArrayList();
				params4.add(new Integer(3));
				loginStatus = (String) StrLib.getInstance().callFunction(
						"_zsrGetString", params4);

				ArrayList params5 = new ArrayList();
				params5.add(new Integer(4));
				rememberStatus = (String) StrLib.getInstance().callFunction(
						"_zsrGetString", params5);
			}
			StrLib.getInstance().callFunction("_zsrCloseRecord", null);
		}
		return phoneNum;
	}

	private String _zsnGetPwd() {
		return password;
	}

	private String _zsnGetloginStatus() {
		return loginStatus;
	}

	private Integer _zsnGetLoginSuccessStatus() {
		// log.debug("_zsnGetLoginStatus===="+status);
		return new Integer(status);
	}

	private String _zsnGetRemberStatus() {
		return rememberStatus;
	}

	private void deletePassword() throws ZSException {
		ArrayList params1 = new ArrayList();
		params1.add("logininfo");
		StrLib.getInstance().callFunction("_zsrOpenRecord", params1);

		StrLib.getInstance().callFunction("_zsrDeleteRecord", params1);

		StrLib.getInstance().callFunction("_zsrCloseRecord", null);

	}

	private void rememberPassword(String uid, String password,
			String autoLogin, String remember) throws ZSException {

		deletePassword();

		System.out.print("rememberPassword== ");
		log.debug("uid:" + uid + "\tpassword:" + password + "\tautologin:"
				+ autoLogin);

		ArrayList params8 = new ArrayList();
		params8.add("logininfo");
		StrLib.getInstance().callFunction("_zsrOpenRecord", params8);

		int rsNum = ((Integer) StrLib.getInstance().callFunction(
				"_zsrGetRecordsNum", null)).intValue();

		if (rsNum > 0) {
			StrLib.getInstance().callFunction("_zsrCloseRecord", null);
			return;
		}

		ArrayList params = new ArrayList();
		params.add(uid);
		StrLib.getInstance().callFunction("_zsrSetRecord", params);

		ArrayList params1 = new ArrayList();
		params1.add(password);
		StrLib.getInstance().callFunction("_zsrSetRecord", params1);

		ArrayList params2 = new ArrayList();
		params2.add(autoLogin);
		StrLib.getInstance().callFunction("_zsrSetRecord", params2);

		ArrayList params3 = new ArrayList();
		params2.add(remember);
		StrLib.getInstance().callFunction("_zsrSetRecord", params3);

		StrLib.getInstance().callFunction("_zsrCloseRecord", null);
	}
}
