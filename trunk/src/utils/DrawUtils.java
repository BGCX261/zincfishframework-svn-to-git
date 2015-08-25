/**
 * License  3G门户版权所有 2008-2009
 */
package utils;

import javax.microedition.lcdui.Graphics;

import zincfish.zinccss.ICSSConstants;
import zincfish.zinccss.model.Alignment;
import zincfish.zinccss.model.Coordinates;
import zincfish.zinccss.model.Insets;
import zincfish.zinccss.style.StyleSet;

import com.mediawoz.akebono.corerenderer.CRFont;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;

/**
 * @作者 江威
 * @Email weijiang8410@163.com/jiang-wei@3g.net.cn
 */
public class DrawUtils {

	public static final int TOP_LEFT = CRGraphics.TOP | CRGraphics.LEFT; // 绘图的对齐方式
	public static final int BOTTOM_LEFT = CRGraphics.BOTTOM | CRGraphics.LEFT; // 绘图的对齐方式
	public static String dots = ".."; // 省略字符串
	public static final int iTextColor = 0x292929; // 文本颜色
	public static final int iFocusTextColor = 0xDEDEDE; // 聚焦后

	/** 绘制String 超出行宽 折行 */
	public static int drawLString(CRGraphics crg, CRFont ufont, String text,
			int margin, int iStartY, int width, int rowHeight) {
		if (text == null || text.trim().equals("")) {
			return iStartY;
		}
		char ch;
		int startX = margin;
		int length = text.length(); // 不用每次循环都计算text的长度
		for (int i = 0; i < length; i++) {
			ch = text.charAt(i);
			if (startX + ufont.charWidth(ch) > width) {
				startX = margin;
				iStartY += rowHeight;
			}
			ufont.drawChar(crg, ch, startX, iStartY
					+ (rowHeight - ufont.getHeight()) / 2, TOP_LEFT);
			startX += ufont.charWidth(ch);
		}
		iStartY += rowHeight; // 有文字加上行高
		return iStartY;
	}

	/** 右对齐 */
	public static int drawRString(CRGraphics crg, CRFont ufont, String text,
			int margin, int iStartY, int width, int rowHeight) {
		if (text == null || text.trim().equals("")) {
			return iStartY;
		}
		if (margin + ufont.stringWidth(text) > width) {
			return drawLString(crg, ufont, text, margin, iStartY, width,
					rowHeight);
		} else {
			ufont.drawString(crg, text, width - ufont.stringWidth(text),
					iStartY + (rowHeight - ufont.getSize()) / 2, TOP_LEFT);
		}
		return iStartY;
	}

	/**
	 * 绘制String 超出行宽 用... 代替
	 * 
	 * @param iStartX
	 *            X轴 开始点
	 * @param iStartY
	 *            Y轴 开始点
	 * @param width
	 *            X轴 可绘制的宽度
	 * @param rowHeight
	 *            行的高度
	 */
	public static int drawLineString(CRGraphics crg, CRFont ufont, String text,
			int iStartX, int iStartY, int width, int rowHeight) {
		if (text == null || text.trim().equals("")) {
			return iStartY;
		}
		if (ufont.stringWidth(text) + iStartX > width) { // 超出一行
			char ch;
			for (int i = 0; i < text.length(); i++) {
				if (iStartX + ufont.stringWidth(dots) * 2 > width) {
					ufont.drawString(crg, dots, iStartX
							+ (rowHeight - ufont.getSize()) / 2, iStartY,
							TOP_LEFT);
					break;
				}
				ch = text.charAt(i);
				ufont.drawChar(crg, ch, iStartX, iStartY
						+ (rowHeight - ufont.getSize()) / 2, TOP_LEFT);
				iStartX += ufont.charWidth(ch);
			}
		} else {
			ufont.drawString(crg, text, iStartX, iStartY
					+ (rowHeight - ufont.getSize()) / 2, TOP_LEFT);
		}
		iStartY += rowHeight;
		return iStartY;
	}

	// 一下是非绘制文本函数
	/**
	 * @param width
	 *            可绘制的宽度
	 */
	public static int getHeightOfDrawtext(CRFont ufont, String text, int width,
			int fontHeight) {
		if (text == null || text.trim().equals("")) {
			return 0;
		}
		int textLength = text.length(); // 获取文本的长度
		int startX = 0;
		int startY = 0;
		char ch;
		for (int i = 0; i < textLength; i++) {
			ch = text.charAt(i);
			if (startX + ufont.charWidth(ch) > width) {
				startY += fontHeight;
				startX = 0;
			}
			startX += ufont.charWidth(ch);
		}
		startY += fontHeight;
		return startY;
	}

	public static void paintBackground(CRGraphics g, int x, int y, int w,
			int h, StyleSet style) {
		// 背景要画在border内，首先要约束背景绘制的区域
		Insets margin = style.getMargin();
		Insets border = style.getBorder();
		int width = w - margin.left - border.left - border.right - margin.right;
		int height = h - margin.top - border.top - border.bottom
				- margin.bottom;
		margin = null;
		border = null;

		// 填充背景颜色
		Integer backgroundColor = style.getBackgroundColor();
		if (backgroundColor != null) {
			int oldColor = g.getColor();
			g.setColor(backgroundColor.intValue());
			g.fillRect(x, y, width, height);
			g.setColor(oldColor);
		}
		backgroundColor = null;

		// 绘制背景图片
		CRImage[] images = style.getBackgroundImage();
		if (images != null) {
			Alignment[] alignments = style.getBackgroundAlign();// 背景图片的对齐方式
			Coordinates[] repeats = style.getBackgroundRepeat();// 背景图片的平铺方式

			int backgroundCount = Math.max(images.length, Math.max(
					alignments.length, repeats.length));// 计算背景图片数，去各参与元素中最大的
			Coordinates repeat = null;
			int index = 0;
			for (int i = 0; i < backgroundCount; ++i) {
				index = i % images.length;
				repeat = repeats[index];
				paintMosaicImage(g, images[index], x, y, width, height,
						alignments[index], repeat.X > 0 ? repeat.X
								: Integer.MAX_VALUE, repeat.Y > 0 ? repeat.Y
								: Integer.MAX_VALUE);
				repeat = null;
			}
		}
		images = null;
	}

	/**
	 * 绘制组件的背景
	 * 
	 * @param g
	 *            图形上下文
	 */
	public static void paintBackground(CRGraphics g, int w, int h,
			StyleSet style) {
		Insets margin = style.getMargin();
		Insets border = style.getBorder();
		int x = margin.left + border.left;
		int y = margin.top + border.top;
		paintBackground(g, x, y, w, h, style);
		margin = null;
		border = null;
	}

	public static void paintBorder(CRGraphics g, int x, int y, int w, int h,
			StyleSet style) {
		// 绘制边框要留出Margin的距离
		Insets margin = style.getMargin();
		int width = w - margin.left - margin.right;
		int height = h - margin.top - margin.bottom;
		margin = null;

		// 边框的宽度或高度为0则无需绘制
		if (width == 0 || height == 0) {
			return;
		}

		// 绘制普通边框
		Integer[] borderColor = style.getBorderColor();
		if (borderColor != null) {
			int oldColor = g.getColor();
			Insets border = style.getBorder();
			// 当边框的宽度为1是，画笔样式才有效
			if (border.top + border.right + border.bottom + border.left <= 4) {
				g.setStrokeStyle(style.getBorderStroke().intValue());
			}
			// 下面会从同样的逻辑绘制上下左右边框。基本逻辑是宽度为1画直线，宽度大于1填充矩形
			// Top
			if (borderColor[ICSSConstants.INDEX_TOP] != null) {
				g.setColor(borderColor[0].intValue());
				if (border.top == 1) {
					g.drawLine(x, y, x + width - border.right - 1, y);
				} else if (border.top != 0) {
					g.fillRect(x, y, width - border.right, border.top);
				}
			}
			// Right
			if (borderColor[ICSSConstants.INDEX_RIGHT] != null) {
				g.setColor(borderColor[1].intValue());
				if (border.right == 1) {
					g.drawLine(x + width - 1, y, x + width - 1, y + height
							- border.bottom - 1);
				} else if (border.right != 0) {
					g.fillRect(x + width - border.right, y, border.right,
							height - border.bottom);
				}
			}
			// Bottom
			if (borderColor[ICSSConstants.INDEX_BOTTOM] != null) {
				g.setColor(borderColor[2].intValue());
				if (border.bottom == 1) {
					g.drawLine(x + border.left, y + height - 1, x + width - 1,
							y + height - 1);
				} else if (border.bottom != 0) {
					g.fillRect(x + border.left, y + height - border.bottom,
							width - border.left, border.bottom);
				}
			}
			// Left
			if (borderColor[ICSSConstants.INDEX_LEFT] != null) {
				g.setColor(borderColor[3].intValue());
				if (border.left == 1) {
					g.drawLine(x, y + border.top, x, y + height - 1);
				} else if (border.left != 0) {
					g.fillRect(x, y + border.top, border.left, height
							- border.top);
				}
			}
			g.setColor(oldColor);
			border = null;
			borderColor = null;
		}

		// 绘制图片边框
		CRImage[] borderImages = style.getBorderImage();
		if (borderImages != null) {
			Insets border = style.getBorder();
			Alignment[] alignments = style.getBorderAlign();
			// 下面会从同样的逻辑绘制上、下、左、右、左上、右上、左下、右下的图片边框。基本逻辑是平铺图片
			// Top
			if (borderImages[ICSSConstants.INDEX_TOP] != null) {
				int ltw = 0, rtw = 0;
				// Top left
				CRImage img = borderImages[ICSSConstants.INDEX_TOP_LEFT];
				if (img != null) {
					ltw = img.getWidth();
					paintMosaicImage(
							g,
							img,
							x,
							y,
							ltw,
							border.top,
							alignments == null
									|| alignments[ICSSConstants.INDEX_TOP_LEFT] == null ? Alignment.TOP_LEFT
									: alignments[ICSSConstants.INDEX_TOP_LEFT],
							1, 1);
					img = null;
				}

				// Top right
				img = borderImages[ICSSConstants.INDEX_TOP_RIGHT];
				if (img != null) {
					rtw = img.getWidth();
					paintMosaicImage(
							g,
							img,
							x + width - rtw,
							y,
							rtw,
							border.top,
							alignments == null
									|| alignments[ICSSConstants.INDEX_TOP_RIGHT] == null ? Alignment.TOP_LEFT
									: alignments[ICSSConstants.INDEX_TOP_RIGHT],
							1, 1);
					img = null;
				}

				paintMosaicImage(
						g,
						borderImages[ICSSConstants.INDEX_TOP],
						x + ltw,
						y,
						width - ltw - rtw,
						border.top,
						alignments == null
								|| alignments[ICSSConstants.INDEX_TOP] == null ? Alignment.TOP_LEFT
								: alignments[ICSSConstants.INDEX_TOP],
						Integer.MAX_VALUE, 1);
			}

			// Right
			if (borderImages[ICSSConstants.INDEX_RIGHT] != null) {
				paintMosaicImage(
						g,
						borderImages[ICSSConstants.INDEX_RIGHT],
						x + width - border.right,
						y + border.top,
						border.right,
						height - border.top - border.bottom,
						alignments == null
								|| alignments[ICSSConstants.INDEX_RIGHT] == null ? Alignment.TOP_LEFT
								: alignments[ICSSConstants.INDEX_RIGHT], 1,
						Integer.MAX_VALUE);
			}

			// Bottom
			if (borderImages[ICSSConstants.INDEX_BOTTOM] != null) {

				int lbw = 0, rbw = 0;
				// Bottom right
				CRImage img = borderImages[ICSSConstants.INDEX_BOTTOM_RIGHT];
				if (img != null) {
					rbw = img.getWidth();
					paintMosaicImage(
							g,
							img,
							x + width - rbw,
							y + height - border.bottom,
							rbw,
							border.bottom,
							alignments == null
									|| alignments[ICSSConstants.INDEX_BOTTOM_RIGHT] == null ? Alignment.TOP_LEFT
									: alignments[ICSSConstants.INDEX_BOTTOM_RIGHT],
							1, 1);
					img = null;
				}

				// Bottom left
				img = borderImages[ICSSConstants.INDEX_BOTTOM_LEFT];
				if (img != null) {
					lbw = img.getWidth();
					paintMosaicImage(
							g,
							img,
							x,
							y + height - border.bottom,
							lbw,
							border.bottom,
							alignments == null
									|| alignments[ICSSConstants.INDEX_BOTTOM_LEFT] == null ? Alignment.TOP_LEFT
									: alignments[ICSSConstants.INDEX_BOTTOM_LEFT],
							1, 1);
					img = null;
				}

				paintMosaicImage(
						g,
						borderImages[ICSSConstants.INDEX_BOTTOM],
						x + lbw,
						y + height - border.bottom,
						width - lbw - rbw,
						border.bottom,
						alignments == null
								|| alignments[ICSSConstants.INDEX_BOTTOM] == null ? Alignment.TOP_LEFT
								: alignments[ICSSConstants.INDEX_BOTTOM],
						Integer.MAX_VALUE, 1);
			}

			// Left
			if (borderImages[ICSSConstants.INDEX_LEFT] != null) {
				paintMosaicImage(
						g,
						borderImages[ICSSConstants.INDEX_LEFT],
						x,
						y + border.top,
						border.left,
						height - border.top - border.bottom,
						alignments == null
								|| alignments[ICSSConstants.INDEX_LEFT] == null ? Alignment.TOP_LEFT
								: alignments[ICSSConstants.INDEX_LEFT], 1,
						Integer.MAX_VALUE);
			}

			border = null;
			alignments = null;
			borderImages = null;
		}
	}

	/**
	 * 绘制边界
	 * 
	 * @param g
	 *            图形上下文
	 */
	public static void paintBorder(CRGraphics g, int w, int h, StyleSet style) {
		Insets margin = style.getMargin();
		int x = margin.left;
		int y = margin.top;
		paintBorder(g, x, y, w, h, style);
		margin = null;
	}

	/**
	 * 平铺图片
	 * 
	 * @param g
	 *            图形上下文
	 * 
	 * @param image
	 *            图片
	 * 
	 * @param x
	 *            绘制区域的起始横坐标
	 * 
	 * @param y
	 *            绘制区域的起始纵坐标
	 * 
	 * @param width
	 *            绘制区域的宽度
	 * 
	 * @param height
	 *            绘制区域的高度
	 * 
	 * @param alignment
	 *            对齐方式
	 * 
	 * @param repeatX
	 *            水平平铺样式
	 * 
	 * @param repeatY
	 *            垂直平铺样式
	 */
	public static void paintMosaicImage(CRGraphics g, CRImage image, int x,
			int y, int width, int height, Alignment alignment, int repeatX,
			int repeatY) {
		int currentClipX = g.getClipX();
		int currentClipY = g.getClipY();
		int currentClipW = g.getClipWidth();
		int currentClipH = g.getClipHeight();

		g = intersectG(g, x, y, width, height);

		// 绘制图片
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int imax = Math.min(repeatX, width / imageWidth + 1);// 水平平铺次数
		int jmax = Math.min(repeatY, height / imageHeight + 1);// 垂直平铺次数
		byte dx = 1;// 控制水平平铺方向，1表示自左向右，-1表示自右向左
		byte dy = 1;// 控制垂直平铺方向，1表示自上向下，-1表示自下向上
		// 处理垂直对齐位置
		if (alignment.isVerticalCenter()) {
			y += (height - imageHeight * jmax) / 2;
		} else if (alignment.isBottom()) {
			y += height - imageHeight;
			dy = -1;
		}
		// 处理水平对齐位置
		if (alignment.isHorizontalCenter()) {
			x += (width - imageWidth * imax) / 2;
		} else if (alignment.isRight()) {
			x += width - imageWidth;
			dx = -1;
		}
		// 平铺图片
		for (int i = 0; i < imax; ++i) {
			for (int j = 0; j < jmax; ++j) {
				image.draw(g, x + i * imageWidth * dx,
						y + j * imageHeight * dy, Graphics.SOLID);
			}
		}

		// 还原clip
		g.setClip(currentClipX, currentClipY, currentClipW, currentClipH);
	}

	public static CRGraphics intersectG(CRGraphics g, int x, int y, int w, int h) {
		// 保持当前clip
		int currentClipX1 = g.getClipX();
		int currentClipX2 = currentClipX1 + g.getClipWidth();
		int currentClipY1 = g.getClipY();
		int currentClipY2 = currentClipY1 + g.getClipHeight();

		// 取绘制区域和g的clip的交集
		int clipX1 = x;
		int clipX2 = x + w;
		int clipY1 = y;
		int clipY2 = y + h;
		if (clipX1 < currentClipX1)
			clipX1 = currentClipX1;
		if (clipY1 < currentClipY1)
			clipY1 = currentClipY1;
		if (clipX2 > currentClipX2)
			clipX2 = currentClipX2;
		if (clipY2 > currentClipY2)
			clipY2 = currentClipY2;

		// 设置clip
		g.setClip(clipX1, clipY1, clipX2 - clipX1, clipY2 - clipY1);
		return g;
	}
}
