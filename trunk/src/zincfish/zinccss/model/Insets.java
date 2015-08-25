package zincfish.zinccss.model;

/**
 * <code>Insets</code>定义了边距
 * 
 * @author Jarod Yv
 */
public class Insets {

	/** 上边距 */
	public int top;

	/** 下边距 */
	public int bottom;

	/** 右边距 */
	public int right;

	/** 左边距 */
	public int left;

	/**
	 * 构造函数
	 */
	public Insets() {
		this(0, 0, 0, 0);
	}

	/**
	 * 构造函数
	 * 
	 * @param top
	 *            上边距
	 * @param right
	 *            右边距
	 * @param bottom
	 *            下边距
	 * @param left
	 *            左边距
	 */
	public Insets(int top, int right, int bottom, int left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

}
