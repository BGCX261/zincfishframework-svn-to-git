package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import utils.ArrayList;
import utils.StringUtil;
import zincfish.zinccss.ICSSConstants;
import zincfish.zinccss.model.Alignment;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>SNSUnitDOM</code> 代表根据&lt;unit&gt;标签解析出来的DOM节点
 * 
 * @author Jarod Yv
 */
public class SNSUnitDOM extends AbstractSNSDOM {

	/** onload属性 */
	public String onload = null;
	/** timer属性 */
	public long timer = 0L;
	/* 与xml关联的脚本列表 */
	private ArrayList scriptFiles = null;
	/* <script></script>标签间直接定义的脚本代码 */
	private String script = null;
	/* CSS文件的路径 */
	private String css = null;

	private String url = null;

	public SNSUnitDOM() {
		type = TYPE_UNIT;
		canFocus = false;
	}

	/**
	 * 加入脚本文件的路径
	 * 
	 * @param path
	 *            脚本所在的路径
	 */
	public void addScriptFiles(String path) {
		if (scriptFiles == null)
			scriptFiles = new ArrayList(2);
		scriptFiles.add(path);
	}

	/**
	 * 获取脚本文件
	 * 
	 * @return 脚本文件路径的列表
	 */
	public ArrayList getScriptFiles() {
		return scriptFiles;
	}

	public void setCSSFile(String path) {
		this.css = path;
	}

	public String getCSSFile() {
		return this.css;
	}

	public void addScript(String script) {
		if (this.script == null)
			this.script = script;
		else
			this.script += script;
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
		if (ZMLTag.ON_LOAD_ATTR.equals(name))
			return onload;
		if (ZMLTag.TIMER_ATTR.equals(name))
			return String.valueOf(timer);
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
		if (ZMLTag.ON_LOAD_ATTR.equals(name)) {
			onload = value;
		} else if (ZMLTag.TIMER_ATTR.equals(name)) {
			timer = StringUtil.Str2Long(value);
		}
	}

	public AbstractSNSDOM deserialize(DataInputStream dis) throws IOException {
		deserializeCommonAttributes(dis);
		deserializeSpecialAttributes(dis);
		int childrenNum = dis.readInt();
		for (int i = 0; i < childrenNum; i++) {
			AbstractSNSDOM child = deserialize(dis);
			addChild(child);
			child = null;
		}
		return this;
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
		onload = dis.readUTF();
		timer = dis.readLong();
		int scriptFileNums = dis.readInt();
		scriptFiles = new ArrayList(scriptFileNums);
		for (int i = 0; i < scriptFileNums; i++) {
			scriptFiles.add(dis.readUTF());
		}
		script = dis.readUTF();
		css = dis.readUTF();
		// TODO 反序列菜单数据
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seezincfish.zincdom.AbstractSNSDOM#serializeSpecialAttributes(java.io.
	 * DataOutputStream)
	 */
	protected void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException {
		dos.writeUTF(onload);
		dos.writeLong(timer);
		int scriptFileNums = scriptFiles == null ? 0 : scriptFiles.size();
		dos.writeInt(scriptFileNums);
		for (int i = 0; i < scriptFileNums; i++) {
			dos.writeUTF((String) scriptFiles.get(i));
		}
		dos.writeUTF(script);
		dos.writeUTF(css);
		// TODO 序列菜单数据
	}

	public Object getDefaultStylePropertyValue(String name) {
		if (ICSSConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_LAYOUT;
		}
		if (ICSSConstants.ALIGN_STYLE_PROPERTY.equals(name)) {
			return Alignment.FILL;
		}
		return super.getDefaultStylePropertyValue(name);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
