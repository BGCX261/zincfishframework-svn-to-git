package zincfish.zinccss.style;

import utils.ArrayList;

/**
 * <code>Style</code>代表一个完整的CSS样式
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class Style {

	private final StyleSelector selector;// 选择器

	private final ArrayList properties;// 属性列表

	/**
	 * 构造函数
	 * 
	 * @param selector
	 *            选择器
	 */
	public Style(StyleSelector selector) {
		this.selector = selector;
		this.properties = new ArrayList(2);
	}

	/**
	 * 获取选择器
	 * 
	 * @return 选择器
	 */
	public StyleSelector getSelector() {
		return selector;
	}

	/**
	 * 获取属性列表
	 * 
	 * @return 属性列表
	 */
	public ArrayList getProperties() {
		return properties;
	}

	/**
	 * 根据名字从属性列表中获取样式属性。如果不存在则返回null
	 * 
	 * @param name
	 *            样式属性名
	 * @return 匹配该样式名的样式属性
	 */
	public StyleProperty getProperty(final String name) {
		if (properties != null && properties.size() > 0) {
			for (int i = 0; i < properties.size(); i++) {
				StyleProperty property = (StyleProperty) properties.get(i);
				if (property.getName().equals(name)) {
					return property;
				}
				property = null;
			}
		}
		return null;
	}

	/**
	 * 向样式列表中添加样式属性
	 * 
	 * @param styleProperty
	 *            样式属性
	 */
	public void add(StyleProperty styleProperty) {
		properties.add(styleProperty);
	}

	// public void print() {
	// StringBuffer sb = new StringBuffer();
	// sb.append(selector.name);
	// sb.append('{');
	// sb.append('\n');
	// for (int i = 0; i < properties.size(); i++) {
	// StyleProperty property = (StyleProperty) properties.get(i);
	// sb.append('\t');
	// sb.append(property.getName());
	// sb.append(':');
	// sb.append(property.rawValue);
	// sb.append('\n');
	// property = null;
	// }
	// sb.append('}');
	// System.out.println(sb.toString());
	// sb = null;
	// }
}
