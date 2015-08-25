package zincfish.zinclayout;

import zincfish.zinccss.model.*;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zinclayout.layoutdata.ILayoutData;
import zincfish.zinclayout.layoutdata.StaticLayoutData;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>StaticLayout</code>定义了静态排版样式
 * 
 * @author Jarod Yv
 */
public class StaticLayout implements ILayout {

	/** 唯一实例 */
	public static final StaticLayout instance = new StaticLayout();

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
		StyleSet styleSet = component.getDom().getStyleSet();
		Insets insets = styleSet.getInsets();
		Metrics minSize = styleSet.getMinSize();
		styleSet = null;
		int width = preferredWidth - insets.left - insets.right;
		int height = component.getHeight() - insets.top - insets.bottom;

		int maxWidth = 0;
		int maxHeight = 0;

		int x = 0;
		int y = 0;
		int widgetWidth = 0;
		int widgetHeight = 0;
		Metrics preferredSize = null;

		for (int i = 0; i < component.getChildrenNum(); i++) {
			AbstractSNSComponent com = ((AbstractSNSDOM) component.getDom().children
					.get(i)).getComponent();
			if (com != null) {
				com.regist2Parent();
				if (com.isVisible() && !com.shouldIgnoreLayout()) {
					x = 0;
					y = 0;
					widgetWidth = 0;
					widgetHeight = 0;
					preferredSize = null;
					// 从子组件中获取静态排版数据，进行排版
					ILayoutData layoutData = com.getDom().getStyleSet()
							.getLayoutData();
					if (layoutData instanceof StaticLayoutData) {
						// 获取排版数据中的参数
						StaticLayoutData staticLayoutData = (StaticLayoutData) layoutData;
						Alignment alignment = staticLayoutData.alignment;
						x = staticLayoutData.x;
						y = staticLayoutData.y;

						// 处理组件宽度
						if (staticLayoutData.width > 1) {
							// 指定宽度则采用像素宽
							widgetWidth = staticLayoutData.width;
						} else if (staticLayoutData.width < 0) {
							// 否则使用最小尺寸
							preferredSize = com.getPreferredSize(width, width);
							widgetWidth = preferredSize.width;
						} else {
							// FIXME 百分比
							widgetWidth = staticLayoutData.width * width / 100;
						}

						// 处理组件高度
						if (staticLayoutData.height > 1) {
							// 指定宽度则采用像素高
							widgetHeight = staticLayoutData.height;
						} else if (staticLayoutData.height < 0) {
							// 否则使用最小尺寸
							if (preferredSize == null) {
								preferredSize = com.getPreferredSize(
										widgetWidth, widgetWidth);
							}
							widgetHeight = preferredSize.height;
						} else {
							// FIXME 百分比
							widgetHeight = staticLayoutData.height * height
									/ 100;
						}
						staticLayoutData = null;

						// 处理对齐方式
						if (alignment != null) {
							if (alignment.isRight()) {
								x += width - widgetWidth;
							} else if (alignment.isHorizontalCenter()) {
								x += (width - widgetWidth) / 2;
							}
							if (alignment.isBottom()) {
								y += height - widgetHeight;
							} else if (alignment.isVerticalCenter()) {
								y += (height - widgetHeight) / 2;
							}
						}
						alignment = null;
					} else {
						preferredSize = com.getPreferredSize(width, width);
						widgetWidth = preferredSize.width;
						widgetHeight = preferredSize.height;
					}

					if (layout) {
						com.setBounds(insets.left + x, insets.top + y,
								widgetWidth, widgetHeight);
					}

					maxWidth = Math.max(maxWidth, widgetWidth);
					maxHeight = Math.max(maxHeight, widgetHeight);
				}
				com = null;
			}
		}

		if (!layout) {
			metrics.width = insets.left + Math.max(minSize.width, maxWidth)
					+ insets.right;
			metrics.height = insets.top + Math.max(minSize.height, maxHeight)
					+ insets.bottom;
		}
		insets = null;
		minSize = null;
	}

}
