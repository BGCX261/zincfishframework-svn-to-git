package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import utils.StringUtil;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>SNSPagingDOM</code> 封装了&lt;paging&gt;标签的数据<br>
 * &lt;paging&gt;标签表示一个分页组件
 * 
 * @author Jarod Yv
 */
public class SNSPagingDOM extends AbstractSNSDOM {

	/** 跳转分页的URL */
	public String src = null;
	/** 总页数 */
	public int totalPage = 0;
	/** 当前停留在第几页 */
	public int currentPage = 0;

	/**
	 * 构造函数
	 */
	public SNSPagingDOM() {
		type = TYPE_PAGING;
		canFocus = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincdom.AbstractSNSDOM#getSubAttributeValue(java.lang.String)
	 */
	public String getSubAttributeValue(String name) {
		if (ZMLTag.SRC_ATTR.equals(name))
			return src;
		if (ZMLTag.TOTAL_ATTR.equals(name))
			return String.valueOf(totalPage);
		if(ZMLTag.COUNT_ATTR.equals(name))
			return String.valueOf(currentPage);
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
		if (ZMLTag.SRC_ATTR.equals(name))
			src = value;
		else if (ZMLTag.TOTAL_ATTR.equals(name))
			totalPage = StringUtil.Str2Int(value);
		else if(ZMLTag.COUNT_ATTR.equals(name))
			currentPage=StringUtil.Str2Int(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seezincfish.zincdom.AbstractSNSDOM#serializeSpecialAttributes(java.io.
	 * DataOutputStream)
	 */
	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
		dos.writeUTF(src);
		dos.writeInt(totalPage);
		dos.writeInt(currentPage);
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
		src = dis.readUTF();
		totalPage = dis.readInt();
		currentPage = dis.readInt();
	}
}
