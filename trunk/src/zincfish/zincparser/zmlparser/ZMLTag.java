package zincfish.zincparser.zmlparser;

import java.util.Hashtable;

/**
 * <code>ZMLTag</code> 定义了zml的标签、属性以及属性接受的值
 * 
 * @author Jarod Yv
 */
public final class ZMLTag {
	public static final Hashtable TAG_MAP = new Hashtable() {
		{
			put(UNIT_TAG, "zincfish.zincdom.SNSUnitDOM");
			put(DIV_TAG, "zincfish.zincdom.SNSDivDOM");
			put(HLIST_TAG, "zincfish.zincdom.SNSHListDOM");
			put(BODY_TAG, "zincfish.zincdom.SNSBodyDOM");
			put(INPUT_TAG, "zincfish.zincdom.SNSTextFieldDOM");
			put(IMG_TAG, "zincfish.zincdom.SNSImageDOM");
			put(LABEL_TAG, "zincfish.zincdom.SNSLabelDOM");
			put(CHECKBOX_TAG, "zincfish.zincdom.SNSRadioCheckDOM");
			put(SELECT_TAG, "zincfish.zincdom.SNSComboxDOM");
			put(PAGING_TAG, "zincfish.zincdom.SNSPagingDOM");
		}
	};

	public static final String NONE_VALUE = "";
	public static final String TRUE_VALUE = "true";
	public static final String FALSE_VALUE = "false";
	// //////////////////////// zml支持的标签 ////////////////////////
	public static final String UNIT_TAG = "unit";
	public static final String DIV_TAG = "div";
	public static final String SCRIPT_TAG = "script";
	public static final String HLIST_TAG = "hlist";
	public static final String MENU_TAG = "menu";
	public static final String BODY_TAG = "body";
	public static final String INPUT_TAG = "input";
	public static final String IMG_TAG = "img";
	public static final String LABEL_TAG = "label";
	public static final String CHECKBOX_TAG = "checkbox";
	public static final String BR_TAG = "br";
	public static final String SELECT_TAG = "select";
	public static final String OPTION_TAG = "option";
	public static final String PAGING_TAG = "paging";
	public static final String STYLE_TAG = "style";
	public static final String RATE_TAG = "rate";
	// //////////////////////////////////////////////////////////////

	// ////////////////////// 好友数据标签 ///////////////////////////
	public static final String FRIENDS_DATA_TAG = "frienddata";
	public static final String FRIEND_LIST_TAG = "friendlist";
	public static final String GROUP_LIST_TAG = "grouplist";
	public static final String FRIEND_TAG = "friend";
	// //////////////////////////////////////////////////////////////

	// ////////////////////////// 公共属性 ///////////////////////////
	public static final String ID_ATTR = "id";
	public static final String CLASS_ATTR = "class";
	public static final String AVAILABLE_ATTR = "available";
	public static final String VISIBLE_ATTR = "visible";
	public static final String NAME_ATTR = "name";
	public static final String VALUE_ATTR = "value";
	public static final String DATA_ATTR = "data";
	public static final String TIPS_ATTR = "tips";
	public static final String BIND_ATTR = "bind";
	// //////////////////////////////////////////////////////////////

	// //////////////////////// 通用事件接口 /////////////////////////
	public static final String ON_INIT_ATTR = "oninit";
	public static final String ON_CLICK_ATTR = "onclick";
	public static final String ON_FOCUS_ATTR = "onfocus";
	public static final String ON_LOSE_FOCUS_ATTR = "onlosefocus";
	public static final String ON_LOAD_ATTR = "onload";
	public static final String ON_BEFORE_LOAD = "beforeload";
	// //////////////////////////////////////////////////////////////

	public static final String SRC_ATTR = "src";
	public static final String TYPE_ATTR = "type";
	public static final String SELECTED_ATTR = "selected";
	public static final String CODE_ATTR = "code";
	public static final String TEXT_ATTR = "text";

	// ///////////////////// <unit>标签的私有属性 //////////////////////
	public static final String TIMER_ATTR = "timer";
	// //////////////////////////////////////////////////////////////

	// /////////////////// <input>标签的私有属性 /////////////////////
	public static final String SIZE_ATTR = "size";
	public static final String MAX_SIZE_ATTR = "maxsize";
	public static final String EMPTY_OK_ATTR = "emptyok";
	// //////////////////////////////////////////////////////////////

	// /////////////// <paging> <rate>标签的私有属性 //////////////////
	public static final String TOTAL_ATTR = "total";
	public static final String COUNT_ATTR = "count";
	// //////////////////////////////////////////////////////////////

	// //////////////////// <img>标签的私有属性 //////////////////////
	public static final String ALT_ATTR = "alt";
	public static final String NEXT_ATTR = "next";
	public static final String PREV_ATTR = "prev";
	public static final String FRAME_WIDTH_ATTR = "framewidth";
	public static final String FRAME_HEIGHT_ATTR = "frameheight";
	public static final String ROTATION_ATTR = "rotation";
	// //////////////////////////////////////////////////////////////

	// ////////////////// <list>标签type属性接受的值 //////////////////
	public static final String LIST_H = "h";
	public static final String LIST_V = "v";
	public static final String LIST_C = "c";
	// public static final String LIST_STYLE = "vstyle"; // 排版样式
	// public static final String LIST_SLABEL = "vslabel"; // 标签样式
	// //////////////////////////////////////////////////////////////

	// //////////////// <select>标签type属性接受的值 ////////////////
	public static final String COMBOTYPE_MULTI = "multi";
	public static final String COMBOTYPE_FILE = "file";
	public static final String COMBOTYPE_DATE = "date";
	public static final String COMBOTYPE_FRIEND = "friend";
	// //////////////////////////////////////////////////////////////

	// ///////////////// <label>标签type属性接受的值 //////////////////
	public static final String WRAP_VALUE_BREAK = "break";
	public static final String WRAP_VALUE_SCROLL = "scroll";
	public static final String WRAP_VALUE_OMIT = "omit";
	public static final String WRAP_VALUE_LABEL = "label";
	// //////////////////////////////////////////////////////////////

	// ////////////////// <input>标签type属性接受的值 //////////////////
	public static final String CONSTRAINT_ANY = "any";
	public static final String CONSTRAINT_NUMERIC = "num";
	public static final String CONSTRAINT_PHONENUMBER = "phone";
	public static final String CONSTRAINT_URL = "url";
	public static final String CONSTRAINT_PASSWORD = "psw";
	public static final String CONSTRAINT_EMAIL = "email";
	public static final String CONSTRAINT_DECIMAL = "decimal";
	// //////////////////////////////////////////////////////////////

	// /////////////////<head>标签gender属性接受的值////////////////////
	public static final String MALE = "male";
	public static final String FEMALE = "female";
	public static final String SECRET = "secret";
	// ////////////////<pop>标签type属性接受的值////////////////////////
	public static final String POP_TEXT = "poptext";
	// head属性
	public static final String USR_NAME = "user-name";
	public static final String VIEW_NUM = "view-count";
	public static final String SIGNATURE = "signature";
	// hr属性
	public static final String LINE_TYPE_ATTR = "linetype";
	public static final String FRIEND_DATA_TAG = "frienddata";
	public static final String LIST_TAG = "list";
	public static final String TIME_ATTR = "time";
	public static final Object DESCRIPT_ATTR = "descript";
	public static final Object MID_ATTR = "mid";
	public static final Object GROUP_ATTR = "group";
	public static final Object OPERATOR_ATTR = "operator";

}
