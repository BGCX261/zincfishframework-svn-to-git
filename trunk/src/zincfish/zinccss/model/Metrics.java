package zincfish.zinccss.model;

import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>Metrics</code>定义了一块区域的尺寸和位置
 * 
 * @author Jarod Yv
 */
public class Metrics {

	/** 横坐标位置 */
	public int x;

	/** 纵坐标位置 */
	public int y;

	/** 宽度 */
	public int width;

	/** 高度 */
	public int height;

	/** 最后一行的高度 */
	public int lastLineHeight;

	/** 对于多行组件,firstLineWidth表示第一行的宽度 */
	public int firstLineWidth;

	/** 对于多行组件,nextLineWidth表示下一行的宽度 */
	public int nextLineWidth;

	/** 与该空间区域向关联的组件 */
	public AbstractSNSComponent component = null;

	/** 链表中的下一个空间区域 */
	public Metrics next = null;
	
	/** 链表中的上一个空间区域 */
	public Metrics prev = null;

	/**
	 * 构造函数
	 */
	public Metrics() {
	}

	/**
	 * 构造函数
	 * 
	 * @param component
	 *            向关联的组件
	 */
	public Metrics(AbstractSNSComponent component) {
		this.component = component;
	}

	/**
	 * 构造函数
	 * 
	 * @param component
	 *            向关联的组件
	 * @param x
	 *            横坐标位置
	 * @param y
	 *            纵坐标位置
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public Metrics(AbstractSNSComponent component, int x, int y, int width,
			int height) {
		this(component);
		setBounds(x, y, width, height);
	}

	/**
	 * 判断该空间区域是否一个空的空间区域
	 * 
	 * @return 如果区域的宽或者高为0，则认为是空的区域，返回<code>true</code>
	 */
	public boolean isEmpty() {
		return width == 0 || height == 0;
	}

	/**
	 * 设置空间区域的位置和尺寸
	 * 
	 * @param x
	 *            横坐标位置
	 * @param y
	 *            纵坐标位置
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * 取当前区域和制定区域的合集
	 * 
	 * @param x
	 *            横坐标位置
	 * @param y
	 *            纵坐标位置
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public void add(int x, int y, int width, int height) {
		if (isEmpty()) {
			setBounds(x, y, width, height);
		} else {
			int right = Math.max(this.x + this.width, x + width);
			int bottom = Math.max(this.y + this.height, y + height);
			this.x = Math.min(this.x, x);
			this.y = Math.min(this.y, y);
			this.width = right - this.x;
			this.height = bottom - this.y;
		}
	}
}
