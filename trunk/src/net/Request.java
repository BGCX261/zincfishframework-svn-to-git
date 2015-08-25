package net;

public class Request {
	/** 连接URL* */
	public String _url;
	/***************************************************************************
	 * 来访者的身份- 当移动网关发现所使用的User-Agent 和手机上的默认浏览器所使用的User-Agent
	 * 不对应时就会对内容进行处理（或者是屏蔽）。 解决的方法是把判断条件作为参数传递，而避免使用User-Agent
	 **************************************************************************/
	// public String _User_Agent;
	/** * */
	// public String _X_Wap_Profile;
	// public String _Profile;
	public String _Content_Type;
	public String _Referer;
	public String _Pragma;
	public String _Host;
	public byte ConnectType;
	public boolean isClose;
	public byte ProcessType = 0;
	public byte[] postData;
	private byte ConnectTimes = 0;
	private Object Obj = null;
	public boolean ischeckMsg = false;
	public boolean ispreData = false;
	public boolean isREDIRECT = false;
	
	/**
	 * 日否需要即使处理的请求.如果true就马上发送请求;如果不是就封包,加到请求队列,每隔20秒发送-cheningcong
	 */
	public boolean isInstant = true;

	public Object getObj() {
		return Obj;
	}

	public Request(String url, byte[] postData, byte connectType,
			byte ProcessType, Object Obj, boolean ischeckMsg, boolean ispreData,boolean isInstant) {
		this.ProcessType = ProcessType;
		this.ConnectType = connectType;
		this._url = url;
		this.postData = postData;
		this.Obj = Obj;
		this.ischeckMsg = ischeckMsg;
		this.isInstant = isInstant;
		// this._User_Agent = NQ.User_Agent;
		this._Referer = null;
		this.ispreData = ispreData;
		if (ProcessType == NQ.client1) {
			this._Content_Type = NQ.Content_Type1;
			// this._X_Wap_Profile = NQ.X_Wap_Profile;
			// this._Profile = NQ.Profile;
		} else if (ProcessType == NQ.client2) {
			this._Content_Type = NQ.Content_Type2;
		}
	}

	public void set_Referer(String _Referer) {
		this._Referer = _Referer;
	}

	public void addConnectTimes() {
		ConnectTimes++;
	}

	public byte getProcessType() {
		return ProcessType;
	}

	public byte getConnectTimes() {
		return ConnectTimes;
	}

	public void set_Url(String _url) {
		this._url = _url;
	}

	public void set_Close() {
		this.isClose = true;
	}

	public boolean VecConnectTimes() {
		int _ConnectTimes = 0;
		switch (ProcessType) {
		case NQ.client1:
			_ConnectTimes = NQ.Client1ConntectTimes;
			break;
		case NQ.client2:
			_ConnectTimes = NQ.Client2ConntectTimes;
			break;
		default:
			_ConnectTimes = NQ.Client2ConntectTimes;
			break;
		}
		return _ConnectTimes >= ConnectTimes ? true : false;
	}
}
