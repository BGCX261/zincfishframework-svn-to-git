package zincfish.zinclayout.layoutdata;

import zincfish.zinccss.model.Alignment;

/**
 * <code>StaticLayoutData</code>定义了StaticLayout使用的数据结构
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class StaticLayoutData implements ILayoutData {

	/** 唯一实例 */
	public static final StaticLayoutData instance = new StaticLayoutData(
			Alignment.TOP_LEFT, 1, 1);

	public int x;
	public int y;

	public int width;
	public int height;

	public Alignment alignment;

	public StaticLayoutData(Alignment alignment) {
		this(alignment, -1, -1);
	}

	public StaticLayoutData(Alignment alignment, int width, int height) {
		this.alignment = alignment;
		this.width = width;
		this.height = height;
	}

}
