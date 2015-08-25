package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import utils.StringUtil;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>SNSRateDOM</code>封装了&lt;rate&gt;标签的数据<br>
 * &lt;rate&gt;标签表示投票中的得票比例;
 * 
 * @author Jarod Yv
 */
public class SNSRateDOM extends AbstractSNSDOM {

	/** 投票总数 */
	public int total = 0;
	/** 得票总数 */
	public int count = 0;

	protected void deserializeSpecialAttributes(DataInputStream dis)
			throws IOException {
		total = dis.readInt();
		count = dis.readInt();
	}

	public String getSubAttributeValue(String name) {
		if (ZMLTag.TOTAL_ATTR.equals(name))
			return String.valueOf(total);
		if (ZMLTag.COUNT_ATTR.equals(name))
			return String.valueOf(count);
		return null;
	}

	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
		dos.writeInt(total);
		dos.writeInt(count);
	}

	public void setSubAttributeValue(String name, String value) {
		if (ZMLTag.TOTAL_ATTR.equals(name))
			total = StringUtil.Str2Int(value);
		else if (ZMLTag.COUNT_ATTR.equals(name))
			count = StringUtil.Str2Int(value);
	}

}
