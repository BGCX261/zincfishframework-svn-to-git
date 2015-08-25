package net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import log.Log;
import screen.BrowserScreen;
import utils.ArrayList;
import zincfish.zinclib.NetLib;
import zincfish.zinclib.NetLib.CallBackFunction;
import zincfish.zincscript.ZincScript;
import net.Respond;

/**
 * 网络控制器
 */
public class Net implements Runnable {

	private static Net instance = null;
	private static final int QUEUE_LEN = 3;
	private static final int SLEEP_TIME = 10000;
	private static final int INTERVAL = 100;
	private static final int NOW_REQUEST = 0;
	private static final int NOW_RESPOND = 0;
	private static final String HEAT_URL = "http://test.3g.cn/sns/interface/_packet.aspx?guid="
			+ NetLib.guid;

	private ArrayList unistantReqList = null;// 非即时发送队列
	private ArrayList instantReqList = new ArrayList();// 即时发送队列
	private ArrayList RespondQueue = null;// 接收队列
	private boolean isChangeConnectType = true;
	private byte ConnectType = NQ.cmwap1;
	public boolean isContinue = false;
	private static Log log = Log.getLog("Net");

	/**
	 * 获取实例
	 * 
	 * @return {@link Net}
	 */
	public static Net getInstance() {
		if (instance == null) {
			instance = new Net();
		}
		return instance;
	}

	/*
	 * 私有构造函数
	 */
	private Net() {
		_init();
		new Thread(this).start();
	}

	/*
	 * 初始化
	 */
	private void _init() {
		unistantReqList = new ArrayList(QUEUE_LEN);
		RespondQueue = new ArrayList(QUEUE_LEN);
	}

	/**
	 * 自动更换网络设置
	 * 
	 * @param _ConnectType
	 *            网络连接类型
	 */
	private void SetRms_ConnectType(byte _ConnectType) {
		if (!isChangeConnectType) {
			return;
		}
		ConnectType = _ConnectType;
		// FIXME 如果需要更换网络连接类型时，放开此处代码
		// if (SettingData.CONNECTION_PORT != ConnectType) {
		// SettingData.CONNECTION_PORT = ConnectType;
		// SettingData.saveSetting();
		// isChangeConnectType = false;
		// }
		isChangeConnectType = true;
	}

	/**
	 * 为请求包设置网络连接类型
	 * 
	 * @param request
	 *            请求包
	 * @return 设置过网络连接类型的请求包
	 */
	public Request setConnectType(Request request) {
		if (!isChangeConnectType) {
			return request;
		}
		request.ConnectType = request.ConnectType == NQ.cmwap1 ? NQ.cmwap2
				: NQ.cmwap1;
		return request;
	}

	/**
	 * 设置网络连接类型
	 * 
	 * @param ConnectType
	 *            网络连接类型
	 */
	public void setConnectType(byte ConnectType) {
		isChangeConnectType = true;
		this.ConnectType = ConnectType;
	}

	/**
	 * add HttpUrl into the Request Queue
	 * 
	 * @param http
	 *            the HttpUrl
	 */
	public void addRequest(Request request) {
		try {
			// FIXME 如果新加入的请求需要冲掉之前的请求，请打开下面的代码
			/*
			 * if (RequestQueue.size() > 0) { RequestQueue.remove(0); }
			 */
			if (request.isInstant) {
				// 即时请求,加到即时请求队列
				instantReqList.add(request);
			} else {
				// 非即时请求,加到非即时请求队列
				unistantReqList.add(request);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 清除所有请求
	 */
	public void clearRequest() {
		if (!isContinue) {
			unistantReqList.removeAll();
			ignorThisConnection();
			isContinue = false;
		}
	}

	/**
	 * 关闭当前请求
	 */
	public void ignorThisConnection() {
		Request request = NetConnection.getRequest();
		if (request != null)
			request.set_Close();
		request = null;
	}

	/**
	 * 判断当前网络是否空闲
	 * 
	 * @return
	 */
	public boolean isIdle() {
		return NetConnection.ConnectStep == NetConnection.ConnectStep_Free;
	}

	/**
	 * add HttpUrl into the Request Queue
	 * 
	 * @param url
	 *            the HttpUrl's url
	 */
	public void addRequest(String url, Object Obj, boolean ischeckMsg,
			boolean ispreData, boolean isInstant) {
		addRequest(url, null, Obj, ischeckMsg, ispreData, isInstant);
	}

	/**
	 * add HttpUrl and post data into the Request Queue
	 * 
	 * @param url
	 *            HttpUrl's url
	 * @param data
	 *            HttpUrl's post data
	 */
	public void addRequest(String url, byte[] data, Object Obj,
			boolean ischeckMsg, boolean ispreData, boolean isInstant) {
		Request request = new Request(url, data, ConnectType, NQ.client1, Obj,
				ischeckMsg, ispreData, isInstant);
		addRequest(request);
	}

	public void addResponse(Respond response) {
		// XXX 在登录注册的时候会出错,暂时屏蔽掉--by陈静聪
		/*
		 * BrowserScreen.getInstance().loadUnit(response.ResponseData, null); //
		 * try { // RespondQueue.add(response); // } catch (Exception e) { // }
		 */

		// XXX added by陈静聪
		try {
			RespondQueue.add(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private Request getRequset() {
	// try {
	// Request request = (Request) unistantReqList.remove(NOW_REQUEST);
	// return request;
	// } catch (Exception e) {
	// return null;
	// }
	// }

	/**
	 * Get Data From the Respond Queue
	 * 
	 * @return the HttpUrl and the Respond Data
	 */
	public Respond getRespond() {
		try {
			Respond respond = (Respond) RespondQueue.remove(NOW_RESPOND);
			return respond;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 判断请求队列是否为空
	 * 
	 * @return
	 */
	private boolean isEmptyRequest() {
		int RequestQueueSize = unistantReqList.size();
		return RequestQueueSize == 0;
	}

	/**
	 * Respond Queue whether empty or not empty
	 * 
	 * @return true: empty false: not empty
	 */
	public boolean isEmptyRespond() {
		int RespondQueueSize = RespondQueue.size();
		return RespondQueueSize == 0;
	}

	public synchronized void run() {
		int heartInterval = 0;
		while (true) {
			if (instantReqList != null && instantReqList.size() > 0) {
				Request lastRequest = (Request) instantReqList.get(NOW_REQUEST);
				if (lastRequest != null && lastRequest.isInstant) {
					// 即时请求,立即联网
					lastRequest.addConnectTimes();
					try {
						// Thread.sleep(SLEEP_TIME);
						Respond response = HttpConnection(lastRequest);
						processResponse(response);
						instantReqList.remove(lastRequest);
					} catch (Exception e) {
						processException(lastRequest, NQ.UNSUCCESS, true);
					} finally {
						// BrowserScreen.getInstance().setHourglass(null);
						// BrowserScreen.getInstance().setEnabledMode(true);
					}
				}
				lastRequest = null;
			}
			// 不需要即时响应
			// 每隔20秒发送一次心跳包
			if ((heartInterval += INTERVAL) >= SLEEP_TIME) {
				heartInterval = 0;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);

				try {
					dos.writeByte(unistantReqList.size());
					if (!isEmptyRequest()) {
						// 封包
						for (int i = 0; i < unistantReqList.size(); i++) {
							Request req = (Request) unistantReqList.get(i);
							if (!req.isInstant) {
								dos.writeShort(0x0001);
								dos.writeUTF(req._url);
								log.debug(unistantReqList.size()
										+ "heart packaging=>req._url:"
										+ req._url + "\n");
								unistantReqList.remove(req);
								i--;
							}
						}
						dos.flush();
						baos.flush();
						// 发送心跳包到指定的接口 TODO 调试,暂时屏蔽
						addRequest(HEAT_URL, baos.toByteArray(), "notInstant",
								false, false, true);
						log.debug("heart sended=>url:" + HEAT_URL + "\n");
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						dos.close();
						baos.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}

			}

			// if (!isEmptyRequest()) {
			//
			// for (int i = 0; i < RequestQueue.size(); i++) {
			// Request request = (Request) RequestQueue.get(i);
			// // 需要即时响应的请求
			// if (request.isInstant) {
			// // 显示联网动画，阻塞操作
			// // WheelLoadingComponent hourglass =
			// // WheelLoadingComponent
			// // .getInstance();
			// // BrowserScreen.getInstance().setHourglass(hourglass);
			// // BrowserScreen.getInstance().setEnabledMode(false);
			//
			// request.addConnectTimes();
			// try {
			//
			// Respond response = HttpConnection(request);
			// processResponse(response);
			// } catch (Exception e) {
			// processException(request, NQ.UNSUCCESS, true);
			// } finally {
			// // BrowserScreen.getInstance().setHourglass(null);
			// // BrowserScreen.getInstance().setEnabledMode(true);
			//
			// }
			// }
			// RequestQueue.remove(request);
			// i--;
			// }
			//
			// }

			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				log.error(e, "run()--" + e.getMessage());
			}
		}

	}

	public Respond HttpConnection(Request request) throws Exception {
		// TODO 在此处根据URL格式的不同，区分不同的处理流程
		if (request._url.indexOf("http://") < 0) {
			log.debug("-------------> url = " + request._url);
			BrowserScreen.getInstance().loadUnit(request._url, null);
			// 处理回调函数
			CallBackFunction callBackFuntion = null;
			ArrayList callBackParams = null;
			String callBack = null;

			if (request.getObj() instanceof CallBackFunction) {
				callBackFuntion = (CallBackFunction) request.getObj();
				callBack = callBackFuntion.callback;
				callBackParams = callBackFuntion.callBackParams;
			}
			// 回调
			if (callBack != null) {
				if (callBackParams != null) {
					ZincScript.getZincScript().callFunction(callBack,
							callBackParams);
				} else {
					ZincScript.getZincScript().executeDynamicScript(callBack);
				}

			}
			return null;
		} else {
			try {
				Respond Response = NetConnection.connect(request, instance);
				return Response;
			} catch (Exception ex) {
				throw ex;
			}
		}
	}

	/**
	 * 根据响应类型不同，调用不同的方法处理响应
	 * 
	 * @param response
	 *            响应
	 */
	public void processResponse(Respond response) {
		if (response == null)
			return;
		Request request = response.request;
		int ResponseState = response.ResponseState;
		if (request == null || request.isClose)
			if (ResponseState != NQ.CMD_PIC)
				return;

		// NetLib.showMsg("prcssURL--"+request._url);
		// NetLib.showMsg("prcssPra--"+request.getObj());

		switch (ResponseState) {
		case NQ.ILLEGAL:
		case NQ.MOBILE:
			processException(request, ResponseState, false);
			break;
		case NQ.REDIRECT:
			// NetLib.showMsg("<<<<NQ.REDIRECT>>>>");
			addRequest(request);
			break;
		case NQ.CLOSE:
			break;
		default:
			addResponse(response);
			SetRms_ConnectType(request.ConnectType);

			// Added by chenjingcong
			NetController.getInstance().processSuccessResp(response);
			break;
		}
	}

	private void processException(Request request, int responcestate,
			boolean changeConnectionPort) {
		// byte processType = request.getProcessType();
		// switch (processType) {
		// case NQ.client1:
		// case NQ.client2:
		if (!request.VecConnectTimes()) {
			addResponse(new Respond(responcestate, request));
		} else {
			if (changeConnectionPort)
				setConnectType(request);
			addRequest(request);
		}
		// break;
		// }
	}

	/**
	 * 网络执行的步骤
	 * 
	 * @return int
	 */
	public int getConnectStep() {
		return NetConnection.ConnectStep;
	}

	/* ***************************** */
	/* 处理超时 */
	/* ***************************** */
	// private TimerTask requestDelayTimer = null;

	// public class RequestDelayTimer implements Runnable {
	//
	// private Request _request;
	// private int _times;
	// private int _timesMax;
	//
	// public RequestDelayTimer(Request request, int _timesMax) {
	// this._request = request;
	// this._timesMax = _timesMax;
	// }
	//
	// public void run() {
	// _times++;
	// if (_times > _timesMax) {
	// Process();
	// }
	// }
	//
	// public void Process() {
	// Request(_request);
	// clean();
	// }
	//
	// public void clean() {
	// requestDelayTimer.cancel();
	// requestDelayTimer = null;
	// _request = null;
	// _times = 0;
	// }
	// }
}
