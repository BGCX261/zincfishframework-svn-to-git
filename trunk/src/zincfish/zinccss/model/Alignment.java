package zincfish.zinccss.model;

/**
 * <code>Alignment</code>定义了对齐方式
 * 
 * @author Jarod Yv
 */
public class Alignment {
	// ////////////////////////// 基本对齐样式值 //////////////////////////
	private static final byte TOP_VALUE = 1 << 0;// 上
	private static final byte LEFT_VALUE = 1 << 1;// 左
	private static final byte RIGHT_VALUE = 1 << 2;// 右
	private static final byte BOTTOM_VALUE = 1 << 3;// 下
	private static final byte CENTER_VALUE = 1 << 4;// 居中
	private static final byte FILL_VALUE = 1 << 5;// 填充
	// //////////////////////////////////////////////////////////////////

	// //////////////////////////// 对齐样式 /////////////////////////////
	/** 锚定左上角 */
	public static final Alignment TOP_LEFT = new Alignment(
			(byte) (TOP_VALUE | LEFT_VALUE));
	/** 顶部对齐 */
	public static final Alignment TOP = new Alignment(TOP_VALUE);
	/** 锚定右上角 */
	public static final Alignment TOP_RIGHT = new Alignment(
			(byte) (TOP_VALUE | RIGHT_VALUE));
	/** 左对齐 */
	public static final Alignment LEFT = new Alignment(LEFT_VALUE);
	/** 居中显示 */
	public static final Alignment CENTER = new Alignment(CENTER_VALUE);
	/** 右对齐 */
	public static final Alignment RIGHT = new Alignment(RIGHT_VALUE);
	/** 锚定左下角 */
	public static final Alignment BOTTOM_LEFT = new Alignment(
			(byte) (BOTTOM_VALUE | LEFT_VALUE));
	/** 底对齐 */
	public static final Alignment BOTTOM = new Alignment(BOTTOM_VALUE);
	/** 锚定右下角 */
	public static final Alignment BOTTOM_RIGHT = new Alignment(
			(byte) (BOTTOM_VALUE | RIGHT_VALUE));

	/** 填充 */
	public static final Alignment FILL = new Alignment(FILL_VALUE);
	/** 顶部填充 */
	public static final Alignment FILL_TOP = new Alignment(
			(byte) (FILL_VALUE | TOP_VALUE));
	/** 左边填充 */
	public static final Alignment FILL_LEFT = new Alignment(
			(byte) (FILL_VALUE | LEFT_VALUE));
	/** 居中填充 */
	public static final Alignment FILL_CENTER = new Alignment(
			(byte) (FILL_VALUE | CENTER_VALUE));
	/** 右边填充 */
	public static final Alignment FILL_RIGHT = new Alignment(
			(byte) (FILL_VALUE | RIGHT_VALUE));
	/** 底部填充 */
	public static final Alignment FILL_BOTTOM = new Alignment(
			(byte) (FILL_VALUE | BOTTOM_VALUE));
	// //////////////////////////////////////////////////////////////////

	// 对齐值
	private byte value;

	/*
	 * 构造函数
	 * 
	 * @param value 对齐值
	 */
	private Alignment(byte value) {
		this.value = value;
	}

	/**
	 * 判断是否是顶对齐
	 * 
	 * @return 如果是顶对齐则返回<code>true</code>
	 */
	public boolean isTop() {
		return (value & TOP_VALUE) == TOP_VALUE;
	}

	/**
	 * 判断是否是底对齐
	 * 
	 * @return 如果是底对齐则返回<code>true</code>
	 */
	public boolean isBottom() {
		return (value & BOTTOM_VALUE) == BOTTOM_VALUE;
	}

	/**
	 * 判断是否是左对齐
	 * 
	 * @return 如果是左对齐则返回<code>true</code>
	 */
	public boolean isLeft() {
		return (value & LEFT_VALUE) == LEFT_VALUE;
	}

	/**
	 * 判断是否是右对齐
	 * 
	 * @return 如果是右对齐则返回<code>true</code>
	 */
	public boolean isRight() {
		return (value & RIGHT_VALUE) == RIGHT_VALUE;
	}

	/**
	 * 判断是否是水平居中
	 * 
	 * @return 如果是水平居中则返回<code>true</code>
	 */
	public boolean isHorizontalCenter() {
		return (value & CENTER_VALUE) == CENTER_VALUE
				|| (isTop() || isBottom()) && !(isLeft() || isRight());
	}

	/**
	 * 判断是否是垂直居中
	 * 
	 * @return 如果是垂直居中则返回<code>true</code>
	 */
	public boolean isVerticalCenter() {
		return (value & CENTER_VALUE) == CENTER_VALUE
				|| (isLeft() || isRight()) && !(isTop() || isBottom());
	}

	/**
	 * 判断是否是填充
	 * 
	 * @return 如果是填充则返回<code>true</code>
	 */
	public boolean isFill() {
		return (value & FILL_VALUE) == FILL_VALUE;
	}

	/**
	 * 根据内容的宽度和实际宽度计算对齐的横坐标位置
	 * 
	 * @param width
	 *            实际空间宽度
	 * @param contentWidth
	 *            内容宽度
	 * @return 对齐后的横坐标的位置
	 */
	public int alignX(int width, int contentWidth) {
		if (isHorizontalCenter()) {
			return (width - contentWidth) / 2;
		} else if (isRight()) {
			return width - contentWidth;
		}
		return 0;
	}

	/**
	 * 根据内容的高度和实际高度计算对齐的纵坐标位置
	 * 
	 * @param height
	 *            实际空间高度
	 * @param contentHeight
	 *            内容高度
	 * @return 对齐后的纵坐标的位置
	 */
	public int alignY(int height, int contentHeight) {
		if (isVerticalCenter()) {
			return (height - contentHeight) / 2;
		} else if (isBottom()) {
			return height - contentHeight;
		}
		return 0;
	}

	/**
	 * 组合水平和垂直的对齐方式成为一个新的对齐方式
	 * 
	 * @param verticalAlignment
	 *            垂直方向上的对齐方式
	 * @param horizontalAlignment
	 *            水平方向上的对齐方式
	 * @return 组合后的对齐方式
	 */
	public static Alignment combine(Alignment verticalAlignment,
			Alignment horizontalAlignment) {
		if (verticalAlignment != null && horizontalAlignment != null) {
			byte value = (byte) (((verticalAlignment.isTop() ? TOP_VALUE : 0)
					| (verticalAlignment.isVerticalCenter() ? CENTER_VALUE : 0) | (verticalAlignment
					.isBottom() ? BOTTOM_VALUE : 0)) | ((horizontalAlignment
					.isLeft() ? LEFT_VALUE : 0)
					| (horizontalAlignment.isHorizontalCenter() ? CENTER_VALUE
							: 0) | (horizontalAlignment.isRight() ? RIGHT_VALUE
					: 0)));
			return new Alignment(value);
		}
		return null;
	}

}
