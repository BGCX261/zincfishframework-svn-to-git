package zincfish.zinclib;

import java.util.Hashtable;
import screen.BrowserScreen;
import utils.ArrayList;
import utils.DOMUtil;
import zincfish.zincdom.*;
import zincfish.zincwidget.*;
import zincfish.zincwidget.focus.FocusManager;
import zincfish.zincparser.zmlparser.ZMLTag;
import zincfish.zincscript.ZSException;

public class DOMLib extends AbstractLib {
	private static final int FUNCTION_NUM = 14;

	private static final String GET_ATT_VALUE = "_zsdGetAttValue";
	private static final byte GET_ATT_VALUE_CODE = 1;
	private static final String SET_ATT_VALUE = "_zsdSetAttValue";
	private static final byte SET_ATT_VALUE_CODE = 2;
	private static final String GET_NET_VALUE = "_zsdGetNetValue";
	private static final byte GET_NET_VALUE_CODE = 3;
	private static final String SET_NET_VALUE = "_zsdSetNetValue";
	private static final byte SET_NET_VALUE_CODE = 4;
	private static final String GET_COM_VALUE = "_zsdGetComValue";
	private static final byte GET_COM_VALUE_CODE = 5;
	private static final String SET_COM_VALUE = "_zsdSetComValue";
	private static final byte SET_COM_VALUE_CODE = 6;
	private static final String ADD_DOM = "_zsdAddDom";
	private static final byte ADD_DOM_CODE = 7;
	private static final String DEL_DOM = "_zsdDelDom";
	private static final byte DEL_DOM_CODE = 8;
	private static final String CURRENT_DOM = "_zsdSetCurrentDOM";
	private static final byte CURRENT_DOM_CODE = 9;
	private static final String REFRESH = "_zsdRefresh";
	private static final byte REFRESH_CODE = 10;
	private static final String GET_CHIDREN_NUM = "_zsdGetChildrenNum";
	private static final byte GET_CHIDREN_NUM_CODE = 11;
	private static final String GET_BROTHER_NUM = "_zsdGetBrotherNum";
	private static final byte GET_BROTHER_NUM_CODE = 12;
	private static final String GET_DOM_TYPE = "_zsdGetDomType";
	private static final byte GET_DOM_TYPE_CODE = 13;
	private static final String GET_PARENT_ID = "_zsdGetParentID";
	private static final byte GET_PARENT_ID_CODE = 14;
	private static final String DEL_ALL_CHILDREN = "_zsdDelAllChildren";
	private static final byte DEL_ALL_CHILDREN_CODE = 15;
	private static final String GET_CUR_V_ITEM_ID = "_zsdGetCurVItemID";
	private static final byte GET_CUR_V_ITEM_ID_CODE = 50;
	private static final String GET_CUR_DOM_ID = "_zsdGetFocusID";
	private static final byte GET_CUR_DOM_ID_CODE = 51;
	private static final String SAVE_TEMP_STR = "_zsdSaveTempString";
	private static final byte SAVE_TEMP_STR_CODE = 55;
	private static final String LOAD_TEMP_STR = "_zsdLoadTempString";
	private static final byte LOAD_TEMP_STR_CODE = 56;
	private static final String REMOVE_TEMP_STR = "_zsdRemoveTempString";
	private static final byte REMOVE_TEMP_STR_CODE = 57;
	private static final String CLEAR_SUB_DOMS = "_zsdClearSubDoms";
	private static final byte CLEAR_SUB_DOMS_CODE = 59;
	private static final String GET_DATA_VALUE = "_zsdGetDataValue";
	private static final byte GET_DATA_VALUE_CODE = 60;
	private static final String GET_CHILDREN_NUM = "_zsdGetChildrenNum";
	private static final byte GET_CHILDREN_NUM_CODE = 62;
	private static final String GET_CHILDREN_ID = "_zsdGetChildrenID";
	private static final byte GET_CHILDREN_ID_CODE = 63;

	private AbstractSNSDOM currentDOM = null;
	private Hashtable tempStrTable = new Hashtable();
	/**
	 * 所有库都采用单例模式，确保内存中已有一份库的实例
	 */
	private static AbstractLib instance = null;

	public static AbstractLib getInstance() {
		if (instance == null) {
			instance = new DOMLib();
			instance.createFunctionMap();
		}
		return instance;
	}

	public Object callFunction(String name, ArrayList params)
			throws ZSException {
		Byte code = (Byte) functionMap.get(name);

		if (code != null) {
			switch (code.byteValue()) {
			case GET_ATT_VALUE_CODE:
			case GET_NET_VALUE_CODE:
			case GET_COM_VALUE_CODE:
				String attrValue = _zsdGetAttributeValue(params, code
						.byteValue());
				return attrValue == null ? "" : attrValue;
			case SET_ATT_VALUE_CODE:
			case SET_NET_VALUE_CODE:
			case SET_COM_VALUE_CODE:
				_zsdSetAttributeValue(params, code.byteValue());
				return null;
			case ADD_DOM_CODE:
				_zsdAddDOM(params);
				return null;
			case DEL_DOM_CODE:
				_zsdDelDOM(params);
				return null;
			case CURRENT_DOM_CODE:
				_zsdSetCurrentDOM(params);
				return null;
			case REFRESH_CODE:
				_zsdRefresh(params);
				return null;
			case GET_CHIDREN_NUM_CODE:
				return _zsdGetChidrenNum(params);
			case GET_BROTHER_NUM_CODE:
				return _zsdGetBrotherNum(params);
			case GET_DOM_TYPE_CODE:
				return _zsdGetDomType(params);
			case GET_PARENT_ID_CODE:
				return _zsdGetParentID(params);
			case DEL_ALL_CHILDREN_CODE:
				_zsdDelAllChildren(params);
				return null;
			case GET_CUR_V_ITEM_ID_CODE:
				// return _zsdGetCurVItemID(params);
			case GET_CUR_DOM_ID_CODE:
				// return _zsdGetFocusID(params);
			case SAVE_TEMP_STR_CODE:
				_zsdSaveTempString(params);
				return null;
			case REMOVE_TEMP_STR_CODE:
				_zsdRemoveTempString(params);
				return null;
			case CLEAR_SUB_DOMS_CODE:
				_zsdClearSubDoms(params);
				return null;
			case LOAD_TEMP_STR_CODE:
				return _zsdLoadTempString(params);
			case GET_DATA_VALUE_CODE:
				return _zsdGetDataValue(params);
			case GET_CHILDREN_NUM_CODE:
				return _zsdGetChildrenNum(params);
			case GET_CHILDREN_ID_CODE:
				return _zsdGetChildrenID(params);
			default:
				throw new ZSException("函数" + name + "不存在");
			}
		} else {
			throw new ZSException("函数" + name + "不存在");
		}
	}

	private void _zsdSaveTempString(ArrayList params) {
		if (params == null || params.size() < 2) {
			return;
		}
		tempStrTable.put(params.get(0), params.get(1));
	}

	private String _zsdLoadTempString(ArrayList params) {
		if (params == null || params.size() < 1) {
			return null;
		}
		return (String) tempStrTable.get(params.get(0));
	}

	private void _zsdRemoveTempString(ArrayList params) {
		if (params == null || params.size() < 1) {
			return;
		}
		tempStrTable.remove(params.get(0));
	}

	protected void createFunctionMap() {
		if (functionMap == null)
			functionMap = new Hashtable(FUNCTION_NUM);
		functionMap.put(GET_ATT_VALUE, new Byte(GET_ATT_VALUE_CODE));
		functionMap.put(SET_ATT_VALUE, new Byte(SET_ATT_VALUE_CODE));
		functionMap.put(GET_NET_VALUE, new Byte(GET_NET_VALUE_CODE));
		functionMap.put(SET_NET_VALUE, new Byte(SET_NET_VALUE_CODE));
		functionMap.put(GET_COM_VALUE, new Byte(GET_COM_VALUE_CODE));
		functionMap.put(SET_COM_VALUE, new Byte(SET_COM_VALUE_CODE));
		functionMap.put(ADD_DOM, new Byte(ADD_DOM_CODE));
		functionMap.put(DEL_DOM, new Byte(DEL_DOM_CODE));
		functionMap.put(CURRENT_DOM, new Byte(CURRENT_DOM_CODE));
		functionMap.put(REFRESH, new Byte(REFRESH_CODE));
		functionMap.put(GET_CHIDREN_NUM, new Byte(GET_CHIDREN_NUM_CODE));
		functionMap.put(GET_BROTHER_NUM, new Byte(GET_BROTHER_NUM_CODE));
		functionMap.put(GET_DOM_TYPE, new Byte(GET_DOM_TYPE_CODE));
		functionMap.put(DEL_ALL_CHILDREN, new Byte(DEL_ALL_CHILDREN_CODE));
		functionMap.put(GET_PARENT_ID, new Byte(GET_PARENT_ID_CODE));
		functionMap.put(GET_CUR_V_ITEM_ID, new Byte(GET_CUR_V_ITEM_ID_CODE));
		functionMap.put(GET_CUR_DOM_ID, new Byte(GET_CUR_DOM_ID_CODE));
		functionMap.put(SAVE_TEMP_STR, new Byte(SAVE_TEMP_STR_CODE));
		functionMap.put(LOAD_TEMP_STR, new Byte(LOAD_TEMP_STR_CODE));
		functionMap.put(REMOVE_TEMP_STR, new Byte(REMOVE_TEMP_STR_CODE));
		functionMap.put(CLEAR_SUB_DOMS, new Byte(CLEAR_SUB_DOMS_CODE));
		functionMap.put(GET_DATA_VALUE, new Byte(GET_DATA_VALUE_CODE));
		functionMap.put(GET_CHILDREN_NUM, new Byte(GET_CHILDREN_NUM_CODE));
		functionMap.put(GET_CHILDREN_ID, new Byte(GET_CHILDREN_ID_CODE));
	}

	public final AbstractSNSDOM getDOM(String id) {
		return DOMUtil.getDOMByID(BrowserScreen.getInstance().getBuffer()
				.getCurrentBuffer(), id);
	}

	private final void _zsdSetCurrentDOM(ArrayList params) {
		String id = (String) params.get(0);
		currentDOM = getDOM(id);
		id = null;
	}

	public final void _zsdRefresh(ArrayList params) {
		BrowserScreen br = BrowserScreen.getInstance();
		SNSUnitDOM root = br.getBuffer().getCurrentBuffer();

		DOMUtil.invalidateAlldom(root);

		br.layout(root);
		root = null;
		br = null;
	}

	private final String _zsdGetAttributeValue(ArrayList params, byte code) {
		AbstractSNSDOM dom = currentDOM;
		String name = null;
		if (params.size() == 2) {
			String id = (String) params.get(0);
			dom = getDOM(id);
			id = null;
		}
		name = (String) params.get(params.size() - 1);
		if (dom != null) {
			switch (code) {
			case GET_ATT_VALUE_CODE:
				if (dom.getCommonValue(name) != null) {
					return dom.getCommonValue(name);
				} else if (dom.getNetValue(name) != null) {
					return dom.getNetValue(name);
				} else {
					return dom.getSubAttributeValue(name);
				}
			case GET_NET_VALUE_CODE:
				return dom.getNetValue(name);
			case GET_COM_VALUE_CODE:
				return dom.getCommonValue(name);
			default:
				return null;
			}
		}
		return null;
	}

	public String getAttValue(AbstractSNSDOM dom, String name) {
		if (dom == null) {
			return null;
		}
		String value = dom.getCommonValue(name);
		if (value == null) {
			value = dom.getNetValue(name);
		}
		if (value == null) {
			value = dom.getSubAttributeValue(name);
		}
		return value;
	}

	// 获取DOM的孩子数量
	private final String _zsdGetChildrenID(ArrayList params) {
		String domid = (String) params.get(0);
		int index = ((Integer) params.get(1)).intValue();
		AbstractSNSDOM dom = getDOM(domid);
		if (dom != null && dom.hasChildren() && index < dom.children.size()) {
			AbstractSNSDOM subdom = (AbstractSNSDOM) dom.children.get(index);
			return subdom.id;
		}
		return "";
	}

	// 获取DOM的孩子数量
	private final Integer _zsdGetChildrenNum(ArrayList params) {
		String domid = (String) params.get(0);
		AbstractSNSDOM dom = getDOM(domid);
		if (dom != null && dom.hasChildren()) {
			return new Integer(dom.children.size());
		}
		if (dom instanceof SNSComboBoxDOM
				&& ((SNSComboBoxDOM) dom).options != null) {
			return new Integer(((SNSComboBoxDOM) dom).options.size());
		}
		return new Integer(0);
	}

	private final String _zsdGetDataValue(ArrayList params) {
		final String id = (String) params.get(0);
		final String name = (String) params.get(1);

		ArrayList attParams = new ArrayList() {
			{
				add(id);
				add("data");
			}
		};
		String data = (String) _zsdGetAttributeValue(attParams,
				GET_ATT_VALUE_CODE);

		if (data != null) {
			data += " ";
		} else {
			return null;
		}
		Hashtable table = new Hashtable();
		StringBuffer tempStr = new StringBuffer();
		String k = null;
		String v = null;
		int offsetK = 0;
		int offsetV = 0;
		for (int i = 0; i < data.length(); i++) {
			char curChar = data.charAt(i);
			tempStr.append(curChar);
			if (curChar == '=') {
				k = tempStr.toString().substring(offsetK, i);
				offsetV = i + 1;
			} else if (curChar == ' ' || i == data.length() - 1) {
				v = tempStr.toString().substring(offsetV + 1, i - 1);
				offsetK = i + 1;
				table.put(k, v);
			}
		}

		return (String) table.get(name);
	}

	private final void _zsdSetAttributeValue(ArrayList params, byte code) {
		// log.debug(currentDOM + "----------_zsdSetAttributeValue-----0--" +
		// params.size());
		if (params == null || params.size() < 2) {// 记住这个错误 找了半天
			return;
		}
		AbstractSNSDOM dom = currentDOM;
		String name = null;
		String value = null;
		int size = params.size();
		if (size == 3) {
			String id = (String) params.get(0);
			dom = getDOM(id);
			// //System.out.println("att **********dom:"+dom);
			id = null;
		}
		name = (String) params.get(size - 2);
		value = (String) params.get(size - 1);

		// //System.out.println("att **********name:"+name);
		// //System.out.println("att **********value:"+value);
		if (dom != null) {
			switch (code) {

			case SET_ATT_VALUE_CODE:
				dom.setCommonValue(name, value);
				dom.setNetValue(name, value);
				dom.setSubAttributeValue(name, value);
				break;
			case SET_NET_VALUE_CODE:
				dom.setNetValue(name, value);
				break;
			case SET_COM_VALUE_CODE:
				dom.setCommonValue(name, value);
				break;
			}
		}
		// dom.updateView();
		name = null;
		value = null;
		dom = null;
	}

	/**
	 * <li>[0] <code>string</code>:要添加的DOM节点的类型(类名); <li>[1] <code>string</code>
	 * :要添加的DOM节点的ID; <li>[2] <code>string</code>:要加入的容器ID; <li>[3]
	 * <code>int</code>:加入的位置索引.如果要加入最后一个,传入-1或者不传入第4个参数.
	 * 
	 * @param params
	 */
	private final void _zsdAddDOM(ArrayList params) {
		String name = (String) params.get(0);// 要添加的DOM节点的类型(类名)
		AbstractSNSDOM dom = null;
		try {
			// name = name.substring(0,1).toUpperCase()+name.substring(1)+"DOM";
			// log.debug("_zsdAddDOM name==="+name+"\tZMLTag.TAG_MAP.get(name)==="+ZMLTag.TAG_MAP.get(name));
			name = (String) ZMLTag.TAG_MAP.get(name);
			dom = (AbstractSNSDOM) Class.forName(name).newInstance();

			if (dom != null) {
				dom.id = (String) params.get(1);

				SNSUnitDOM currentUnit = BrowserScreen.getInstance()
						.getBuffer().getCurrentBuffer();
				// 判断是否有相同id的组件
				AbstractSNSDOM tmpDOM = getDOM(dom.id);
				if (tmpDOM != null) {// 如果有相同的组件则替换
					DOMUtil.replaceSubtree(tmpDOM, dom);
				} else {// 如果没有相同组件，则加到指定位置
					String containerID = (String) params.get(2);// 获取要加入的容器
					tmpDOM = getDOM(containerID);

					// if(tmpDOM.children!= null)
					// System.out.println("add father.children size==="+tmpDOM.children.size()+"===id=="+tmpDOM.id);

					int index = -1;
					if (params.size() == 4) {
						index = ((Integer) params.get(3)).intValue();// 获取要加入的位置
					}
					containerID = null;
					if (index == -1) {
						if (tmpDOM == null) {
							tmpDOM = currentUnit;
						}
						// log.debug("_zsdAddDOM ==>"+dom.id);
						tmpDOM.addChild(dom);
					} else {
						if (tmpDOM == null)
							tmpDOM = currentUnit;
						tmpDOM.addChild(index, dom);
					}
				}
				tmpDOM = null;
				currentUnit = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dom != null) {
				currentDOM = dom;
				dom = null;
			}
			name = null;
		}
	}

	private final void _zsdDelDOM(ArrayList params) {
		AbstractSNSDOM dom = currentDOM;
		System.out.println("_zsdDelDOM--- >" + dom);
		if (params.size() == 1) {
			String id = (String) params.get(0);
			dom = getDOM(id);
		}
		System.out.println("_zsdDelDOM-dom-0- >" + dom);
		if (dom != null) {
			System.out.println("_zsdDelDOM-dom-- >" + dom.id);
			AbstractSNSDOM father = dom.father;
			if (father != null) {
				System.out.println("_zsdDelDOM-father-- >" + father.id);
				father.children.remove(dom);

				dom.father = null;
			}
			// dom.release();
			dom = null;
		}
	}

	private final void _zsdClearSubDoms(ArrayList params) {
		AbstractSNSDOM dom = currentDOM;

		if (params.size() == 1) {
			String id = (String) params.get(0);
			dom = getDOM(id);
		}

		if (dom != null && dom.children != null) {

			dom.children.removeAll();
			// System.out.println("clear dom ====>>"+dom.id+"===dom.children.size==="+dom.children.size());
		}
	}

	private final Integer _zsdGetChidrenNum(ArrayList params) {
		AbstractSNSDOM dom = currentDOM;
		if (params.size() >= 1) {
			String id = (String) params.get(0);
			dom = getDOM(id);
		}
		int num = -1;
		if (dom != null) {
			num = dom.children == null ? 0 : dom.children.size();
		}
		dom = null;
		return new Integer(num);
	}

	private final Integer _zsdGetBrotherNum(ArrayList params) {
		AbstractSNSDOM dom = currentDOM;
		if (params.size() == 1) {
			String id = (String) params.get(0);
			dom = getDOM(id);
		}
		int num = -1;
		if (dom != null) {
			AbstractSNSDOM father = dom.father;
			if (father != null)
				num = father.children.size();
			father = null;
		}
		dom = null;
		return new Integer(num);
	}

	private final String _zsdGetDomType(ArrayList params) {
		// TODO 根据ID返回相应节点的类型
		return null;
	}

	private final void _zsdDelAllChildren(ArrayList params) {
		String domid = (String) params.get(0);
		AbstractSNSDOM father = DOMUtil.getDOMByID(BrowserScreen.getInstance()
				.getCurrentUnit().getDom(), domid);
		if (father.hasChildren()) {
			father.children.removeAll();
		}
	}

	private final String _zsdGetParentID(ArrayList params) {
		// TODO 根据ID返回相应节点的父节点的ID
		if (params == null || params.size() == 0) {
			AbstractSNSComponent currentComponent = FocusManager
					.getFocusManager().getFocusedWidget();
			return currentComponent == null ? null
					: currentComponent.getDom().id;
		} else {
			String id = (String) params.get(0);
			AbstractSNSDOM dom = getDOM(id);
			if (dom != null) {
				dom = dom.father;
				return dom == null ? null : dom.id;
			}
			id = null;
			dom = null;
		}
		return null;
	}
}
