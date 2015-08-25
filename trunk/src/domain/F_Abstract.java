/**
 * License  3G门户版权所有 2008-2009
 */
package domain;

import utils.ArrayList;

/**
 * @作者 江威
 * @Email weijiang8410@163.com/jiang-wei@3g.net.cn
 */
public abstract class F_Abstract {

	/** 子节点 */
	public ArrayList children = null;
	/** DOM对应的标签名 */
	public String tagName = null;
	/** 父节点 */
	public F_Abstract father = null;
	
	public void addChild(F_Abstract child) {
		if(children == null) {
			children = new ArrayList();
		}
		children.add(child);
	}
}
