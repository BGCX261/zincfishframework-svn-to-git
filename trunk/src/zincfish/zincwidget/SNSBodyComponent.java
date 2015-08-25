package zincfish.zincwidget;

import ui.component.ScrollComponent;
import utils.DOMUtil;
import zincfish.zinccss.model.Metrics;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSBodyDOM;
import com.mediawoz.akebono.corefilter.CFMotion;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.filters.motion.FMLinear;

/**
 * <code>SNSBodyComponent</code> 组件是界面上一个可操作的独立界面单元
 * 
 * @author Jarod Yv
 */
public class SNSBodyComponent extends AbstractSNSComponent {
	private static final int LEN = 30;
	public AbstractSNSComponent bindComponentNorth = null;
	public AbstractSNSComponent bindComponentSouth = null;
	public AbstractSNSComponent bindComponentWest = null;
	public AbstractSNSComponent bindComponentEast = null;
	/* body的出现动画 */
	private CFMotion motion = null;

	/* 当前获得焦点的组件 */
	private AbstractSNSComponent currentComponent = null;

	/* 滚动条 */
	private ScrollComponent scroll = null;

	private int viewX = 0;
	private int viewY = 0;
	private int viewW = 0;
	private int viewH = 0;

	protected boolean animate() {
		if (motion != null) {
			if (motion.isFinished()) {
				detachAnimator(motion);
				motion = null;
			} else {
				offsetX = motion.getCurX();
				offsetY = motion.getCurY();
			}
			return true;
		}
		return false;
	}

	public void init(AbstractSNSDOM dom) {
		super.init(dom);
		SNSBodyDOM body = (SNSBodyDOM) this.dom;
		offsetY = body.getOffsetY();
		bindComponentNorth = DOMUtil.DOMTree2ViewTree(body.bindDOMNorth);
		bindComponentEast = DOMUtil.DOMTree2ViewTree(body.bindDOMEast);
		bindComponentSouth = DOMUtil.DOMTree2ViewTree(body.bindDOMSouth);
		bindComponentWest = DOMUtil.DOMTree2ViewTree(body.bindDOMWest);
		body = null;
	}

	protected void layout() {
		AbstractSNSComponent fatherCpmponent = getParentComponent();
		if (fatherCpmponent != null) {
			viewX = fatherCpmponent.iX;
			viewY = fatherCpmponent.iY;
			viewW = fatherCpmponent.getWidth();
			viewH = getParentComponent().getHeight();
		} else {
			viewX = iX;
			viewY = iY;
			viewW = iWidth;
			viewH = iHeight;
		}
		fatherCpmponent = null;
		// 对绑定组件进行排版
		layoutBind();
		// 排版子组件
		super.layout();
		// 排版完成后初始化滚动条
		initScroll();
	}

	/**
	 * 对绑定组件进行排版
	 */
	private void layoutBind() {
		Metrics preferredSize = null;
		if (bindComponentNorth != null
				&& bindComponentNorth.needToComputePreferredSize(iWidth)) {
			preferredSize = bindComponentNorth.getPreferredSize(iWidth, iWidth);
			bindComponentNorth.setBounds(iX, iY, iWidth, preferredSize.height);
			viewY += bindComponentNorth.getHeight();
			if (offsetY == 0)
				offsetY -= bindComponentNorth.getHeight();
			viewH -= bindComponentNorth.getHeight();
			preferredSize = null;
		}
		if (bindComponentEast != null
				&& bindComponentEast.needToComputePreferredSize(iWidth)) {
			preferredSize = bindComponentEast.getPreferredSize(iWidth, iWidth);
			bindComponentEast.setBounds(iX, iY, preferredSize.width,
					preferredSize.height);
			bindComponentEast.iX = iWidth - bindComponentEast.getWidth();
			viewW -= bindComponentEast.getWidth();
			preferredSize = null;
		}
		if (bindComponentSouth != null
				&& bindComponentSouth.needToComputePreferredSize(iWidth)) {
			preferredSize = bindComponentSouth.getPreferredSize(iWidth, iWidth);
			bindComponentSouth.setBounds(iX, iY, preferredSize.width,
					preferredSize.height);
			bindComponentSouth.iY = iHeight - bindComponentSouth.getHeight();
			viewH -= bindComponentSouth.getHeight();
			preferredSize = null;
		}
		if (bindComponentWest != null
				&& bindComponentWest.needToComputePreferredSize(iWidth)) {
			preferredSize = bindComponentWest.getPreferredSize(iWidth, iWidth);
			bindComponentWest.setBounds(iX, iY, preferredSize.width,
					preferredSize.height);
			viewX += bindComponentWest.getWidth();
			offsetX -= bindComponentWest.getWidth();
			viewW -= bindComponentWest.getWidth();
			preferredSize = null;
		}
	}

	/**
	 * 根据高度初始化滚动条
	 */
	private void initScroll() {
		if (iHeight > viewH) {
			if (scroll == null) {
				scroll = new ScrollComponent(viewW - 5, viewY, 5, viewH);
				scroll.setContainerHeight(iHeight);
				scroll.setVisible(false);
				addComponent(scroll);
			} else {
				if (iHeight != scroll.getContainerHeight()) {
					scroll.setContainerHeight(iHeight);
				}
			}
		} else {
			if (scroll != null) {
				if (indexOfComponent(scroll) > -1) { // 核对是否该组件已经被添加
					removeComponent(scroll);
				}
				scroll.release();
				scroll = null;
			}
		}
	}

	public void setMotion(int startX, int startY) {
		if (startY != 0) {
			motion = new FMLinear(1, FMLinear.CSPEED, 0, offsetY, 0, offsetY
					+ startY, 5, 0, 0);
			// motion = new FMLinear(1, FMLinear.PULLBACK, 0, offsetY, 0,
			// offsetY
			// + startY, 15, 0, startY / 3);
		} else if (startX != 0) {
			motion = new FMLinear(1, FMLinear.PULLBACK, 0, offsetY, 0, offsetY,
					5, 0, startX / 3);
		} else {
			return;
		}
		if (scroll != null)
			scroll.scroll(motion);
		attachAnimator(motion);
	}

	/**
	 * 停止页面滚动的动画
	 */
	public void finishMotion() {
		if (motion != null) {
			detachAnimator(motion);
			motion = null;
		}
		((SNSBodyDOM) dom).setOffsetY(offsetY);
	}

	public byte getType() {
		return ((SNSBodyDOM) dom).type;
	}

	/**
	 * 获取当前获得焦点的组件
	 * 
	 * @return 当前获得焦点的组件
	 */
	public AbstractSNSComponent getCurrentComponent() {
		return currentComponent;
	}

	/**
	 * 设置当前获得焦点的组件
	 * 
	 * @param component
	 *            当前获得焦点的组件
	 */
	public void setCurrentComponent(AbstractSNSComponent component) {
		if (component != null) {
			this.currentComponent = component;
			((SNSBodyDOM) dom).setCurrentDOM(component.getDom());
		}
	}

	public void scroll(AbstractSNSComponent component) {
		if (component != null) {
			int dist = 0;
			finishMotion();
			if (component.getAbsoluteY() + component.getHeight() > viewY
					+ viewH) {
				dist = component.getAbsoluteY() + component.getHeight() - viewY
						- viewH + LEN;
				if (offsetY + dist >= iHeight - viewH - viewY)
					dist -= LEN;
			} else if (component.getAbsoluteY() < viewY) {
				dist = component.getAbsoluteY() - viewY - LEN;
				if (dist < -viewY)
					dist += LEN;
			}
			setMotion(0, dist);
		}
	}

	public void handleEged(boolean forward) {
		if (motion != null && !motion.isFinished())
			return;
		if (forward) {
			if (iHeight - offsetY > viewY + viewH) {
				setMotion(0, iHeight - offsetY - viewH - viewY);
			} else {
				setMotion(LEN, 0);
			}
		} else {
			if (offsetY > 0) {
				setMotion(0, -offsetY);
			} else {
				setMotion(-LEN, 0);
			}
		}
	}

	public void release() {
		motion = null;
		if (scroll != null)
			scroll.release();
		scroll = null;
		super.release();
	}

	protected void paintImpl(CRGraphics g) {
		g.setClip(iX, iY, iWidth, iHeight);
		if (bindComponentNorth != null) {
			bindComponentNorth.paintCurrentFrame(g, bindComponentNorth.iX,
					bindComponentNorth.iY);
		}
		if (bindComponentEast != null) {
			bindComponentEast.paintCurrentFrame(g, bindComponentEast.iX,
					bindComponentEast.iY);
		}
		if (bindComponentSouth != null) {
			bindComponentSouth.paintCurrentFrame(g, bindComponentSouth.iX,
					bindComponentSouth.iY);
		}
		if (bindComponentWest != null) {
			bindComponentWest.paintCurrentFrame(g, bindComponentWest.iX,
					bindComponentWest.iY);
		}
		g.setClip(viewX, viewY, viewW, viewH);
		// 绘制滚动条
		if (scroll != null && scroll.isVisible())
			scroll.paintCurrentFrame(g, scroll.iX, scroll.iY);
	}

	public int getViewWidth() {
		return viewW;
	}
}
