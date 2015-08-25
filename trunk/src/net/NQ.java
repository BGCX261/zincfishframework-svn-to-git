package net;

public class NQ {
	/** Response返回状态 **/
	public static final int DEFAULT = 0; // 默认值
	public static final int SUCCESS = 1; // 成功
	public static final int REDIRECT = 2; // 重定向
	public static final int MOBILE = 3; // 移动
	public static final int UNSUCCESS = 4; // 失败
	public static final int CLOSE = 5;
	public static final int DOWNLOAD = 6;
	public static final int CANNOT_ACCESS = 7;// 不合法
	public static final int OUT_OF_MEMORY = 8;// 内存溢出
	public static final int ILLEGAL = 9;// 非法数据
	public static final int LOCATION_ERROR = 10;// 跳转失败

	public static final int CMD_SUCCESS = 0x01;
	public static final int CMD_OTHER = 0x71;
	public static final int CMD_PIC = 0x72;

	/*** Request参数 ****/
	public static final String Accept = "application/vnd.wap.xhtml+xml,application/xml,text/vnd.wap.wml,text/html,application/xhtml+xml,image/jpeg;q=0.5,image/png;q=0.5,image/gif;q=0.5,image/*;q=0.6,video/*,audio/*,*/*;q=0.6";
	// public static final String Accept = "*/*";
	public static final String Accept_Encoding = "gzip;q=1.0,identity;q=0.8";
	// public static final String Connection = "Keep-Alive";
	public static final String Connection = "close";
	// #ifdef UA
	// #expand public static final String User_Agent = "%UA%";
	// #else
	public static final String User_Agent = "GGLook";
	// #endif
	// public static final String X_Wap_Profile =
	// "http://nds1.nds.nokia.com/uaprof/N6230r200.xml";

	public static final String Content_Type1 = "application/x-www-form-urlencoded;charset=utf-8";
	public static final String Content_Type2 = "text/html;charset=utf-8";
	// public static final String Connection_2 = "Keep-Alive";
	public static final String Pragma = "no-cache";
	public static final String Host = "10.0.0.172:80";
	public static final byte cmwap1 = 0;
	public static final byte cmwap2 = 1;
	public static final byte client1 = 1;
	public static final byte client2 = 2;
	/** 影子连接 */
	public static final byte client3 = 3;
	public static final byte Client1ConntectTimes = 1;
	public static final byte Client2ConntectTimes = 1;

	public static String additional;
	public static final String additional_1 = "&frpro=gglook&KA=";
	public static String city;
	public static final String OUT_OF_MEMORY_INFO = "返回数据过大，您的手机内存不足。请尝试到设置中关闭特效或降低图片质量，以节省更多内存。";
	public static final String ILLEGAL_INFO = "服务器返回数据异常，请尝试刷新页面。";
	public static final String MOBILE_INFO = "数据被运营商拦截或篡改，请尝试刷新页面。";
	public static final String LOCATION_ERROR_INFO = "无法获取跳转页面的地址，页面跳转被取消。";
}
