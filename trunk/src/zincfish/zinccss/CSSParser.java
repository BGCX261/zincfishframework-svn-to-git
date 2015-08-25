package zincfish.zinccss;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import utils.ArrayList;
import zincfish.zinccss.style.Style;
import zincfish.zinccss.style.StyleProperty;
import zincfish.zinccss.style.StyleSelector;
import zincfish.zincdom.AbstractSNSDOM;

/**
 * <code>CSSParser</code>主要完成CSS的解析
 * 
 * @author Jarod Yv
 */
public final class CSSParser {

	private static CSSParser instance = null;// CSS解析器的唯一实例

	private CSSConverter converter = null;// 样式属性转换器

	private ArrayList registeredStyles = null;// 注册的样式列表

	/**
	 * 获取CSS解析器的唯一实例
	 * 
	 * @return CSS解析器的唯一实例
	 */
	public static CSSParser getInstance() {
		if (instance == null)
			instance = new CSSParser();
		return instance;
	}

	/*
	 * 私有构造函数，确保只有一个CSSParser实例
	 */
	private CSSParser() {
		converter = CSSConverter.getInstance();
		registeredStyles = new ArrayList(5);
	}

	/**
	 * 解析CSS文件，将解析出的样式放入样式列表
	 * 
	 * @param path
	 *            CSS文件路径
	 */
	public void parseCSS(String path) throws Exception {
		if (path != null) {
			InputStream is = getClass().getResourceAsStream(path);

			if (is != null) {
				parseCSS(is);
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					is = null;
				}
			}
		}
	}

	/**
	 * 解析CSS输入流，将解析出的样式放入样式列表
	 * 
	 * @param inputStream
	 *            CSS输入流
	 */
	public void parseCSS(InputStream inputStream) throws Exception {
		if (inputStream != null) {
			Reader reader = new InputStreamReader(inputStream);
			try {
				boolean selectorsCapture = true;// 标识是否是选择器
				boolean commentCapture = false;// 标识是否是注释

				StringBuffer rawSelectors = new StringBuffer();// 选择器缓存
				StringBuffer rawDefinitions = new StringBuffer();// 样式原始数据缓存

				for (int c = reader.read(); c != -1;) {
					if (commentCapture) {// 对注释做跳过处理
						if (c == '*') {
							if ((c = reader.read()) == '/') {
								commentCapture = false;// 遇到'*/'结束注释
							} else {
								continue;
							}
						}
					} else {
						if (c == '*') {
							throw new IllegalArgumentException(
									"CSS ERROR: 非法的css注释块");
						}
						if (c == '/') {
							if ((c = reader.read()) == '*') {
								commentCapture = true;// 遇到'/*'开始注释
								c = reader.read();
								continue;
							} else {
								if (selectorsCapture) {
									rawSelectors.append('/');
								} else {
									rawDefinitions.append('/');
								}
							}
						}

						if (selectorsCapture) {
							if (c == '{') {
								selectorsCapture = false;// 遇到'{'说明选择器定义结束
							} else {
								rawSelectors.append((char) c);
							}
						} else {
							if (c == '}') {// 遇到'}'说明样式定义完成
								// 根据原始数据创建样式表
								Style[] styles = converter.convertStyleSheets(
										rawSelectors.toString(), rawDefinitions
												.toString());
								if (styles != null) {
									for (int i = 0; i < styles.length; ++i) {
										registerStyle(styles[i]);
									}
								}
								styles = null;
								// 清除StringBuffer
								rawSelectors.delete(0, rawSelectors.length());
								rawDefinitions.delete(0, rawDefinitions
										.length());
								// 切换到获取选择器状态
								selectorsCapture = true;
							} else {
								rawDefinitions.append((char) c);// 将字符加入原始数据
							}
						}
					}
					c = reader.read();
				}
				rawSelectors = null;
				rawDefinitions = null;
				if (commentCapture) {// 如果所有数据读完还没有关闭注释，则抛出异常
					throw new IllegalArgumentException("CSS ERROR: 非法注释块");
				}

				if (!selectorsCapture) {// 如果所有数据读完还没有打开获取选择器状态，则排除异常
					throw new IllegalArgumentException("CSS ERROR: 非法的选择器");
				}
				return;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						reader = null;
					}
				}
			}
		}
		throw new IllegalArgumentException("CSS ERROR: 无效的CSS输入流");
	}

	/**
	 * 注册选择器。如果已经注册，则拷贝到新选择器
	 * 
	 * @param style
	 *            样式
	 */
	private void registerStyle(final Style style) {
		if (style != null) {
			Style registredStyle = null;
			if (registeredStyles != null && registeredStyles.size() > 0) {
				for (int i = 0; i < registeredStyles.size(); i++) {
					registredStyle = (Style) registeredStyles.get(i);
					if (style.getSelector()
							.equals(registredStyle.getSelector())) {
						break;
					}
					registredStyle = null;
				}
			}

			if (registredStyle != null) {
				// 如果一个样式已经被同一个选择器注册过，则将其整个拷贝到新选择器
				ArrayList properties = style.getProperties();
				if (properties != null && properties.size() > 0) {
					for (int i = 0; i < properties.size(); i++) {
						StyleProperty property = (StyleProperty) properties
								.get(i);
						registredStyle.add(property);
						property = null;
					}
				}
				properties = null;
			} else {
				registeredStyles.add(style);
			}
			registredStyle = null;
		}
	}

	/**
	 * 返回与传入组件相关联的样式列表
	 * 
	 * @param dom
	 *            组件
	 * @return 与组件向关联的样式列表
	 */
	public ArrayList getStyles(final AbstractSNSDOM dom) {
		if (dom != null && registeredStyles != null
				&& registeredStyles.size() > 0) {
			ArrayList scores = new ArrayList();// 记录样式的类型
			ArrayList styles = new ArrayList();// 保存样式
			for (int i = 0; i < registeredStyles.size(); i++) {
				Style style = (Style) registeredStyles.get(i);
				int score = matchSelector(style, dom);
				if (score != 0) {
					Integer integerScore = new Integer(score);
					boolean added = false;
					// 根据score的大小，将score小的插入到前面
					for (int j = 0; j < scores.size(); ++j) {
						if (score >= ((Integer) scores.get(j)).intValue()) {
							scores.add(j, integerScore);
							styles.add(j, style);
							added = true;
							break;
						}
					}
					if (!added) {
						scores.add(integerScore);
						styles.add(style);
					}
					integerScore = null;
				}
				style = null;
			}
			scores = null;
			if (dom.style != null) {
				// 将xml作者定义的样式放入链表最前面
				Style authorStyle = parseAuthorStyle(dom);
				styles.add(0, authorStyle);
				authorStyle = null;
			}
			if (styles != null) {
				return styles;
			}
		}
		return null;
	}

	private final Style parseAuthorStyle(AbstractSNSDOM dom) {
		Style[] styles = CSSConverter.getInstance().convertStyleSheets(
				dom.tagName, dom.style);
		if (styles != null && styles.length > 0) {
			return styles[0];
		}
		return null;
	}

	/**
	 * 根据选择器匹配样式
	 * 
	 * @param style
	 *            样式
	 * @param dom
	 *            组件
	 * @return 匹配到的结果
	 */
	private int matchSelector(Style style, AbstractSNSDOM dom) {
		int score = 0;
		StyleSelector styleSelector = style.getSelector();// 获取选择器
		AbstractSNSDOM currentDOM = dom;// 设置当前用于匹配的组件
		String[] pseudoClasses = dom.getAvailablePseudoClasses();// 获取组件支持的伪类
		while (styleSelector != null) {
			boolean isCompatible = false;// 表示是否匹配上，如果匹配上则不再向下匹配
			// 开始匹配。匹配的顺序是id > class > tag > Pseudo class
			while (currentDOM != null && !isCompatible) {
				// Id
				if (styleSelector.isHasId()) {
					if (currentDOM.id != null
							&& currentDOM.id.equals(styleSelector.getId())) {
						isCompatible = true;
						score += 1000000;
					}
				}

				// Class
				if (!isCompatible && styleSelector.isHasStyleClass()) {
					String styleClasses = currentDOM.classes;
					if (styleClasses != null) {
						if (styleClasses.equals(styleSelector.getStyleClass())) {
							isCompatible = true;
							score += 10000;
						}
					}
					styleClasses = null;
				}

				// Tag
				if (styleSelector.isHasTag()) {
					// Tag
					if (!isCompatible
							&& currentDOM.tagName != null
							&& currentDOM.tagName
									.equals(styleSelector.getTag())) {
						isCompatible = true;
						score += 100;
					}
					// Inherited tag
					if (!isCompatible
							&& currentDOM.getInheritedTag() != null
							&& currentDOM.getInheritedTag().equals(
									styleSelector.getTag())) {
						isCompatible = true;
						score++;
					}
				}

				if (!isCompatible && score == 0) {
					return 0;
				}

				// Pseudo class
				if (styleSelector.isHasPseudoClass() && pseudoClasses != null) {
					for (int i = pseudoClasses.length - 1; i >= 0; --i) {
						for (int j = styleSelector.getPseudoClasses().length - 1; j >= 0; --j) {
							if (pseudoClasses[i].equals(styleSelector
									.getPseudoClasses()[j])) {
								isCompatible = true;
								score += 100000000;
							}
						}
					}
				}
				// 如果没有匹配到，则匹配父组件
				currentDOM = currentDOM.father;
			}

			styleSelector = styleSelector.parent;
			if (currentDOM == null && styleSelector != null || !isCompatible) {
				return 0;
			}
		}
		styleSelector = null;
		currentDOM = null;
		pseudoClasses = null;
		return score;
	}

	/**
	 * 清除所有注册的样式
	 */
	public void removeAllStyles() {
		registeredStyles.removeAll();
	}

	/**
	 * 释放资源
	 */
	public void release() {
		removeAllStyles();
		registeredStyles = null;
		converter = null;
		instance = null;
		// System.gc();
	}

	// public void print() {
	// for (int i = 0; i < registeredStyles.size(); i++) {
	// Style style = (Style) registeredStyles.get(i);
	// style.print();
	// style = null;
	// }
	// }
}
