package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import utils.StringUtil;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>SNSCheckBoxDOM</code>封装了&lt;checkbox&gt;的数据
 * 
 * @author Jarod Yv
 */
public class SNSCheckBoxDOM extends SNSLabelDOM {
	/** 标示是否被选中 */
	public boolean isSelected = false;

	public SNSCheckBoxDOM() {
		this.type = TYPE_CHECKBOX;
		canFocus = true;
	}

	public String getSubAttributeValue(String name) {
		if (ZMLTag.SELECTED_ATTR.equals(name))
			return isSelected ? ZMLTag.TRUE_VALUE : ZMLTag.FALSE_VALUE;
		return super.getSubAttributeValue(name);
	}

	public void setSubAttributeValue(String name, String value) {
		if (ZMLTag.SELECTED_ATTR.equals(name))
			isSelected = StringUtil.Str2Bool(value);
		else
			super.setSubAttributeValue(name, value);
	}

	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
		super.serializeSpecialAttributes(dos);
		dos.writeBoolean(isSelected);
	}

	protected void deserializeSpecialAttributes(DataInputStream dis)
			throws IOException {
		super.deserializeSpecialAttributes(dis);
		isSelected = dis.readBoolean();

	}

}
