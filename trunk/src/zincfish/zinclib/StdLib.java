package zincfish.zinclib;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.TimeZone;
import midlet.SNSMIDlet;
import screen.BrowserScreen;
import utils.ArrayList;
import utils.DOMUtil;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSUnitDOM;
import zincfish.zincscript.ZSException;
import zincfish.zincwidget.AbstractSNSComponent;
import zincfish.zincwidget.SNSBodyComponent;
import com.mediawoz.akebono.corerenderer.CRDisplay;
import data.UnitBuffer;

/**
 * <code>StdLib</code>是一个预先实现的标准函数库<br>
 * 库中实现了一些常用方法，方便脚本使用者编写程序<br>
 * 为了解析上方便，规定库函数一律用下划线开头
 * 
 * @author Jarod Yv
 */
public final class StdLib extends AbstractLib {
	private static final int FUNCTION_NUM = 10;
	private static final String PRINTLN = "_zssprintln";
	private static final byte PRINTLN_CODE = 2;
	private static final String EXIT = "_zssExit";
	private static final byte EXIT_CODE = 3;
	private static final String SUB_STRING = "_zssSubString";
	private static final byte SUB_STRING_CODE = 4;
	private static final String STRING_LEN = "_zssStringLen";
	private static final byte STRING_LEN_CODE = 5;
	private static final String RANDOM = "_zssRandom";
	private static final byte RANDOM_CODE = 6;
	private static final String SWITCH = "_zssSwitch";
	private static final byte SWITCH_CODE = 8;
	private static final String GET_CURRENT_DATE = "_zssGetCurrentDate";
	private static final byte GET_CURRENT_DATE_CODE = 10;
	/** @author jiangwei */
	/** 获取系统当前时间 参数形式:() <li>return String 类型 */
	private static final String GET_SYSTEM_TIME = "_zssGetSystemTime";
	private static final byte GET_SYSTEM_TIME_CODE = 11;
	/**
	 * 比较当前String 是否以指定的String开头 <strong>相当于java String类中的startsWith方法</strong>
	 * 参数形式:(String content, String startW) <li>return Integer类型 1为true 0为false
	 */
	private static final String START_WITH = "_zssStartsWith";
	private static final byte START_WITH_CODE = 56;
	/**
	 * 通过组件ID设置改组件是否可见<li>1 可见 <li>0 不可见 参数形式:(String domid, int i) void类型
	 */
	private static final String SET_COMPONENT_VISIABLE = "_zssSetCompVisiable";
	private static final byte SET_COMPONENT_VISIABLE_CODE = 57;
	private static final String SLEEP = "_zssSleep";
	private static final byte SLEEP_CODE = 100;

	private static final String GET_SCREEN_WIDTH = "_zssGetScreenWidth";
	private static final byte GET_SCREEN_WIDTH_CODE = 106;

	private static final String SHOW_POP_WINDOW = "_zssShowPopWindow";
	private static final byte SHOW_POP_WINDOW_CODE = 107;
	private static final String HIDE_POP_WINDOW = "_zssHidePopWindow";
	private static final byte HIDE_POP_WINDOW_CODE = 108;
	private static final String STR_INT = "_zssStr2Int";
	private static final byte STR_INT_CODE = 110;
	private static final String GET_GLOBAL_VAR_VALUE = "_zssGetGlobalVarValue"; // 获取变量的值
	private static final byte GET_GLOBAL_VAR_VALUE_CODE = 111;
	private static final String SET_GLOBAL_VAR_VALUE = "_zssSetGlobalVarValue";
	private static final byte SET_GLOBAL_VAR_VALUE_CODE = 112;
	// 测试用
	private static final String GET_DOM_CLASS_VALUE = "_zssGetDOMValue";
	private static final byte GET_DOM_CLASS_VALUE_CODE = 113;
	private static final String GET_COM_CLASS_VALUE = "_zssGetCOMValue";
	private static final byte GET_COM_CLASS_VALUE_CODE = 114;

	/**
	 * 所有库都采用单例模式，确保内存中已有一份库的实例
	 */
	private static AbstractLib instance = null;

	public static AbstractLib getInstance() {
		if (instance == null) {
			instance = new StdLib();
			instance.createFunctionMap();
		}
		return instance;
	}

	protected void createFunctionMap() {
		if (functionMap == null)
			functionMap = new Hashtable(FUNCTION_NUM);
		functionMap.put(PRINTLN, new Byte(PRINTLN_CODE));
		functionMap.put(EXIT, new Byte(EXIT_CODE));
		functionMap.put(SUB_STRING, new Byte(SUB_STRING_CODE));
		functionMap.put(STRING_LEN, new Byte(STRING_LEN_CODE));
		functionMap.put(RANDOM, new Byte(RANDOM_CODE));
		functionMap.put(SWITCH, new Byte(SWITCH_CODE));
		functionMap.put(GET_CURRENT_DATE, new Byte(GET_CURRENT_DATE_CODE));
		functionMap.put(GET_SYSTEM_TIME, new Byte(GET_SYSTEM_TIME_CODE));
		functionMap.put(START_WITH, new Byte(START_WITH_CODE));
		functionMap.put(SET_COMPONENT_VISIABLE, new Byte(
				SET_COMPONENT_VISIABLE_CODE));
		functionMap.put(SLEEP, new Byte(SLEEP_CODE));
		functionMap.put(GET_SCREEN_WIDTH, new Byte(GET_SCREEN_WIDTH_CODE));
		functionMap.put(HIDE_POP_WINDOW, new Byte(HIDE_POP_WINDOW_CODE));
		functionMap.put(SHOW_POP_WINDOW, new Byte(SHOW_POP_WINDOW_CODE));
		functionMap.put(STR_INT, new Byte(STR_INT_CODE));
		functionMap.put(GET_GLOBAL_VAR_VALUE, new Byte(
				GET_GLOBAL_VAR_VALUE_CODE));
		functionMap.put(SET_GLOBAL_VAR_VALUE, new Byte(
				SET_GLOBAL_VAR_VALUE_CODE));
		functionMap
				.put(GET_DOM_CLASS_VALUE, new Byte(GET_DOM_CLASS_VALUE_CODE));
		functionMap
				.put(GET_COM_CLASS_VALUE, new Byte(GET_COM_CLASS_VALUE_CODE));
	}

	public Object callFunction(String name, ArrayList params)
			throws ZSException {
		Byte code = (Byte) functionMap.get(name);
		if (code != null) {
			switch (code.byteValue()) {
			case PRINTLN_CODE:
				_zssprintln(params);
				return null;
			case EXIT_CODE:
				_zssExit();
				return null;
			case SUB_STRING_CODE:
				return _zssSubString(params);
			case STRING_LEN_CODE:
				return _zssStringLen(params);
			case RANDOM_CODE:
				return _zssRandom(params);
			case SWITCH_CODE:
				_zssSwitch(params);
				return null;
			case GET_CURRENT_DATE_CODE:
				return _zssGetCurrentDate(params);
			case START_WITH_CODE:
				return _zssStartsWith(params);
			case SET_COMPONENT_VISIABLE_CODE:
				_zssSetCompVisiable(params);
				return null;
			case SLEEP_CODE:
				_zssSleep(params);
				return null;
			case SHOW_POP_WINDOW_CODE:
				_zssShowPopWindow(params);
				return null;
			case HIDE_POP_WINDOW_CODE:
				_zssHidePopWindow(params);
				return null;
			case GET_SYSTEM_TIME_CODE:
				return _zssGetSystemTime();
			case GET_SCREEN_WIDTH_CODE:
				return _zssGetScreenWidth();
			case STR_INT_CODE:
				return _zssStr2Int(params);
			case GET_GLOBAL_VAR_VALUE_CODE:
				return _zssGetGlobalVarValue(params);
			case SET_GLOBAL_VAR_VALUE_CODE:
				return _zssSetGlobalVarValue(params);
			case GET_DOM_CLASS_VALUE_CODE:
				return _zssGetDOMValue(params);
			case GET_COM_CLASS_VALUE_CODE:
				return _zssGetCOMValue(params);
			default:
				throw new ZSException("函数" + name + "不存在");
			}
		} else {
			throw new ZSException("函数" + name + "不存在");
		}
	}

	private Integer _zssStr2Int(ArrayList params) {
		if (params == null || params.size() == 0)
			return null;
		return new Integer(Integer.parseInt((String) params.get(0)));
	}

	private Integer _zssGetScreenWidth() {
		return new Integer(CRDisplay.getWidth());
	}

	private void _zssprint(ArrayList param) {
		if (param == null || param.size() == 0)
			return;
		String s = "";
		for (int i = 0; i < param.size(); i++)
			s += param.get(i);
		System.out.print(s);
		s = null;
	}

	private void _zssShowPopWindow(ArrayList params) {
		// String id = (String) params.get(0);
		// BrowserScreen.getInstance().popWindow(id);
	}

	private void _zssHidePopWindow(ArrayList params) {
//		String id = (String) params.get(0);
//		BrowserScreen.getInstance().hideWindow(id);
	}

	private void _zssprintln(ArrayList param) {
		_zssprint(param);
		System.out.println();
	}

	private void _zssExit() {
		SNSMIDlet.Exit();
	}

	private Object _zssSubString(ArrayList params) {
		String s = (String) params.get(0);
		int b = ((Integer) params.get(1)).intValue();
		int e = ((Integer) params.get(2)).intValue();
		return s.substring(b, e);
	}

	private Object _zssStringLen(ArrayList params) {
		String s = (String) params.get(0);
		int l = s.length();
		s = null;
		return new Integer(l);
	}

	private Random random = null;

	private Object _zssRandom(ArrayList params) {
		if (random == null)
			random = new Random(System.currentTimeMillis());
		int base = 0;
		int i = 0;
		int ignore = Integer.MIN_VALUE;
		if (params.size() == 3)
			ignore = ((Integer) params.remove(2)).intValue();
		if (params.size() == 2)
			base = ((Integer) params.remove(0)).intValue();
		i = ((Integer) params.get(0)).intValue();
		do {
			i = i - base + 1;
			base += Math.abs(random.nextInt()) % i;
		} while (base == ignore);
		return new Integer(base);
	}

	private void _zssSwitch(ArrayList params) {
		String path = (String) params.get(0);

		if (!path.startsWith("/")) {

			String url = null;
			if (params.size() > 1)
				url = (String) params.get(1);
			BrowserScreen.getInstance().loadUnit(path, url);
		} else { // 下面是通过uint ID 获取当前的UINTDOM
			SNSUnitDOM uintdom = UnitBuffer.getInstance().getBufferByDOMID(path);
			UnitBuffer.getInstance().addBuffer(uintdom);
		}

		String url = null;
		if (params.size() > 1)
			url = (String) params.get(1);
		BrowserScreen.getInstance().getZinc().reset();
		BrowserScreen.getInstance().loadUnit(path, url);
	}

	public Object _zssGetCurrentDate(ArrayList params) {
		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("GMT+8:00"));
		StringBuffer date = new StringBuffer();
		date.append(calendar.get(Calendar.YEAR));
		date.append('-');
		date.append(calendar.get(Calendar.MONTH));
		date.append('-');
		date.append(calendar.get(Calendar.DAY_OF_MONTH));
		calendar = null;
		return date.toString();
	}

	public Object _zssGetCurrentDateTime(ArrayList params) {
		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("GMT+8:00"));
		StringBuffer date = new StringBuffer();
		date.append((calendar.get(Calendar.MONTH) + 1));
		date.append('-');
		date.append(calendar.get(Calendar.DAY_OF_MONTH));
		date.append("  ");
		date.append(calendar.get(Calendar.HOUR_OF_DAY));
		date.append(":");
		date.append(calendar.get(Calendar.MINUTE));
		calendar = null;
		return date.toString();
	}

	// 获取全局变量
	public Object _zssGetDOMValue(ArrayList params) {
		String id = (String) params.get(0);
		AbstractSNSDOM dom = DOMUtil.getDOMByID(BrowserScreen.getInstance()
				.getBuffer().getCurrentBuffer(), id);
		if (dom != null) {
			return dom.toString();
		}
		return null;
	}

	// 获取全局变量
	public Object _zssGetCOMValue(ArrayList params) {
		String id = (String) params.get(0);
		AbstractSNSDOM dom = DOMUtil.getDOMByID(BrowserScreen.getInstance()
				.getCurrentUnit().getDom(), id);
		if (dom != null) {
			return dom.toString();
		}
		return null;
	}

	// 获取全局变量
	public Object _zssGetGlobalVarValue(ArrayList params) {
		String var = (String) params.get(0);
		if ("short_msg".equals(var)) {
			return GlobalVar.short_msg + "";
		} else if ("sys_msg".equals(var)) {
			return GlobalVar.sys_msg + "";
		} else if ("leave_words".equals(var)) {
			return GlobalVar.leave_words + "";
		} else if ("comment".equals(var)) {
			return GlobalVar.comment + "";
		}
		return null;
	}

	// 设置全局变量的值
	public Object _zssSetGlobalVarValue(ArrayList params) {
		try {
			String var = (String) params.get(0);
			String value = (String) params.get(1);
			if ("short_msg".equals(var)) {
				GlobalVar.short_msg = Short.parseShort(value);
			} else if ("sys_msg".equals(var)) {
				GlobalVar.sys_msg = Short.parseShort(value);
			} else if ("leave_words".equals(var)) {
				GlobalVar.leave_words = Short.parseShort(value);
			} else if ("comment".equals(var)) {
				GlobalVar.comment = Short.parseShort(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** <li>获取当前系统时间 @author jiangwei */
	public String _zssGetSystemTime() {
		String time = System.currentTimeMillis() + "";
		return time;
	}

	/**
	 * 是否以指定字符串开头<li>是 return 1<li>否 return 0
	 * 
	 * @author jiangwei
	 */
	public Integer _zssStartsWith(ArrayList params) {
		if (params != null && params.size() == 2) {
			String str = (String) params.get(0);
			String prefix = (String) params.get(1);
			if (str.startsWith(prefix)) {
				return new Integer(1);
			}
		}
		return new Integer(0);
	}

	/**
	 * 设置组件是否可见 <li>1 组件可见<li>0 组件不可见
	 * 
	 * @author jiangwei
	 */
	public void _zssSetCompVisiable(ArrayList params) {
		
	}

	public void _zssSleep(ArrayList params) {
		if (params == null || params.size() == 0) {
			return;
		}
		int ms = ((Integer) params.get(0)).intValue();
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** <li>组件的遍历 <li>如果组件聚焦 return 改组件 @author jiangwei */
	public AbstractSNSComponent iteratorComponent(
			AbstractSNSComponent snscomponent) {
		AbstractSNSComponent iAbstractSNSComponent = null;
		for (int i = 0; i < snscomponent.getChildrenNum(); i++) {
			AbstractSNSComponent subcomponent = (AbstractSNSComponent) snscomponent
					.componentAt(i);
			if (subcomponent.hasChildren()) {
				iAbstractSNSComponent = iteratorComponent(subcomponent);
				if (iAbstractSNSComponent != null) {
					return iAbstractSNSComponent;
				}
			}
			if (subcomponent.isFocused()) {
				return subcomponent;
			}
		}
		return null;
	}

	/** <li>组件的遍历 @author jiangwei */
	public int iteratorComponent(AbstractSNSComponent snscomponent,
			String componentID, int index) {
		int iIndex = -1;
		for (int i = 0; i < snscomponent.getChildrenNum(); i++) {
			AbstractSNSComponent subcomponent = (AbstractSNSComponent) snscomponent
					.componentAt(i);
			AbstractSNSDOM dom = subcomponent.getDom();
			if (dom != null && dom.id != null) {
				if (dom.id.equals(componentID)) {
					return index;
				}
			}
			if (subcomponent.hasChildren()) {
				index = 0;
				iIndex = iteratorComponent(subcomponent, componentID, index);
				if (iIndex != -1) {
					return iIndex;
				}
			}
			index = index + 1;
		}
		return -1;
	}
}
