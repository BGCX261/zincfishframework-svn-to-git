package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import utils.StringUtil;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>SNSTextFieldDOM</code> 封装了从&lt;input&gt;标签中解析出的数据
 * 
 * @author Jarod Yv
 */
public class SNSTextFieldDOM extends SNSLabelDOM {

	/** 输入框接收内容的类型 */
	public int constraintFlag = 0;

	/** 输入框中显示的文字个数 */
	public int size = 0;

	/** 支持的最大文字个数 */
	public int maxSize = 0;

	/** 是否允许空白 */
	public boolean isEmptyOK = false;

	/** 是否多行输入 */
	public boolean isMulti = false;

	public SNSTextFieldDOM() {
		this.type = TYPE_TEXT_FIELD;
		canFocus = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincdom.AbstractSNSDOM#getSubAttributeValue(java.lang.String)
	 */
	public String getSubAttributeValue(String name) {
		if (ZMLTag.TYPE_ATTR.equals(name))
			return String.valueOf(constraintFlag);
		if (ZMLTag.SIZE_ATTR.equals(name))
			return String.valueOf(size);
		if (ZMLTag.MAX_SIZE_ATTR.equals(name))
			return String.valueOf(maxSize);
		if (ZMLTag.EMPTY_OK_ATTR.equals(name))
			return isEmptyOK ? ZMLTag.TRUE_VALUE : ZMLTag.FALSE_VALUE;
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
		if (ZMLTag.TYPE_ATTR.equals(name))
			constraintFlag = StringUtil.Str2Int(value);
		if (ZMLTag.SIZE_ATTR.equals(name))
			size = StringUtil.Str2Int(value);
		if (ZMLTag.MAX_SIZE_ATTR.equals(name))
			maxSize = StringUtil.Str2Int(value);
		if (ZMLTag.EMPTY_OK_ATTR.equals(name))
			isEmptyOK = StringUtil.Str2Bool(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seezincfish.zincdom.AbstractSNSDOM#serializeSpecialAttributes(java.io.
	 * DataOutputStream)
	 */
	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
		dos.writeInt(constraintFlag);
		dos.writeInt(size);
		dos.writeInt(maxSize);
		dos.writeBoolean(isEmptyOK);
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
		constraintFlag = dis.readInt();
		size = dis.readInt();
		maxSize = dis.readInt();
		isEmptyOK = dis.readBoolean();
	}

}
