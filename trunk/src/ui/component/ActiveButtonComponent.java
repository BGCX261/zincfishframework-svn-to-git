package ui.component;

import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.ui.UComponent;

public class ActiveButtonComponent extends UComponent {
	private boolean isActive;// 是否被激活

	private CRImage activeImg;// 激活的图片

	private CRImage unactiveImg;// 未激活的图片

	public ActiveButtonComponent(int animTickCount, int ix, int iy, int width,
			int height, final CRImage activeImg, final CRImage unactiveImg) {
		super(animTickCount, ix, iy, width, height);
		this.activeImg = activeImg;
		this.unactiveImg = unactiveImg;
	}

	protected void drawCurrentFrame(CRGraphics g) {
		if (isActive) {
			if (activeImg != null) {
				activeImg.draw(g, getWidth() / 2, getHeight() / 2, 3);
			}
		} else {
			if (unactiveImg != null) {
				unactiveImg.draw(g, getWidth() / 2, getHeight() / 2, 3);
			}
		}
	}

	protected boolean animate() {
		return false;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
