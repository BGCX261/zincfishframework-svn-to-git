package zincfish.zinccss.style;

import utils.ArrayList;
import zincfish.zinccss.CSSParser;
import zincfish.zinccss.ICSSConstants;
import zincfish.zinccss.model.*;
import zincfish.zinclayout.*;
import zincfish.zinclayout.layoutdata.ILayoutData;
import zincfish.zincdom.AbstractSNSDOM;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.corerenderer.fonts.CRSystemFont;

/**
 * <code>StyleSet</code> 是所有有效的CSS样式的集合
 * 
 * @author Jarod Yv
 */
public class StyleSet {

	// /////////////////////////// 缓存的样式标记 ///////////////////////////
	private final static long VALID_CACHED_FLAG_LAYOUT = 1 << 0;
	private final static long VALID_CACHED_FLAG_LAYOUT_DATA = 1 << 1;
	private final static long VALID_CACHED_FLAG_MARGIN = 1 << 2;
	private final static long VALID_CACHED_FLAG_BORDER = 1 << 3;
	private final static long VALID_CACHED_FLAG_PADDING = 1 << 4;
	private final static long VALID_CACHED_FLAG_INSETS = 1 << 5;
	private final static long VALID_CACHED_FLAG_MIN_SIZE = 1 << 6;
	private final static long VALID_CACHED_FLAG_COLOR = 1 << 7;
	private final static long VALID_CACHED_FLAG_BORDER_COLOR = 1 << 8;
	private final static long VALID_CACHED_FLAG_BORDER_STROKE = 1 << 9;
	private final static long VALID_CACHED_FLAG_BORDER_IMAGE = 1 << 10;
	private final static long VALID_CACHED_FLAG_BORDER_ALIGN = 1 << 11;
	private final static long VALID_CACHED_FLAG_BACKGROUND_COLOR = 1 << 12;
	private final static long VALID_CACHED_FLAG_BACKGROUND_IMAGE = 1 << 13;
	private final static long VALID_CACHED_FLAG_BACKGROUND_REPEAT = 1 << 14;
	private final static long VALID_CACHED_FLAG_BACKGROUNG_ALIGN = 1 << 15;
	private final static long VALID_CACHED_FLAG_GRAYED_COLOR = 1 << 16;
	private final static long VALID_CACHED_FLAG_GAP = 1 << 17;
	private final static long VALID_CACHED_FLAG_SPAN = 1 << 18;
	private final static long VALID_CACHED_FLAG_WEIGHT = 1 << 19;
	private final static long VALID_CACHED_FLAG_ALIGN = 1 << 20;
	private final static long VALID_CACHED_FLAG_FONT = 1 << 21;
	private final static long VALID_CACHED_FLAG_POSITION_TYPE = 1 << 22;
	private final static long VALID_CACHED_FLAG_POSITION_INSETS = 1 << 23;
	private final static long VALID_CACHED_FLAG_FLOAT = 1 << 24;
	// /////////////////////////////////////////////////////////////////////
	/** 样式的有效位 */
	private long validCachedFlags = 0L;

	/** 与此样式集合向关联的DOM */
	public AbstractSNSDOM dom = null;

	/** 缓存的样式 */
	private ArrayList cachedStyles = null;
	// /////////////////////////// 所有支持的样式 ///////////////////////////
	private ILayout cachedLayout = null;
	private ILayoutData cachedLayoutData = null;
	private Insets cachedMargin = null;
	private Insets cachedBorder = null;
	private Insets cachedPadding = null;
	private Insets cachedInsets = null;
	private Metrics cachedMinSize = null;

	private Integer cachedColor = null;
	private Integer[] cachedBorderColor = null;
	private Integer cachedBorderStroke = null;
	private CRImage[] cachedBorderImage = null;
	private Alignment[] cachedBorderAlign = null;
	private Integer cachedBackgroundColor = null;
	private CRImage[] cachedBackgroundImage = null;
	private Coordinates[] cachedBackgroundRepeat = null;
	private Alignment[] cachedBackgroundAlign = null;
	private Integer cachedGrayedColor = null;

	private Coordinates cachedGap = null;
	private Coordinates cachedSpan = null;
	private Coordinates cachedWeight = null;
	private Alignment cachedAlign = null;

	private CRSystemFont cachedFont = null;

	private Byte cachedPositionType = null;
	private Insets cachedPositionInsets = null;
	private Alignment cachedFloat = null;

	// /////////////////////////////////////////////////////////////////////

	/**
	 * 构造函数
	 * 
	 * @param dom
	 *            与此样式相关联的DOM
	 */
	public StyleSet(AbstractSNSDOM dom) {
		this.dom = dom;
	}

	/**
	 * 返回排版样式
	 * 
	 * @return cachedLayout
	 */
	public ILayout getLayout() {
		if ((validCachedFlags & VALID_CACHED_FLAG_LAYOUT) != VALID_CACHED_FLAG_LAYOUT) {
			cachedLayout = (ILayout) getStylePropertyValue(
					ICSSConstants.LAYOUT_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_LAYOUT;
		}
		return cachedLayout;
	}

	/**
	 * 返回排版数据
	 * 
	 * @return cachedLayoutData
	 */
	public ILayoutData getLayoutData() {
		if ((validCachedFlags & VALID_CACHED_FLAG_LAYOUT_DATA) != VALID_CACHED_FLAG_LAYOUT_DATA) {
			cachedLayoutData = (ILayoutData) getStylePropertyValue(
					ICSSConstants.LAYOUT_DATA_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_LAYOUT_DATA;
		}
		return cachedLayoutData;
	}

	/**
	 * 返回margin值
	 * 
	 * @return cachedMargin
	 */
	public Insets getMargin() {
		if ((validCachedFlags & VALID_CACHED_FLAG_MARGIN) != VALID_CACHED_FLAG_MARGIN) {
			cachedMargin = (Insets) getStylePropertyValue(
					ICSSConstants.MARGIN_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_MARGIN;
		}
		return cachedMargin;
	}

	/**
	 * 返回border值
	 * 
	 * @return cachedBorder
	 */
	public Insets getBorder() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BORDER) != VALID_CACHED_FLAG_BORDER) {
			cachedBorder = (Insets) getStylePropertyValue(
					ICSSConstants.BORDER_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_BORDER;
		}
		return cachedBorder;
	}

	/**
	 * 返回padding值
	 * 
	 * @return cachedPadding
	 */
	public Insets getPadding() {
		if ((validCachedFlags & VALID_CACHED_FLAG_PADDING) != VALID_CACHED_FLAG_PADDING) {
			cachedPadding = (Insets) getStylePropertyValue(
					ICSSConstants.PADDING_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_PADDING;
		}
		return cachedPadding;
	}

	/**
	 * 获取组件的最小尺寸<br>
	 * 如果没有CSS没有定义min-size属性，则返回默认最小尺寸<br>
	 * 子类可以重写此方法，返回适合自己的最小尺寸。
	 * 
	 * @return cachedMinSize
	 */
	public Metrics getMinSize() {
		if ((validCachedFlags & VALID_CACHED_FLAG_MIN_SIZE) != VALID_CACHED_FLAG_MIN_SIZE) {
			cachedMinSize = (Metrics) getStylePropertyValue(
					ICSSConstants.MIN_SIZE_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_MIN_SIZE;
		}
		return cachedMinSize;
	}

	/**
	 * 获取边界的大小
	 * 
	 * @return margin+border+padding
	 */
	public Insets getInsets() {
		if ((validCachedFlags & VALID_CACHED_FLAG_INSETS) != VALID_CACHED_FLAG_INSETS) {

			Insets margin = getMargin();
			Insets border = getBorder();
			Insets padding = getPadding();

			cachedInsets = new Insets();
			cachedInsets.top = margin.top + border.top + padding.top;
			cachedInsets.left = margin.left + border.left + padding.left;
			cachedInsets.bottom = margin.bottom + border.bottom
					+ padding.bottom;
			cachedInsets.right = margin.right + border.right + padding.right;

			validCachedFlags |= VALID_CACHED_FLAG_INSETS;

			margin = null;
			border = null;
			padding = null;
		}
		return cachedInsets;
	}

	/**
	 * 返回字体颜色值。<br>
	 * 默认情况下该值为<code>0x000000</code>.
	 * 
	 * @return {@link #cachedColor}
	 */
	public Integer getColor() {
		if ((validCachedFlags & VALID_CACHED_FLAG_COLOR) != VALID_CACHED_FLAG_COLOR) {
			Object colorValue = getStylePropertyValue(
					ICSSConstants.COLOR_STYLE_PROPERTY, true);
			if (colorValue != null) {
				cachedColor = (Integer) colorValue;
			} else {
				cachedColor = null;
			}
			validCachedFlags |= VALID_CACHED_FLAG_COLOR;
			colorValue = null;
		}
		return cachedColor;
	}

	/**
	 * 获取float属性
	 * 
	 * @return {@link #cachedFloat}
	 */
	public Alignment getFloat() {
		if ((validCachedFlags & VALID_CACHED_FLAG_FLOAT) != VALID_CACHED_FLAG_FLOAT) {
			cachedFloat = (Alignment) getStylePropertyValue(
					ICSSConstants.FLOAT_STYLE_PROPERTY, true);
			validCachedFlags |= VALID_CACHED_FLAG_FLOAT;
		}
		return cachedFloat;
	}

	/**
	 * 获取position类型
	 * 
	 * @return {@link #cachedPositionType}
	 */
	public Byte getPositionType() {
		if ((validCachedFlags & VALID_CACHED_FLAG_POSITION_TYPE) != VALID_CACHED_FLAG_POSITION_TYPE) {
			Object positionType = getStylePropertyValue(
					ICSSConstants.POSITION_STYLE_PROPERTY, true);
			if (positionType != null) {
				cachedPositionType = (Byte) positionType;
			} else {
				cachedPositionType = null;
			}
			validCachedFlags |= VALID_CACHED_FLAG_POSITION_TYPE;
			positionType = null;
		}
		return cachedPositionType;
	}

	/**
	 * 获取position的偏移距离
	 * 
	 * @return {@link #cachedPositionInsets}
	 */
	public Insets getPositionInsets() {
		if ((validCachedFlags & VALID_CACHED_FLAG_POSITION_INSETS) != VALID_CACHED_FLAG_POSITION_INSETS) {
			cachedPositionInsets = new Insets();
			cachedPositionInsets.left = getPositionAnchor(ICSSConstants.ALIGN_LEFT);
			cachedPositionInsets.top = getPositionAnchor(ICSSConstants.ALIGN_TOP);
			cachedPositionInsets.right = getPositionAnchor(ICSSConstants.ALIGN_RIGHT);
			cachedPositionInsets.bottom = getPositionAnchor(ICSSConstants.ALIGN_BOTTOM);
			validCachedFlags |= VALID_CACHED_FLAG_POSITION_INSETS;
		}
		return cachedPositionInsets;
	}

	/**
	 * 获取position一个方向上的偏移距离
	 * 
	 * @param anchor
	 *            锚定方向
	 * @return 该方向上的position偏移距离
	 */
	private int getPositionAnchor(String anchor) {
		Object obj = getStylePropertyValue(anchor, true);
		if (obj != null) {
			return ((Integer) obj).intValue();
		}
		return 0;
	}

	/**
	 * 获取字体
	 * 
	 * @return {@link #cachedFont}
	 */
	public CRSystemFont getFont() {
		if ((validCachedFlags & VALID_CACHED_FLAG_FONT) != VALID_CACHED_FLAG_FONT) {
			int fontFace = getFontFace();
			int fontStyle = getFontStyle();
			int fontSize = getFontSize();
			cachedFont = new CRSystemFont(fontFace, fontStyle, fontSize);
			validCachedFlags |= VALID_CACHED_FLAG_FONT;
		}
		return cachedFont;
	}

	/**
	 * 获取字体名称
	 * 
	 * @return The font face
	 */
	private int getFontFace() {
		Object fontFaceValue = getStylePropertyValue(
				ICSSConstants.FONT_FACE_STYLE_PROPERTY, true);
		if (fontFaceValue != null) {
			return ((Integer) fontFaceValue).intValue();
		}
		return CRSystemFont.FACE_SYSTEM;
	}

	/**
	 * 获取字体样式
	 * 
	 * @return The font style
	 */
	private int getFontStyle() {
		int fontStyle = CRSystemFont.STYLE_PLAIN;
		Object fontStyleValue = null;
		for (AbstractSNSDOM dom = this.dom; dom != null; dom = dom.father) {
			fontStyleValue = dom.getStyleSet().getStylePropertyValue(
					ICSSConstants.FONT_STYLE_STYLE_PROPERTY, false);
			if (fontStyleValue != null) {
				fontStyle |= ((Integer) fontStyleValue).intValue();
				fontStyleValue = null;
			}
		}
		return fontStyle;
	}

	/**
	 * 获取字体大小
	 * 
	 * @return The font size
	 */
	private int getFontSize() {
		Object fontSizeValue = getStylePropertyValue(
				ICSSConstants.FONT_SIZE_STYLE_PROPERTY, true);
		if (fontSizeValue != null) {
			return ((Integer) fontSizeValue).intValue();
		}
		return CRSystemFont.SIZE_SMALL;
	}

	/**
	 * 返回边界颜色数组。<br>
	 * 数组长为4。默认值为<code>null</code>
	 * 
	 * @return cachedBorderColor
	 */
	public Integer[] getBorderColor() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BORDER_COLOR) != VALID_CACHED_FLAG_BORDER_COLOR) {
			Object borderColorValue = getStylePropertyValue(
					ICSSConstants.BORDER_COLOR_STYLE_PROPERTY, false);
			if (borderColorValue != null) {
				cachedBorderColor = (Integer[]) borderColorValue;
			} else {
				cachedBorderColor = null;
			}
			validCachedFlags |= VALID_CACHED_FLAG_BORDER_COLOR;
			borderColorValue = null;
		}
		return cachedBorderColor;
	}

	/**
	 * 返回边框画笔风格。<br>
	 * 默认值为<code>Graphics.SOLID</code>。
	 * 
	 * @return cachedBorderStroke
	 */
	public Integer getBorderStroke() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BORDER_STROKE) != VALID_CACHED_FLAG_BORDER_STROKE) {
			Object borderStrokeValue = getStylePropertyValue(
					ICSSConstants.BORDER_STROKE_STYLE_PROPERTY, false);
			if (borderStrokeValue != null) {
				cachedBorderStroke = (Integer) borderStrokeValue;
			} else {
				cachedBorderStroke = new Integer(0);
			}
			validCachedFlags |= VALID_CACHED_FLAG_BORDER_STROKE;
			borderStrokeValue = null;
		}
		return cachedBorderStroke;
	}

	/**
	 * 返回边框图片数组。<br>
	 * 数组长度为8。默认值为<code>null</code>。
	 * 
	 * @return cachedBorderImage
	 */
	public CRImage[] getBorderImage() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BORDER_IMAGE) != VALID_CACHED_FLAG_BORDER_IMAGE) {
			Object borderImageValue = getStylePropertyValue(
					ICSSConstants.BORDER_IMAGE_STYLE_PROPERTY, false);
			if (borderImageValue != null) {
				cachedBorderImage = (CRImage[]) borderImageValue;
			} else {
				cachedBorderImage = null;
			}
			validCachedFlags |= VALID_CACHED_FLAG_BORDER_IMAGE;
			borderImageValue = null;
		}
		return cachedBorderImage;
	}

	/**
	 * 返回边框图片对齐方式数组。<br>
	 * 数组长度为8。默认值为<code>null</code>。
	 * 
	 * @return cachedBorderAlign
	 */
	public Alignment[] getBorderAlign() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BORDER_ALIGN) != VALID_CACHED_FLAG_BORDER_ALIGN) {
			Object borderAlignValue = getStylePropertyValue(
					ICSSConstants.BORDER_ALIGN_STYLE_PROPERTY, false);
			if (borderAlignValue != null) {
				cachedBorderAlign = (Alignment[]) borderAlignValue;
			} else {
				cachedBorderAlign = null;
			}
			validCachedFlags |= VALID_CACHED_FLAG_BORDER_ALIGN;
			borderAlignValue = null;
		}
		return cachedBorderAlign;
	}

	/**
	 * 返回背景颜色值。<br>
	 * 默认值为<code>null</code>
	 * 
	 * @return cachedBackgroundColor
	 */
	public Integer getBackgroundColor() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BACKGROUND_COLOR) != VALID_CACHED_FLAG_BACKGROUND_COLOR) {
			Object backgroundColorValue = getStylePropertyValue(
					ICSSConstants.BACKGROUND_COLOR_STYLE_PROPERTY, false);
			if (backgroundColorValue != null) {
				cachedBackgroundColor = (Integer) backgroundColorValue;
			} else {
				cachedBackgroundColor = null;
			}
			validCachedFlags |= VALID_CACHED_FLAG_BACKGROUND_COLOR;
			backgroundColorValue = null;
		}
		return cachedBackgroundColor;
	}

	/**
	 * 返回背景图片数组
	 * 
	 * @return cachedBackgroundImage
	 */
	public CRImage[] getBackgroundImage() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BACKGROUND_IMAGE) != VALID_CACHED_FLAG_BACKGROUND_IMAGE) {
			Object backgroundImageValue = getStylePropertyValue(
					ICSSConstants.BACKGROUND_IMAGE_STYLE_PROPERTY, false);
			if (backgroundImageValue != null) {
				cachedBackgroundImage = (CRImage[]) backgroundImageValue;
			} else {
				cachedBackgroundImage = null;
			}
			validCachedFlags |= VALID_CACHED_FLAG_BACKGROUND_IMAGE;
			backgroundImageValue = null;
		}
		return cachedBackgroundImage;
	}

	/**
	 * 返回背景图片的对齐方式数组
	 * 
	 * @return cachedBackgroundAlign
	 */
	public Alignment[] getBackgroundAlign() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BACKGROUNG_ALIGN) != VALID_CACHED_FLAG_BACKGROUNG_ALIGN) {
			Object backgroundAlignValue = getStylePropertyValue(
					ICSSConstants.BACKGROUND_ALIGN_STYLE_PROPERTY, false);
			if (backgroundAlignValue != null) {
				cachedBackgroundAlign = (Alignment[]) backgroundAlignValue;
			} else {
				cachedBackgroundAlign = AbstractSNSDOM.DEFAULT_BACKGROUND_ALIGN;
			}
			validCachedFlags |= VALID_CACHED_FLAG_BACKGROUNG_ALIGN;
			backgroundAlignValue = null;
		}
		return cachedBackgroundAlign;
	}

	/**
	 * 返回背景图片的平铺平铺样式数组
	 * 
	 * @return cachedBackgroundRepeat
	 */
	public Coordinates[] getBackgroundRepeat() {
		if ((validCachedFlags & VALID_CACHED_FLAG_BACKGROUND_REPEAT) != VALID_CACHED_FLAG_BACKGROUND_REPEAT) {
			Object backgroundRepeatValue = getStylePropertyValue(
					ICSSConstants.BACKGROUND_REPEAT_STYLE_PROPERTY, false);
			if (backgroundRepeatValue != null) {
				cachedBackgroundRepeat = (Coordinates[]) backgroundRepeatValue;
			} else {
				cachedBackgroundRepeat = AbstractSNSDOM.DEFAULT_BACKGROUND_REPEAT;
			}
			validCachedFlags |= VALID_CACHED_FLAG_BACKGROUND_REPEAT;
			backgroundRepeatValue = null;
		}
		return cachedBackgroundRepeat;
	}

	/**
	 * 返回变灰的颜色
	 * 
	 * @return cachedGrayedColor
	 */
	public Integer getGrayedColor() {
		if ((validCachedFlags & VALID_CACHED_FLAG_GRAYED_COLOR) != VALID_CACHED_FLAG_GRAYED_COLOR) {
			Object grayedColorValue = getStylePropertyValue(
					ICSSConstants.GRAYED_COLOR_STYLE_PROPERTY, true);
			if (grayedColorValue != null) {
				cachedGrayedColor = (Integer) grayedColorValue;
			} else {
				cachedGrayedColor = null;
			}
			validCachedFlags |= VALID_CACHED_FLAG_GRAYED_COLOR;
			grayedColorValue = null;
		}
		return cachedGrayedColor;
	}

	/**
	 * 返回组件之间的间距。<br>
	 * 此间距表示该组件下所有子组件的水平和垂直间距。
	 * 
	 * @return cachedGap
	 */
	public Coordinates getGap() {
		if ((validCachedFlags & VALID_CACHED_FLAG_GAP) != VALID_CACHED_FLAG_GAP) {
			cachedGap = (Coordinates) getStylePropertyValue(
					ICSSConstants.GAP_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_GAP;
		}
		return cachedGap;
	}

	/**
	 * 返回span值
	 * 
	 * @return cachedSpan
	 */
	public Coordinates getSpan() {
		if ((validCachedFlags & VALID_CACHED_FLAG_SPAN) != VALID_CACHED_FLAG_SPAN) {
			cachedSpan = (Coordinates) getStylePropertyValue(
					ICSSConstants.SPAN_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_SPAN;
		}
		return cachedSpan;
	}

	/**
	 * 
	 * 返回weight值
	 * 
	 * @return cachedWeight
	 */
	public Coordinates getWeight() {
		if ((validCachedFlags & VALID_CACHED_FLAG_WEIGHT) != VALID_CACHED_FLAG_WEIGHT) {
			cachedWeight = (Coordinates) getStylePropertyValue(
					ICSSConstants.WEIGHT_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_WEIGHT;
		}
		return cachedWeight;
	}

	/**
	 * 返回对齐属性
	 * 
	 * @return cachedAlign
	 */
	public Alignment getAlign() {
		if ((validCachedFlags & VALID_CACHED_FLAG_ALIGN) != VALID_CACHED_FLAG_ALIGN) {
			cachedAlign = (Alignment) getStylePropertyValue(
					ICSSConstants.ALIGN_STYLE_PROPERTY, false);
			validCachedFlags |= VALID_CACHED_FLAG_ALIGN;
		}
		return cachedAlign;
	}

	/**
	 * 根据样式属性名返回样式表中相关的样式
	 * 
	 * @param name
	 *            样式属性名
	 * @param inherited
	 *            标识该样式属性是否可以继承
	 * @return 指定的样式属性值
	 */
	private Object getStylePropertyValue(String name, boolean inherited) {
		ArrayList styles = getStyles(dom);
		if (styles != null) {
			for (int i = 0; i < styles.size(); i++) {
				Style style = (Style) styles.get(i);
				if (!checkStyleCompatibility(style)) {
					continue;
				}
				StyleProperty styleAttribute = style.getProperty(name);
				style = null;
				if (styleAttribute != null) {
					Object value = styleAttribute.getValue();
					styleAttribute = null;
					if (value != null) {
						return value;
					}
					return dom.getDefaultStylePropertyValue(name);
				}
			}
		}
		styles = null;
		if (inherited && dom.father != null) {
			StyleSet styleSet = dom.father.getStyleSet();
			Object parentStyleProperty = styleSet.getStylePropertyValue(name,
					inherited);
			styleSet = null;
			if (parentStyleProperty != null) {
				return parentStyleProperty;
			}
		}
		return dom.getDefaultStylePropertyValue(name);
	}

	/**
	 * 根据样式属性名返回相应的默认样式属性
	 * 
	 * @param name
	 *            样式属性
	 * @return 相关的默认样式属性
	 */

	/**
	 * 返回与此组件向关联的样式列表
	 * 
	 * @return 此组件向关联的样式列表
	 */
	private ArrayList getStyles(AbstractSNSDOM dom) {
		if (cachedStyles == null) {
			cachedStyles = CSSParser.getInstance().getStyles(dom);
		}
		return cachedStyles;
	}

	/**
	 * 测试样式的有效性
	 * 
	 * @param style
	 *            样式
	 * 
	 * @return 如果该样式对该组件有效，则返回<code>true</code>
	 */
	private boolean checkStyleCompatibility(Style style) {
		AbstractSNSDOM dom = this.dom;
		for (StyleSelector selector = style.getSelector(); selector != null; selector = selector.parent) {
			dom = getCompatibleWidget(selector, dom, selector != style
					.getSelector());
			if (dom == null) {
				return false;
			}
		}
		dom = null;
		return true;
	}

	/**
	 * 获取与传入选择器匹配的组件
	 * 
	 * @param selector
	 *            样式选择器
	 * 
	 * @param dom
	 *            匹配组件的起点
	 * 
	 * @param checkParents
	 *            标识是否匹配父组件
	 * 
	 * @return 匹配到的组件
	 */
	private AbstractSNSDOM getCompatibleWidget(StyleSelector selector,
			AbstractSNSDOM dom, boolean checkParents) {
		for (; dom != null; dom = checkParents ? dom.father : null) {
			// tag
			if (selector.isHasTag()) {
				if (!selector.getTag().equals(dom.tagName)
						&& !selector.getTag().equals(dom.getInheritedTag())) {
					continue;
				}
			}
			// ID
			if (selector.isHasId()) {
				if (!selector.getId().equals(dom.id)) {
					continue;
				}
			}
			// class
			if (selector.isHasStyleClass()) {
				String widgetStyleClasses = dom.classes;
				if (widgetStyleClasses != null) {
					boolean isCompatible = false;
					if (selector.getStyleClass().equals(widgetStyleClasses)) {
						isCompatible = true;
					}
					widgetStyleClasses = null;
					if (!isCompatible) {
						continue;
					}
				} else {
					continue;
				}
			}
			// PseudoClass
			if (selector.isHasPseudoClass()) {
				String[] pseudoClasses = selector.getPseudoClasses();
				if (pseudoClasses != null) {
					int i = pseudoClasses.length - 1;
					for (; i >= 0; --i) {
						if (!dom.isPseudoClassCompatible(pseudoClasses[i])) {
							break;
						}
					}
					pseudoClasses = null;
					if (i != -1) {
						continue;
					}
				}
			}
			return dom;
		}
		return null;
	}

	/**
	 * 设置样式的有效位
	 * 
	 * @param validCachedFlags
	 *            有效位
	 */
	public void setValidCachedFlags(long validCachedFlags) {
		this.validCachedFlags = validCachedFlags;
	}

	/**
	 * 清空所有缓存的样式
	 */
	public void clearCachedStyle() {
		if (cachedStyles != null)
			cachedStyles.removeAll();
		cachedStyles = null;
	}
}
