package zincfish.zinccss.model;

/**
 * <code>Coordinates</code>定义了与坐标系相关的数据结构
 * 
 * @author Jarod Yv
 */
public class Coordinates {

	/** 横坐标相关数据 */
	public int X;

	/** 纵坐标相关数据 */
	public int Y;

	/**
	 * 构造函数
	 */
	public Coordinates() {
	}

	/**
	 * 构造函数
	 * 
	 * @param X
	 *            横坐标相关数据
	 * @param Y
	 *            纵坐标相关数据
	 */
	public Coordinates(int X, int Y) {
		this.X = X;
		this.Y = Y;
	}

}