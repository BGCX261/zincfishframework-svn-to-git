package zincfish.zinclayout;

import zincfish.zinccss.model.*;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>InlineLayout</code> 定义了线性排版样式。
 * 
 * @author Jarod Yv
 */
public class InlineLayout implements ILayout {

	private boolean horizontal = false;// 标识是水平还是垂直排版
	private Alignment alignment = null;// 对齐方式

	/**
	 * 构造函数
	 */
	public InlineLayout() {
		this(false, Alignment.FILL);
	}

	/**
	 * 构造函数
	 * 
	 * @param horizontal
	 *            标识是否是水平排版
	 */
	public InlineLayout(boolean horizontal) {
		this(horizontal, Alignment.FILL_CENTER);
	}

	/**
	 * 构造函数
	 * 
	 * @param horizontal
	 *            标识是否是水平排版
	 * @param alignment
	 *            对齐样式
	 */
	public InlineLayout(boolean horizontal, Alignment alignment) {
		this.horizontal = horizontal;
		this.alignment = alignment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seezincfish.zinclayout.Layout#measurePreferredSize(zincfish.zincwidget.
	 * AbstractSNSComponent, int, zincfish.zinccss.model.Metrics)
	 */
	public void measurePreferredSize(AbstractSNSComponent component,
			int preferredWidth, Metrics metrics) {
		measure(component, false, preferredWidth, metrics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zinclayout.Layout#doLayout(zincfish.zincwidget.AbstractSNSComponent
	 * )
	 */
	public void doLayout(AbstractSNSComponent component) {
		measure(component, true, component.getWidth(), null);
	}

	/**
	 * 计算目标组件子组件的排版
	 * 
	 * @param component
	 *            目标组件
	 * @param layout
	 *            标识是否需要排版子组件
	 * @param preferredWidth
	 *            参考宽度
	 * @param metrics
	 *            组件的尺寸
	 */
	private void measure(AbstractSNSComponent component, boolean layout,
			int preferredWidth, Metrics metrics) {
		// 获取组件的样式，得到相关的尺寸
		StyleSet styleSet = component.getDom().getStyleSet();
		Alignment targetAlignment = styleSet.getAlign();
		Insets insets = styleSet.getInsets();
		Metrics minSize = styleSet.getMinSize();
		Coordinates gap = styleSet.getGap();
		styleSet = null;

		int width = preferredWidth - insets.left - insets.right;// 可用于子组件排版的宽度
		int height = component.getHeight() - insets.top - insets.bottom;// 可用于子组件排版的高度
		int contentWidth = 0;// 排版过程中的宽度
		int contentHeight = 0;// 排版过程中的高度
		Metrics first = null;// 记录第一个组件的尺寸
		Metrics current = null;// 记录当前组件的尺寸

		// 处理子组件
		for (int i = 0; i < component.getChildrenNum(); i++) {
			AbstractSNSComponent com = component.getChildComponent(i);
			if (com != null) {
				com.regist2Parent();
				// 累加子组件的尺寸
				if (com.isVisible() && !com.shouldIgnoreLayout()) {
					Metrics preferredSize = null;
					if (horizontal) {
						preferredSize = com.getPreferredSize(width
								- contentWidth, width - contentWidth);
						contentWidth += preferredSize.width
								+ (first != null ? gap.X : 0);
						contentHeight = Math.max(preferredSize.height,
								contentHeight);
					} else {
						preferredSize = com.getPreferredSize(width, width);
						contentWidth = Math.max(preferredSize.width,
								contentWidth);
						contentHeight += preferredSize.height
								+ (first != null ? gap.Y : 0);
					}

					if (first == null) {
						first = current = preferredSize;
					} else {
						current.next = preferredSize;
						current = preferredSize;
					}

					preferredSize = null;
				}
				com = null;
			}
		}

		// 如果不是排版，直接更新组件的尺寸即可
		if (!layout) {
			metrics.width = insets.left + Math.max(minSize.width, contentWidth)
					+ insets.right;
			metrics.height = insets.top
					+ Math.max(minSize.height, contentHeight) + insets.bottom;
			return;
		}

		// 调整组件的位置
		int x = 0;
		int y = 0;
		int contentX = 0;
		int contentY = 0;
		if (targetAlignment != null) {
			if (targetAlignment.isFill()) {
				if (horizontal) {
					contentHeight = height;
				} else {
					contentWidth = width;
				}
			}
			x = insets.left + targetAlignment.alignX(width, contentWidth);
			y = insets.top + targetAlignment.alignY(height, contentHeight);
		}

		// 排列子组件的位置
		if (horizontal) {// 横向排列
			for (Metrics widgetMetrics = first; widgetMetrics != null; widgetMetrics = widgetMetrics.next) {
				AbstractSNSComponent com = widgetMetrics.component;
				int h = widgetMetrics.height;
				contentY = alignment.alignY(contentHeight, h);
				if (alignment.isFill()) {
					h = contentHeight;
				}
				com.setBounds(x, y + contentY, widgetMetrics.width, h);
				x += (widgetMetrics.width + gap.X);
				com = null;
			}
		} else {// 纵向排列
			for (Metrics widgetMetrics = first; widgetMetrics != null; widgetMetrics = widgetMetrics.next) {
				AbstractSNSComponent com = widgetMetrics.component;
				int w = widgetMetrics.width;
				contentX = alignment.alignX(contentWidth, w);
				if (alignment.isFill()) {
					w = contentWidth;
				}
				com.setBounds(x + contentX, y, w, widgetMetrics.height);
				y += widgetMetrics.height + gap.Y;
				com = null;
			}
		}
		targetAlignment = null;
		insets = null;
		minSize = null;
		gap = null;
	}
}