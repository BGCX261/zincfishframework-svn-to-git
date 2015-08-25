package zincfish.zinccss.style;

import utils.ArrayList;

/**
 * <code>StyleSelector</code>定义了CSS选择器
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class StyleSelector {
	/** 选择器的名字 */
	public String name;

	/** 父样式选择器 */
	public StyleSelector parent;

	private boolean hasTag = false;// 标识是否含有tag类型的选择器
	private boolean hasId = false;// 标识是否含有id类型的选择器
	private boolean hasStyleClass = false;// 标识是否含有class类型的选择器
	private boolean hasPseudoClass = false;// 标识是否含有伪类

	private String tag;// 标签类型选择器标签名
	private String id;// id类型选择器id名
	private String styleClass;// class类型选择器class名
	private String[] pseudoClasses;// 伪类名

	/**
	 * 构造函数
	 * 
	 * @param name
	 *            选择器的名字
	 */
	public StyleSelector(String name) {
		this.name = name;

		StringBuffer tagReader = new StringBuffer();
		StringBuffer idReader = new StringBuffer();
		StringBuffer classReader = new StringBuffer();
		ArrayList pseudoClassReaders = new ArrayList(2);

		StringBuffer currentReader = tagReader;
		for (int i = 0; i < name.length(); ++i) {
			char c = name.charAt(i);
			switch (c) {
			case '#':
				currentReader = idReader;
				break;
			case '.':
				currentReader = classReader;
				break;
			case ':':
				currentReader = new StringBuffer();
				pseudoClassReaders.add(currentReader);
				break;
			default:
				currentReader.append(c);
				break;
			}
		}
		currentReader = null;

		// 判断是否含有Tag类型的选择器
		hasTag = tagReader.length() != 0;
		if (hasTag) {
			tag = tagReader.toString();
		}
		tagReader = null;
		// 判断是否含有id类型的选择器
		hasId = idReader.length() != 0;
		if (hasId) {
			id = idReader.toString();
		}
		idReader = null;
		// 判断是否含有class类型的选择器
		hasStyleClass = classReader.length() != 0;
		if (hasStyleClass) {
			styleClass = classReader.toString();
		}
		classReader = null;
		// 判断是否含有伪类
		int size = pseudoClassReaders.size();
		hasPseudoClass = size != 0;
		if (hasPseudoClass) {
			pseudoClasses = new String[size];
			for (int i = 0; i < size; ++i) {
				pseudoClasses[i] = ((StringBuffer) pseudoClassReaders.get(i))
						.toString();
			}
		}
		pseudoClassReaders.removeAll();
		pseudoClassReaders = null;
	}

	/**
	 * 判断是否含有父样式
	 * 
	 * @return 如果含有父样式，返回<code>true</code>，否则返回<code>false</code>
	 */
	public boolean hasParent() {
		return parent != null;
	}

	/**
	 * 获取伪类
	 * 
	 * @return the pseudoClasses
	 */
	public String[] getPseudoClasses() {
		return pseudoClasses;
	}

	/**
	 * 获取标签名
	 * 
	 * @return 标签名
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * 获取id值
	 * 
	 * @return id值
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取class值
	 * 
	 * @return class值
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		StyleSelector styleSelector = (StyleSelector) obj;
		if (styleSelector != null) {
			boolean parentEquality = styleSelector.parent == null;
			if (parent != null) {
				parentEquality = parent.equals(styleSelector.parent);
			}
			return parentEquality && name.equals(styleSelector.name);
		}
		return false;
	}

	/**
	 * 判断是标签选择器
	 * 
	 * @return 如果是标签选择器，返回<code>true</code>
	 */
	public boolean isHasTag() {
		return hasTag;
	}

	/**
	 * 判断是id选择器
	 * 
	 * @return 如果是id选择器，返回<code>true</code>
	 */
	public boolean isHasId() {
		return hasId;
	}

	/**
	 * 判断是class选择器
	 * 
	 * @return 如果是class选择器，返回<code>true</code>
	 */
	public boolean isHasStyleClass() {
		return hasStyleClass;
	}

	/**
	 * 判断是伪类选择器
	 * 
	 * @return 如果是伪类选择器，返回<code>true</code>
	 */
	public boolean isHasPseudoClass() {
		return hasPseudoClass;
	}

}
