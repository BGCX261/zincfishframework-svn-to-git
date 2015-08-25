package ui.pointScroll;

import com.mediawoz.akebono.corefilter.CFMotion;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.ui.UComponent;

public class CSimplePoint extends UComponent {
	/* 暗点的Alpha值 */
	private static final int GRAY_ALPHA = 50;
	/* 点图片 */
	private CRImage pointImg;

	/* 发光图片 */
	private CRImage lightImg;

	/* 点变暗的Motion */
	private CFMotion pointFadeMotion;

	/* 点变亮的Motion */
	private CFMotion pointMotion;

	/* 点是否为亮的 */
	private boolean isActive;

	/**
	 * 绘制图片的中点纵坐标
	 */
	private int imgY;

	/**
	 * 绘制图片的中点的横坐标
	 */
	private int imgX;

	/**
	 * @param animTickCount
	 *            the animation tick interval.
	 * @param ix
	 *            the x position of this Panel.
	 * @param iy
	 *            the y position of this Panel.
	 * @param pointImg
	 *            点的图像
	 * @param lightImg
	 *            发光的图像
	 */
	public CSimplePoint(int animTickCount, int ix, int iy, CRImage pointImg,
			CRImage lightImg) {
		super(animTickCount, ix, iy, 0, 0);
		this.pointImg = pointImg;
		this.lightImg = lightImg;

		setSize(lightImg.getWidth(), lightImg.getHeight());
		imgX = 0;
		imgY = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mediawoz.akebono.ui.UComponent#drawCurrentFrame(com.mediawoz.akebono
	 * .corerenderer.CRGraphics)
	 */
	protected void drawCurrentFrame(CRGraphics g) {
		if (isActive) {
			pointImg.draw(g, imgX, imgY, 3);
			lightImg.draw(g, imgX, imgY, 3);
		} else {
			pointImg.draw(g, imgX, imgY, 3);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.coreanimation.CAAnimator#animate()
	 */
	protected boolean animate() {
		boolean ret = false;
		if (isActive) {
			if (pointMotion != null) {
				if (pointMotion.isFinished()) {
					pointMotion = null;
					pointImg.setAlpha(255);
				} else {
					int alpha = GRAY_ALPHA + (255 - GRAY_ALPHA)
							* pointMotion.getProgress() / 100;
					pointImg.setAlpha(alpha);
				}
				ret = true;
			}
		} else {
			if (pointFadeMotion != null) {
				if (pointFadeMotion.isFinished()) {
					pointFadeMotion = null;
					pointImg.setAlpha(GRAY_ALPHA);
				} else {
					int alpha = 255 - (255 - GRAY_ALPHA)
							* pointFadeMotion.getProgress() / 100;
					pointImg.setAlpha(alpha);
				}
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * 使该点变亮
	 * 
	 * @param motion
	 *            点变亮的Motion
	 */
	public void active(CFMotion motion) {
		pointMotion = motion;
		isActive = true;
	}

	/**
	 * 使该点变暗
	 * 
	 * @param motion
	 *            点变暗的Motion
	 */
	public void reset(CFMotion motion) {
		pointFadeMotion = motion;
		isActive = false;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		pointImg = null;
		lightImg = null;
		pointFadeMotion = null;
		pointMotion = null;
	}

}