package zincfish.zinccss;

/**
 * <code>ICSSConstants</code>接口是一个常量接口，定义了CSS解析和引用中使用的常量。
 * 
 * @author Jarod Yv
 */
public interface ICSSConstants {

	// ///////////////////////////// CSS支持的样式属性 ////////////////////////////
	public static final String COLOR_STYLE_PROPERTY = "color";

	public static final String FONT_FACE_STYLE_PROPERTY = "font-face";
	public static final String FONT_STYLE_STYLE_PROPERTY = "font-style";
	public static final String FONT_SIZE_STYLE_PROPERTY = "font-size";

	public static final String BACKGROUND_COLOR_STYLE_PROPERTY = "bg-color";
	public static final String BACKGROUND_IMAGE_STYLE_PROPERTY = "bg-image";
	public static final String BACKGROUND_ALIGN_STYLE_PROPERTY = "bg-align";
	public static final String BACKGROUND_REPEAT_STYLE_PROPERTY = "bg-repeat";

	public static final String BORDER_COLOR_STYLE_PROPERTY = "border-color";
	public static final String BORDER_IMAGE_STYLE_PROPERTY = "border-image";
	public static final String BORDER_ALIGN_STYLE_PROPERTY = "border-align";
	public static final String BORDER_STROKE_STYLE_PROPERTY = "border-stroke";

	public static final String LAYOUT_STYLE_PROPERTY = "layout";
	public static final String LAYOUT_DATA_STYLE_PROPERTY = "layout-data";

	public static final String MARGIN_STYLE_PROPERTY = "margin";
	public static final String BORDER_STYLE_PROPERTY = "border";
	public static final String PADDING_STYLE_PROPERTY = "padding";

	public static final String GAP_STYLE_PROPERTY = "gap";
	public static final String SPAN_STYLE_PROPERTY = "span";
	public static final String WEIGHT_STYLE_PROPERTY = "weight";
	public static final String MIN_SIZE_STYLE_PROPERTY = "min-size";

	public static final String ALIGN_STYLE_PROPERTY = "align";

	public static final String TRANSITION_STYLE_PROPERTY = "transition";

	public static final String GRAYED_COLOR_STYLE_PROPERTY = "grayed-color";

	public static final String POSITION_STYLE_PROPERTY = "position";

	public static final String FLOAT_STYLE_PROPERTY = "float";
	// //////////////////////////////////////////////////////////////////////////
	public static final String NONE_VALUE = "none";

	// ////////////////////////// font-face支持的属性值 //////////////////////////
	public static final String FONT_FACE_SYSTEM = "system";
	public static final String FONT_FACE_MONOSPACE = "monospace";
	public static final String FONT_FACE_PROPORTIONAL = "proportional";
	// //////////////////////////////////////////////////////////////////////////

	// ///////////////////////// font-style支持的属性值 //////////////////////////
	public static final String FONT_STYLE_PLAIN = "plain";
	public static final String FONT_STYLE_BOLD = "bold";
	public static final String FONT_STYLE_ITALIC = "italic";
	public static final String FONT_STYLE_UNDERLINED = "underlined";
	// //////////////////////////////////////////////////////////////////////////

	// ////////////////////////// font-size支持的属性值 //////////////////////////
	public static final String FONT_SIZE_SMALL = "small";
	public static final String FONT_SIZE_MEDIUM = "medium";
	public static final String FONT_SIZE_LARGE = "large";
	// //////////////////////////////////////////////////////////////////////////

	// ///////////////////////// border-stroke支持的属性值 ////////////////////////
	public static final String STROKE_SOLID = "solid";
	public static final String STROKE_DOTTED = "dotted";
	// //////////////////////////////////////////////////////////////////////////

	// //////////////////////////// 支持的图片翻转样式 ////////////////////////////
	public static final String IMG_TRANS_MIRROR = "mirror";
	public static final String IMG_TRANS_MIRROR_ROT270 = "mirror_rot270";
	public static final String IMG_TRANS_MIRROR_ROT180 = "mirror_rot180";
	public static final String IMG_TRANS_MIRROR_ROT90 = "mirror_rot90";
	public static final String IMG_TRANS_ROT270 = "rot270";
	public static final String IMG_TRANS_ROT180 = "rot180";
	public static final String IMG_TRANS_ROT90 = "rot90";
	// //////////////////////////////////////////////////////////////////////////

	// //////////////////////////// align支持的属性值 ////////////////////////////
	public static final String ALIGN_TOP = "top";
	public static final String ALIGN_RIGHT = "right";
	public static final String ALIGN_BOTTOM = "bottom";
	public static final String ALIGN_LEFT = "left";
	public static final String ALIGN_CENTER = "center";
	public static final String ALIGN_TOP_LEFT = "top-left";
	public static final String ALIGN_TOP_RIGHT = "top-right";
	public static final String ALIGN_BOTTOM_RIGHT = "bottom-right";
	public static final String ALIGN_BOTTOM_LEFT = "bottom-left";
	public static final String ALIGN_FILL = "fill";
	public static final String ALIGN_FILL_LEFT = "fill-left";
	public static final String ALIGN_FILL_RIGHT = "fill-right";
	public static final String ALIGN_FILL_TOP = "fill-top";
	public static final String ALIGN_FILL_BOTTOM = "fill-bottom";
	public static final String ALIGN_FILL_CENTER = "fill-center";
	// //////////////////////////////////////////////////////////////////////////

	// //////////////////////////////// 排版样式 ////////////////////////////////
	public static final String INLINE_LAYOUT = "inlinelayout";
	public static final String FLOW_LAYOUT = "flowlayout";
	public static final String TABLE_LAYOUT = "tablelayout";
	public static final String GRID_LAYOUT = "gridlayout";
	public static final String BORDER_LAYOUT = "borderlayout";
	public static final String STATIC_LAYOUT = "staticlayout";
	public static final String FORM_LAYOUR = "formlayout";
	// //////////////////////////////////////////////////////////////////////////

	// ////////////////////////////// 排版样式数据 ///////////////////////////////
	public static final String GRID_LAYOUT_DATA = "gld";
	public static final String BORDER_LAYOUT_DATA = "bld";
	public static final String STATIC_LAYOUT_DATA = "sld";
	// //////////////////////////////////////////////////////////////////////////

	// ///////////////////////// 有效的borderlayoutdata //////////////////////////
	public static final String BORDER_NORTH = "north";
	public static final String BORDER_SOUTH = "south";
	public static final String BORDER_EAST = "east";
	public static final String BORDER_WEST = "west";
	public static final String BORDER_CENTER = "center";
	// //////////////////////////////////////////////////////////////////////////

	// ////////////////////////////// 支持的伪类样式 //////////////////////////////
	public static final String PSEUDO_CLASS_HOVER = "hover";
	public static final String PSEUDO_CLASS_DISABLED = "disabled";
	public static final String PSEUDO_CLASS_SELECTED = "selected";
	// //////////////////////////////////////////////////////////////////////////

	// /////////////////////////// 对齐数组中的下标索引 ///////////////////////////
	public static final int INDEX_TOP = 0x00;
	public static final int INDEX_TOP_RIGHT = 0x04;
	public static final int INDEX_RIGHT = 0x01;
	public static final int INDEX_BOTTOM_RIGHT = 0x05;
	public static final int INDEX_BOTTOM = 0x02;
	public static final int INDEX_BOTTOM_LEFT = 0x06;
	public static final int INDEX_LEFT = 0x03;
	public static final int INDEX_TOP_LEFT = 0x07;
	// //////////////////////////////////////////////////////////////////////////

	// ///////////////////////////// 界面切换过渡样式 ////////////////////////////
	public static final String TRANSITION_PAGE_CURL = "pagecurl";
	public static final String TRANSITION_RADOM_FALL = "randomfall";
	public static final String TRANSITION_BREAKE_OUT = "breakout";
	public static final String TRANSITION_FLY = "fly";
	public static final String TRANSITION_FADE = "fade";
	public static final String TRANSITION_MOSAIC = "mosaic";
	public static final String TRANSITION_DISSOLVE = "dissolve";
	// //////////////////////////////////////////////////////////////////////////

	// //////////////////////////// position支持的属性 ///////////////////////////
	public static final String POSOTION_STATIC = "static";
	public static final String POSITION_RELATIVE = "relative";
	public static final String POSITION_ABSOLUTE = "absolute";
	public static final String POSITION_FIXED = "fixed";
	// //////////////////////////////////////////////////////////////////////////

	// ////////////////////////// position映射的数据类型 /////////////////////////
	public static final Byte POSITION_TYPE_STATIC = new Byte((byte) 0x00);
	public static final Byte POSITION_TYPE_RELATIVE = new Byte((byte) 0x01);
	public static final Byte POSITION_TYPE_ABSOLUTE = new Byte((byte) 0x02);
	public static final Byte POSITION_TYPE_FIXED = new Byte((byte) 0x03);
	// //////////////////////////////////////////////////////////////////////////

	public static final String PREFIX_URL = "url";// url前缀

}
