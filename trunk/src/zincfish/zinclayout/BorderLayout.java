package zincfish.zinclayout;

import zincfish.zinccss.model.*;
import zincfish.zinccss.style.StyleSet;
import zincfish.zinclayout.layoutdata.BorderLayoutData;
import zincfish.zinclayout.layoutdata.ILayoutData;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>BorderLayout</code> 定义了边界排版样式<br>
 * 采用BorderLayout排版的组件会锚定在"north", "south", "east", "west"和"center"5个方位
 * 
 * @author Jarod Yv
 */
public class BorderLayout implements ILayout {

	/** 排版样式的唯一实例 */
	public static final BorderLayout instance = new BorderLayout();

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
		Insets insets = styleSet.getInsets();// 获取组件的留白
		Metrics minSize = styleSet.getMinSize();// 获取组件的最小尺寸
		Coordinates gap = styleSet.getGap();// 获取组件间的间距
		styleSet = null;
		int width = preferredWidth - insets.left - insets.right;// 组件实际宽度
		int height = component.getHeight() - insets.top - insets.bottom;// 组件实际高度
		int top = 0;
		int left = 0;
		int right = 0;
		int bottom = 0;
		int centerHeight = 0;
		int centerWidth = 0;

		// 预设5个方位的组件
		AbstractSNSComponent northWidget = null;
		AbstractSNSComponent westWidget = null;
		AbstractSNSComponent eastWidget = null;
		AbstractSNSComponent southWidget = null;
		AbstractSNSComponent centerWidget = null;

		// 根据borderlayoutdata，分配子组件到5个方位
		for (int i = 0; i < component.getChildrenNum(); i++) {
			AbstractSNSComponent com = component.getChildComponent(i);
			if (com != null) {
				com.regist2Parent();
				if (com.isVisible() && !com.shouldIgnoreLayout()) {
					ILayoutData layoutData = com.getDom().getStyleSet()
							.getLayoutData();// 获取子组件的排版数据
					if (layoutData != null
							&& layoutData instanceof BorderLayoutData) {// borderlayout下只有borderlayoutdata有效
						byte position = ((BorderLayoutData) layoutData).position;
						layoutData = null;
						switch (position) {// 将子组件分配到5个方位
						case BorderLayoutData.NORTH:
							northWidget = com;
							break;
						case BorderLayoutData.WEST:
							westWidget = com;
							break;
						case BorderLayoutData.EAST:
							eastWidget = com;
							break;
						case BorderLayoutData.SOUTH:
							southWidget = com;
							break;
						default:
							centerWidget = com;
							break;
						}
					} else if (centerWidget == null) {// 默认居中
						centerWidget = com;
					}
				}
				com = null;
			}
		}

		// 计算组件间间距
		int verticalTopGap = (northWidget != null && (westWidget != null
				|| centerWidget != null || eastWidget != null || southWidget != null)) ? gap.Y
				: 0;
		int verticalBottompGap = (southWidget != null && (westWidget != null
				|| centerWidget != null || eastWidget != null)) ? gap.Y : 0;
		int horizontalLeftGap = (westWidget != null && (centerWidget != null || eastWidget != null)) ? gap.X
				: 0;
		int horizontalRightGap = (eastWidget != null && (centerWidget != null || westWidget != null)) ? gap.X
				: 0;

		int horizontalGap = horizontalLeftGap + horizontalRightGap;
		int verticalGap = verticalTopGap + verticalBottompGap;

		// North
		if (northWidget != null) {
			Metrics preferredSize = northWidget.getPreferredSize(width, width);
			centerWidth = preferredSize.width;
			top = preferredSize.height;
			preferredSize = null;
		}

		// West
		if (westWidget != null) {
			Metrics preferredSize = westWidget.getPreferredSize(width
					- horizontalGap, width - horizontalGap);
			left = preferredSize.width;
			centerHeight = preferredSize.height;
			preferredSize = null;
		}

		// East
		if (eastWidget != null) {
			Metrics preferredSize = eastWidget.getPreferredSize(width - left
					- horizontalGap, width - left - horizontalGap);
			right = preferredSize.width;
			centerHeight = Math.max(centerHeight, preferredSize.height);
			preferredSize = null;
		}

		// South
		if (southWidget != null) {
			Metrics preferredSize = southWidget.getPreferredSize(width, width);
			centerWidth = Math.max(centerWidth, preferredSize.width);
			bottom = preferredSize.height;
			preferredSize = null;
		}

		// Center
		if (centerWidget != null) {
			Metrics preferredSize = centerWidget.getPreferredSize(width - left
					- right - horizontalGap, width - left - right
					- horizontalGap);
			centerWidth = Math.max(centerWidth, preferredSize.width);
			centerHeight = Math.max(centerHeight, preferredSize.height);
			preferredSize = null;
		}

		// 如果不需要排版子组件，直接更新组件宽度高度就OK
		if (!layout) {
			metrics.width = insets.left
					+ Math.max(minSize.width, left + centerWidth + right
							+ horizontalGap) + insets.right;
			metrics.height = insets.top
					+ Math.max(minSize.height, top + centerHeight + bottom
							+ verticalGap) + insets.bottom;
			return;
		}

		centerWidth = width - left - right - horizontalGap;
		centerHeight = height - top - bottom - verticalGap;

		// 如果需要对子组件进行排版，依次对5个方位的子组件进行排版
		// Center
		if (centerWidget != null) {
			centerWidget.setBounds(insets.left + left + horizontalLeftGap,
					insets.top + top + verticalTopGap, centerWidth,
					centerHeight);
		}

		// North
		if (northWidget != null) {
			northWidget.setBounds(insets.left, insets.top, width, top);
		}

		// West
		if (westWidget != null) {
			westWidget.setBounds(insets.left,
					insets.top + top + verticalTopGap, left, centerHeight);
		}

		// East
		if (eastWidget != null) {
			eastWidget.setBounds(insets.left + width - right, insets.top + top
					+ verticalTopGap, right, centerHeight);
		}

		// South
		if (southWidget != null) {
			southWidget.setBounds(insets.left, insets.top + height - bottom,
					width, bottom);
		}
		insets = null;
		minSize = null;
		gap = null;
	}

}
