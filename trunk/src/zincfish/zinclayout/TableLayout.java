package zincfish.zinclayout;

import zincfish.zinccss.model.*;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>TableLayout</code>定义了表格排版样式
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public class TableLayout implements ILayout {

	/** 唯一实例 */
	public static final TableLayout instance = new TableLayout();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zinclayout.ILayout#measurePreferredSize(zincfish.zincwidget.
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
	 * zincfish.zinclayout.ILayout#doLayout(zincfish.zincwidget.AbstractSNSComponent
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
		Alignment targetAlignment = styleSet.getAlign();
		Insets insets = styleSet.getInsets();
		Metrics minSize = styleSet.getMinSize();
		Coordinates gap = styleSet.getGap();
		styleSet = null;
		int width = preferredWidth - insets.left - insets.right;
		int height = component.getHeight() - insets.top - insets.bottom;
		int[] colHeights = null;
		int col = 0;
		int row = 0;
		int ncol = 0;
		int nrow = 0;
		Metrics first = null;
		Metrics current = null;

		for (int i = 0; i < component.getChildrenNum(); i++) {
			AbstractSNSComponent com = ((AbstractSNSDOM) component.getDom().children
					.get(i)).getComponent();
			if (com != null) {
				com.regist2Parent();
				if (com.isVisible() && !com.shouldIgnoreLayout()) {
					if (com.getDom().brNum > 0) {
						if (col != 0) {
							col = 0;
							row++;
						}
						continue;
					}

					Metrics widgetMetrics = com.getPreferredSize(width, width);
					if (first == null) {
						first = current = widgetMetrics;
					} else {
						current.next = widgetMetrics;
						current = widgetMetrics;
					}

					Coordinates span = com.getDom().getStyleSet().getSpan();
					int colspan = span.X;
					int rowspan = span.Y;
					if (colHeights != null)
						for (int j = 0; j < colspan; j++) {
							if (colHeights[col + j] > row) {
								col += (j + 1);
								j = -1;
							}
						}

					widgetMetrics.x = col;
					widgetMetrics.y = row;
					ncol = Math.max(ncol, col + colspan);
					nrow = Math.max(nrow, row + rowspan);

					if (rowspan > 1) {
						if (colHeights == null) {
							colHeights = new int[ncol];
						} else if (colHeights.length < ncol) {
							int[] newheights = new int[ncol];
							System.arraycopy(colHeights, 0, newheights, 0,
									colHeights.length);
							colHeights = newheights;
						}
						for (int j = 0; j < colspan; j++) {
							colHeights[col + j] = row + rowspan;
						}
					}

					col += colspan;
				}
				com = null;
			}
		}

		// Compute weights for each col an row
		int[] colWeights = new int[ncol];
		int[] rowWeights = new int[nrow];
		align(first, colWeights, null, true, 0);
		align(first, rowWeights, null, false, 0);

		// Compute sizes for each col an row
		int[] colWidths = new int[ncol];
		int[] rowHeights = new int[nrow];
		align(first, colWidths, colWeights, true, width - gap.X * (ncol - 1));
		align(first, rowHeights, rowWeights, false, height - gap.Y * (nrow - 1));

		// Compute content size
		int contentWidth = sum(colWidths, 0, ncol, gap.X);
		int contentHeight = sum(rowHeights, 0, nrow, gap.Y);

		if (!layout) {
			metrics.width = insets.left + Math.max(minSize.width, contentWidth)
					+ insets.right;
			metrics.height = insets.top
					+ Math.max(minSize.height, contentHeight) + insets.bottom;
			return;
		}

		// Compute the content box origine
		int contentX = 0;
		int contentY = 0;
		if (targetAlignment != null) {
			contentX = targetAlignment.alignX(width, contentWidth);
			contentY = targetAlignment.alignY(height, contentHeight);
		}
		contentX += insets.left;
		contentY += insets.top;

		// Arrange each child widget
		for (Metrics widgetMetrics = first; widgetMetrics != null; widgetMetrics = widgetMetrics.next) {
			AbstractSNSComponent com = widgetMetrics.component;
			Coordinates widgetSpan = com.getDom().getStyleSet().getSpan();
			int x = contentX + sum(colWidths, 0, widgetMetrics.x, gap.X)
					+ ((widgetMetrics.x > 0) ? gap.Y : 0);
			int y = contentY + sum(rowHeights, 0, widgetMetrics.y, gap.Y)
					+ ((widgetMetrics.y > 0) ? gap.Y : 0);
			int widgetWidth = sum(colWidths, widgetMetrics.x, widgetSpan.X,
					gap.X);
			int widgetHeight = sum(rowHeights, widgetMetrics.y, widgetSpan.Y,
					gap.Y);
			widgetSpan = null;
			height = widgetMetrics.height;
			com.setBounds(x, y, widgetWidth, widgetHeight);
			com = null;
		}

	}

	private final void align(Metrics first, int[] values, int[] weights,
			boolean horizontal, int fullSize) {
		for (int size = 1, next = 0; size != 0; size = next, next = 0) {
			for (Metrics metrics = first; metrics != null; metrics = metrics.next) {
				Coordinates span = metrics.component.getDom().getStyleSet()
						.getSpan();
				int orientedSpan = horizontal ? span.X : span.Y;
				if (orientedSpan == size) {
					int value;
					if (weights != null) {
						value = horizontal ? metrics.width : metrics.height;
					} else {
						Coordinates weight = metrics.component.getDom()
								.getStyleSet().getWeight();
						value = horizontal ? weight.X : weight.Y;
					}

					int index = horizontal ? metrics.x : metrics.y;
					if (weights != null && weights[index] != 0) {
						value = weights[index] * fullSize / 100;
					}
					values[index] = Math.max(values[index], value);

				} else if ((orientedSpan > size)
						&& ((next == 0) || (next > orientedSpan))) {
					next = orientedSpan;
				}
			}
		}
	}

	private final int sum(int[] values, int from, int length, int gap) {
		int sum = 0;
		for (int i = 0; i < length; i++) {
			sum += values[from + i];
		}
		if (length > 1) {
			sum += (length - 1) * gap;
		}
		return sum;
	}

}
