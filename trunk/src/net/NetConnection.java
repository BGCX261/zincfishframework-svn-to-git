package net;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import log.Log;

import utils.StringUtil;

public final class NetConnection {
	private static NetConnection singleton = new NetConnection();
	// private final static String X_Wap_Profile = "x-wap-profile";
	// private final static String User_Agent = "User-Agent";
	// private final static String Profile = "profile";
	private final static String X_Online_Host = "X-Online-Host";

	// private final static String Referer = "Referer";
	private final static String Content_Type = "Content-Type";
	private final static String Content_Length = "Content-Length";

	// private final static String wapUrl = "http://10.0.0.172:80/";

	private static HttpConnection httpConn;
	private static Request request;
	private static byte[] response;
	private static int reponseRange = 0;
	private static String reponse_type;
	private static String reponse_Name;
	private static Exception exceptionPipe;
	private static int ResponseCode;
	private static int ResponseType;

	/*
	 * 网络链接状态 0: 初始化状态 1: 网络链接状态 2: 数据接受状态 -1: 空闲状态
	 */
	public static int ConnectStep;
	private static Log log;
	public static final int ConnectStep_Free = 0; // 空闲状态
	public static final int ConnectStep_Send = 1; // 发送请求
	public static final int ConnectStep_Work = 2;

	public static synchronized Respond connect(Request request, Net _net)
			throws Exception {
		if (request == null) {
			throw new IllegalStateException("Cannot invoke this method!");
		}
		_Init();
		ConnectStep = ConnectStep_Free;
		NetConnection.request = request;
		// 启动网络
		singleton.run();
		ConnectStep = ConnectStep_Free;
		if (exceptionPipe != null) {
			throw exceptionPipe;
		}
		return new Respond(ResponseType, NetConnection.request, response,
				reponse_Name, reponse_type, reponseRange);
	}

	private static void _Init() {
		severConnection();
		request = null;
		response = null;
		exceptionPipe = null;
		ResponseCode = 0;
		ResponseType = 0;
		log = Log.getLog("NetConnection");
	}
	
	
	public void run() {

		DataOutputStream dos = null;
		DataInputStream dis = null;
		ByteArrayOutputStream bos = null;
		String url = null;

		// 设置HTTP头信息
		try {
			ConnectStep = ConnectStep_Send;
			url = request._url; // http - url
			int ConnectType = request.ConnectType;
			byte[] data = request.postData; // http - post data
			// 头信息
			String _Content_Type = request._Content_Type; // http - Content-Type
			String _X_Online_Host = null;
			String _Content_Length = null;
			String method = null; // http - method (post or get)
			int permissions = 0;
			if (data == null) {
				method = HttpConnection.GET;
				permissions = Connector.READ;
			} else {
				method = HttpConnection.POST;
				permissions = Connector.READ_WRITE;
				_Content_Length = String.valueOf(data.length);
			}
			// 打开HTTP链接
			String _url = url;
			
			//FIXME 测试  网络连接 直连还是代理
			ConnectType = NQ.cmwap1;
			
			if (ConnectType == NQ.cmwap2) {
				_X_Online_Host = StringUtil.getOnlineHost(url);
				_url = StringUtil.getCmwapUrl(url);
			}
     
			log.debug(ConnectType +"=Connector.open：" + _url);

			httpConn = (HttpConnection) Connector.open(_url, permissions, true);
			ConnectStep = ConnectStep_Work;
			httpConn.setRequestMethod(method);
			// 设置头信息
			if (null != _X_Online_Host)
				httpConn = setRequestProperty(httpConn, X_Online_Host,
						_X_Online_Host);
			httpConn = setRequestProperty(httpConn, "Accept", NQ.Accept);
			httpConn = setRequestProperty(httpConn, "Connection", NQ.Connection);
			if (permissions == Connector.READ_WRITE) {
				httpConn = setRequestProperty(httpConn, Content_Type,
						_Content_Type);
				httpConn = setRequestProperty(httpConn, Content_Length,
						_Content_Length);
				dos = httpConn.openDataOutputStream();
				dos.write(data);
			}
		} catch (IOException e) {
			log.debug("<===e1==>");
			e.printStackTrace();
			ResponseType = NQ.UNSUCCESS;
			exceptionPipe = new Exception("发送请求出现异常，请尝试刷新页面。");
			return;
		} catch (SecurityException e) {
			ResponseType = NQ.CANNOT_ACCESS;
			return;
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
			} catch (Exception e) {
			} finally {
				dos = null;
			}
			ConnectStep = ConnectStep_Free;
		}
		boolean isClose = request.isClose;
		if (isClose) {
			ResponseType = NQ.CLOSE;
			return;
		}
		try {
			try {
				// NetLib.showMsg("getting respondCode..");
				ResponseCode = httpConn.getResponseCode();
				// NetLib.showMsg("ResponseCode--"+ResponseCode);
			} catch (IOException ex1) {
				log.debug("<===e2==>");
				ex1.printStackTrace();
				ResponseType = NQ.UNSUCCESS;
				exceptionPipe = new Exception("服务器响应异常，请尝试刷新页面。");
				// NetLib.showMsg("response exception");
				return;
			}
			// }
			isClose = request.isClose;
			if (isClose) {
				ResponseType = NQ.CLOSE;
				return;
			}
			if (ResponseCode >= 300 && ResponseCode <= 307) {
				String nurl = null;
				/** @todo 重定向需要处理 ****/
				try {
					nurl = httpConn.getHeaderField("Location").trim();
				} catch (IOException ex) {
					nurl = null;
				}
				if (nurl != null) {
					nurl = StringUtil.handUrl(url, nurl);
					// 过渡处理
					ResponseType = NQ.REDIRECT;
					request.set_Url(nurl);
					request.isREDIRECT = true;
				} else {
					ResponseType = NQ.LOCATION_ERROR;
					// exceptionPipe = new IllegalStateException(
					// "无法获取跳转页面的地址，页面跳转被取消。");
				}
			} else if (ResponseCode != HttpConnection.HTTP_OK
					&& ResponseCode != HttpConnection.HTTP_PARTIAL) {
				log.debug("<===e3==>");
				ResponseType = NQ.UNSUCCESS;
			} else {
				String contentType = httpConn.getHeaderField("Content-Type");
				if (request.ischeckMsg
				/* || contentType.indexOf("application/glk") != -1 */) {
					dis = httpConn.openDataInputStream();
					ResponseType = NQ.SUCCESS;
					byte check = dis.readByte();
					if (NQ.CMD_SUCCESS == check || NQ.CMD_OTHER == check
							&& !request.isClose) {
						if (NQ.CMD_OTHER == check)
							ResponseType = NQ.CMD_OTHER;
						int reponlen = dis.readInt();
						bos = new ByteArrayOutputStream();
						response = new byte[reponlen];
						dis.readFully(response);
						if (!request.isClose) {
							Respond respond = new Respond(ResponseType,
									request, response, reponse_Name,
									reponse_type, reponseRange);
							Net.getInstance().processResponse(respond);
							respond = null;
						}
						response = null;

					} else {
						ResponseType = NQ.MOBILE;
						return;
						// exceptionPipe = new IllegalStateException(
						// "服务器返回数据异常，请尝试刷新页面。");
					}
				} else if (contentType.indexOf("vnd.wap.wml") != -1
						|| contentType.indexOf("html") != -1) {
					run();
					// if (request.ischeckMsg) {
					// ResponseType = NQ.MOBILE;
					// // exceptionPipe = new IllegalArgumentException(
					// // "数据被运营商拦截或篡改，请尝试刷新页面。");
					// return;
				} else {
					dis = httpConn.openDataInputStream();
					ResponseType = NQ.SUCCESS;
					bos = new ByteArrayOutputStream();
					response = new byte[1024];
					int ch = 0;
					while ((ch = dis.read(response)) != -1 && !request.isClose) {
						bos.write(response, 0, ch);
					}
					if (!request.isClose) {
						bos.flush();
						response = null;
						response = bos.toByteArray();
					}

				}
				isClose = request.isClose;
				if (isClose) {
					ResponseType = NQ.CLOSE;
					return;
				}
			}
		} catch (Exception e) {
			log.debug("<===e4==>");
			e.printStackTrace();
			ResponseType = NQ.UNSUCCESS;
			return;
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {
			} finally {
				bos = null;
			}
			try {
				if (dis != null) {
					dis.close();
				}
			} catch (Exception e) {
			} finally {
				dis = null;
			}
			try {
				if (httpConn != null) {
					httpConn.close();
					httpConn = null;
				}
			} catch (Exception e) {
			}
		}
		Net.getInstance().isContinue = false;
		ConnectStep = ConnectStep_Free;

	}

	public static void severConnection() {
		try {
			if (httpConn == null) {
				return;
			}
			httpConn.close();
		} catch (Exception e) {
		} finally {
			httpConn = null;
		}
	}

	public static HttpConnection setRequestProperty(HttpConnection hc,
			String RequestPropertyName, String RequestPropertyValue)
			throws IOException {
		if (hc == null && RequestPropertyName == null)
			throw new NullPointerException(
					"setRequestProperty()  set  RequestPropertyValue = null");
		if (RequestPropertyValue != null) {
			hc.setRequestProperty(RequestPropertyName, RequestPropertyValue);
		}
		return hc;
	}

	public static Request getRequest() {
		return request;
	}

}
