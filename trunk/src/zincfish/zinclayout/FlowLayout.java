package zincfish.zinclayout;

import zincfish.zinccss.model.*;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>FlowLayout</code> 定义了流式排版样式。
 * 
 * @author Jarod Yv
 */
public class FlowLayout implements ILayout {

	/**
	 * <code>LineInfo</code>类用于保存行信息
	 * 
	 * @author Jarod Yv
	 */
	private class LineInfo {
		public LineInfo next;
		public Metrics firstMetrics;
		private Metrics lastMetrics;
		public int width;
		public int height;

		public void addMetrics(Metrics metrics) {
			if (firstMetrics == null) {
				firstMetrics = lastMetrics = metrics;
			} else {
				lastMetrics.next = metrics;
				lastMetrics = metrics;
			}
		}

	}

	private Alignment alignment = null;// 对齐方式

	/**
	 * 构造函数
	 */
	public FlowLayout() {
		this(Alignment.TOP_LEFT);
	}

	/**
	 * 构造函数
	 * 
	 * @param alignment
	 *            对齐方式
	 */
	public FlowLayout(Alignment alignment) {
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
	 * 
	 * @param layout
	 * 
	 * @param preferredWidth
	 * 
	 * @param metrics
	 */
	private void measure(AbstractSNSComponent component, boolean layout,
			int preferredWidth, Metrics metrics) {
		StyleSet styleSet = component.getDom().getStyleSet();
		Alignment targetAlignment = styleSet.getAlign();// 组件的对齐方式
		Insets insets = styleSet.getInsets();// 组件的留白
		Metrics minSize = styleSet.getMinSize();// 组件的最小尺寸
		Coordinates gap = styleSet.getGap();// 组件间距
		styleSet = null;
		int width = preferredWidth - insets.left - insets.right;// 实际用于排版的宽度
		int height = component.getHeight() - insets.top - insets.bottom;// 实际用于排版的高度
		int contentWidth = 0;
		int contentHeight = 0;

		LineInfo firstLine = new LineInfo();
		LineInfo currentLine = firstLine;
		boolean isBreak = false;
		for (int i = 0; i < component.getChildrenNum(); i++) {
			AbstractSNSComponent com = component.getChildComponent(i);
			if (com != null) {
				com.regist2Parent();
				if (com.isVisible() && !com.shouldIgnoreLayout()) {
					Metrics preferredSize = null;
					if (isBreak) {
						preferredSize = com.getPreferredSize(width, width);
					} else {
						preferredSize = com.getPreferredSize(width
								- currentLine.width, width);
					}

					if (isBreak
							|| currentLine.width
									+ (currentLine.width == 0 ? 0 : gap.X)
									+ preferredSize.firstLineWidth > width) {// 如果需要折行
						// 计算内容的尺寸
						contentWidth = Math
								.max(contentWidth, currentLine.width);
						contentHeight += currentLine.height
								+ (contentHeight == 0 ? 0 : gap.Y)
								+ (com.getDom().brNum > 0 ? (com.getDom().brNum - 1) * 12
										: 0);
						// 创建新的一行
						currentLine.next = new LineInfo();
						currentLine = currentLine.next;
						currentLine.width = preferredSize.width;
						currentLine.height = preferredSize.height;
					} else {// 不需要折行
						if (preferredSize.nextLineWidth > 0) {
							currentLine.width = preferredSize.width;
							currentLine.height += preferredSize.height
									- preferredSize.lastLineHeight;
						} else {
							currentLine.width += (currentLine.width == 0 ? 0
									: gap.X)
									+ preferredSize.width;
							currentLine.height = Math.max(currentLine.height,
									preferredSize.height);
						}
					}
					// 非换行则将组件的尺寸加入到行中
					currentLine.addMetrics(preferredSize);
					isBreak = com.getDom().brNum > 0;
					currentLine.height += com.getDom().brNum > 1 ? (com
							.getDom().brNum - 1) * 12 : 0;
				}
				com = null;
			}
		}

		if (currentLine.width != 0) {
			contentWidth = Math.max(contentWidth, currentLine.width);
			contentHeight += currentLine.height
					+ (contentHeight == 0 ? 0 : gap.Y);
		}

		if (!layout) {
			metrics.width = insets.left + Math.max(minSize.width, contentWidth)
					+ insets.right;
			metrics.height = insets.top
					+ Math.max(minSize.height, contentHeight) + insets.bottom;
			return;
		}

		int x = 0;
		int y = 0;
		int contentX = 0;
		int contentY = 0;

		if (targetAlignment != null) {
			x = insets.left + targetAlignment.alignX(width, contentWidth);
			y = insets.top + targetAlignment.alignY(height, contentHeight);
		}
		int brNum = 0;
		for (LineInfo line = firstLine; line != null; line = line.next) {
			if (targetAlignment != null) {
				contentX = targetAlignment.alignX(contentWidth, line.width);
			}
			for (Metrics widgetMetrics = line.firstMetrics; widgetMetrics != null; widgetMetrics = widgetMetrics.next) {
				AbstractSNSComponent com = widgetMetrics.component;
				int h = widgetMetrics.height;
				contentY += alignment.alignY(line.height, h) + brNum * 12;
				if (alignment.isFill()) {
					h = line.height;
				}
				com.setBounds(x + contentX, y + contentY, widgetMetrics.width,
						h);
				if (widgetMetrics.nextLineWidth > 0)
					contentX = 0;
				contentX += widgetMetrics.width + gap.X;
				brNum = com.getDom().brNum;
				if (brNum > 0)
					brNum = brNum - 1;
				else
					contentY = widgetMetrics.height
							- widgetMetrics.lastLineHeight;
				com = null;
			}
			y += line.height + gap.Y;
		}
		targetAlignment = null;
		insets = null;
		minSize = null;
		gap = null;
		firstLine = null;
		currentLine = null;
	}
}
