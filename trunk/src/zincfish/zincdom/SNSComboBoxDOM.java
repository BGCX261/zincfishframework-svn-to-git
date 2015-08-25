package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import utils.ArrayList;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>SNSComboboxDOM</code> 封装了从&lt;select&gt;标签中解析出的数据
 * 
 * @author JarodYv
 */
public class SNSComboBoxDOM extends SNSLabelDOM {

	public static final byte SINGLE_PICKER = 0x00;
	public static final byte MULTI_PICKER = 0x01;
	public static final byte FILE_PICKER = 0x02;
	public static final byte DATE_PICKER = 0x03;
	public static final byte FRIEND_PICKER = 0x04;

	/** 选中第几个选项 */
	public int index = 0;

	/** 选项 */
	public ArrayList options = null;

	/** 下拉框类型 */
	public byte comboType = SINGLE_PICKER;

	public SNSComboBoxDOM() {
		this.type = TYPE_COMBOX;
	}

	public void addOption(String text, String value) {
		if (options == null)
			options = new ArrayList(5);
		Option option = new Option(text, value);
		options.add(option);
		option = null;
	}

	public void setIndex(int index) {
		if (options == null || index < 0 || index >= options.size())
			return;
		this.index = index;
		value = ((Option) options.get(index)).value;
	}

	public int getIndex() {
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincdom.AbstractSNSDOM#getSubAttributeValue(java.lang.String)
	 */
	public String getSubAttributeValue(String name) {
		if (ZMLTag.TYPE_ATTR.equals(name))
			return String.valueOf(type);
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
		type = dis.readByte();
		int num = dis.readInt();
		if (num > 0) {
			options = new ArrayList(num);
			for (int i = 0; i < num; i++) {
				Option option = new Option();
				option.text = dis.readUTF();
				option.value = dis.readUTF();
				options.add(option);
				option = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seezincfish.zincdom.AbstractSNSDOM#serializeSpecialAttributes(java.io.
	 * DataOutputStream)
	 */
	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
		dos.writeByte(type);
		int num = options == null ? 0 : options.size();
		dos.writeInt(num);
		if (num > 0) {
			for (int i = 0; i < num; i++) {
				Option option = (Option) options.get(i);
				dos.writeUTF(option.text);
				dos.writeUTF(option.value);
				dos.writeBoolean(option.isSelected);
				option = null;
			}
		}
	}

	/**
	 * <code>Option</code> 封装了从&lt;option&gt;标签中解析出的数据
	 * 
	 * @author Jarod Yv
	 */
	public class Option {
		/** 显示的文字 */
		public String text = null;
		/** 代表的值 */
		public String value = null;
		/** 多选下拉列表中标示是否被选中 */
		public boolean isSelected = false;

		public Option() {
		}

		public Option(String text, String value) {
			this.text = text;
			this.value = value;
		}
	}
}
