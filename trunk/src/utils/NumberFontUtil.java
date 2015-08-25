package utils;

import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;

public final class NumberFontUtil {
	/* 数字图片 */
	private static final CRImage[] NUMBER_IMAGE = new CRImage[10];
	/** 数字字体的高度 */
	public static final int NUMBER_FONT_HEIGHT;
	/** 数字字体的高度 */
	public static final int NUMBER_FONT_WIDTH;

	static {
		for (int i = 0; i < NUMBER_IMAGE.length; i++) {
			NUMBER_IMAGE[i] = CRImage
					.loadFromResource("/img/num/" + i + ".png");
		}
		NUMBER_FONT_HEIGHT = NUMBER_IMAGE[0].getHeight();
		NUMBER_FONT_WIDTH = NUMBER_IMAGE[0].getWidth();
	}

	/**
	 * 绘制数字
	 * 
	 * @param g
	 *            图形上下文
	 * @param num
	 *            要绘制的数字
	 * @param x
	 *            绘制的起始横坐标
	 * @param y
	 *            获知的其实纵坐标
	 */
	public static final void drawNumber(CRGraphics g, int num, int x, int y) {
		if (num < 0) {// 处理负数
			// FIXME 此处应当先画负号
			num = -num;
		}
		int tmp = 0;
		x += getNumberWidth(num);
		do {
			x -= NUMBER_FONT_WIDTH;
			tmp = num % 10;
			NUMBER_IMAGE[tmp].draw(g, x, y, 20);
			num /= 10;
		} while (num > 0);
	}

	/**
	 * 获取数字的绘制长度
	 * 
	 * @param num
	 *            数字
	 * @return 其绘制长度
	 */
	public static final int getNumberWidth(int num) {
		int width = 0;
		do {
			num /= 10;
			width += NUMBER_FONT_WIDTH;
		} while (num > 0);
		return width;
	}
}
