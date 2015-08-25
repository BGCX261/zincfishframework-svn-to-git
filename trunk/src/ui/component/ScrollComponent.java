package ui.component;

import com.mediawoz.akebono.corefilter.CFMotion;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.filters.motion.FMLinear;
import com.mediawoz.akebono.ui.UComponent;

import config.Resources;

/**
 * <code>ScrollComponent</code>是滚动条组件
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class ScrollComponent extends UComponent {
	private static final int HEIGHT = 7;// 上下圆角部分的高度
	private CFMotion moveMotion = null;// 滚动的motion
	private CFMotion fadeMotion = null;// 淡出淡入的motion
	private CRImage scrollImage = null;// 滚动条图片
	private int offsetY = 0;// 滚动条垂直方向偏移
	private int scrollHeight = 0;// 滚动条的高度
	private int containerHeight = 0;// 容器的高度
	private boolean isShowing = false;// 标识是淡出还是淡入

	public ScrollComponent(int ix, int iy, int width, int height) {
		super(1, ix, iy, width, height);
		scrollImage = Resources.getInstance().getScrollBar();
	}

	protected void drawCurrentFrame(CRGraphics g) {
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipW = g.getClipWidth();
		int clipH = g.getClipHeight();
		g.setClip(0, offsetY, iWidth, HEIGHT);
		scrollImage.draw(g, 0, offsetY, 20);
		g.setClip(0, offsetY + scrollHeight - HEIGHT, iWidth, HEIGHT);
		scrollImage.draw(g, 0, offsetY + scrollHeight, 36);

		int len = scrollHeight - HEIGHT * 2;
		int step = 0;
		int y = HEIGHT;
		while (len > 0) {
			step = len > 11 ? 11 : len;
			g.setClip(0, offsetY + y, iWidth, step);
			scrollImage.draw(g, 0, offsetY + y - 1, 20);
			len -= step;
			y += step;
		}

		g.setClip(clipX, clipY, clipW, clipH);
	}

	protected boolean animate() {
		boolean ret = false;
		if (moveMotion != null) {
			if (moveMotion.isFinished()) {
				moveMotion = null;
			} else {
				offsetY = moveMotion.getCurY() * iHeight / containerHeight;
			}
			ret = true;
		}
		if (fadeMotion != null) {
			if (fadeMotion.isFinished()) {
				detachAnimator(fadeMotion);
				fadeMotion = null;
				if (isShowing) {// 如果是淡入，则在淡入完成后，启动淡出动画
					isShowing = false;
					fadeMotion = new FMLinear(1, FMLinear.CSPEED, 100, 0, 0, 0,
							20, 0, 0);
					attachAnimator(fadeMotion);
				} else {
					setVisible(false);
				}
			} else {
				setVisible(true);
				int alpha = 255 * fadeMotion.getCurX() / 100;
				scrollImage.setAlpha(alpha);
			}
			ret = true;
		}
		return ret;
	}

	/**
	 * 滚动滚动条
	 * 
	 * @param motion
	 *            页面的滚动Motion。由于要求滚动条的滚动和页面的滚动同步，因此在此直接传入页面的Motion进来
	 */
	public void scroll(CFMotion motion) {
		this.moveMotion = motion;
		// 如果上一次的淡入(或淡出)没有完成，下一次淡入/淡出动作又到来，
		// 则需先记下上一次淡入淡出的状态，然后从此状态开始淡出或淡入
		int oldAlpha = 0;// 用于记录淡入的状态
		if (fadeMotion != null) {
			oldAlpha = fadeMotion.getCurX();
			detachAnimator(fadeMotion);
			fadeMotion = null;
		}
		isShowing = true;
		setVisible(true);
		fadeMotion = new FMLinear(1, FMLinear.CSPEED, oldAlpha, 0, 100, 0, 10,
				0, 0);
		attachAnimator(fadeMotion);
	}

	/**
	 * 设置滚动条的高度
	 * 
	 * @param height
	 *            滚动条的高度
	 */
	public void setContainerHeight(int height) {
		containerHeight = height;
		scrollHeight = iHeight * iHeight / height;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		if (fadeMotion != null) {
			detachAnimator(fadeMotion);
			fadeMotion = null;
		}
		if (moveMotion != null) {
			detachAnimator(moveMotion);
			moveMotion = null;
		}
		scrollImage = null;
	}

	/**
	 * 获取容器的高度
	 * 
	 * @return 容器的高度
	 */
	public int getContainerHeight() {
		return containerHeight;
	}
}
