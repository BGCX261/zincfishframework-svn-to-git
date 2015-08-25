package zincfish.zincwidget;

import utils.ArrayList;
import utils.DrawUtils;
import zincfish.zinccss.ICSSConstants;
import zincfish.zinccss.model.Alignment;
import zincfish.zinccss.model.Insets;
import zincfish.zinccss.model.Metrics;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zinclayout.ILayout;
import zincfish.zincscript.ZincScript;
import zincfish.zincwidget.focus.FocusException;

import com.mediawoz.akebono.corerenderer.CRDisplay;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.ui.IKeyInputHandler;
import com.mediawoz.akebono.ui.UPanel;

/**
 * <p>
 * <code>AbstractSNSComponent</code> 是所有组件的基类，它对组件库中的所有组件进行了抽象。
 * </p>
 * <p>
 * <code>AbstractSNSComponent</code>继承自{@link com.mediawoz.akebono.ui.UPanel}
 * ，实现了{@link com.mediawoz.akebono.ui.IKeyInputHandler}
 * 接口。它是构成ViewTree的节点，本身是自组织的，其派生类具有各自的绘制和按键响应逻辑。
 * </p>
 * 
 * @author Jarod Yv
 */
public abstract class AbstractSNSComponent extends UPanel implements
		IKeyInputHandler {

	/** 该组件对应的DOM对象 */
	protected AbstractSNSDOM dom = null;

	/** 标识组件是否获得焦点 */
	protected boolean isFocused = false;

	/** 孩子节点的索引 */
	// public int index = 0;

	/* 表示组件是否被选中 */
	protected boolean isSelected = false;

	/* 缓存上一次排版的最佳宽度 */
	private int lastPreferredWidth = -1;

	// 用于焦点切换
	// protected int visualCenterX;
	// protected int visualCenterY;

	/* 缓存排版时的组件尺寸 */
	private Metrics cachedMetrics = null;

	/* 具有position属性的节点 */
	private ArrayList positionComponents = null;

	/* 标识该组件是否被注册 */
	private boolean isRegisted = false;

	public int offsetX = 0;

	public int offsetY = 0;

	/**
	 * 构造函数
	 */
	public AbstractSNSComponent() {
		super(1, 0, 0, 0, 0);
	}

	/**
	 * 构造函数
	 * 
	 * @param iAnimTickCount
	 */
	public AbstractSNSComponent(int iAnimTickCount) {
		super(iAnimTickCount, 0, 0, 0, 0);
	}

	/**
	 * 初始化组件<br>
	 * 各子组件根据自身的需要，重写此方法，完成必要的初始化工作。
	 */
	public void init(AbstractSNSDOM dom) {
		this.dom = dom;
		setVisible(dom.isVisible);
	}

	/**
	 * 如果组件有动画，在此指定动画的其实位置。结束位置为组件的排版位置。
	 * 
	 * @param motionX
	 *            动画开始的x坐标
	 * @param motionY
	 *            动画开始的y坐标
	 */
	public abstract void setMotion(int motionX, int motionY);

	/**
	 * 获取当前子节点
	 * 
	 * @return
	 */
	// public AbstractSNSComponent getCurrentChild() {
	// return getChildComponent(index);
	// }

	/**
	 * 获取与该组件相关联的DOM
	 * 
	 * @return {@link #dom}
	 */
	public AbstractSNSDOM getDom() {
		return dom;
	}

	/**
	 * 获取与该组件相关联的DOM
	 * 
	 * @param dom
	 *            与该组件相关联的DOM
	 */
	public void setDom(AbstractSNSDOM dom) {
		this.dom = dom;
	}

	/**
	 * 获取当前组件的索引
	 * 
	 * @return {@link #index}
	 */
	// public int getIndex() {
	// return index;
	// }

	/**
	 * 设置当前组件的索引
	 * 
	 * @param index
	 *            当前组件的索引
	 */
	// public void setIndex(int index) {
	// this.index = index;
	// }

	/**
	 * 设置组件的焦点状态。不同组件获得焦点后会有不同的动作。
	 * 
	 * @param isFocused
	 *            聚焦状态
	 */
	public void setFocus(boolean isFocused) {
		if (canFocus() && this.isFocused != isFocused) {// 设置焦点的前提是能够获得焦点
			this.isFocused = isFocused;
			dom.invalidateStylePropertiesCache(true);// 当选中状态发生变化时，需要重新绑定样式
			if (isFocused) {
				ZincScript.getZincScript().executeDynamicScript(dom.onFocus);
			} else {
				ZincScript.getZincScript()
						.executeDynamicScript(dom.onLoseFocus);
			}
			// 当父组件的聚焦状态需要传递给子组件时，打开下面的代码
			if (hasChildren()) {
				for (int i = 0; i < getChildrenNum(); i++) {
					AbstractSNSComponent c = getChildComponent(i);
					c.setFocus(isFocused);
					c = null;
				}
			}
		}
	}

	/**
	 * 判断组件是否能够获得焦点
	 * 
	 * @return <ul>
	 *         <li><code>true</code> - 能获得焦点
	 *         <li><code>false</code> - 不能获得焦点
	 *         </ul>
	 */
	public boolean canFocus() {
		return isVisible() && this.dom != null && this.dom.canFocus
				&& this.dom.isAvailable;
	}

	/**
	 * 判断当前组件是否含有子节点
	 * 
	 * @return <ul>
	 *         <li><code>true</code> - 有子节点
	 *         <li><code>false</code> - 没有子节点
	 *         </ul>
	 */
	public boolean hasChildren() {
		return dom != null && dom.hasChildren();
	}

	/**
	 * 获取子节点的个数
	 * 
	 * @return 子节点的个数。如果没有子节点则返回-1
	 */
	public int getChildrenNum() {
		if (hasChildren()) {
			return dom.children.size();
		}
		return -1;
	}

	/**
	 * 释放资源。其派生类需要根据各自的需要重写此方法
	 */
	public void release() {
		clearCachedStyleProperties();// 清理缓存的样式
		// 清理子组件
		for (int i = 0; i < getChildrenNum(); i++) {
			AbstractSNSComponent c = getChildComponent(i);
			if (c != null) {
				c.release();
			}
			c = null;
		}
		// 清除与子组件的关联
		removeAllComponents();
		// 清除与DOM的关联
		if (this.dom != null) {
			this.dom.setComponent(null);
			this.dom = null;
		}
		// System.gc();
	}

	/**
	 * 判断组件是否获得焦点
	 * 
	 * @return <ul>
	 *         <li><code>true</code> - 已经获得焦点
	 *         <li><code>false</code> - 没有获得焦点
	 *         </ul>
	 */
	public boolean isFocused() {
		return isFocused;
	}

	/**
	 * 返回组件的最佳尺寸
	 * 
	 * @param preferredWidth
	 *            最佳宽度
	 * @return 组件的最佳尺寸
	 */
	public Metrics getPreferredSize(int preferredWidth, int nextLineWidth) {
		Metrics metrics = getCachedMetrics();
		if (needToComputePreferredSize(preferredWidth)) {
			StyleSet style = dom.getStyleSet();
			ILayout layout = style.getLayout();
			if (layout == null) {// 如果没有定义排版样式, 则采用简单的最小宽度+留白的方式获得组件的尺寸
				Insets insets = style.getInsets();
				// Metrics minSize = style.getMinSize();
				metrics.firstLineWidth = metrics.width = insets.left
				/* + minSize.width */+ insets.right;
				metrics.lastLineHeight = metrics.height = insets.top
				/* + minSize.height */+ insets.bottom;
				// minSize = null;
				insets = null;
			} else {// 如果定义了排版样式, 则利用排版样式进行排版
				layout.measurePreferredSize(this, preferredWidth, metrics);
			}
			lastPreferredWidth = preferredWidth;// 缓存本次计算的最佳宽度
			layout = null;
			style = null;
		}
		return metrics;
	}

	/**
	 * 判断是否需要重新计算最佳尺寸.<br>
	 * 当组件的位置没有确定下来或者传入的最佳宽度与上一次的最佳宽度不等时, 则需要重新计算组件的最佳尺寸.
	 * 
	 * @param preferredWidth
	 *            最佳宽度
	 * @return <ul>
	 *         <li><code>true</code> - 需要重新计算组件的最佳尺寸
	 *         <li><code>false</code> - 不需要重新计算组件的最佳尺寸
	 *         </ul>
	 */
	protected boolean needToComputePreferredSize(int preferredWidth) {
		return dom.isInvalidated() || preferredWidth != lastPreferredWidth;
	}

	/**
	 * 返回组件的尺寸
	 * 
	 * @return {@link #cachedMetrics}
	 */
	public Metrics getCachedMetrics() {
		if (cachedMetrics == null) {
			cachedMetrics = new Metrics(this);
		}
		// cachedMetrics.next = null; // 最后一个Metric不链向任何其他对象
		return cachedMetrics;
	}

	/**
	 * 设置组件的尺寸边界.<br>
	 * 如果组件的尺寸位置没有确定或者尺寸位置与传入的尺寸位置不等, 则重新设置组件的尺寸位置, 并重新排版.
	 * 
	 * @param x
	 *            横坐标位置
	 * @param y
	 *            纵坐标位置
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public void setBounds(int x, int y, int width, int height) {
		if (dom.isInvalidated() || this.iX != x || this.iY != y
				|| this.iWidth != width || this.iHeight != height) {
			// 设置位置和尺寸
			this.iX = x;
			this.iY = y;
			this.iWidth = width;
			this.iHeight = height;
			// 计算组件的中心尺寸
			// Insets margin = dom.getStyleSet().getMargin();
			// visualCenterX = (width - margin.left - margin.right) / 2
			// + margin.left;
			// visualCenterY = (height - margin.top - margin.bottom) / 2
			// + margin.top;
			// margin = null;

			// 重新排版
			layout();
		}
	}

	/**
	 * 对组件进行排版
	 */
	protected void layout() {
		// 先将组件设置为已经确定尺寸位置，因为排版过程中可能重新设置为未确定
		dom.setInvalidated(false);
		// 根据layout属性进行排版
		ILayout layout = dom.getStyleSet().getLayout();
		if (layout != null) {
			layout.doLayout(this);
		}
		layout = null;
		if (positionComponents != null) {
			for (int i = 0; i < positionComponents.size(); i++) {
				AbstractSNSComponent component = (AbstractSNSComponent) positionComponents
						.get(i);
				Insets positionInsets = component.dom.getStyleSet()
						.getPositionInsets();
				System.out.println("l = " + positionInsets.left + " t = "
						+ positionInsets.top);
				if (component.shouldIgnoreLayout()) {
					component.setBounds(iX + positionInsets.left
							- positionInsets.right, iY + positionInsets.top
							- positionInsets.bottom, iWidth, iHeight);
				} else {
					component.setBounds(iX + positionInsets.left
							- positionInsets.right, iY + positionInsets.top
							- positionInsets.bottom, component.getWidth(),
							component.getHeight());
				}
				positionInsets = null;
				component = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mediawoz.akebono.ui.UPanel#drawCurrentFrame(com.mediawoz.akebono.
	 * corerenderer.CRGraphics)
	 */
	protected void drawCurrentFrame(CRGraphics g) {
		if (isVisible()) {
			paint(g);// 绘制组件本身
			paintChildren(g);// 绘制子组件
			paintPositionComponent(g);// 绘制含有position属性的组件
		}
	}

	/**
	 * 绘制组件本身
	 * 
	 * @param g
	 *            图形上下文
	 */
	protected void paint(CRGraphics g) {
		StyleSet style = dom.getStyleSet();
		if (style != null) {
			DrawUtils.paintBackground(g, iWidth, iHeight, style);// 绘制背景
			DrawUtils.paintBorder(g, iWidth, iHeight, style);// 绘制边框
		}
		style = null;
		paintImpl(g);
	}

	/**
	 * 子类实现该方法，用于绘制组件自身特有的样式和数据
	 * 
	 * @param g
	 *            图像上下文
	 */
	protected abstract void paintImpl(CRGraphics g);

	/**
	 * 依次绘制子组件
	 * 
	 * @param g
	 *            图形上下文
	 */
	protected void paintChildren(CRGraphics g) {
		for (int i = 0; i < getChildrenNum(); i++) {
			AbstractSNSComponent component = getChildComponent(i);
			if (component != null && component.isVisible()
					&& !component.isRegisted && component.isInScreen()) {
				component.paintCurrentFrame(g, component.iX - offsetX,
						component.iY - offsetY);
			}
			component = null;
		}
	}

	/**
	 * 绘制具有position属性的组件
	 * 
	 * @param g
	 *            图形上下文
	 */
	protected void paintPositionComponent(CRGraphics g) {
		if (positionComponents != null) {
			for (int i = 0; i < positionComponents.size(); i++) {
				AbstractSNSComponent component = (AbstractSNSComponent) positionComponents
						.get(i);
				if (component != null && component.isInScreen()) {
					component.paintCurrentFrame(g, component.iX, component.iY);
				}
				component = null;
			}
		}
	}

	/**
	 * 判断组件是否在屏幕范围内
	 * 
	 * @return <ul>
	 *         <li><code>true</code> - 组件或组件的部分在屏幕区域内
	 *         <li><code>false</code> - 组件完全不在屏幕区域内
	 *         </ul>
	 */
	protected boolean isInScreen() {
		int AbsoluteY = getAbsoluteY();
		int AbsoluteX = getAbsoluteX();
		return (AbsoluteY < CRDisplay.getHeight() || AbsoluteY + getHeight() > 0)
				&& (AbsoluteX < CRDisplay.getWidth() || AbsoluteX + getWidth() > 0);
	}

	public int getAbsoluteX() {
		if (dom.father != null)
			return dom.father.getComponent().getAbsoluteX() + iX - offsetX;
		else
			return iX - offsetX;
	}

	public int getAbsoluteY() {
		if (dom.father != null)
			return dom.father.getComponent().getAbsoluteY() + iY - offsetY;
		else
			return iY - offsetY;
	}

	/**
	 * 获取指定的孩子组件
	 * 
	 * @param index
	 *            子组件的索引
	 * @return 相应的子组件{@link AbstractSNSComponent}
	 */
	public AbstractSNSComponent getChildComponent(int index) {
		if (hasChildren()) {
			if (index >= 0 && index < getChildrenNum()) {
				AbstractSNSDOM childDOM = (AbstractSNSDOM) dom.children
						.get(index);
				if (childDOM != null)
					return childDOM.getComponent();
			}
		}
		return null;
	}

	/**
	 * 获取父组件
	 * 
	 * @return 父组件
	 */
	public AbstractSNSComponent getParentComponent() {
		return dom.father == null ? null : dom.father.getComponent();
	}

	/**
	 * 清理缓存的样式属性
	 */
	private void clearCachedStyleProperties() {
		dom.clearCachedStyle(false);// NOTE 此处传入false, 不必清理子组件的样式, 后面会有方法清理到子组件
		cachedMetrics = null;
	}

	/**
	 * 判断组件是否被选中
	 * 
	 * @return {@link #isSelected}
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * 设置组件选中与否
	 * 
	 * @param isSelected
	 *            组件选中与否
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean keyPressed(int keyCode) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.ui.IKeyInputHandler#keyReleased(int)
	 */
	public boolean keyReleased(int keyCode) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.ui.IKeyInputHandler#keyRepeated(int)
	 */
	public boolean keyRepeated(int keyCode) {
		return false;
	}

	/**
	 * 判断组件是否需要在排版时忽略<br>
	 * 如果组件的position属性为"absolute"或者"fixed", 则在排版时暂时不对其进行排版。
	 * 
	 * @return
	 */
	public boolean shouldIgnoreLayout() {
		Byte position = dom.getStyleSet().getPositionType();
		if (position != null) {
			return position == ICSSConstants.POSITION_TYPE_FIXED
					|| position == ICSSConstants.POSITION_TYPE_ABSOLUTE;
		}
		position = null;
		return false;
	}

	/**
	 * 判断组件是否需要被注册<br>
	 * 如果组件定义了position样式属性, 并且不是static类型, 则需要注册
	 * 
	 * @return
	 */
	public boolean shouldBeRegisted() {
		Byte position = dom.getStyleSet().getPositionType();
		if (position != null) {
			return position != ICSSConstants.POSITION_TYPE_STATIC;
		}
		return false;
	}

	public void addPositionComponent(AbstractSNSComponent component) {
		if (positionComponents == null)
			positionComponents = new ArrayList(2);
		positionComponents.add(component);
	}

	public void setRegisted(boolean isRegisted) {
		this.isRegisted = isRegisted;
	}

	public boolean isRegisted() {
		return this.isRegisted;
	}

	/**
	 * 对于含有position样式属性的组件，排版时不拍它，把它注册给其父组件，等排版结束后，在处理这些注册了的有position属性的位置。
	 */
	public void regist2Parent() {
		if (shouldBeRegisted() && !isRegisted) {
			AbstractSNSComponent parent = null;
			Byte position = dom.getStyleSet().getPositionType();
			if (position == ICSSConstants.POSITION_TYPE_FIXED) {
				parent = dom.getRoot().getComponent();
			} else {
				parent = getParentComponent();
			}
			position = null;
			if (parent != null) {
				parent.addPositionComponent(this);
				isRegisted = true;
			}
			parent = null;
		}
	}

	public int getViewWidth() {
		return getWidth();
	}

	public int getViewHeight() {
		return getHeight();
	}

	public AbstractSNSComponent getBrother(boolean forward) {
		AbstractSNSDOM father = dom.father;
		if (father != null) {
			int index = father.children.indexOf(dom);
			if (forward)
				index++;
			else
				index--;
			if (index >= 0 && index < father.children.size()) {
				AbstractSNSDOM brother = (AbstractSNSDOM) father.children
						.get(index);
				return brother.getComponent();
			}
		}
		return null;
	}

	/**
	 * 获取下一获得焦点的组件
	 * 
	 * @param root
	 *            整个的组件树
	 * @param focusedComponent
	 *            当前获得焦点的组件
	 * @param nearestFocusableComponent
	 *            与组件距离最近的可获得焦点的组件
	 * @param forward
	 *            <code>true</code>-向下搜索,<code>false</code>-向上搜索
	 * @param direction
	 *            搜索的方向-上,下,左,右四个方向
	 * @param checkItself
	 *            是否判断自身能否获得焦点
	 * @param checkChild
	 *            是否判断孩子能否获得焦点
	 * @param checkParent
	 *            是否判断上层组件能否获得焦点
	 * @return 下一个获得焦点的组件
	 */
	public AbstractSNSComponent getOtherFocus(AbstractSNSComponent root,
			AbstractSNSComponent focusedComponent,
			AbstractSNSComponent nearestFocusableComponent, boolean forward,
			Alignment direction, boolean checkItself, boolean checkChild,
			boolean checkParent) throws FocusException {
		if (checkItself && root != this && !isFocused() && canFocus()) {
			if (focusedComponent == null || direction == null) {
				throw new FocusException(this);
			}
			if (direction.isLeft() || direction.isRight())
				throw new FocusException(this);
			if (isNearest(focusedComponent, nearestFocusableComponent,
					direction)) {
				nearestFocusableComponent = this;
			}
		} else if (checkChild && isVisible()) {
			// 扫描孩子
			AbstractSNSComponent childWidget = forward ? getChildComponent(0)
					: getChildComponent(getChildrenNum() - 1);
			if (childWidget != null) {
				nearestFocusableComponent = childWidget.getOtherFocus(root,
						focusedComponent, nearestFocusableComponent, forward,
						direction, true, true, false);
			}
		}
		if (root != this) {
			// 扫描兄弟
			AbstractSNSComponent brotherWidget = getBrother(forward);
			if (brotherWidget != null) {
				nearestFocusableComponent = brotherWidget.getOtherFocus(root,
						focusedComponent, nearestFocusableComponent, forward,
						direction, true, true, false);
			}
			brotherWidget = null;
			// 扫描父亲
			if (checkParent && isVisible()) {
				AbstractSNSComponent parent = getParentComponent();
				if (parent != null) {
					nearestFocusableComponent = parent.getOtherFocus(root,
							focusedComponent, nearestFocusableComponent,
							forward, direction, true, false, true);
				}
				parent = null;
			}
		}
		return nearestFocusableComponent;
	}

	/**
	 * 判断<code>nearestComponent</code>是否在<code>direction</code>方向上距离
	 * <code>originComponent</code>最近
	 * 
	 * @param originComponent
	 *            原组件
	 * @param nearestComponent
	 *            被判断的组件
	 * @param direction
	 *            方向
	 * @return <code>true</code>- <code>nearestComponent</code>是在
	 *         <code>direction</code>方向上距离<code>originComponent</code>最近的组件.
	 */
	private boolean isNearest(AbstractSNSComponent originComponent,
			AbstractSNSComponent nearestComponent, Alignment direction)
			throws FocusException {
		if (originComponent != null && direction != null) {
			int origX = originComponent.getAbsoluteX()
			/* + originComponent.visualCenterX */;
			int origY = originComponent.getAbsoluteY()
			/* + originComponent.visualCenterY */;
			int dx = getAbsoluteX() /* + visualCenterX */- origX;
			int dy = getAbsoluteY() /* + visualCenterY */- origY;
			if (nearestComponent != null) {
				int nearestDx = nearestComponent.getAbsoluteX()
				/* + nearestComponent.visualCenterX */- origX;
				int nearestDy = nearestComponent.getAbsoluteY()
				/* + nearestComponent.visualCenterY */- origY;
				if (direction.isTop() || direction.isBottom()) {
					if (Math.abs(dy) <= Math.abs(nearestDy)) {
						if (direction.isBottom() && dy > 0 || direction.isTop()
								&& dy < 0) {
							return Math.abs(dx) <= Math.abs(nearestDx);
						}
					} else {
						throw new FocusException(nearestComponent);
					}
				} else {
					throw new FocusException(this);
				}
			} else {
				if (direction.isTop() || direction.isBottom()) {
					return dy != 0
							&& (direction.isBottom() && dy > 0 || direction
									.isTop()
									&& dy < 0);
				} else {
					throw new FocusException(this);
				}
			}
		}
		return false;
	}
}
