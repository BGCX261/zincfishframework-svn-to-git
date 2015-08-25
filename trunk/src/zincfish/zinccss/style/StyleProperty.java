package zincfish.zinccss.style;

import zincfish.zinccss.CSSConverter;

/**
 * <code>StyleProperty</code>代表样式的属性, 样式属性格式上name-value对
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class StyleProperty {

	private String name;// 样式属性名

	private Object value;// 样式属性值

	public String rawValue;// 原始值

	/**
	 * 构造函数
	 * 
	 * @param name
	 *            样式属性名
	 * @param rawValue
	 *            原始值
	 */
	public StyleProperty(String name, String rawValue) {
		this.name = name.toLowerCase();
		this.rawValue = rawValue;
	}

	/**
	 * 构造函数
	 * 
	 * @param name
	 *            样式属性名
	 * @param value
	 *            样式属性值
	 */
	public StyleProperty(String name, Object value) {
		this(name, null);
		this.value = value;
	}

	/**
	 * 获取样式属性名
	 * 
	 * @return 样式属性名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取样式值
	 * 
	 * @return 样式值
	 */
	public Object getValue() {
		if (rawValue != null) {
			// 转换样式属性值
			value = CSSConverter.getInstance()
					.convertStyleProperty(name, rawValue);
			// 清除原始样式值
			rawValue = null;
		}
		return value;
	}

	/**
	 * 创建当前样式的一份完全拷贝
	 * 
	 * @return 当前样式的完全拷贝
	 */
	public StyleProperty copy() {
		StyleProperty styleProperty = new StyleProperty(name, rawValue);
		styleProperty.value = value;
		return styleProperty;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		name = null;
		value = null;
		rawValue = null;
	}
}
