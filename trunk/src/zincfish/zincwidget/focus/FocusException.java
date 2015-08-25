package zincfish.zincwidget.focus;

import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <code>FocusException</code> 用于在遍历View
 * Tree时，当找到下一个获得焦点的组件时抛出，从而中断整个遍历过程，并把获得焦点组件的指针传递出来。
 * 
 * @author Jarod Yv
 */
public class FocusException extends Exception {
	/** 下一个获得焦点的组件 */
	private AbstractSNSComponent focusComponent = null;

	/**
	 * 构造函数
	 * 
	 * @param component
	 *            要传递出来的组件的引用
	 */
	public FocusException(AbstractSNSComponent component) {
		this.focusComponent = component;
	}

	/**
	 * 获取获得焦点的组件
	 * 
	 * @return {@link #focusComponent}
	 */
	public AbstractSNSComponent getFocusComponent() {
		return focusComponent;
	}
}
