package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import utils.StringUtil;
import zincfish.zinccss.ICSSConstants;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>LabelDOM</code> 封装了&lt;lable&gt;标签的数据。<br>
 * &lt;lable&gt;标签定义了页面上的一段文字。
 * 
 * @author Jarod Yv
 */
public class SNSLabelDOM extends AbstractSNSDOM {

	/** 不做任何修饰处理的显示 */
	public static final byte WRAP_PLAIN = 0x00;
	/** 表示文字过长时滚动显示 */
	public static final byte WRAP_SCROLL = 0x01;
	/** 表示文字过长时省略显示 */
	public static final byte WRAP_OMIT = 0x02;
	/** 表示文字过长时折行显示 */
	public static final byte WRAP_BREAK = 0x03;
	/** 表示多行文本 */
	public static final byte WRAP_MULTI = 0x04;

	/**
	 * &lt;lable&gt;中"wap"属性的值，用于标示文字的折行信息
	 * 
	 * @see #WRAP_SCROLL
	 * @see #WRAP_BREAK
	 * @see #WRAP_OMIT
	 * @see #WRAP_PLAIN
	 */
	public byte wrap = WRAP_PLAIN;

	/** &lt;lable&gt;中"text"属性的值，是要实现的文本内容 */
	public String text = null;

	/**
	 * 构造函数
	 */
	public SNSLabelDOM() {
		this.type = TYPE_LABEL;
		canFocus = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincdom.AbstractSNSDOM#getSubAttributeValue(java.lang.String)
	 */
	public String getSubAttributeValue(String name) {
		if (name == null || name.equals(ZMLTag.NONE_VALUE))
			return null;
		if (ZMLTag.TEXT_ATTR.equals(name))
			return text;
		if (ZMLTag.TYPE_ATTR.equals(name))
			return String.valueOf(wrap);
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
		if (name == null || name.equals(ZMLTag.NONE_VALUE))
			return;
		if (ZMLTag.TEXT_ATTR.equals(name))
			text = value;
		else if (ZMLTag.TYPE_ATTR.equals(name))
			wrap = (byte) StringUtil.Str2Int(value);
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
		text = dis.readUTF();
		wrap = dis.readByte();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seezincfish.zincdom.AbstractSNSDOM#serializeSpecialAttributes(java.io.
	 * DataOutputStream)
	 */
	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
		dos.writeUTF(text);
		dos.writeByte(wrap);
	}

	public Object getDefaultStylePropertyValue(String name) {
		if (ICSSConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return null;
		}
		return super.getDefaultStylePropertyValue(name);
	}

}
