package utils;

import screen.BrowserScreen;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincparser.zmlparser.ZMLTag;
import zincfish.zincwidget.AbstractSNSComponent;
import zincfish.zincwidget.ComponentFactory;

/**
 * <code>DOMUtil</code> 是一个工具类，用于对DOM Tree进行操作。
 * 
 * @author Jarod Yv
 */
public final class DOMUtil {

	/**
	 * 在目标节点下添加子节点
	 * 
	 * @param target
	 *            目标节点
	 * @param subTree
	 *            要添加的子节点
	 * @since fingerling
	 */
	public static final void addSubtree(AbstractSNSDOM target,
			AbstractSNSDOM subTree) {
		if (target == null || subTree == null)
			return;
		if (target.children == null) {
			target.children = new ArrayList(10);
		}
		target.children.add(subTree);
		subTree.father = target;
	}

	/**
	 * 替换目标节点下的某个子节点
	 * 
	 * @param target
	 *            目标节点
	 * @param srcTree
	 *            被替换的节点
	 * @param subTree
	 *            替换的节点
	 */
	public static final void setSubtree(AbstractSNSDOM target,
			AbstractSNSDOM srcTree, AbstractSNSDOM subTree) {
		if (target == null || subTree == null || srcTree == null)
			return;
		int index = getIndex(target, srcTree);
		if (index >= 0)
			target.children.set(index, subTree);
	}

	/**
	 * 获取目标节点下指定节点的索引
	 * 
	 * @param target
	 *            目标节点
	 * @param subTree
	 *            子节点
	 * @return 子节点在子节点队列中的索引。如果不存在则返回-1。
	 * @since fingerling
	 */
	public static final int getIndex(AbstractSNSDOM target,
			AbstractSNSDOM subTree) {
		if (target == null || subTree == null || target.children == null)
			return -1;
		return target.children.indexOf(subTree);
	}

	/**
	 * 替换目标树下的子树。本方法不单单是替换当前节点的子节点，而是会遍历整棵树，找到id相同的子树，完成替换。
	 * 
	 * @param target
	 *            遍历的根节点
	 * @param subTree
	 *            用于替换的子树
	 * @since fingerling
	 */
	public static final boolean replaceSubtree(AbstractSNSDOM target,
			AbstractSNSDOM subTree) {

		if (target == null || subTree == null)
			return false;
		boolean exist = false;
		if (target.children != null) {
			for (int i = 0; i < target.children.size(); i++) {
				AbstractSNSDOM child = (AbstractSNSDOM) target.children.get(i);
				// System.out.println("target:"+target.id+"\tchild:"+child+"\tsubTree:"+subTree);
				// 找到,替换
				if (child.id != null && child.id.equals(subTree.id)) { // 有一些标签没有ID
					subTree.father = child.father;
					setSubtree(target, child, subTree);
					// child.release(); 安全隐患NullPointerException
					child = null;
					// exist = true; // 证明存在这样的节点
					return true;
				} else {
					exist = replaceSubtree(child, subTree);
					if (exist) { // 证明存在这样的节点 不用再遍历下面的兄弟节点
						return true;
					}
				}
				child = null;
			}
		}
		return false;
	}

	public static final AbstractSNSDOM getDOMByID(AbstractSNSDOM root, String id) {
		if (root == null || id == null || id.equals(ZMLTag.NONE_VALUE))
			return null;
		if (id.equals(root.id))
			return root;
		if (root.hasChildren()) {
			for (int i = 0; i < root.children.size(); i++) {
				AbstractSNSDOM child = (AbstractSNSDOM) root.children.get(i);
				child = getDOMByID(child, id);
				if (child != null)
					return child;
			}
		}
		return null;
	}

	/**
	 * 将DOM Tree转换成View Tree
	 * 
	 * @param rootDOM
	 *            DOM Tree的根节点
	 * @return 转换后的View Tree
	 */
	public static final AbstractSNSComponent DOMTree2ViewTree(
			AbstractSNSDOM rootDOM) {
		// 隐藏的组件我们暂时不生成View Tree节点
		if (rootDOM == null || !rootDOM.isVisible)
			return null;

		AbstractSNSComponent rootComponent = rootDOM.getComponent();
		if (rootComponent == null) {// 如果该DOM节点没有生成相应的View节点，则创建View节点
			rootComponent = ComponentFactory.createComponent(rootDOM);
			rootComponent.init(rootDOM);
			rootDOM.setComponent(rootComponent);
		}
		// 递归地处理子节点
		if (rootDOM.hasChildren()) {
			rootComponent.removeAllComponents();// 首先将所有的子组件从Panel上删除掉，后面我们会加入进来
			for (int i = 0; i < rootDOM.children.size(); i++) {
				AbstractSNSDOM dom = (AbstractSNSDOM) rootDOM.children.get(i);
				AbstractSNSComponent component = DOMTree2ViewTree(dom);
				if (component != null) {
					rootComponent.addComponent(component);
					// if (component.isFocused()) {
					// rootComponent.setIndex(rootComponent
					// .getComponentCount() - 1);
					// }
				}
				component = null;
				dom = null;
			}
		}
		return rootComponent;
	}

	public static final String getNetString(String id) {
		AbstractSNSDOM dom = getDOMByID(BrowserScreen.getInstance().getBuffer()
				.getCurrentBuffer(), id);
		String netString = getNetStringImpl(dom);
		if (netString != null)
			netString = netString.substring(0, netString.length() - 1);
		dom = null;
		return netString;
	}

	private static final String getNetStringImpl(AbstractSNSDOM dom) {
		if (dom != null) {
			StringBuffer stringBuffer = new StringBuffer();
			String netString = dom.getNetString();
			if (netString != null) {
				stringBuffer.append(netString);
				stringBuffer.append('&');
			}
			netString = null;
			if (dom.hasChildren()) {
				for (int i = 0; i < dom.children.size(); i++) {
					AbstractSNSDOM child = (AbstractSNSDOM) dom.children.get(i);
					netString = getNetStringImpl(child);
					if (netString != null) {
						stringBuffer.append(netString);
					}
					netString = null;
					child = null;
				}
			}
			return stringBuffer.toString();
		}
		return null;
	}

	/**
	 * 设置所有组件排版失效, 需要对其重新排版
	 * 
	 * @param rootdom
	 *            DOM Tree的根节点
	 */
	public static final void invalidateAlldom(AbstractSNSDOM rootdom) {
		if (rootdom == null) {
			return;
		}
		rootdom.setInvalidated(true);
		if (rootdom.hasChildren()) {
			for (int i = 0; i < rootdom.children.size(); i++) {
				AbstractSNSDOM chdom = (AbstractSNSDOM) rootdom.children.get(i);
				invalidateAlldom(chdom);
				chdom = null;
			}
		}
	}
}
