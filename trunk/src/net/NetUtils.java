package net;

import java.io.*;

import javax.microedition.io.*;

import utils.StringUtil;
import net.NQ;
import net.NetConnection;

public class NetUtils {
	// private final static String X_Wap_Profile = "x-wap-profile";
	// private final static String User_Agent = "User-Agent";
	private final static String X_Online_Host = "X-Online-Host";
	private final static String Content_Type = "Content-Type";
	private final static String Content_Length = "Content-Length";
	private static byte ConnectType = 0x00;// SettingData.CONNECTION_PORT;

	// public static void changeContentType() {
	// SettingData.CONNECTION_PORT = ConnectType == NQ.cmwap1 ?NQ.cmwap2:
	// NQ.cmwap1;
	// }

	public static HttpConnection createHttpConn(String url, byte[] data)
			throws IOException {
		String _X_Online_Host = null;
		String _url = url;
		if (ConnectType == NQ.cmwap2) {
			_X_Online_Host = StringUtil.getOnlineHost(url);
			_url = StringUtil.getCmwapUrl(url);
		}
		String method = null;
		int permissions = 0;
		if (data == null) {
			method = HttpConnection.GET;
			permissions = Connector.READ;
		} else {
			method = HttpConnection.POST;
			permissions = Connector.READ_WRITE;
		}
		HttpConnection conn = null;
		conn = (HttpConnection) Connector.open(_url, permissions, true);
		conn.setRequestMethod(method);

		if (null != _X_Online_Host)
			conn = NetConnection.setRequestProperty(conn, X_Online_Host,
					_X_Online_Host);
		// conn = NetConnection
		// .setRequestProperty(conn, User_Agent, NQ.User_Agent);
		// conn = NetConnection.setRequestProperty(conn, X_Wap_Profile,
		// NQ.X_Wap_Profile);
		// conn = NetConnection.setRequestProperty(conn, "Connection",
		// NQ.Connection);
		// conn = NetConnection.setRequestProperty(conn, "Accept", NQ.Accept);
		if (permissions == Connector.READ_WRITE) {
			conn = NetConnection.setRequestProperty(conn, Content_Type,
					NQ.Content_Type1);
			conn = NetConnection.setRequestProperty(conn, Content_Length,
					String.valueOf(data.length));
			DataOutputStream dos = conn.openDataOutputStream();
			try {
				dos.write(data, 0, data.length);
			} catch (Exception e) {
			} finally {
				try {
					if (dos != null)
						dos.close();
				} catch (Exception e) {
				} finally {
					dos = null;
				}
			}
		}
		// databank.netThreadNum++;
		return conn;
	}

	// public static long getFileLenth(String url) {
	// long nFileLength = -1;
	// HttpConnection httpConn = null;
	// try {
	// while (url != null) {
	// httpConn = createHttpConn(url, null);
	//
	// int responseCode = httpConn.getResponseCode();
	// if ((responseCode == HttpConnection.HTTP_TEMP_REDIRECT)
	// || (responseCode == HttpConnection.HTTP_MOVED_TEMP)
	// || (responseCode == HttpConnection.HTTP_MOVED_PERM)) {
	// try {
	// url = httpConn.getHeaderField("location").trim();
	// } catch (Exception e) {
	// url = null;
	// }
	// } else if (responseCode >= HttpConnection.HTTP_BAD_REQUEST) {
	// url = null;
	// return -2; // -2 represent access is error
	// } else {
	// String type = httpConn.getRequestProperty("Content-Type");
	// if (type != null) {
	// if (type.indexOf("text") != -1) {
	// continue;
	// }
	// }
	// url = null;
	// nFileLength = httpConn.getLength();
	// }
	// }
	// } catch (IOException e) {
	// } finally {
	// try {
	// if (httpConn != null)
	// httpConn.close();
	// } catch (Exception ex) {
	// } finally {
	// httpConn = null;
	// }
	// databank.netThreadNum--;
	// }
	// return nFileLength;
	// }

	public static byte[] downloadImage(String url, byte[] postData)
			throws Exception {
		if (url == null)
			return null;
		HttpConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		try {
			// if (databank.isLittleMemory())
			// System.gc();
			conn = createHttpConn(url, postData);
			int responceCode = conn.getResponseCode();
			if (responceCode == HttpConnection.HTTP_OK) {
				is = conn.openInputStream();
				bos = new ByteArrayOutputStream();
				byte[] data = new byte[512];
				int ch = -1;
				while ((ch = is.read(data)) != -1) {
					bos.write(data, 0, ch);
				}
				data = null;
				bos.flush();
				data = bos.toByteArray();
				return data;
			} else {
				throw new Exception("图片路径错误或者图片已损坏，本张图片下载失败!");
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (bos != null)
					bos.close();
			} catch (Exception e) {
			} finally {
				bos = null;
			}
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			} finally {
				is = null;
			}
			try {
				if (conn != null)
					conn.close();
			} catch (IOException e) {
			} finally {
				conn = null;
			}
			// databank.netThreadNum--;
//			System.gc();
		}
	}
}
