package zincfish.zinclayout;

import zincfish.zinccss.model.*;
import zincfish.zinccss.style.StyleSet;
import zincfish.zinclayout.layoutdata.GridLayoutData;
import zincfish.zinclayout.layoutdata.ILayoutData;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>GridLayout</code>定义了单元格排版样式
 * 
 * @author Jarod Yv
 */
public class GridLayout implements ILayout {

	// public static final GridLayout instanceOneByOne = new GridLayout();

	/* 单元格的列数 */
	private int numCols;
	/* 单元格的行数 */
	private int numRows;

	/**
	 * 构造函数
	 */
	public GridLayout() {
		this(1, 1);
	}

	/**
	 * 构造函数
	 * 
	 * @param numCols
	 *            列数
	 * @param numRows
	 *            行数
	 */
	public GridLayout(int numCols, int numRows) {
		this.numCols = numCols;
		this.numRows = numRows;
	}

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
		Insets insets = styleSet.getInsets();
		// Metrics minSize = styleSet.getMinSize();
		Coordinates gap = styleSet.getGap();
		int[] splits = null;
		ILayoutData layoutData = styleSet.getLayoutData();// 获取排版数据
		if (layoutData != null && layoutData instanceof GridLayoutData) {
			splits = ((GridLayoutData) layoutData).getSplits();
			layoutData = null;
		}
		styleSet = null;
		int gapWidth = gap.X * (numCols - 1);// 横向间距之和
		// int gapHeight = gap.Y * (numRows - 1);// 纵向间距之和
		int width = preferredWidth - insets.left - insets.right;
		// int height = component.getHeight() - insets.top - insets.bottom;
		int[] eachGridWidth = new int[numCols];// 记录各列的宽度
		int[] eachGridHeight = new int[numRows];// 记录各行的高度
		// 计算单元格宽度
		if (splits != null) {
			int total = 0;
			int splitWidth = width - gapWidth;
			for (int i = 0; i < numCols; i++) {
				total += splits[i];
				if (splits[i] == 0) {
					AbstractSNSComponent com = component.getChildComponent(i);
					Metrics size = com.getPreferredSize(width, width);
					eachGridWidth[i] = size.width;
					splitWidth -= size.width;
					size = null;
					com = null;
				}
			}
			for (int i = 0; i < numCols; i++) {
				if (eachGridWidth[i] == 0)
					eachGridWidth[i] = splitWidth * splits[i] / total;
			}
			splits = null;
		} else {
			for (int i = 0; i < numCols; i++)
				eachGridWidth[i] = (width - gapWidth) / numCols;
		}
		// 计算单元格高度
		int col = 0;
		int row = 0;
		for (; row < numRows; row++) {
			int rowHeight = 0;
			for (; col < numCols; col++) {
				int index = row * numCols + col;
				if (index < component.getChildrenNum()) {
					AbstractSNSComponent com = component
							.getChildComponent(index);
					Metrics size = com.getPreferredSize(eachGridWidth[col],
							eachGridWidth[col]);
					rowHeight = Math.max(size.height, rowHeight);
					size = null;
					com = null;
				} else {
					break;
				}
			}
			col = 0;
			eachGridHeight[row] = rowHeight + gap.Y;
		}
		row = 0;
		int preferredContentWidth = 0;
		int preferredContentHeight = 0;

		for (int i = 0; i < component.getChildrenNum(); i++) {
			AbstractSNSComponent com = component.getChildComponent(i);
			if (com != null) {
				com.regist2Parent();
				if (com.isVisible() && !com.shouldIgnoreLayout()) {
					if (layout) {// 需要重排子组件
						int x = insets.left;
						int y = insets.top;
						Metrics size = com.getCachedMetrics();
						Alignment align = com.getDom().getStyleSet().getAlign();
						if (align.isFill()) {
							size.width = eachGridWidth[col];
							size.height = eachGridHeight[row];
						}
						for (int j = 0; j < col; j++)
							x += (eachGridWidth[j] + gap.X);
						x += align.alignX(eachGridWidth[col], size.width);
						for (int j = 0; j < row; j++)
							y += (eachGridHeight[j] + gap.Y);
						y += align.alignY(eachGridHeight[row], size.height);
						align = null;
						com.setBounds(x, y, size.width, size.height);
						size = null;
					} else {// 不需要重排则只需更新尺寸
						preferredContentWidth = Math.max(preferredContentWidth,
								eachGridWidth[col]);
						preferredContentHeight = Math.max(
								preferredContentHeight, eachGridHeight[row]);
					}
					col++;
					if (col >= numCols) {
						col = 0;
						row++;
						if (row >= numRows) {
							break;
						}
					}
				}
				com = null;
			}
		}

		if (!layout) {
			metrics.width = width;
			metrics.height = insets.top + insets.bottom;
			for (int i = 0; i < numRows; i++)
				metrics.height += eachGridHeight[i];
		}
		insets = null;
		// minSize = null;
		gap = null;
	}
}
