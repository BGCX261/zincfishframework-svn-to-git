package zincfish.zinccss;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;
import com.mediawoz.akebono.corerenderer.CRImage;
import config.ImageManager;
import utils.StringTokenizer;
import utils.StringUtil;
import zincfish.zinccss.model.*;
import zincfish.zinccss.style.*;
import zincfish.zinclayout.*;
import zincfish.zinclayout.layoutdata.BorderLayoutData;
import zincfish.zinclayout.layoutdata.GridLayoutData;
import zincfish.zinclayout.layoutdata.StaticLayoutData;

/**
 * <code>CSSConverter</code>负责将原始的CSS数据(字符串)转换成有意义的数据对象
 * 
 * @author JarodYv
 */
public class CSSConverter {

	private static CSSConverter instance = null;// 样式转换器的唯一实例

	/**
	 * 获取样式转换器的唯一实例
	 * 
	 * @return 样式转换器的唯一实例
	 */
	public static CSSConverter getInstance() {
		if (instance == null)
			instance = new CSSConverter();
		return instance;
	}

	/**
	 * 将原始的CSS数据转换成样式{@link Style}数组
	 * 
	 * @param rawSelectors
	 *            选择器数据
	 * @param rawDefinitions
	 *            样式数据
	 * @return 样式{@link Style}数组
	 */
	public Style[] convertStyleSheets(String rawSelectors, String rawDefinitions) {
		// 解析选择器，选择器用StringTokenizer.COMMA分隔
		StringTokenizer selectors = new StringTokenizer(rawSelectors,
				StringTokenizer.COMMA);
		int numSelectors = selectors.countTokens();// 选择器个数
		Style[] styles = new Style[numSelectors];// 有几个选择器就应该有几个样式
		for (int i = 0; i < numSelectors; ++i) {
			// 创建选择器树
			StyleSelector previousStyleSelector = null;
			StringTokenizer contextualSelectors = new StringTokenizer(selectors
					.nextToken(), " \t\n\r");
			while (contextualSelectors.hasMoreTokens()) {
				StyleSelector styleSelector = new StyleSelector(
						contextualSelectors.nextToken().toLowerCase());
				if (previousStyleSelector != null) {
					styleSelector.parent = previousStyleSelector;
				}
				previousStyleSelector = styleSelector;
				styleSelector = null;
			}
			contextualSelectors = null;
			// 创建样式
			styles[i] = new Style(previousStyleSelector);
			previousStyleSelector = null;
		}
		selectors = null;

		// 解析样式属性数据，样式属性用";"分隔
		StringTokenizer definitions = new StringTokenizer(rawDefinitions, ";");
		while (definitions.hasMoreTokens()) {
			String definition = definitions.nextToken().trim();
			if (definition.length() > 2) {
				StringTokenizer property = new StringTokenizer(definition, ":");
				if (property.countTokens() == 2) {
					String name = property.nextToken().trim();
					String rawValue = property.nextToken().trim();
					// 将属性添加到所有样式
					for (int i = 0; i < styles.length; ++i) {
						styles[i].add(new StyleProperty(name, rawValue));
					}
					name = null;
					rawValue = null;
				}
				property = null;
			}
		}
		definitions = null;

		return styles;
	}

	/**
	 * 将原始样式属性值转换成相应的对象
	 * 
	 * @param name
	 *            样式属性名
	 * @param rawData
	 *            样式属性原始值
	 * @return 转化后的样式属性对象
	 */
	public Object convertStyleProperty(String name, String rawData)
			throws IllegalArgumentException {

		// 转换color|bg-color|grayed-color样式属性
		if (ICSSConstants.COLOR_STYLE_PROPERTY.equals(name)
				|| ICSSConstants.BACKGROUND_COLOR_STYLE_PROPERTY.equals(name)
				|| ICSSConstants.GRAYED_COLOR_STYLE_PROPERTY.equals(name)) {
			return convertColor(rawData);
		}

		// 转换margin|border|padding样式属性
		if (ICSSConstants.MARGIN_STYLE_PROPERTY.equals(name)
				|| ICSSConstants.BORDER_STYLE_PROPERTY.equals(name)
				|| ICSSConstants.PADDING_STYLE_PROPERTY.equals(name)) {
			return convertInset(rawData);
		}

		// 转换bg-image样式属性
		if (ICSSConstants.BACKGROUND_IMAGE_STYLE_PROPERTY.equals(name)) {
			return convertImageArray(rawData, 1, StringTokenizer.SPLITER);
		}

		// 转换border-image样式属性
		if (ICSSConstants.BORDER_IMAGE_STYLE_PROPERTY.equals(name)) {
			return convertBorderImage(rawData);
		}

		// 转换bg-repeat样式属性
		if (ICSSConstants.BACKGROUND_REPEAT_STYLE_PROPERTY.equals(name)) {
			return convertRepeatArray(rawData, 1, StringTokenizer.SPLITER);
		}

		// 转换layout样式属性
		if (ICSSConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return convertLayout(rawData);
		}

		// 转换layout-data样式属性
		if (ICSSConstants.LAYOUT_DATA_STYLE_PROPERTY.equals(name)) {
			return convertLayoutData(rawData);
		}

		// 转换align样式属性
		if (ICSSConstants.ALIGN_STYLE_PROPERTY.equals(name)) {
			return convertAlignment(rawData);
		}

		// 转换bg-align样式属性
		if (ICSSConstants.BACKGROUND_ALIGN_STYLE_PROPERTY.equals(name)) {
			return convertAlignmentArray(rawData, 1, StringTokenizer.SPLITER);
		}

		// 转换boder-align样式属性
		if (ICSSConstants.BORDER_ALIGN_STYLE_PROPERTY.equals(name)) {
			return convertAlignmentArray(rawData, 8,
					StringTokenizer.DEFAULT_DELIM);
		}

		// 转换min-size样式属性
		if (ICSSConstants.MIN_SIZE_STYLE_PROPERTY.equals(name)) {
			return convertMetrics(rawData);
		}

		// 转换font-face样式属性
		if (ICSSConstants.FONT_FACE_STYLE_PROPERTY.equals(name)) {
			return convertFontFace(rawData);
		}

		// 转换font-style样式属性
		if (ICSSConstants.FONT_STYLE_STYLE_PROPERTY.equals(name)) {
			return convertFontStyle(rawData);
		}

		// 转换font-size样式属性
		if (ICSSConstants.FONT_SIZE_STYLE_PROPERTY.equals(name)) {
			return convertFontSize(rawData);
		}

		// 转换border-color样式属性
		if (ICSSConstants.BORDER_COLOR_STYLE_PROPERTY.equals(name)) {
			return convertBorderColor(rawData);
		}

		// 转换border-stroke样式属性
		if (ICSSConstants.BORDER_STROKE_STYLE_PROPERTY.equals(name)) {
			return convertStroke(rawData);
		}

		// 转换gap样式属性
		if (ICSSConstants.GAP_STYLE_PROPERTY.equals(name)) {
			return convertGap(rawData);
		}

		// 转换span样式属性
		if (ICSSConstants.SPAN_STYLE_PROPERTY.equals(name)) {
			return convertSpan(rawData);
		}

		// 转换weight样式属性
		if (ICSSConstants.WEIGHT_STYLE_PROPERTY.equals(name)) {
			return convertWeight(rawData);
		}

		// 转换float样式属性
		if (ICSSConstants.FLOAT_STYLE_PROPERTY.equals(name)) {
			return convertFloat(rawData);
		}

		// 转换position样式属性
		if (ICSSConstants.POSITION_STYLE_PROPERTY.equals(name)) {
			return convertPosition(rawData);
		}

		// 转换left|top|right|bottom样式
		if (ICSSConstants.ALIGN_LEFT.equals(name)
				|| ICSSConstants.ALIGN_TOP.equals(name)
				|| ICSSConstants.ALIGN_RIGHT.equals(name)
				|| ICSSConstants.ALIGN_BOTTOM.equals(name)) {
			return convertInteger(rawData);
		}

		// 转换transition样式属性
		if (ICSSConstants.TRANSITION_STYLE_PROPERTY.equals(name)) {
			return convertTransition(rawData);
		}

		throw new IllegalArgumentException("CSS ERROR:未知属性名-->" + name);
	}

	/**
	 * 转换颜色值
	 * 
	 * @param rawData
	 *            原始值
	 * 
	 * @return 转换成整型颜色值
	 */
	private final Integer convertColor(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		rawData = rawData.trim();

		if (rawData.charAt(0) == '#') {
			return new Integer(Integer.parseInt(rawData.substring(1), 16));
		}
		throw new IllegalArgumentException("CSS ERROR:非法颜色值");
	}

	/**
	 * 转换边框的颜色.由于有四个边框,因此返回一个长度为4的数组
	 * 
	 * @param rawData
	 *            原始值
	 * 
	 * @return 转换后的颜色数组
	 */
	private final Integer[] convertBorderColor(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData);
		if (values.countTokens() == 1) {
			Integer color = convertColor(values.nextToken());
			return new Integer[] { color, color, color, color };
		} else if (values.countTokens() >= 4) {
			return new Integer[] { convertColor(values.nextToken()),
					convertColor(values.nextToken()),
					convertColor(values.nextToken()),
					convertColor(values.nextToken()) };
		}
		throw new IllegalArgumentException("CSS ERROR:非法边框颜色值");
	}

	/**
	 * 转换字体风格
	 * 
	 * @param rawData
	 *            原始值
	 * 
	 * @return 转换后的字体风格
	 */
	private final Integer convertFontFace(String rawData) {
		int face = Font.FACE_SYSTEM;

		if (ICSSConstants.FONT_FACE_MONOSPACE.equals(rawData)) {
			face = Font.FACE_MONOSPACE;
		} else if (ICSSConstants.FONT_FACE_PROPORTIONAL.equals(rawData)) {
			face = Font.FACE_PROPORTIONAL;
		}

		return new Integer(face);
	}

	/**
	 * 转换字体样式
	 * 
	 * @param rawData
	 *            原始数据
	 * 
	 * @return 转换后的字体样式
	 */
	private final Integer convertFontStyle(String rawData) {
		StringTokenizer values = new StringTokenizer(rawData);
		int style = Font.STYLE_PLAIN;
		while (values.hasMoreTokens()) {
			String fontAttribute = values.nextToken().toLowerCase();
			if (ICSSConstants.FONT_STYLE_BOLD.equals(fontAttribute)) {
				style |= Font.STYLE_BOLD;
			} else if (ICSSConstants.FONT_STYLE_ITALIC.equals(fontAttribute)) {
				style |= Font.STYLE_ITALIC;
			} else if (ICSSConstants.FONT_STYLE_UNDERLINED
					.equals(fontAttribute)) {
				style |= Font.STYLE_UNDERLINED;
			}
		}
		return new Integer(style);
	}

	/**
	 * 转换字体大小
	 * 
	 * @param rawData
	 *            原始数据
	 * 
	 * @return 转换后的字体大小值
	 */
	private final Integer convertFontSize(String rawData) {
		int size = Font.SIZE_SMALL;
		if (ICSSConstants.FONT_SIZE_LARGE.equals(rawData)) {
			size = Font.SIZE_LARGE;
		} else if (ICSSConstants.FONT_SIZE_MEDIUM.equals(rawData)) {
			size = Font.SIZE_MEDIUM;
		}
		return new Integer(size);
	}

	/**
	 * 转换画笔风格<br>
	 * CSS语法：
	 * <ul>
	 * <li><code>border-stroke:stroke</code></li>
	 * </ul>
	 * 其中stroke接受的值为：
	 * <ul>
	 * <li><code>dotted</code></li>
	 * <li><code>solid</code></li>
	 * </ul>
	 * 
	 * @param rawData
	 *            原始数据
	 * @return 画笔风格
	 */
	private final Integer convertStroke(String rawData) {
		int stroke;
		if (ICSSConstants.STROKE_DOTTED.equals(rawData)) {
			stroke = Graphics.DOTTED;
		} else if (ICSSConstants.STROKE_SOLID.equals(rawData)) {
			stroke = Graphics.SOLID;
		} else {
			throw new IllegalArgumentException("CSS ERROR:非法笔刷样式 -->" + rawData);
		}
		return new Integer(stroke);
	}

	/**
	 * 转换span值<br>
	 * CSS语法为：
	 * <ul>
	 * <li><code>span:x y</code></li>
	 * </ul>
	 * 
	 * @param rawData
	 *            原始值
	 * @return 转换后的span对象
	 */
	private final Coordinates convertSpan(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData, 2,
				StringTokenizer.DEFAULT_DELIM);
		if (intValues != null) {
			return new Coordinates(intValues[0], intValues[1]);
		}
		throw new IllegalArgumentException("CSS ERROR:非法span值");
	}

	/**
	 * @param rawData
	 * @return The converted {@link Weight}
	 */
	private Coordinates convertWeight(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] fpValues = convertIntArray(rawData, 2,
				StringTokenizer.DEFAULT_DELIM);
		if (fpValues != null) {
			return new Coordinates(fpValues[0], fpValues[1]);
		}
		throw new IllegalArgumentException("CSS ERROR:非法weight值");
	}

	/**
	 * @param rawData
	 * @return The converted {@link Gap}
	 */
	private final Coordinates convertGap(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData, 1,
				StringTokenizer.DEFAULT_DELIM);
		if (intValues != null) {
			if (intValues.length == 1) {
				return new Coordinates(intValues[0], intValues[0]);
			} else if (intValues.length >= 1) {
				return new Coordinates(intValues[0], intValues[1]);
			}
		}
		throw new IllegalArgumentException("CSS ERROR:非法gap值");
	}

	/**
	 * 转换边距
	 * 
	 * @param rawData
	 *            原始数据
	 * 
	 * @return 转换后的边距对象
	 */
	private final Insets convertInset(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData, 1,
				StringTokenizer.DEFAULT_DELIM);
		if (intValues.length == 1) {
			return new Insets(intValues[0], intValues[0], intValues[0],
					intValues[0]);
		} else if (intValues.length >= 4) {
			return new Insets(intValues[0], intValues[1], intValues[2],
					intValues[3]);
		}
		throw new IllegalArgumentException("CSS ERROR:非法的边距值");
	}

	/**
	 * 转换尺寸
	 * 
	 * @param rawData
	 *            原始数据
	 * 
	 * @return 转换后的尺寸对象
	 */
	private final Metrics convertMetrics(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData, 2,
				StringTokenizer.DEFAULT_DELIM);
		if (intValues.length == 2) {
			return new Metrics(null, 0, 0, intValues[0], intValues[1]);
		} else if (intValues.length >= 4) {
			return new Metrics(null, intValues[0], intValues[1], intValues[2],
					intValues[3]);
		}
		throw new IllegalArgumentException("CSS ERROR:非法metrics参数");
	}

	/**
	 * 转换对齐样式<br>
	 * 语法为：
	 * <ul>
	 * <li><code>left top ...</code></li>
	 * </ul>
	 * 注意：只有第一个参数有效，后面的参数全部丢弃<br>
	 * 支持的参数为：
	 * <ul>
	 * <li><code>top</code></li>
	 * <li><code>top-right</code></li>
	 * <li><code>right</code></li>
	 * <li><code>bottom-right</code></li>
	 * <li><code>bottom</code></li>
	 * <li><code>bottom-left</code></li>
	 * <li><code>left</code></li>
	 * <li><code>top-left</code></li>
	 * </ul>
	 * 
	 * 
	 * @param rawData
	 *            原始数据
	 * @return 对齐样式
	 */
	private final Alignment convertAlignment(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData.trim());
		while (values.hasMoreTokens()) {
			String alignmentValue = values.nextToken().toLowerCase();
			if (ICSSConstants.ALIGN_TOP_LEFT.equals(alignmentValue)) {
				return Alignment.TOP_LEFT;
			} else if (ICSSConstants.ALIGN_TOP.equals(alignmentValue)) {
				return Alignment.TOP;
			} else if (ICSSConstants.ALIGN_TOP_RIGHT.equals(alignmentValue)) {
				return Alignment.TOP_RIGHT;
			} else if (ICSSConstants.ALIGN_LEFT.equals(alignmentValue)) {
				return Alignment.LEFT;
			} else if (ICSSConstants.ALIGN_CENTER.equals(alignmentValue)) {
				return Alignment.CENTER;
			} else if (ICSSConstants.ALIGN_RIGHT.equals(alignmentValue)) {
				return Alignment.RIGHT;
			} else if (ICSSConstants.ALIGN_BOTTOM_LEFT.equals(alignmentValue)) {
				return Alignment.BOTTOM_LEFT;
			} else if (ICSSConstants.ALIGN_BOTTOM.equals(alignmentValue)) {
				return Alignment.BOTTOM;
			} else if (ICSSConstants.ALIGN_BOTTOM_RIGHT.equals(alignmentValue)) {
				return Alignment.BOTTOM_RIGHT;
			} else if (ICSSConstants.ALIGN_FILL.equals(alignmentValue)) {
				return Alignment.FILL;
			} else if (ICSSConstants.ALIGN_FILL_TOP.equals(alignmentValue)) {
				return Alignment.FILL_TOP;
			} else if (ICSSConstants.ALIGN_FILL_LEFT.equals(alignmentValue)) {
				return Alignment.FILL_LEFT;
			} else if (ICSSConstants.ALIGN_FILL_CENTER.equals(alignmentValue)) {
				return Alignment.FILL_CENTER;
			} else if (ICSSConstants.ALIGN_FILL_RIGHT.equals(alignmentValue)) {
				return Alignment.FILL_RIGHT;
			} else if (ICSSConstants.ALIGN_FILL_BOTTOM.equals(alignmentValue)) {
				return Alignment.FILL_BOTTOM;
			}
		}
		throw new IllegalArgumentException("CSS ERROR:非法对齐参数-->" + rawData);
	}

	/**
	 * 将图片数据转换成图片对象<br>
	 * css语法如下:
	 * <ul>
	 * <li><code>url(src)</code></li>
	 * <li><code>url(src,x,y,width,height)</code></li>
	 * <li><code>url(src,x,y,width,height,transform)</code></li>
	 * </ul>
	 * 
	 * @param rawData
	 *            原始数据
	 * 
	 * @return 转换后的图片对象
	 */
	private final CRImage convertImage(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		String rawParams = null;
		if ((rawParams = extractRawParams(rawData.trim(),
				ICSSConstants.PREFIX_URL)) != null) {
			CRImage image = convertImageDefinition(rawParams);
			rawParams = null;
			if (image != null) {
				return image;
			}
		}
		throw new IllegalArgumentException("CSS ERROR:非法图片值 : " + rawData);
	}

	/**
	 * 根据CSS的定义生成图片对象<br>
	 * 解析的语法为 :
	 * <ul>
	 * <li><code>src</code></li>
	 * <li><code>src,x,y,width,height</code></li>
	 * <li><code>src,x,y,width,height,transform</code></li>
	 * </ul>
	 * 
	 * @param rawData
	 *            原始数据
	 * @return 图片对象
	 */
	private final CRImage convertImageDefinition(String rawData) {
		StringTokenizer st = new StringTokenizer(rawData, StringTokenizer.COMMA);
		int numTokens = st.countTokens();
		if (numTokens >= 1) {
			CRImage fullImage = null;
			String imgSrc = st.nextToken();
			// FIXME 此处可能需要对图片的URL进行容错处理
			fullImage = ImageManager.instance.getImage(imgSrc);
			if (fullImage != null) {
				if (numTokens >= 5) {
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					int width = Integer.parseInt(st.nextToken());
					int height = Integer.parseInt(st.nextToken());
					int transform = Sprite.TRANS_NONE;
					if (numTokens == 6) {
						transform = StringUtil.rotation2Int(st.nextToken());
					}
					try {
						return CRImage.createImage(fullImage, x, y, width,
								height, transform);
					} catch (Exception e) {
						System.err.println("CSS ERROR:生成图片出错-->" + imgSrc);
					}
				} else {
					return fullImage;
				}
			}
		}
		return null;
	}

	/**
	 * 转换边框图片
	 * 
	 * @param rawData
	 *            原始样式数据
	 * @return 边框图片数组
	 */
	private final CRImage[] convertBorderImage(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData);
		int numValues = values.countTokens();
		if (numValues == 1) {
			CRImage[] images = new CRImage[8];
			CRImage image = convertImage(rawData);
			if (image != null) {
				for (int i = 0; i < 8; ++i) {
					images[i] = image;
				}
			}
			return images;
		}
		if (numValues == 5) {
			CRImage[] images = new CRImage[8];
			CRImage image = convertImage(values.nextToken());
			if (image != null) {
				try {
					int imageWidth = image.getWidth();
					int imageHeight = image.getHeight();
					int top = Integer.parseInt(values.nextToken().trim());
					int right = Integer.parseInt(values.nextToken().trim());
					int bottom = Integer.parseInt(values.nextToken().trim());
					int left = Integer.parseInt(values.nextToken().trim());

					if (top != 0) {
						images[0] = CRImage.createImage(image, left, 0,
								imageWidth - left - right, top, 0); // top
						if (right != 0) {
							images[1] = CRImage.createImage(image, imageWidth
									- right, 0, right, top, 0); // top-right
						}
					}
					if (right != 0) {
						images[2] = CRImage.createImage(image, imageWidth
								- right, top, right,
								imageHeight - top - bottom, 0); // right
						if (bottom != 0) {
							images[3] = CRImage.createImage(image, imageWidth
									- right, imageHeight - bottom, right,
									bottom, 0); // bottom-right
						}
					}
					if (bottom != 0) {
						images[4] = CRImage.createImage(image, left,
								imageHeight - bottom,
								imageWidth - left - right, bottom, 0); // bottom
						if (left != 0) {
							images[5] = CRImage.createImage(image, 0,
									imageHeight - bottom, left, bottom, 0); // bottom-left
						}
					}
					if (left != 0) {
						images[6] = CRImage.createImage(image, 0, top, left,
								imageHeight - top - bottom, 0); // left
						if (top != 0) {
							images[7] = CRImage.createImage(image, 0, 0, left,
									top, 0); // top-left
						}
					}

				} catch (Exception e) {
					throw new IllegalArgumentException("CSS ERROR:非法图片边框值");
				}
			} else {
				throw new IllegalArgumentException("CSS ERROR:非法图片边框值");
			}
			return images;
		}
		if (numValues == 8) {
			CRImage[] images = new CRImage[8];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					images[i] = convertImage(values.nextToken());
					continue;
				} catch (Exception e) {
					return null;
				}
			}
			return images;
		}
		throw new IllegalArgumentException("CSS ERROR:非法图片边框值");
	}

	/**
	 * 转换排版样式<br>
	 * CSS语法如下:
	 * <ul>
	 * <li><code>formlayout</code>
	 * <li><code>inlinelayout(bool,align)</code>
	 * <li><code>flowlayout(align)</code>
	 * <li><code>gridlayout(colnum,rownum)</code>
	 * <li><code>borderlayout</code>
	 * <li><code>staticlayout</code>
	 * <li><code>tablelayout</code>
	 * </ul>
	 * 
	 * @param rawData
	 *            原始数据
	 * @return 排版样式
	 */
	private final ILayout convertLayout(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		String rawParams = null;
		if ((rawParams = extractRawParams(rawData, ICSSConstants.FORM_LAYOUR)) != null) {
			Alignment alignment = convertAlignment(rawParams);
			if (alignment != null) {
				return new FormLayout(alignment);
			}
			return new FormLayout();
		} else if ((rawParams = extractRawParams(rawData,
				ICSSConstants.INLINE_LAYOUT)) != null) {
			StringTokenizer st = new StringTokenizer(rawParams,
					StringTokenizer.COMMA);
			if (st.countTokens() >= 2) {
				boolean horizontal = StringUtil.Str2Bool(st.nextToken());
				Alignment alignment = convertAlignment(st.nextToken());
				if (alignment != null) {
					return new InlineLayout(horizontal, alignment);
				}
				return new InlineLayout(horizontal);
			}
			return new InlineLayout();
		} else if ((rawParams = extractRawParams(rawData,
				ICSSConstants.FLOW_LAYOUT)) != null) {
			Alignment alignment = convertAlignment(rawParams);
			if (alignment != null) {
				return new FlowLayout(alignment);
			}
			return new FlowLayout();
		} else if ((rawParams = extractRawParams(rawData,
				ICSSConstants.GRID_LAYOUT)) != null) {
			StringTokenizer st = new StringTokenizer(rawParams,
					StringTokenizer.COMMA);
			if (st.countTokens() >= 2) {
				int numCols = Integer.parseInt(st.nextToken().trim());
				int numRows = Integer.parseInt(st.nextToken().trim());
				return new GridLayout(numCols, numRows);
			}
		} else if (rawData.startsWith(ICSSConstants.BORDER_LAYOUT)) {
			return BorderLayout.instance;
		} else if (rawData.startsWith(ICSSConstants.STATIC_LAYOUT)) {
			return StaticLayout.instance;
		} else if (rawData.startsWith(ICSSConstants.TABLE_LAYOUT)) {
			return TableLayout.instance;
		}
		throw new IllegalArgumentException("CSS ERROR:非法排版参数-->" + rawData);
	}

	/**
	 * 转化排版数据<br>
	 * CSS语法为:
	 * <ul>
	 * <li><code>bld(data)</code></li>
	 * <li><code>sld(align,x,y)</code></li>
	 * </ul>
	 * 
	 * @param rawData
	 *            原始值
	 * @return 转换后的排版数据
	 */
	private final Object convertLayoutData(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		String rawParams = null;
		// GridLayoutData
		if ((rawParams = extractRawParams(rawData,
				ICSSConstants.GRID_LAYOUT_DATA)) != null) {
			int[] intValues = convertIntArray(rawParams, 0,
					StringTokenizer.COMMA);
			if (intValues != null) {
				return new GridLayoutData(intValues);
			}
		}
		// BorderLayouData
		if ((rawParams = extractRawParams(rawData,
				ICSSConstants.BORDER_LAYOUT_DATA)) != null) {
			if (ICSSConstants.BORDER_NORTH.equals(rawParams)) {
				return BorderLayoutData.BORDER_LAYOUT_NORTH;
			} else if (ICSSConstants.BORDER_WEST.equals(rawParams)) {
				return BorderLayoutData.BORDER_LAYOUT_WEST;
			} else if (ICSSConstants.BORDER_EAST.equals(rawParams)) {
				return BorderLayoutData.BORDER_LAYOUT_EAST;
			} else if (ICSSConstants.BORDER_SOUTH.equals(rawParams)) {
				return BorderLayoutData.BORDER_LAYOUT_SOUTH;
			} else if (ICSSConstants.BORDER_CENTER.equals(rawParams)) {
				return BorderLayoutData.BORDER_LAYOUT_CENTER;
			}
			throw new IllegalArgumentException("CSS ERROE:无效的bld值-->"
					+ rawParams);
		}
		// StaticLayoutData
		if ((rawParams = extractRawParams(rawData,
				ICSSConstants.STATIC_LAYOUT_DATA)) != null) {
			int pos = rawParams.indexOf(StringTokenizer.COMMA);
			if (pos != -1) {
				try {
					Alignment alignment = convertAlignment(rawParams.substring(
							0, pos));
					int[] values = convertIntArray(
							rawParams.substring(pos + 1), 2,
							StringTokenizer.COMMA);
					if (values != null) {
						return new StaticLayoutData(alignment, values[0],
								values[1]);
					}
				} catch (Exception e) {
				}
			} else {
				Alignment alignment = convertAlignment(rawParams);
				return new StaticLayoutData(alignment);
			}
			throw new IllegalArgumentException("CSS ERROE:无效的sld值-->"
					+ rawParams);
		}
		throw new IllegalArgumentException("CSS ERROR:非法的排版数据-->" + rawData);
	}

	/**
	 * 转换transition样式
	 * 
	 * @param rawData
	 *            原始样式数据
	 * @return transition数据对象
	 */
	private final Transition convertTransition(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		String rawParams = null;
		Transition transition = null;
		if ((rawParams = extractRawParams(ICSSConstants.TRANSITION_FLY, rawData)) != null) {
			// TODO 解析fly数据
		} else if ((rawParams = extractRawParams(ICSSConstants.TRANSITION_FADE,
				rawData)) != null) {
			// TODO 解析fade数据
		} else {
			throw new IllegalArgumentException("CSS ERROR:非法transition值");
		}
		return transition;
	}

	/**
	 * 转换float样式
	 * 
	 * @param rawData
	 * @return
	 */
	private final Alignment convertFloat(String rawData) {
		if (ICSSConstants.ALIGN_RIGHT.equals(rawData))
			return Alignment.RIGHT;
		else
			return Alignment.LEFT;
	}

	/**
	 * 转换position样式
	 * 
	 * @param rawData
	 *            原始样式数据
	 * @return position样式
	 */
	private final Byte convertPosition(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		if (ICSSConstants.POSITION_FIXED.equals(rawData)) {
			return ICSSConstants.POSITION_TYPE_FIXED;
		}
		if (ICSSConstants.POSITION_RELATIVE.equals(rawData)) {
			return ICSSConstants.POSITION_TYPE_RELATIVE;
		}
		if (ICSSConstants.POSITION_ABSOLUTE.equals(rawData)) {
			return ICSSConstants.POSITION_TYPE_ABSOLUTE;
		}
		return ICSSConstants.POSITION_TYPE_STATIC;
	}

	/**
	 * 将字符串转换成Integer
	 * 
	 * @param rawData
	 *            原始样式数据
	 * @return 转换后的数据
	 */
	private final Integer convertInteger(String rawData) {
		System.out.println("comvert Interger");
		if (isNone(rawData)) {
			System.out.println("rawData = null");
			return null;
		}
		int i = 0;
		try {
			i = Integer.parseInt(rawData);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("int = " + i);
		return new Integer(i);
	}

	/**
	 * 将原始样式数据装化成图片数组
	 * 
	 * @param rawData
	 *            原始CSS样式数据
	 * 
	 * @param wantedSize
	 *            数组大小
	 * 
	 * @param delim
	 *            分隔符
	 * 
	 * @return 转换后的图片数组
	 */
	private final CRImage[] convertImageArray(String rawData, int wantedSize,
			String delim) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData, delim);
		if (values.countTokens() >= wantedSize) {
			CRImage[] images = new CRImage[values.countTokens()];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					images[i] = convertImage(values.nextToken());
					continue;
				} catch (Exception e) {
					return null;
				}
			}
			return images;
		}
		throw new IllegalArgumentException("CSS ERROR:非法图片值!");
	}

	/**
	 * 转换repeat属性
	 * 
	 * @param rawData
	 *            原始样式数据
	 * @return repeat数据对象
	 */
	private final Coordinates convertRepeat(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData.trim(), 1,
				StringTokenizer.DEFAULT_DELIM);
		if (intValues != null) {
			if (intValues.length == 1) {
				return new Coordinates(intValues[0], intValues[0]);
			} else if (intValues.length >= 2) {
				return new Coordinates(intValues[0], intValues[1]);
			}
		}
		throw new IllegalArgumentException("CSS ERROR: 非法的repeat值");
	}

	/**
	 * 装换repeat数组
	 * 
	 * @param rawData
	 *            原始样式数据
	 * @param wantedSize
	 *            数组大小
	 * @param delim
	 *            分隔符
	 * @return repeat对象数据数组
	 */
	private final Coordinates[] convertRepeatArray(String rawData,
			int wantedSize, String delim) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData, delim);
		if (values.countTokens() >= wantedSize) {
			Coordinates[] repeats = new Coordinates[values.countTokens()];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					repeats[i] = convertRepeat(values.nextToken());
					continue;
				} catch (Exception e) {
					return null;
				}
			}
			return repeats;
		}
		throw new IllegalArgumentException("CSS ERROR:非法的repeat值");
	}

	/**
	 * 检测原始值是否为'none'
	 * 
	 * @param rawData
	 *            原始值
	 * 
	 * @return 如果为null或'none'则返回<code>true</code>
	 */
	private boolean isNone(String rawData) {
		return (rawData == null || ICSSConstants.NONE_VALUE.equals(rawData));
	}

	/**
	 * 将原始数据转换成对齐对象数组<br>
	 * 语法格式为:
	 * <ul>
	 * <li><code>align1|align2</code></li>
	 * </ul>
	 * 
	 * @param rawData
	 *            原始数据
	 * @param wantedSize
	 *            要拆分出的数据个数
	 * @param delim
	 *            分隔符
	 * @return 转换后的对齐对象数组
	 */
	private final Alignment[] convertAlignmentArray(String rawData,
			int wantedSize, String delim) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData, delim);
		if (values.countTokens() >= wantedSize) {
			Alignment[] alignments = new Alignment[values.countTokens()];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					alignments[i] = convertAlignment(values.nextToken());
					continue;
				} catch (Exception e) {
					return null;
				}
			}
			return alignments;
		}
		throw new IllegalArgumentException("CSS ERROR:非法对齐参数-->" + rawData);
	}

	/**
	 * 将原始值传换成整形数组
	 * 
	 * @param rawData
	 *            原始值
	 * @param wantedMinSize
	 *            要拆分出的数据个数
	 * @param delim
	 *            分隔符
	 * @return 转换后的整形数组
	 */
	private final int[] convertIntArray(String rawData, int wantedMinSize,
			String delim) {
		StringTokenizer values = new StringTokenizer(rawData, delim);
		if (values.countTokens() >= wantedMinSize || wantedMinSize <= 0) {
			int[] intValues = new int[values.countTokens()];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					intValues[i] = Integer.parseInt(values.nextToken().trim());
					continue;
				} catch (Exception e) {
					e.printStackTrace();
					intValues = null;
					return null;
				}
			}
			return intValues;
		}
		return null;
	}

	/**
	 * 从形式为prefix(XXX)的字符串中抽取出XXX<br>
	 * 语法格式为:
	 * <ul>
	 * <li><code>prefix(XXX)</code></li>
	 * </ul>
	 * 
	 * @param rawData
	 *            原始值
	 * @param prefix
	 *            前缀
	 * @return 解析出的有效数据
	 */
	private final String extractRawParams(String rawData, String prefix) {
		if (rawData.startsWith(prefix)) {
			int posStart = rawData.indexOf(prefix + "(");
			int posEnd = rawData.indexOf(")");
			if (posStart != -1 && posEnd != -1 && posStart < posEnd) {
				return rawData
						.substring(posStart + prefix.length() + 1, posEnd);
			}
		}
		return null;
	}
}
