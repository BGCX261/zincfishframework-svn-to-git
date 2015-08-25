package zincfish.zinclayout;

import zincfish.zinccss.model.Alignment;
import zincfish.zinccss.model.Coordinates;
import zincfish.zinccss.model.Insets;
import zincfish.zinccss.model.Metrics;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>FormLayout</code> 定义了表单排版样式
 * 
 * @author Jarod Yv
 */
public class FormLayout implements ILayout {

	private Alignment alignment = null;// 对齐方式

	public FormLayout() {
		alignment = Alignment.TOP_LEFT;
	}

	public FormLayout(Alignment alignment) {
		this.alignment = alignment;
	}

	public void doLayout(AbstractSNSComponent component) {
		measure(component, true, component.getViewWidth(), null);
	}

	public void measurePreferredSize(AbstractSNSComponent component,
			int preferredWidth, Metrics metrics) {
		measure(component, false, preferredWidth, metrics);
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

		int width = preferredWidth - insets.left - insets.right;// 可用于子组件排版的宽度
		int height = component.getHeight() - insets.top - insets.bottom;// 可用于子组件排版的高度
		int contentWidth = 0;// 排版过程中的宽度
		int contentHeight = 0;// 排版过程中的高度
		Metrics first = null;// 记录第一个组件的尺寸
		Metrics current = null;// 记录当前组件的尺寸
		int leftWidth = width;

		// 处理子组件
		for (int i = 0; i < component.getChildrenNum(); i++) {
			AbstractSNSComponent com = component.getChildComponent(i);
			if (com != null) {
				com.regist2Parent();
				// 累加子组件的尺寸
				if (com.isVisible() && !com.shouldIgnoreLayout()) {
					leftWidth = width - contentWidth;
					Metrics preferredSize = com.getPreferredSize(leftWidth,
							leftWidth);
					contentWidth += preferredSize.width
							+ (first != null ? gap.X : 0);
					contentHeight = Math.max(preferredSize.height,
							contentHeight);

					if (first == null) {
						first = current = preferredSize;
					} else {
						current.next = preferredSize;
						preferredSize.prev = current;
						current = preferredSize;
					}

					preferredSize = null;
				}
				com = null;
			}
		}
		current.width = leftWidth;

		// 如果不是排版，直接更新组件的尺寸即可
		if (!layout) {
			metrics.width = insets.left + Math.max(minSize.width, width)
					+ insets.right;
			metrics.height = insets.top
					+ Math.max(minSize.height, contentHeight) + insets.bottom;
			return;
		}

		// 调整组件的位置
		int x = 0;
		int y = 0;
		// int contentX = 0;
		int contentY = 0;
		if (targetAlignment != null) {
			if (targetAlignment.isFill()) {
				contentHeight = height;
			}
			x = insets.left + targetAlignment.alignX(width, contentWidth);
			y = insets.top + targetAlignment.alignY(height, contentHeight);
		}
		Alignment floatAlignment = styleSet.getFloat();
		// 排列子组件的位置(横向排列)
		for (Metrics widgetMetrics = (floatAlignment == Alignment.RIGHT ? current
				: first); widgetMetrics != null; widgetMetrics = (floatAlignment == Alignment.RIGHT ? widgetMetrics.prev
				: widgetMetrics.next)) {
			AbstractSNSComponent com = widgetMetrics.component;
			int h = widgetMetrics.height;
			if (alignment.isFill()) {
				h = metrics.height;
			}
			contentY = alignment.alignY(
					Math.max(minSize.height, contentHeight), h);
			com.setBounds(x, y + contentY, widgetMetrics.width, h);
			x += (widgetMetrics.width + gap.X);
			com = null;
		}
		floatAlignment = null;
		targetAlignment = null;
		insets = null;
		minSize = null;
		gap = null;
		styleSet = null;
	}
}
