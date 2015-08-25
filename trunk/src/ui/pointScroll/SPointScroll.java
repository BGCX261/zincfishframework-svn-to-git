package ui.pointScroll;

import com.mediawoz.akebono.corefilter.CFMotion;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.filters.motion.FMLinear;
import com.mediawoz.akebono.ui.UPanel;

public class SPointScroll extends UPanel {

	/* 点的数组 */
	private CSimplePoint[] points;

	/* 是点发亮的图片 */
	private CRImage lightImg;

	/* 点图片 */
	private CRImage pointImg;

	/* 点的个数 */
	private int pointNum;

	/* 当前选择的点的Index */
	private int selectIndex = 0;

	/* 上次选择的点的Index */
	private int lastSelectIndex;

	/* 最大的点间距 */
	private static final int MAX_POINT_GAP = 12;

	/* 点从灰变白的Motion */
	private CFMotion pointMotion;

	/* 点发光的Motion */
	private CFMotion lightMotion;

	/* 点的发光渐渐消失的Motion */
	private CFMotion lightFadeMotion;

	/* 非选择点的alpha值 */
	private static final int GRAY_ALPHA = 50;

	/**
	 * 水平显示的风格
	 */
	public static final int STYLE_H = 1;

	/**
	 * 垂直显示的风格
	 */
	public static final int STYLE_V = 2;

	/**
	 * 垂直（STYLE_VCENTER）或者是水平（STYLE_HCENTER）显示的样式
	 * 
	 * @see #STYLE_H
	 * @see #STYLE_V
	 */
	private int style = STYLE_H;

	/**
	 * @param ix
	 *            the x position of this Panel.
	 * @param iy
	 *            the y position of this Panel.
	 * @param width
	 *            the width of this Panel.
	 * @param height
	 *            the height of this Panel.
	 * @param pointNum
	 *            点的总数
	 * @param pointImg
	 *            点的图片
	 * @param lightImg
	 *            发光的图片
	 * @param style
	 *            垂直（STYLE_VCENTER）或者是水平（STYLE_HCENTER）显示的样式
	 */
	public SPointScroll(int ix, int iy, int width, int height,
			CRImage lightImg, CRImage pointImg, int pointNum, int style) {
		super(1, ix, iy, width, height);
		this.style = style;
		this.lightImg = lightImg;
		this.pointImg = pointImg;
		this.lightImg.setAlpha(0);
		this.pointNum = pointNum;
		setPoints(pointNum, 0);
	}

	/**
	 * 设置点的总数和亮的点.
	 * 
	 * @param pointNum
	 *            点的总数
	 * @param selectIndex
	 *            当前亮的点的Index
	 */
	public void setPoints(int pointNum, int selectIndex) {
		this.pointNum = pointNum;
		points = new CSimplePoint[pointNum];
		this.selectIndex = selectIndex;
		CRImage tempImg;
		for (int index = 0; index < pointNum; index++) {
			tempImg = pointImg.copy();
			if (index != selectIndex) {
				tempImg.setAlpha(GRAY_ALPHA);
			}
			points[index] = new CSimplePoint(1, 0, 0, tempImg, lightImg);
			tempImg = null;
		}
		addCPoints();
	}

	/*
	 * 布局和添加点组件到CPagePoint.
	 */
	private void addCPoints() {
		removeAllComponents();

		if (style == STYLE_H) {// 横向
			int width = getWidth();
			int pointWidth = width / pointNum;
			if (pointWidth > MAX_POINT_GAP) {
				pointWidth = MAX_POINT_GAP;
			}
			// 确认中心点的位置
			int centerPointIndex, centerPointX;
			centerPointIndex = pointNum / 2;
			if (pointNum % 2 != 0) {
				centerPointX = width / 2;
			} else {
				centerPointX = (width + pointWidth) / 2;
			}
			// 设置点的位置，将点组件加入到panel
			for (int index = 0; index < points.length; index++) {
				points[index].iX = centerPointX - pointWidth
						* (centerPointIndex - index);
				points[index].iY = iHeight / 2;
				addComponent(points[index]);
			}
		} else {// 纵向
			int height = getHeight();
			int pointWidth = height / pointNum;
			if (pointWidth > MAX_POINT_GAP) {
				pointWidth = MAX_POINT_GAP;
			}
			// 确认中心点的位置
			int centerPointIndex, centerPointY;
			centerPointIndex = pointNum / 2;
			if (pointNum % 2 != 0) {
				centerPointY = height / 2;
			} else {
				centerPointY = (height + pointWidth) / 2;
			}
			// 设置点的位置，将点组件加入到panel
			for (int index = 0; index < points.length; index++) {
				points[index].iY = centerPointY - pointWidth
						* (centerPointIndex - index);
				points[index].iX = iWidth / 2;
				addComponent(points[index]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.ui.UPanel#animate()
	 */
	protected boolean animate() {
		boolean ret = false;
		if (pointMotion != null) {
			if (pointMotion.isFinished()) {
				pointMotion = null;
				lightMotion = new FMLinear(1, FMLinear.CSPEED, 0, 0, 100, 0,
						20, 0, 0);
				attachAnimator(lightMotion);
			}
		}

		if (lightMotion != null) {
			if (lightMotion.isFinished()) {
				lightMotion = null;
				lightFadeMotion = new FMLinear(1, FMLinear.CSPEED, 0, 0, 100,
						0, 20, 0, 0);
				attachAnimator(lightFadeMotion);
			} else {
				int alpha = 255 * lightMotion.getProgress() / 100;
				lightImg.setAlpha(alpha);
			}
			ret = true;
		} else if (lightFadeMotion != null) {
			if (lightFadeMotion.isFinished()) {
				lightFadeMotion = null;
				lightImg.setAlpha(0);
			} else {
				int alpha = 255 - 255 * lightFadeMotion.getProgress() / 100;
				lightImg.setAlpha(alpha);
			}
			ret = true;
		}
		return ret;
	}

	/**
	 * 设置亮点的Index.
	 * 
	 * @param index
	 *            发光的点的index
	 * @param motion
	 *            选择的点从暗变亮的Motion，该motion可以为null
	 */
	public void setLightPointIndex(int index, CFMotion motion) {
		if (index < 0 || index >= pointNum) {
			return;
		}
		lastSelectIndex = selectIndex;
		selectIndex = index;
		if (motion == null) {
			motion = new FMLinear(1, FMLinear.CSPEED, 0, 0, 100, 0, 20, 0, 0);
			attachAnimator(motion);
		}
		pointMotion = motion;
		points[lastSelectIndex].reset(motion);
		points[selectIndex].active(motion);
	}

	/**
	 * 释放资源
	 */
	public void release() {
		if (points != null) {
			for (int i = 0; i < points.length; i++) {
				points[i].release();
				points[i] = null;
			}
			points = null;
		}
		lightImg = null;
		pointImg = null;
		pointMotion = null;
		lightMotion = null;
		lightFadeMotion = null;
	}
}
