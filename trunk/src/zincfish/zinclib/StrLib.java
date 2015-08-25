package zincfish.zinclib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

import utils.ArrayList;
import zincfish.zincscript.ZSException;

/**
 * <code>StrLib</code>库封装了与存储相关的库函数
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class StrLib extends AbstractLib {
	private static final int FUNCTION_NUM = 7;

	private static final String OPEN_RECORD = "_zsrOpenRecord";
	private static final byte OPEN_RECORD_CODE = 1;
	private static final String DELETE_RECORD = "_zsrDeleteRecord";
	private static final byte DELETE_RECORD_CODE = 2;
	private static final String CLOSE_RECORD = "_zsrCloseRecord";
	private static final byte CLOSE_RECORD_CODE = 3;
	private static final String GET_RECORDS_NUM = "_zsrGetRecordsNum";
	private static final byte GET_RECORDS_NUM_CODE = 4;
	private static final String SET_RECORD = "_zsrSetRecord";
	private static final byte SET_RECORD_CODE = 5;
	private static final String GET_STRING = "_zsrGetString";
	private static final byte GET_STRING_CODE = 6;
	private static final String GET_INT = "_zsrGetInt";
	private static final byte GET_INT_CODE = 7;
    
	private static final String GET_VALUE = "_zsrGetValueByName";
	private static final byte GET_VALUE_CODE = 8;
	private static final String SET_VALUE = "_zsrSetValueByName";
	private static final byte SET_VALUE_CODE = 9;
	
	private RecordStore rs = null;// RMS记录
	
	// 字符串编码
	private String ENCODING = "UTF-8";
	/*
	 * 所有库都采用单例模式，确保内存中已有一份库的实例
	 */
	private static AbstractLib instance = null;

	public static AbstractLib getInstance() {
		if (instance == null) {
			instance = new StrLib();
			instance.createFunctionMap();
		}
		return instance;
	}

	public Object callFunction(String name, ArrayList params)
			throws ZSException {
		Byte code = (Byte) functionMap.get(name);
		if (code != null) {
			switch (code.byteValue()) {
			case OPEN_RECORD_CODE:
				_zsrOpenRecord(params);
				return null;
			case DELETE_RECORD_CODE:
				_zsrDeleteRecord(params);
				return null;
			case CLOSE_RECORD_CODE:
				_zsrCloseRecord(params);
				return null;
			case GET_RECORDS_NUM_CODE:
				return _zsrGetRecordsNum(params);
			case SET_RECORD_CODE:
				_zsrSetRecord(params);
				return null;
			case GET_STRING_CODE:
				return _zsrGetString(params);
			case GET_INT_CODE:
				return _zsrGetInt(params);
			case GET_VALUE_CODE:
				return _zsrGetValueByName(params);
			case SET_VALUE_CODE:
				_zsrSetValueByName(params);
				return null;
			default:
				throw new ZSException("函数" + name + "不存在");
			}
		} else {
			throw new ZSException("函数" + name + "不存在");
		}
	}

	protected void createFunctionMap() {
		functionMap = new Hashtable(FUNCTION_NUM);
		functionMap.put(OPEN_RECORD, new Byte(OPEN_RECORD_CODE));
		functionMap.put(DELETE_RECORD, new Byte(DELETE_RECORD_CODE));
		functionMap.put(CLOSE_RECORD, new Byte(CLOSE_RECORD_CODE));
		functionMap.put(GET_RECORDS_NUM, new Byte(GET_RECORDS_NUM_CODE));
		functionMap.put(SET_RECORD, new Byte(SET_RECORD_CODE));
		functionMap.put(GET_STRING, new Byte(GET_STRING_CODE));
		functionMap.put(GET_INT, new Byte(GET_INT_CODE));
		functionMap.put(GET_VALUE, new Byte(GET_VALUE_CODE));
		functionMap.put(SET_VALUE, new Byte(SET_VALUE_CODE));
	}

	/*
	 * 打开RMS记录
	 */
	private final void _zsrOpenRecord(ArrayList params) {
		String recordName = (String) params.get(0);
		try {
			rs = RecordStore.openRecordStore(recordName, true);
		} catch (Exception ex) {
		}
		recordName = null;
	}

	/*
	 * 删除RMS记录
	 */
	private final void _zsrDeleteRecord(final ArrayList params) {
		String recordName = (String) params.get(0);
		try {
			_zsrCloseRecord(params);
			RecordStore.deleteRecordStore(recordName);
		} catch (Exception e) {
		}
	}

	/*
	 * 关闭打开的RMS记录
	 */
	private final void _zsrCloseRecord(ArrayList params) {
		if (rs != null) {
			try {
				rs.closeRecordStore();
			} catch (Exception ex) {
			} finally {
				rs = null;
			}
		}
	}

	private final Integer _zsrGetRecordsNum(ArrayList params) {
		int num = 0;
		try {
			num = rs == null ? 0 : rs.getNumRecords();
		} catch (Exception ex) {
			num = 0;
		}
		return new Integer(num);
	}

	/*
	 * 加入记录
	 */
	private final void _zsrSetRecord(ArrayList params) {
		int index = -1;
		if (params.size() == 2)
			index = ((Integer) params.remove(0)).intValue();
		Object o = params.get(0);
		byte[] data = null;
		if (o instanceof String) {
			data = String2ByteArr((String) o);
		} else {
			data = int2ByteArr(((Integer) o).intValue());
		}
		o = null;
		int len = data == null ? 0 : data.length;
		try {
			if (index < 0)
				rs.addRecord(data, 0, len);
			else
				rs.setRecord(index, data, 0, len);
		} catch (Exception ex) {
		}
		data = null;
	}

	/*
	 * 获取字符串
	 */
	private final String _zsrGetString(ArrayList params) {
		String str = null;
		int index = ((Integer) params.get(0)).intValue();
		byte[] data = getRecode(index);
		str = byteArr2String(data);
		data = null;
		return str;
	}

	private final Integer _zsrGetInt(ArrayList params) {
		int i = Integer.MIN_VALUE;
		int index = ((Integer) params.get(0)).intValue();
		byte[] data = getRecode(index);
		i = byteArr2Int(data);
		data = null;
		return new Integer(i);
	}
	
	private final String _zsrGetValueByName(ArrayList params) {
//		String recordName = (String ) params.get(0);
		_zsrOpenRecord(params); // 打开
		String keyName = (String ) params.get(1);
		String keyValue = null;
		try {
			int rid = getRecordId(keyName);
			if(rid != -1) {
				byte[] bt = rs.getRecord(rid);
				keyValue = new String(bt, ENCODING);
				if(keyValue != null) {
					keyValue = keyValue.substring((keyName + "-").length());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		_zsrCloseRecord(params); // 关闭
		keyName = null;
		return keyValue;
	}
	
	/** 通过键名 获取对应的键值 */
	public int getRecordId(String keyName) {
		RecordEnumeration re = null;
		try {
			re = rs.enumerateRecords(null, null, false);
			while (re.hasNextElement()) {
				int rid = re.nextRecordId(); // 获取record id
				byte[] bt = rs.getRecord(rid); // 通过id 获取bytes
				if (bt == null || bt.length == 0) {
					continue;
				}
				String str = new String(bt, ENCODING);
				if(str != null && str.startsWith(keyName + "-")) {
					bt = null;
					str = null;
					return rid;
				}
				bt = null;
				str = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			re.destroy();
		}
		return -1;
	}
	/** 存储数据 */
	private final void _zsrSetValueByName(ArrayList params) {
		_zsrOpenRecord(params);
		String keyName = (String ) params.get(1);
		String keyValue = (String ) params.get(2);
		try {
			if(keyName == null || keyValue == null) {
				return;
			}
			int rid = getRecordId(keyName);
			byte[] bt = (keyName + "-" + keyValue).getBytes(ENCODING);
			if(rid != -1) {
				rs.setRecord(rid, bt, 0, bt.length);
			} else {
				rs.addRecord(bt, 0, bt.length);
			}
			bt = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			_zsrCloseRecord(params);
		}
		keyValue = null;
		keyName = null;
	}

	public byte[] getRecode(int id) {
		byte[] dataByte = null;
		try {
			dataByte = rs.getRecord(id);
		} catch (Exception ex) {
		}
		return dataByte;
	}

	public byte[] int2ByteArr(int i) {
		byte[] intData = new byte[4];
		intData[0] = (byte) ((i & 0xff000000) >> 24);
		intData[1] = (byte) ((i & 0x00ff0000) >> 16);
		intData[2] = (byte) ((i & 0x0000ff00) >> 8);
		intData[3] = (byte) (i & 0x000000ff);
		return intData;
	}

	public int byteArr2Int(byte[] b) {
		if (b == null || b.length != 4) {
			return 0;
		} else {
			int i = ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16)
					| ((b[2] & 0xff) << 8) | (b[3] & 0xff);
			return i;
		}
	}

	public static byte[] String2ByteArr(String commentContent) {
		if (commentContent == null)
			return null;
		byte[] buffer = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeUTF(commentContent);
			buffer = baos.toByteArray();
		} catch (IOException ex) {
		} finally {
			try {
				baos.close();
				dos.close();
			} catch (IOException ex1) {
			}
			baos = null;
			dos = null;
		}
		return buffer;
	}

	public String byteArr2String(byte[] bs) {
		if (bs == null) {
			return null;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(bs);
		DataInputStream dis = new DataInputStream(bais);
		String ts = "";
		try {
			ts = dis.readUTF();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				dis.close();
				bais.close();
			} catch (IOException ex1) {
			}
			dis = null;
			bais = null;
		}
		return ts;
	}
}
