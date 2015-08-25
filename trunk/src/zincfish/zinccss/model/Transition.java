package zincfish.zinccss.model;

/**
 * <code>Transition</code>封装了切换样式的数据
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class Transition {
	// /////////////////// Transition类型 ///////////////////
	public static final byte TRANSITION_FLY = 0x01;
	public static final byte TRANSITION_FADE = 0x02;
	public static final byte TRANSITION_PAGE_CURL = 0x03;
	public static final byte TRANSITION_MASAIC = 0x04;
	public static final byte TRANSITION_RANDOM_FALL = 0x05;
	public static final byte TRANSITION_BREAK_OUT = 0x06;
	public static final byte TRANSITION_DISSOLVE = 0x07;
	// /////////////////////////////////////////////////////

	/** 类型 */
	public byte type = 0;
	/** 动画频率 */
	public int animationTickCount = 1;
	/** 进入图片 */
	public String imgFrom = null;
	/** 结束图片 */
	public String imgTo = null;
	/** 动画步数 */
	public int animationStep = 0;
	/** 动画方向 */
	public int animationDirection = 0;

}
