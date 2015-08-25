package data;

/**
 * <code>IDOMChangeListener</code> 接口定义了DOM Tree发生变化时的处理接口。当DOM
 * Tree发生变化时，会回调监听器的{@link #updateView()}接口，完成对UI的更新。
 * 
 * @author Jarod Yv
 */
public interface IDOMChangeListener {

	/**
	 * 根据Dom Tree更新View Tree
	 * 
	 * @param isNext
	 *            表示是切换到前一页还是后一页
	 *            <ul>
	 *            <li><code>true</code> - 却换到后一页
	 *            <li><code>false</code> - 切换到前一页
	 *            </ul>
	 */
	public void updateView(boolean isNext);
}
