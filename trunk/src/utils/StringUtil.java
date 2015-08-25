package utils;

import java.io.IOException;
import java.util.Enumeration;
import javax.microedition.lcdui.game.Sprite;
import zincfish.zinccss.ICSSConstants;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>StringUtil</code> 工具类封装了对字符串的常用操作方法, 其中很多方法用于处理URL.
 * 
 * @author Jarod Yv
 */
public class StringUtil {
	/**
	 * 字符串替换
	 * 
	 * @param src
	 *            String 源字符
	 * @param inalt
	 *            String 查找字符
	 * @param realt
	 *            String 替换字符
	 * @return String
	 */
	public final static String replace(String src, String inalt, String realt) {
		if (null == src)
			return null;
		StringBuffer placeString = new StringBuffer();
		int infor = 0;
		int ifn = 0;
		int inaltLen = inalt.length();
		while (true) {
			ifn = src.indexOf(inalt, infor);
			if (-1 < ifn) {
				placeString.append(src.substring(infor, ifn));
				placeString.append(realt);
				infor = inaltLen + ifn;
			} else {
				if (0 == infor)
					return src;
				else
					return placeString.toString() + src.substring(infor);
			}
		}
	}

	// public static String intercept(String src, String startStr, String
	// endStr) {
	// int startIndex;
	// if ((startIndex = src.indexOf(startStr)) == -1)
	// return null;
	// startIndex += startStr.length();
	// int endIndex;
	// if ((endIndex = src.indexOf(endStr, startIndex + 1)) == -1)
	// return null;
	// return src.substring(startIndex, endIndex);
	// }

	/**
	 * 将字符串中的汉字转换成UNICODE编码
	 * 
	 * @param s
	 *            要处理的字符串
	 * @return UNICODE编码的字符串
	 */
	public static final String string2utf(String s) {
		try {
			StringBuffer strbuffer = new StringBuffer();
			byte[] b = s.getBytes("UTF-8");
			// byte[] b = null;
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// DataOutputStream outputstream = new DataOutputStream(baos);
			// outputstream.writeUTF(s);
			// b = baos.toByteArray();
			// outputstream.close();
			// outputstream = null;
			// baos.close();
			// baos = null;

			for (int i = 0; i < b.length; i++) {
				if (Integer.toHexString(b[i]).length() > 2) {
					strbuffer.append('%');
					strbuffer.append(Integer.toHexString(b[i]).substring(6,
							Integer.toHexString(b[i]).length()).toUpperCase());
				} else {
					strbuffer.append((char) b[i]);
				}
			}
			return strbuffer.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 拼接字符串
	 * 
	 * @param str1
	 * @param str2
	 * @param str3
	 * @return
	 */
	public static final String conCatPostStr(String str1, String str2,
			String str3) {
		if (str3 == null || str1 == null)
			return str3;
		StringBuffer strbuffer = new StringBuffer(str3);
		strbuffer.append(str1);
		strbuffer.append('=');
		strbuffer.append(str2 == null ? ZMLTag.NONE_VALUE : str2);
		strbuffer.append('&');

		return strbuffer.toString();
	}

	public static final String conCatGetStr(String str1, String str2,
			String str3) {

		StringBuffer strbuffer = new StringBuffer();
		String temp_url1 = null, temp_url2 = null;
		int i = 0;

		if ((i = str1.indexOf('#', 0)) != -1) {
			strbuffer.append(str1.substring(0, i));
			if (str1.indexOf('=', 0) != -1)
				strbuffer.append('&');
			else if (str1.indexOf('?', 0) == -1)
				strbuffer.append('?');
		} else {
			strbuffer.append(str1);
			if (str1.indexOf('=', 0) != -1)
				strbuffer.append('&');
			else if (str1.indexOf('?', 0) == -1)
				strbuffer.append('?');
		}

		strbuffer.append(str2);

		if (i != -1)
			strbuffer.append(str1.substring(i, str1.length()));
		if (strbuffer.charAt(strbuffer.length() - 1) == '&')
			strbuffer.deleteCharAt(strbuffer.length() - 1);
		if (str3 != null && temp_url1 != null && temp_url2 != null) {
			strbuffer.append(temp_url2);
			strbuffer.append("requestCharsetConvert(utf8,");
			strbuffer.append(str3);
			strbuffer.append(")");
		}

		return strbuffer.toString();
	}

	/**
	 * 将字符串中的汉字转换成UNICODE编码
	 * 
	 * @param s
	 *            要处理的字符串
	 * @return UNICODE编码的字符串
	 */
	public static final String encodeChineseCharacters(String s) {
		try {
			byte[] b = s.getBytes("UTF-8");
			// byte[] b = null;
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// DataOutputStream outputstream = new DataOutputStream(baos);
			// outputstream.writeUTF(s);
			// b = baos.toByteArray();
			// outputstream.close();
			// outputstream = null;
			// baos.close();
			// baos = null;

			StringBuffer strbuffer = new StringBuffer();
			for (int i = 2; i < b.length; i++) {
				if (Integer.toHexString(b[i]).length() > 2) {
					strbuffer.append('%');
					strbuffer.append(Integer.toHexString(b[i]).substring(6,
							Integer.toHexString(b[i]).length()).toUpperCase());
				} else {
					strbuffer.append((char) b[i]);
				}
			}
			return strbuffer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	// public static final String cmwapUrl(String str) {
	//
	// StringBuffer strbuffer = new StringBuffer();
	// strbuffer.append("http://10.0.0.172");
	// strbuffer.append(str.substring(str.indexOf('/', 7), str.length()));
	//
	// return strbuffer.toString();
	// }
	/**
	 * 
	 * @param url
	 * @return
	 */
	public final static String getOnlineHost(String url) {
		int indo = url.indexOf("/", 7);
		if (-1 == indo)
			indo = url.indexOf("?", 7);
		if (indo > -1)
			return url.substring(7, indo);
		else
			return url;
	}

	public static final String getCmwapUrl(String url) {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("http://10.0.0.172");
		int indo = url.indexOf("/", 7);
		if (-1 == indo)
			indo = url.indexOf("?", 7);
		if (indo > -1)
			strbuf.append(url.substring(indo));
		return strbuf.toString();
	}

	// public static String intercept(String src, String startStr, int
	// endPosition) {
	// int index;
	// if ((index = src.indexOf(startStr, endPosition)) != -1)
	// return src.substring(index);
	// else
	// return "";
	// }
	// public static final String proxyUrl(String url, String ip) {
	//
	// StringBuffer strbuffer = new StringBuffer();
	//
	// strbuffer.append("http://" + ip + "/&ggurl=http_");
	//
	// strbuffer.append(url.substring(7, url.length()));
	//
	// return strbuffer.toString();
	// }

	// public static final String directUrl(String url) {
	//
	// StringBuffer strbuffer = new StringBuffer();
	//
	// strbuffer.append("http://10.0.0.172/&ggurl=http_");
	//
	// strbuffer.append(url.substring(7, url.length()));
	//
	// return strbuffer.toString();
	// }

	// public static final String noproxyUrl(String url) {
	//
	// StringBuffer strbuffer = new StringBuffer();
	// strbuffer.append(url.substring(0, url.indexOf("_ggnoproxy")));
	// return strbuffer.toString();
	// }

	// public static final boolean isUrl(String url) {
	// return url.toLowerCase().startsWith("http://");
	// }

	// public static final String adjustUrl(String url) {
	// StringBuffer strbuffer = new StringBuffer();
	// if (!url.toLowerCase().startsWith("http://"))
	// strbuffer.append("http://");
	// strbuffer.append(url);
	//
	// if (strbuffer.toString().indexOf('/', 7) == -1)
	// strbuffer.append('/');
	//
	// return strbuffer.toString();
	// }

	// public static final String ampUrl(String url) {
	//
	// StringBuffer strbuffer = new StringBuffer();
	//
	// int i = 0;
	//
	// while ((i = url.indexOf("&amp;", i)) != -1) {
	// strbuffer.append(url.substring(0, i));
	// strbuffer.append('&');
	// strbuffer.append(url.substring(i + 5, url.length()));
	// url = strbuffer.toString();
	// strbuffer.delete(0, strbuffer.length());
	// i = 0;
	// }
	//
	// while ((i = url.indexOf(' ', i)) != -1) {
	// strbuffer.append(url.substring(0, i));
	// strbuffer.append('+');
	// strbuffer.append(url.substring(i + 1, url.length()));
	// url = strbuffer.toString();
	// strbuffer.delete(0, strbuffer.length());
	// i = 0;
	// }
	//
	// if (url.indexOf('?', 0) != -1
	// && url.indexOf('?', 0) < url.indexOf(';', 0)) {
	//
	// i = 0;
	//
	// while ((i = url.indexOf(';', i)) != -1) {
	//
	// strbuffer.append(url.substring(0, i));
	//
	// strbuffer.append("%3B");
	//
	// strbuffer.append(url.substring(i + 1, url.length()));
	//
	// url = strbuffer.toString();
	//
	// strbuffer.delete(0, strbuffer.length());
	//
	// i = 0;
	//
	// }
	// }
	//
	// return url;
	// }

	public static final String replaceString(String url, String value) {
		StringBuffer stringbuffer = new StringBuffer();
		int currentpos = url.indexOf('$', 0);
		stringbuffer.append(url.substring(0, currentpos));
		stringbuffer.append(value);
		if ((currentpos = url.indexOf(')', currentpos + 1)) != -1)
			stringbuffer.append(url.substring(currentpos + 1, url.length()));
		return stringbuffer.toString();
	}

	public final static int Str2Int(String str) {
		if (str == null)
			return 0;
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static final long Str2Long(String str) {
		if (str == null)
			return 0L;
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return 0L;
		}
	}

	public static final boolean Str2Bool(String str) {
		if (str == null)
			return false;
		if (str.equals(ZMLTag.TRUE_VALUE)) {
			return true;
		} else if (str.equals(ZMLTag.FALSE_VALUE)) {
			return false;
		} else {
			str = str.toLowerCase();
			return str.equals(ZMLTag.TRUE_VALUE) ? true : false;
		}
	}

	/**
	 * 将翻转字符串转换成相应的整型量
	 * 
	 * @param str
	 *            翻转字符串
	 * 
	 * @return 转换后的值
	 */
	public static final int rotation2Int(String str) {
		if (str != null) {
			if (str.equals(ICSSConstants.IMG_TRANS_MIRROR)) {
				return Sprite.TRANS_MIRROR;
			} else if (str.equals(ICSSConstants.IMG_TRANS_MIRROR_ROT270)) {
				return Sprite.TRANS_MIRROR_ROT270;
			} else if (str.equals(ICSSConstants.IMG_TRANS_MIRROR_ROT180)) {
				return Sprite.TRANS_MIRROR_ROT180;
			} else if (str.equals(ICSSConstants.IMG_TRANS_MIRROR_ROT90)) {
				return Sprite.TRANS_MIRROR_ROT90;
			} else if (str.equals(ICSSConstants.IMG_TRANS_ROT270)) {
				return Sprite.TRANS_ROT270;
			} else if (str.equals(ICSSConstants.IMG_TRANS_ROT180)) {
				return Sprite.TRANS_ROT180;
			} else if (str.equals(ICSSConstants.IMG_TRANS_ROT90)) {
				return Sprite.TRANS_ROT90;
			}
		}
		return Sprite.TRANS_NONE;
	}

	// public static final String handlePath(String path) {
	// if (path == null) {
	// return null;
	// }
	//
	// path = !path.startsWith("/widgets") ? "/widgets/" + path : path;
	//
	// StringBuffer sb = new StringBuffer();
	// for (int i = 0; i < path.length(); i++) {
	// char c = path.charAt(i);
	// if (c == '\\') {
	// if (sb.toString().endsWith("/")) {
	// continue;
	// } else {
	// sb.append('/');
	// }
	// } else {
	// sb.append(c);
	// }
	//
	// }
	//
	// return sb.toString();
	// }

	/**
	 * URL 连接处理
	 * 
	 * @param Host
	 *            String
	 * @param url
	 *            String
	 * @return String
	 */
	public static final String handUrl(String Host, String url) {
		if (null == Host && null == url)
			return null;
		if (null == Host) {
			return replace(url, "&amp;", "&");
		} else if (null == url) {
			return Host;
		}
		return getAbsoluteURL(Host, url);
	}

	public final static String getAbsoluteURL(String host, String relatedURL) {
		relatedURL = replace(relatedURL, "&amp;", "&");
		if (relatedURL == null || host == null)
			return null;

		if (relatedURL.startsWith(HTTP)) {
			if (relatedURL.startsWith("http://."))
				relatedURL = relatedURL.substring(7);
			else
				return relatedURL;
		} else if (relatedURL.startsWith("/")) {
			int index = 0;
			int ori = 1;
			while ((index = relatedURL.indexOf("./", ori)) != -1) {
				ori = index + 2;
			}
			return getURLRoot(host) + relatedURL.substring(ori);
		} else if (relatedURL.startsWith("#")) {
			return relatedURL;
		}

		host = getURLBase(host);
		int level = 0;
		int start = 0;
		int end = 0;
		String seg = null;
		while ((end = relatedURL.indexOf('/', start)) != -1) {
			seg = relatedURL.substring(start, end);
			if (seg.equals("..")) {
				level++;
			} else if (!seg.equals(".")) {
				seg = null;
				break;
			}
			start = end + 1;
			seg = null;
		}
		if (start == 0)
			return host + relatedURL;
		relatedURL = relatedURL.substring(start);

		if (level > 0) {
			end = 0;
			start = 7;
			int folder = 0;
			int[] pos = new int[20];
			while ((end = host.indexOf('/', start)) != -1) {
				if (end > start) {
					pos[folder] = ++end;
					folder++;
					start = end;
				} else
					break;
			}
			if (folder <= level)
				return getURLRoot(host) + relatedURL;
			host = host.substring(0, pos[folder - level - 1]);
			pos = null;
			return host + relatedURL;
		} else {
			return host + relatedURL;
		}
	}

	private static final String HTTP = "http://";

	public final static String getURLRoot(String url) {
		if (url == null) {
			return "";
		}
		if (url.startsWith(HTTP)) {
			url = url.substring(7);
		}
		int i = url.length();
		if (i <= 0) {
			return "";
		}

		int j = 0;
		while (j < i && url.charAt(j) != '/' && url.charAt(j) != '\\') {
			j++;
		}
		if (j < i) {
			url = url.substring(0, j + 1);
		} else {
			url += '/';
		}
		return HTTP + url;
	}

	public final static String getURLBase(String url) {
		String tempUrl = url;
		if (tempUrl == null) {
			return "";
		}
		int loc = tempUrl.indexOf("?");
		if (loc != -1)
			tempUrl = tempUrl.substring(0, loc);
		if (!tempUrl.startsWith(HTTP)) {
			tempUrl = HTTP + tempUrl;
		}
		int j = tempUrl.length() - 1;
		while (j >= 0 && tempUrl.charAt(j) != '/' && tempUrl.charAt(j) != '\\') {
			j--;
		}
		tempUrl = tempUrl.substring(0, j + 1);
		if (tempUrl.equals(HTTP)) {
			tempUrl = url + '/';
		}
		return tempUrl;
	}

	public final static String getFileName(String url) {
		String fileName = "unnamed";
		if (url != null) {
			int index = url.lastIndexOf('/');
			if (index > 0)
				fileName = url.substring(index + 1);
			else
				fileName = url;
		}
		return fileName;
	}

	public final static String changeProperty(String src, String property,
			String value) {
		if (src == null || property == null)
			return src;
		int index = src.indexOf(property);
		if (index == -1)
			return null;
		StringBuffer sb = new StringBuffer();
		String fore = src.substring(0, index);
		if (fore != null) {
			sb.append(fore);
			fore = null;
		}
		sb.append(property);
		sb.append('=');
		sb.append(value == null ? "" : value);
		index = src.indexOf("&", index);
		if (index != -1)
			fore = src.substring(index);
		if (fore != null) {
			sb.append(fore);
			fore = null;
		}
		return sb.toString();
	}

	/**
	 * 函数作用: 找到以dir开头的目录 返回prefix + 目标值
	 */
	public static String getStartWithDir(Enumeration _enum, String dir,
			String prefix) {
		String elements;
		while (_enum.hasMoreElements()) {
			elements = (String) _enum.nextElement();
			if (elements.startsWith(dir) && elements.endsWith("/")) {
				return prefix + elements;
			}
		}
		return null;
	}
}
