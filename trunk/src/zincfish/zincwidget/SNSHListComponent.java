package zincfish.zincwidget;

import zincfish.zinccss.model.Coordinates;
import zincfish.zinccss.model.Insets;
import zincfish.zinccss.model.Metrics;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSHListDOM;
import zincfish.zincscript.ZincScript;
import com.mediawoz.akebono.corefilter.CFMotion;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.coreservice.utils.CSDevice;
import com.mediawoz.akebono.filters.motion.FMLinear;

/**
 * <code>SNSHListComponent</code>表示水平滚动列表
 * 
 * @author Jarod Yv
 */
public class SNSHListComponent extends AbstractSNSComponent {
	private int LMargin = 0;
	private int RMargin = 0;

	private CFMotion motion = null;

	// private SPointScroll cPoint = null;

	private CRImage arrow = null;// 箭头

	private int index = 0;

	public Metrics getPreferredSize(int preferredWidth, int nextLineWidth) {
		Metrics metrics = null;
		if (needToComputePreferredSize(preferredWidth)) {
			metrics = super.getPreferredSize(preferredWidth, nextLineWidth);
			if (dom.hasChildren()) {
				int height = 0;
				for (int i = 0; i < dom.children.size(); i++) {
					AbstractSNSDOM cdom = (AbstractSNSDOM) dom.children.get(i);
					AbstractSNSComponent component = cdom.getComponent();
					cdom = null;
					Metrics subMetrics = component.getPreferredSize(
							preferredWidth, nextLineWidth);
					component = null;
					height = Math.max(height, subMetrics.height);
					subMetrics = null;
				}
				Metrics minsize = dom.getStyleSet().getMinSize();
				if (minsize.width != -1)
					metrics.width += minsize.width;
				else
					metrics.width = preferredWidth;
				if (minsize.height != -1)
					metrics.height += Math.max(minsize.height, height);
				else
					metrics.height += height;
				minsize = null;
			}
		}
		return metrics;
	}

	protected void layout() {
		StyleSet style = dom.getStyleSet();
		Insets insets = style.getInsets();
		Coordinates gap = style.getGap();
		int x = insets.left;
		int y = insets.top;
		LMargin = insets.left;
		RMargin = insets.right;
		int space = gap.X;
		gap = null;
		insets = null;
		style = null;
		for (int i = 0; i < dom.children.size(); i++) {
			AbstractSNSDOM cdom = (AbstractSNSDOM) dom.children.get(i);
			AbstractSNSComponent component = cdom.getComponent();
			cdom = null;
			component.setBounds(x, y, component.getCachedMetrics().width,
					component.getCachedMetrics().height);
			x += (component.getCachedMetrics().width + space);
			component = null;
		}
	}

	/** 绘制接口 */
	protected void paintImpl(CRGraphics g) {
		int oldClipX = g.getClipX();
		int oldClipY = g.getClipY();
		int oldClipW = g.getClipWidth();
		int oldClipH = g.getClipHeight();
		g.setClip(LMargin, 0, iWidth - LMargin * 2, iHeight);
		for (int i = 0; i < dom.children.size(); i++) {
			AbstractSNSComponent component = (AbstractSNSComponent) componentAt(i);
			if (component != null && component.isInScreen()) {
				component.paintCurrentFrame(g, component.iX - offsetX,
						component.iY - offsetY);
			}
			component = null;
		}

		g.setClip(oldClipX, oldClipY, oldClipW, oldClipH);
		if (dom.children != null && arrow != null) {
			// 画左箭头
			if (index > 0) {
				arrow.draw(g, 0, getHeight() / 2, CRImage.FLIP_MIRROR,
						CRGraphics.LEFT | CRGraphics.VCENTER);
			}
			// 画右箭头
			if (index < dom.children.size() - 1) {
				arrow.draw(g, iWidth, getHeight() / 2, CRGraphics.RIGHT
						| CRGraphics.VCENTER);
			}
		}
	}

	protected boolean animate() {
		if (motion != null) {
			if (motion.isFinished()) {
				detachAnimator(motion);
				motion = null;
			} else {
				offsetX = motion.getCurX();
			}
			return true;
		}
		return false;
	}

	public void init(AbstractSNSDOM dom) {
		super.init(dom);
		String arrowSrc = ((SNSHListDOM) this.dom).arrowSrc;
		if (arrowSrc != null)
			arrow = CRImage.loadFromResource(arrowSrc);
		else
			arrow = CRImage.loadFromResource("/ui/arrow.png");
		arrowSrc = null;
	}

	public void setMotion(int startX, int startY) {

	}

	public boolean keyPressed(int keyCode) {
		if (motion != null && !motion.isFinished()) {
			return true;
		}

		int keyAction = CSDevice.getGameAction(keyCode);

		switch (keyAction) {
		case CSDevice.KEY_DOWN:
		case CSDevice.KEY_UP:
			return false;
		case CSDevice.KEY_RIGHT:
			if (index < dom.children.size() - 1) {
				AbstractSNSComponent component = (AbstractSNSComponent) componentAt(index);
				component.setFocus(false);
				component = null;
				index++;
				component = (AbstractSNSComponent) componentAt(index);
				if (component.iX + component.getWidth() - offsetX > iWidth
						- RMargin) {
					motion = new FMLinear(1, FMLinear.CSPEED, offsetX, 0,
							LMargin + component.iX + component.getWidth()
									- iWidth - RMargin, 0, 5, 0, 0);
					attachAnimator(motion);
				}
				component.setFocus(true);
				// visualCenterX = component.iX - offsetX;
				component = null;
			} else {
				motion = new FMLinear(1, FMLinear.PULLBACK, offsetX, 0,
						offsetX, 0, 5, 5, 0);
				attachAnimator(motion);
			}
			return true;
		case CSDevice.KEY_LEFT:
			if (index > 0) {
				AbstractSNSComponent component = (AbstractSNSComponent) componentAt(index);
				component.setFocus(false);
				component = null;
				index--;
				component = (AbstractSNSComponent) componentAt(index);
				if (component.iX - offsetX < LMargin) {
					motion = new FMLinear(1, FMLinear.CSPEED, offsetX, 0,
							LMargin - component.iX, 0, 5, 0, 0);
					attachAnimator(motion);
				}
				component.setFocus(true);
				// visualCenterX = component.iX - offsetX;
				component = null;
			} else {
				motion = new FMLinear(1, FMLinear.PULLBACK, offsetX, 0,
						offsetX, 0, 5, -5, 0);
				attachAnimator(motion);
			}
			return true;
		case CSDevice.KEY_FIRE:
			if (dom.children != null && index >= 0
					&& index < dom.children.size()) {
				AbstractSNSDOM curItemDom = (AbstractSNSDOM) dom.children
						.get(index);
				if (curItemDom.onClick != null) {
					ZincScript.getZincScript().executeDynamicScript(
							curItemDom.onClick);
				}
				curItemDom = null;
			}
			return true;
		default:
			return false;
		}
	}

	public void focusItem(int curIndex) {
		if (dom.children != null && curIndex < dom.children.size()) {
			AbstractSNSDOM curtabdom = (AbstractSNSDOM) dom.children
					.get(curIndex);
			if (curtabdom.onFocus != null) {
				cel.componentEventFired(this, 0, curtabdom.onFocus, 0);
			}
			curtabdom = null;
		}
	}

	public void setFocus(boolean isFocused) {
		if (dom.children != null && dom.children.size() > 0) {
			AbstractSNSComponent com = ((AbstractSNSDOM) dom.children
					.get(index)).getComponent();
			com.setFocus(isFocused);
			// visualCenterX = com.iX - offsetX;
			com = null;
		}
	}

	public boolean canFocus() {
		if (dom.children == null && dom.children.size() <= 0)
			return false;
		return super.canFocus();
	}

	public boolean hasChildren() {
		return false;
	}

	public void release() {
		motion = null;
		super.release();
	}

	public int getCurItemIndex() {
		return index;
	}

}
