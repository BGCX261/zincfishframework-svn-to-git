package zincfish.zinclayout;

import zincfish.zinccss.model.Metrics;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>ILayout</code> 定义了排版样式的接口
 * 
 * @author Jarod Yv
 * @since fingerling
 */
public interface ILayout {

	/**
	 * 计算组件的最佳尺寸
	 * 
	 * @param component
	 *            目标组件
	 * @param preferredWidth
	 *            最佳宽度
	 * @param metrics
	 *            计算得到的最佳尺寸
	 */
	public void measurePreferredSize(AbstractSNSComponent component,
			int preferredWidth, Metrics metrics);

	/**
	 * 对目标组件进行排版
	 * 
	 * @param component
	 *            目标组件
	 */
	public void doLayout(AbstractSNSComponent component);

}
