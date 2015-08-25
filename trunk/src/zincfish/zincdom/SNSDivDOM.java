package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import zincfish.zinccss.ICSSConstants;

/**
 * <code>SNSDivDOM</code> 代表根据&lt;div&gt;标签解析出来的DOM节点
 * 
 * @author Jarod Yv
 */
public class SNSDivDOM extends AbstractSNSDOM {

	public SNSDivDOM() {
		type = TYPE_DIV;
		canFocus = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincdom.AbstractSNSDOM#getSubAttributeValue(java.lang.String)
	 */
	public String getSubAttributeValue(String name) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincdom.AbstractSNSDOM#setSubAttributeValue(java.lang.String,
	 * java.lang.String)
	 */
	public void setSubAttributeValue(String name, String value) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincdom.AbstractSNSDOM#deserializeSpecialAttributes(java.io.
	 * DataInputStream)
	 */
	protected void deserializeSpecialAttributes(DataInputStream dis)
			throws IOException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seezincfish.zincdom.AbstractSNSDOM#serializeSpecialAttributes(java.io.
	 * DataOutputStream)
	 */
	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
	}

	public Object getDefaultStylePropertyValue(String name) {
		if (ICSSConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_LAYOUT;
		}
		return super.getDefaultStylePropertyValue(name);
	}
}
