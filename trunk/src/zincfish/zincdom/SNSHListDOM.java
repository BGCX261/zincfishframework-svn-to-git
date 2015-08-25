package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * <code>SNSHListDOM</code>封装了列表的数据。<code>SNSHListDOM</code>是一个List容器
 * 
 * @author Jarod Yv
 */
public class SNSHListDOM extends AbstractSNSDOM {

	/** 获得焦点子组件的索引 */
	public int itemIndex = 0;

	/** 箭头图片的地址 */
	public String arrowSrc = null;

	public SNSHListDOM() {
		this.type = TYPE_LIST;
		this.isVisible = true;
		canFocus = true;
	}

	public String getSubAttributeValue(String name) {
		return null;
	}

	public void setSubAttributeValue(String name, String value) {

	}

	protected void deserializeSpecialAttributes(DataInputStream dis)
			throws IOException {
		// TODO Auto-generated method stub

	}

	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
		// TODO Auto-generated method stub

	}
}
