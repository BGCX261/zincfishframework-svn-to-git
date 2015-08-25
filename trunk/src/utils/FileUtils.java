package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import zincfish.zincdom.SNSUnitDOM;

public class FileUtils {

	public static final String DOWNLOAD_PATH = "download/";
	public static final String IMAGE_PATH = "image/";
	public static final String PAGE_PATH = "page/";
	public static final String FILEURL = "file:///";
	public static final String MEGA_ROOT = "/";
	public static final String SNS_PATH = null;

	public static final DataInputStream getFileInputStream(String path,
			String name) {
		name = convertURL2BufferFileName(name);
		FileConnection fileConnection = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(FILEURL);
			sb.append(SNS_PATH);
			sb.append(PAGE_PATH);
			sb.append(name);
			fileConnection = (FileConnection) Connector.open(sb.toString(),
					Connector.READ);
			if (fileConnection.exists())
				return fileConnection.openDataInputStream();
			else
				return null;
		} catch (Exception e) {
			return null;
		} finally {
			if (fileConnection != null) {
				try {
					fileConnection.close();
				} catch (IOException e) {
				} finally {
					fileConnection = null;
				}
			}
		}
	}

	public static final void write2Buffer(SNSUnitDOM unit) {
		String name = unit.getUrl();
		name = convertURL2BufferFileName(name);
		FileConnection fileConnection = null;
		DataOutputStream dos = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(FILEURL);
			sb.append(SNS_PATH);
			sb.append(PAGE_PATH);
			sb.append(name);
			fileConnection = (FileConnection) Connector.open(sb.toString(),
					Connector.READ);
			if (!fileConnection.exists())
				fileConnection.create();
			dos = fileConnection.openDataOutputStream();
			unit.serialize(dos);
		} catch (Exception e) {
		} finally {
			name = null;
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
				} finally {
					dos = null;
				}
			}
			if (fileConnection != null) {
				try {
					fileConnection.close();
				} catch (IOException e) {
				} finally {
					fileConnection = null;
				}
			}
		}
	}

	public static final SNSUnitDOM readFromBuffer(String url) {
		String name = convertURL2BufferFileName(url);
		FileConnection fileConnection = null;
		DataInputStream dis = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(FILEURL);
			sb.append(SNS_PATH);
			sb.append(PAGE_PATH);
			sb.append(name);
			fileConnection = (FileConnection) Connector.open(sb.toString(),
					Connector.READ);
			if (fileConnection.exists()) {
				dis = fileConnection.openDataInputStream();
				SNSUnitDOM unit = new SNSUnitDOM();
				unit.setUrl(url);
				unit.deserialize(dis);
				return unit;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			url = null;
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
				} finally {
					dis = null;
				}
			}
			if (fileConnection != null) {
				try {
					fileConnection.close();
				} catch (IOException e) {
				} finally {
					fileConnection = null;
				}
			}
		}
	}

	public static final DataOutputStream getFileOutputStream(String path,
			String name) {
		name = convertURL2BufferFileName(name);
		FileConnection fileConnection = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(FILEURL);
			sb.append(SNS_PATH);
			sb.append(PAGE_PATH);
			sb.append(name);
			fileConnection = (FileConnection) Connector.open(sb.toString(),
					Connector.READ);
			if (fileConnection.exists())
				fileConnection.delete();

			fileConnection.create();
			return fileConnection.openDataOutputStream();
		} catch (Exception e) {
			return null;
		} finally {
			if (fileConnection != null) {
				try {
					fileConnection.close();
				} catch (IOException e) {
				} finally {
					fileConnection = null;
				}
			}
		}
	}

	/**
	 * 判断缓存中是否含有此文件
	 * 
	 * @param path
	 *            文件夹
	 * @param name
	 *            文件名
	 * @return <ul>
	 *         <li><code>true</code> - 含有此文件
	 *         <li><code>false</code> - 不含有此文件
	 *         </ul>
	 */
	public static final boolean hasThisFile(String path, String name) {
		name = convertURL2BufferFileName(name);
		FileConnection fileConnection = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(FILEURL);
			sb.append(SNS_PATH);
			sb.append(PAGE_PATH);
			sb.append(name);
			fileConnection = (FileConnection) Connector.open(sb.toString(),
					Connector.READ);
			sb = null;
			return fileConnection.exists();
		} catch (Exception e) {
			return false;
		} finally {
			if (fileConnection != null) {
				try {
					fileConnection.close();
				} catch (IOException e) {
				} finally {
					fileConnection = null;
				}
			}
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 *            目录名
	 * @return <ul>
	 *         <li><code>true</code> - 创建成功
	 *         <li><code>false</code> - 创建失败
	 *         </ul>
	 */
	public static final boolean createPath(String path) {
		FileConnection fileConnection = null;
		try {
			fileConnection = (FileConnection) Connector.open(path,
					Connector.READ_WRITE);
			if (!fileConnection.exists())
				fileConnection.mkdir();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (fileConnection != null) {
				try {
					fileConnection.close();
				} catch (IOException e) {
				} finally {
					fileConnection = null;
				}
			}
		}
	}

	/**
	 * 将url地址转换成缓存文件名<br>
	 * 转换规则为:'/'替换成'_'; '?'替换成'-'
	 * 
	 * @param url
	 *            URL地址
	 * @return 转换后的缓存文件名
	 */
	private static final String convertURL2BufferFileName(String url) {
		if (url == null)
			return null;
		url.replace('/', '_');
		url.replace('?', '-');
		return url;
	}

}
