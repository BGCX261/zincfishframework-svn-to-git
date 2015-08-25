package zincfish.zincparser.zmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.microedition.lcdui.TextField;
import utils.StringUtil;
import zincfish.zinccss.ICSSConstants;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSBodyDOM;
import zincfish.zincdom.SNSCheckBoxDOM;
import zincfish.zincdom.SNSComboBoxDOM;
import zincfish.zincdom.SNSDivDOM;
import zincfish.zincdom.SNSHListDOM;
import zincfish.zincdom.SNSImageDOM;
import zincfish.zincdom.SNSLabelDOM;
import zincfish.zincdom.SNSPagingDOM;
import zincfish.zincdom.SNSRateDOM;
import zincfish.zincdom.SNSTextFieldDOM;
import zincfish.zincdom.SNSUnitDOM;
import zincfish.zincparser.xmlparser.IXmlParser;
import zincfish.zincparser.xmlparser.ParserException;
import zincfish.zincparser.xmlparser.XmlParser;
import zincfish.zincscript.ZincScript;
import data.Friend;
import data.FriendData;
import data.MenuData;

public final class ZMLParser {
	private SNSUnitDOM root = null;
	private AbstractSNSDOM currentDOM = null;
	private Hashtable attr = null;
	private IXmlParser parser;// xml parser
	private boolean isRunning = false;
	private MenuData menu = null;

	private ZMLParser() {
		parser = new XmlParser();
		attr = new Hashtable(10);
	}

	/**
	 * get the instance of parser
	 * 
	 * @return WML parser
	 */
	public static final ZMLParser getSNSParser() {
		return new ZMLParser();
	}

	public final void parse() throws ParserException, IOException {
		isRunning = true;
		int eventType = parser.getEventType();
		// parse all tags
		while (eventType != XmlParser.END_DOCUMENT && isRunning) {
			// start tag event
			if (eventType == XmlParser.START_TAG) {
				parseStartTag();
			}
			// end tag event
			else if (eventType == XmlParser.END_TAG) {
				parseEndTag();
			}
			// text event
			else if (eventType == XmlParser.TEXT) {
				parseText();
			}
			// get next tag
			try {
				eventType = parser.next();
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * ���?ʼ��ǩ
	 * 
	 * @throws ParserException
	 */
	private final void parseStartTag() throws ParserException {
		// tag's name
		String currentTag = parser.getName();
		// number of attributes
		int attributeNum = parser.getAttributeNum();
		// get all attributes
		for (int i = 0; i < attributeNum; i++) {
			attr.put(parser.getAttributeName(i), parser.getAttributeValue(i));
		}
		if (ZMLTag.LABEL_TAG.equals(currentTag)) {
			parseLabel();
		} else if (ZMLTag.BR_TAG.equals(currentTag)) {
			parseBR();
		} else if (ZMLTag.MENU_TAG.equals(currentTag)) {
			parseMenu();
		} else if (ZMLTag.IMG_TAG.equals(currentTag)) {
			parseImage();
		} else if (ZMLTag.DIV_TAG.equals(currentTag)) {
			parseDiv();
		} else if (ZMLTag.INPUT_TAG.equals(currentTag)) {
			parseTextField();
		} else if (ZMLTag.BODY_TAG.equals(currentTag)) {
			parseBody();
		} else if (ZMLTag.UNIT_TAG.equals(currentTag)) {
			parseUnit();
		} else if (ZMLTag.PAGING_TAG.equals(currentTag)) {
			parsePaging();
		} else if (ZMLTag.HLIST_TAG.equals(currentTag)) {
			parseHList();
		} else if (ZMLTag.SCRIPT_TAG.equals(currentTag)) {
			parseScript();
		} else if (ZMLTag.STYLE_TAG.equals(currentTag)) {
			parseStyle();
		} else if (ZMLTag.RATE_TAG.equals(currentTag)) {
			parseRate();
		} else if (ZMLTag.OPTION_TAG.equals(currentTag)) {
			parseOption();
		} else if (ZMLTag.SELECT_TAG.equals(currentTag)) {
			parseComboBox();
		} else if (ZMLTag.CHECKBOX_TAG.equals(currentTag)) {
			parseCheckBox();
		} else if (ZMLTag.FRIEND_DATA_TAG.equals(currentTag)) {
			parseFiendData();
		}  else if (ZMLTag.FRIEND_TAG.equals(currentTag)) {
			parseFriend();
		}

		String onInitEvent = (String) attr.get(ZMLTag.ON_INIT_ATTR);
		ZincScript.getZincScript().executeDynamicScript(onInitEvent);
		onInitEvent = null;
		currentTag = null;
		attr.clear();
		// System.out.println("tag end= " + currentTag);
	}

	private void parseFriend() {
		String mid = (String) attr.get(ZMLTag.MID_ATTR);
		if (attr.get(ZMLTag.MID_ATTR) == null) {
			mid = (String) attr.get(ZMLTag.ID_ATTR);
		}

		String name = (String) attr.get(ZMLTag.NAME_ATTR);
		String group = (String) attr.get(ZMLTag.GROUP_ATTR);
		String descript = (String) attr.get(ZMLTag.DESCRIPT_ATTR);
		String operator = (String) attr.get(ZMLTag.OPERATOR_ATTR);
		Friend friend = new Friend();
		friend.setMid(mid);
		friend.setName(name);
		friend.setGroup(group);
		friend.setDescript(descript);
		friend.setOperator(operator);

		FriendData.getInstance().frienddatalist.add(friend);

	}

	private void parseFiendData() {
		String id = (String) attr.get(ZMLTag.ID_ATTR);
		String time = (String) attr.get(ZMLTag.TIME_ATTR);
		FriendData.getInstance().setId(id);
		FriendData.getInstance().setTime(time);

	}

	private final void parseEndTag() {
		String currentTag = parser.getName();
		// if (ZMLTag.MENU_TAG.equals(currentTag)) {
		// if (menu.getFather() == null) { // 说明是跟菜单
		// if (root != null) {
		// if (menu.type == MenuData.LEFT_MENU
		// && root.getMenu(MenuData.LEFT_MENU) != null) { // 说明左菜单已经存在
		// if (root.leftmenuList == null) {
		// root.leftmenuList = new ArrayList(3);
		// root.leftmenuList.add(root
		// .getMenu(MenuData.LEFT_MENU));
		// }
		// root.leftmenuList.add(menu);
		// } else {
		// root.setMenu(menu); //
		// }
		// }
		// menu = null;
		// } else {
		// menu = menu.getFather(); // 把当前的menu 设为 该menu的父menu
		// }
		// return;
		// }
		if (!currentTag.equals(ZMLTag.BR_TAG)
				&& !currentTag.equals(ZMLTag.OPTION_TAG)) {
			if (currentDOM != null) {
				currentDOM.tagName = currentTag;
				AbstractSNSDOM dom = currentDOM;
				currentDOM = dom.father == null ? root : dom.father;
				if (dom.bind > AbstractSNSDOM.BIND_NONE) {
					currentDOM.children.remove(dom);
					dom.father = null;
				}
				dom = null;
			}
		}
		currentTag = null;
	}

	/*
	 * 解析纯文本
	 * 
	 * @throws ParserException
	 */
	private final void parseText() throws ParserException {
		boolean isWhitespace = false;
		try {
			isWhitespace = parser.isWhitespace();
		} catch (ParserException ex) {
			// System.out.println("error2:" + ex.getMessage());
		}
		if (!isWhitespace) {
			String text = parser.getText();
			text = text.trim();
			SNSLabelDOM dom = new SNSLabelDOM();
			dom.wrap = SNSLabelDOM.WRAP_BREAK;
			dom.text = text;
			// if (currentDOM.type == AbstractDOM.TYPE_RICH_TEXT_VIEWER) {
			// ((RichTextViewerDOM) currentDOM).addContent(dom);
			// dom.father = currentDOM;
			// } else {
			add2DOMTree(dom);
			parseEndTag();
			// }
			dom = null;
			text = null;
		}
	}

	private void add2DOMTree(AbstractSNSDOM dom) throws ParserException {
		if (root == null) {
			root = new SNSUnitDOM();
			currentDOM = root;
		}
		currentDOM.addChild(dom);
		currentDOM = dom;
		if (currentDOM.bind > AbstractSNSDOM.BIND_NONE) {
			((SNSBodyDOM) root.children.get(root.children.size() - 1))
					.addBind(currentDOM);
		}
	}

	/*
	 * 解析<label>标签
	 * 
	 * @throws ParserException
	 */
	private final void parseLabel() throws ParserException {
		SNSLabelDOM label = new SNSLabelDOM();
		handelGeneralAttributes(label);

		String type = (String) attr.get(ZMLTag.TYPE_ATTR);
		if (type != null) {
			if (type.equals(ZMLTag.WRAP_VALUE_SCROLL))
				label.wrap = SNSLabelDOM.WRAP_SCROLL;
			else if (type.equals(ZMLTag.WRAP_VALUE_OMIT))
				label.wrap = SNSLabelDOM.WRAP_OMIT;
			else if (type.equals(ZMLTag.WRAP_VALUE_BREAK))
				label.wrap = SNSLabelDOM.WRAP_BREAK;
			else if (type.equals(ZMLTag.COMBOTYPE_MULTI))
				label.wrap = SNSLabelDOM.WRAP_MULTI;
			else
				label.wrap = SNSLabelDOM.WRAP_PLAIN;
		}
		type = null;
		label.text = (String) attr.get(ZMLTag.TEXT_ATTR);

		add2DOMTree(label);
		label = null;
	}

	private final void parseBR() {
		if (currentDOM != null) {
			AbstractSNSDOM dom = (AbstractSNSDOM) currentDOM.children
					.get(currentDOM.children.size() - 1);
			dom.brNum++;
			dom = null;
		}
	}

	/**
	 * 解析&lt;img&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseImage() throws ParserException {
		SNSImageDOM dom = new SNSImageDOM();
		handelGeneralAttributes(dom);

		dom.src = (String) attr.get(ZMLTag.SRC_ATTR);
		dom.alt = (String) attr.get(ZMLTag.ALT_ATTR);
		dom.prevImageURL = (String) attr.get(ZMLTag.PREV_ATTR);
		dom.nextImageURL = (String) attr.get(ZMLTag.NEXT_ATTR);
		dom.frameWidth = StringUtil.Str2Int((String) attr
				.get(ZMLTag.FRAME_WIDTH_ATTR));
		dom.frameHeight = StringUtil.Str2Int((String) attr
				.get(ZMLTag.FRAME_HEIGHT_ATTR));
		dom.rotation = StringUtil.rotation2Int((String) attr
				.get(ZMLTag.ROTATION_ATTR));

		add2DOMTree(dom);
		dom = null;
	}

	/**
	 * 解析&lt;input&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseTextField() throws ParserException {
		SNSTextFieldDOM dom = new SNSTextFieldDOM();
		handelGeneralAttributes(dom);

		String constraint = (String) attr.get(ZMLTag.TYPE_ATTR);
		if (constraint != null && !constraint.equals(ZMLTag.NONE_VALUE)) {
			if (constraint.equals(ZMLTag.CONSTRAINT_NUMERIC))
				dom.constraintFlag = TextField.NUMERIC;
			else if (constraint.equals(ZMLTag.CONSTRAINT_PASSWORD))
				dom.constraintFlag = TextField.PASSWORD;
			else if (constraint.equals(ZMLTag.CONSTRAINT_PHONENUMBER))
				dom.constraintFlag = TextField.PHONENUMBER;
			else if (constraint.equals(ZMLTag.CONSTRAINT_URL))
				dom.constraintFlag = TextField.URL;
			else if (constraint.equals(ZMLTag.CONSTRAINT_EMAIL))
				dom.constraintFlag = TextField.EMAILADDR;
			else if (constraint.equals(ZMLTag.CONSTRAINT_DECIMAL))
				dom.constraintFlag = TextField.DECIMAL;
			else
				dom.constraintFlag = TextField.ANY;
		}
		constraint = null;
		dom.size = StringUtil.Str2Int((String) attr.get(ZMLTag.SIZE_ATTR));
		dom.maxSize = StringUtil.Str2Int((String) attr
				.get(ZMLTag.MAX_SIZE_ATTR));
		dom.isEmptyOK = StringUtil.Str2Bool((String) attr
				.get(ZMLTag.EMPTY_OK_ATTR));

		add2DOMTree(dom);
		dom = null;
	}

	/**
	 * 解析&lt;select&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseComboBox() throws ParserException {
		SNSComboBoxDOM dom = new SNSComboBoxDOM();
		handelGeneralAttributes(dom);

		String type = ((String) attr.get(ZMLTag.TYPE_ATTR));
		if (type != null) {
			if (type.equals(ZMLTag.COMBOTYPE_MULTI))
				dom.comboType = SNSComboBoxDOM.MULTI_PICKER;
			else if (type.equals(ZMLTag.COMBOTYPE_FRIEND))
				dom.comboType = SNSComboBoxDOM.FRIEND_PICKER;
			else if (type.equals(ZMLTag.COMBOTYPE_FILE))
				dom.comboType = SNSComboBoxDOM.FILE_PICKER;
			else if (type.equals(ZMLTag.COMBOTYPE_DATE))
				dom.comboType = SNSComboBoxDOM.DATE_PICKER;
			else
				dom.comboType = SNSComboBoxDOM.SINGLE_PICKER;
		}
		type = null;

		add2DOMTree(dom);
		dom = null;
	}

	/**
	 * 解析&lt;select&gt;中的&lt;option&gt;标签
	 */
	private final void parseOption() {
		if (currentDOM.type == AbstractSNSDOM.TYPE_COMBOX) {
			SNSComboBoxDOM comboBox = (SNSComboBoxDOM) currentDOM;
			String text = (String) attr.get(ZMLTag.TEXT_ATTR);
			String value = (String) attr.get(ZMLTag.VALUE_ATTR);
			comboBox.addOption(text, value);
			text = null;
			value = null;
		}
	}

	/**
	 * 解析&lt;style&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseStyle() throws ParserException {
		String css = (String) attr.get(ZMLTag.SRC_ATTR);
		if (css != null)
			root.setCSSFile(css);
		css = null;
	}

	/**
	 * 解析&lt;body&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseBody() throws ParserException {
		SNSBodyDOM dom = new SNSBodyDOM();
		handelGeneralAttributes(dom);

		add2DOMTree(dom);
		dom = null;
	}

	/**
	 * 解析&lt;menu&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseMenu() throws ParserException {
		MenuData menuItem = new MenuData();
		menuItem.id = (String) attr.get(ZMLTag.ID_ATTR);
		menuItem.text = (String) attr.get(ZMLTag.TEXT_ATTR);
		menuItem.onClick = (String) attr.get(ZMLTag.ON_CLICK_ATTR);
		menuItem.code = StringUtil.Str2Int((String) attr.get(ZMLTag.CODE_ATTR));
		// String s = (String) attr.get(ZMLTag.TYPE_ATTR);
		// if (s != null && !s.equals(ZMLTag.NONE_VALUE)) {
		// if (ZMLTag.ALIGN_LEFT.equals(s)) {
		// menuItem.type = MenuData.LEFT_MENU;
		// } else if (ZMLTag.ALIGN_RIGHT.equals(s)) {
		// menuItem.type = MenuData.RIGHT_MENU;
		// } else if (ZMLTag.ALIGN_CENTER.equals(s)) {
		// menuItem.type = MenuData.POP_MENU;
		// } else {
		// menuItem.type = MenuData.LEFT_MENU;
		// }
		// }
		// s = null;

		if (menu != null) {
			menu.addSubMenu(menuItem);
		}
		menu = menuItem;
		menuItem = null;
	}

	/**
	 * 解析公共属性
	 * 
	 * @param dom
	 *            当前操作的DOM
	 */
	private final void handelGeneralAttributes(AbstractSNSDOM dom) {
		dom.id = (String) attr.get(ZMLTag.ID_ATTR);
		dom.classes = (String) attr.get(ZMLTag.CLASS_ATTR);
		dom.name = (String) attr.get(ZMLTag.NAME_ATTR);
		dom.value = (String) attr.get(ZMLTag.VALUE_ATTR);
		dom.data = (String) attr.get(ZMLTag.DATA_ATTR);

		String attribute = (String) attr.get(ZMLTag.AVAILABLE_ATTR);
		if (attribute != null)
			dom.isAvailable = StringUtil.Str2Bool(attribute);
		attribute = null;
		attribute = (String) attr.get(ZMLTag.VISIBLE_ATTR);
		if (attribute != null)
			dom.isVisible = StringUtil.Str2Bool(attribute);
		attribute = null;
		// 通用事件接口
		dom.onClick = (String) attr.get(ZMLTag.ON_CLICK_ATTR);
		dom.onFocus = (String) attr.get(ZMLTag.ON_FOCUS_ATTR);
		dom.onLoseFocus = (String) attr.get(ZMLTag.ON_LOSE_FOCUS_ATTR);
		if (!dom.canFocus) {
			dom.canFocus = (dom.onClick != null || dom.onFocus != null || dom.onLoseFocus != null);
		}

		// tips
		dom.tips = (String) attr.get(ZMLTag.TIPS_ATTR);
		// xml作者自定义样式
		dom.style = (String) attr.get(ZMLTag.STYLE_TAG);
		// bind
		attribute = (String) attr.get(ZMLTag.BIND_ATTR);
		if (attribute != null) {
			if (ICSSConstants.BORDER_NORTH.equals(attribute))
				dom.bind = AbstractSNSDOM.BIND_NORTH;
			else if (ICSSConstants.BORDER_EAST.equals(attribute))
				dom.bind = AbstractSNSDOM.BIND_EAST;
			else if (ICSSConstants.BORDER_SOUTH.equals(attribute))
				dom.bind = AbstractSNSDOM.BIND_SOUTH;
			else if (ICSSConstants.BORDER_WEST.equals(attribute))
				dom.bind = AbstractSNSDOM.BIND_WEST;
			else
				dom.bind = AbstractSNSDOM.BIND_NONE;
		}
		attribute = null;
	}

	/**
	 * 解析&lt;div&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseDiv() throws ParserException {
		SNSDivDOM dom = new SNSDivDOM();
		handelGeneralAttributes(dom);
		add2DOMTree(dom);
		dom = null;
	}

	/**
	 * 解析&lt;paging&gt;标签
	 * 
	 * @throws ParserException
	 */
	public final void parsePaging() throws ParserException {
		SNSPagingDOM dom = new SNSPagingDOM();
		handelGeneralAttributes(dom);

		dom.src = (String) attr.get(ZMLTag.SRC_ATTR);
		dom.totalPage = StringUtil
				.Str2Int((String) attr.get(ZMLTag.TOTAL_ATTR));
		dom.currentPage = StringUtil.Str2Int((String) attr
				.get(ZMLTag.COUNT_ATTR));

		add2DOMTree(dom);
		dom = null;
	}

	/**
	 * 解析&lt;hlist&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseHList() throws ParserException {
		SNSHListDOM dom = new SNSHListDOM();
		handelGeneralAttributes(dom);

		dom.arrowSrc = (String) attr.get(ZMLTag.SRC_ATTR);

		add2DOMTree(dom);
		dom = null;
	}

	/**
	 * 解析&lt;checkbox&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseCheckBox() throws ParserException {
		SNSCheckBoxDOM dom = new SNSCheckBoxDOM();
		handelGeneralAttributes(dom);

		dom.isSelected = StringUtil.Str2Bool((String) attr
				.get(ZMLTag.SELECTED_ATTR));
		dom.text = (String) attr.get(ZMLTag.TEXT_ATTR);

		add2DOMTree(dom);
		dom = null;
	}

	private final void parseRate() throws ParserException {
		SNSRateDOM dom = new SNSRateDOM();
		handelGeneralAttributes(dom);

		dom.total = Integer.parseInt((String) attr.get(ZMLTag.TOTAL_ATTR));
		dom.count = Integer.parseInt((String) attr.get(ZMLTag.COUNT_ATTR));

		add2DOMTree(dom);
		dom = null;
	}

	/**
	 * 解析&lt;script&gt;标签
	 * 
	 * @throws ParserException
	 */
	private final void parseScript() throws ParserException {
		String attribue = (String) attr.get(ZMLTag.SRC_ATTR);

		if (attribue != null) {
			root.addScriptFiles(attribue);
		}
		attribue = null;
	}

	/**
	 * 解析&lt;unit&gt;标签，生成DOM Tree的根
	 * 
	 * @throws ParserException
	 */
	private final void parseUnit() throws ParserException {
		root = new SNSUnitDOM();

		root.id = (String) attr.get(ZMLTag.ID_ATTR);
		root.onload = (String) attr.get(ZMLTag.ON_LOAD_ATTR);
		root.timer = StringUtil.Str2Long((String) attr.get(ZMLTag.TIMER_ATTR));

		currentDOM = root;
	}

	/**
	 * set wml page stream
	 */
	public void setInput(InputStream is, String encode) throws ParserException {
		this.parser.setInput(is, encode);
	}

	public void release() {
		root = null;
		currentDOM = null;
		menu = null;
	}

	/**
	 * @return the currentDOM
	 */
	public AbstractSNSDOM getCurrentDOM() {
		return currentDOM;
	}

	public SNSUnitDOM getResult() {
		return root;
	}
}
