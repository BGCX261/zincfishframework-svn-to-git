package ui.component;

import com.mediawoz.akebono.corerenderer.CRDisplay;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.ui.UComponent;

/**
 * <p>
 * <code>WheelLoadingComponent</code> 模拟了一个WEB2.0中旋转的加载动画
 * </p>
 * 
 * @author Jarod Yv
 * @since fingerling
 */
public class WheelLoadingComponent extends UComponent {
	/* 下面的"loading..."图片 */
	private CRImage loading = null;
	/* 旋转动画 */
	private CRImage hourglass = null;
	/* 旋转动画横坐标的偏移 */
	private int offset;
	/* 旋转动画绘制的横坐标 */
	private int hourglassX = 0;
	/* loading图片绘制的纵坐标 */
	private int loadingY = 0;
	/* 唯一实例 */
	private static WheelLoadingComponent instance = null;

	/**
	 * 获取实例
	 * 
	 * @return WheelLoadingComponent的唯一实例
	 */
	public static WheelLoadingComponent getInstance() {
		if (instance == null) {
			CRImage image = CRImage.loadFromResource("/img/load.png");
			CRImage loading = CRImage.loadFromResource("/img/loading.png");
			instance = new WheelLoadingComponent(1,
					(CRDisplay.getWidth() - 72) / 2,
					(CRDisplay.getHeight() - 32) / 2, image, loading);
			image = null;
			loading = null;
		}
		return instance;
	}

	private WheelLoadingComponent(int animTickCount, int ix, int iy,
			CRImage img, CRImage load) {
		super(animTickCount, ix, iy, 72, 36);
		this.hourglass = img;
		this.loading = load;
		hourglassX = 24;
		loadingY = 20;
		offset = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mediawoz.akebono.ui.UComponent#drawCurrentFrame(com.mediawoz.akebono
	 * .corerenderer.CRGraphics)
	 */
	protected void drawCurrentFrame(CRGraphics g) {
		int oldClipX = g.getClipX();
		int oldClipY = g.getClipY();
		int oldClipW = g.getClipWidth();
		int oldClipH = g.getClipHeight();
		g.setClip(hourglassX, 0, 16, 16);
		hourglass.draw(g, hourglassX - offset, 0, 20);
		g.setClip(oldClipX, oldClipY, oldClipW, oldClipH);
		loading.draw(g, 0, loadingY, 20);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.coreanimation.CAAnimator#animate()
	 */
	protected boolean animate() {
		offset += 16;
		if (offset >= 192)
			offset = 0;
		return true;
	}
}
