package zincfish.zinclayout.layoutdata;

/**
 * <code>BorderLayoutData</code>定义了BorderLayout使用的数据结构
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class BorderLayoutData implements ILayoutData {

	// /////////////////////////////// 方位代码 ///////////////////////////////
	public static final byte CENTER = 0x00;
	public static final byte NORTH = 0x01;
	public static final byte EAST = 0x02;
	public static final byte WEST = 0x03;
	public static final byte SOUTH = 0x04;
	// //////////////////////////////////////////////////////////////////////

	// /////////////////////////////// 排版数据 ///////////////////////////////
	public static final BorderLayoutData BORDER_LAYOUT_CENTER = new BorderLayoutData(
			CENTER);
	public static final BorderLayoutData BORDER_LAYOUT_NORTH = new BorderLayoutData(
			NORTH);
	public static final BorderLayoutData BORDER_LAYOUT_EAST = new BorderLayoutData(
			EAST);
	public static final BorderLayoutData BORDER_LAYOUT_WEST = new BorderLayoutData(
			WEST);
	public static final BorderLayoutData BORDER_LAYOUT_SOUTH = new BorderLayoutData(
			SOUTH);
	// //////////////////////////////////////////////////////////////////////

	public byte position;

	/*
	 * 私有构造函数
	 */
	private BorderLayoutData(byte position) {
		this.position = position;
	}

}
