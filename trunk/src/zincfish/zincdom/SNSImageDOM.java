package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.lcdui.game.Sprite;
import utils.StringUtil;
import zincfish.zincparser.zmlparser.ZMLTag;

/**
 * <code>ImageDOM</code> 封装了&lt;img&gt;标签的数据。<br>
 * &lt;img&gt;标签定义了页面上的一张图片或图片动画。
 * 
 * @author Jarod Yv
 * @since fingerling
 */
public class SNSImageDOM extends AbstractSNSDOM {
	/** &lt;img&gt;中"src"属性的值, 是本张图片的地址 */
	public String src = null;

	/** &lt;img&gt;中"alt"属性的值, 是图片的替代文字 */
	public String alt = null;

	/** &lt;img&gt;中"prev"属性的值, 是上一张图片的地址 */
	public String prevImageURL = null;

	/** &lt;img&gt;中"next"属性的值, 是下一张图片的地址 */
	public String nextImageURL = null;

	/** &lt;img&gt;中"framewidth"属性的值, 代表图片一帧的宽度,如果为0表示全部显示 */
	public int frameWidth = 0;

	/** &lt;img&gt;中"frameheight"属性的值, 代表图片一帧的高度,如果为0表示全部显示 */
	public int frameHeight = 0;

	/** &lt;img&gt;中"rotation"属性的值, 是图片的翻转属性 */
	public int rotation = Sprite.TRANS_NONE;

	/**
	 * 构造函数
	 */
	public SNSImageDOM() {
		this.type = TYPE_IMAGE;
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
		if (ZMLTag.SRC_ATTR.equals(name))
			return src;
		if (ZMLTag.ALT_ATTR.equals(name))
			return alt;
		if (ZMLTag.PREV_ATTR.equals(name))
			return prevImageURL;
		if (ZMLTag.NEXT_ATTR.equals(name))
			return nextImageURL;
		if (ZMLTag.FRAME_WIDTH_ATTR.equals(name))
			return String.valueOf(frameWidth);
		if (ZMLTag.FRAME_HEIGHT_ATTR.equals(name))
			return String.valueOf(frameHeight);
		if (ZMLTag.ROTATION_ATTR.equals(name))
			return String.valueOf(rotation);
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
		if (ZMLTag.SRC_ATTR.equals(name))
			src = value;
		else if (ZMLTag.ALT_ATTR.equals(name))
			alt = value;
		else if (ZMLTag.PREV_ATTR.equals(name))
			prevImageURL = value;
		else if (ZMLTag.NEXT_ATTR.equals(name))
			nextImageURL = value;
		else if (ZMLTag.FRAME_WIDTH_ATTR.equals(name))
			frameWidth = StringUtil.Str2Int(value);
		else if (ZMLTag.FRAME_HEIGHT_ATTR.equals(name))
			frameHeight = StringUtil.Str2Int(value);
		else if (ZMLTag.ROTATION_ATTR.equals(name))
			rotation = StringUtil.Str2Int(value);
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
		dos.writeUTF(alt);
		dos.writeUTF(prevImageURL);
		dos.writeUTF(nextImageURL);
		dos.writeInt(frameWidth);
		dos.writeInt(frameHeight);
		dos.writeInt(rotation);
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
		alt = dis.readUTF();
		prevImageURL = dis.readUTF();
		nextImageURL = dis.readUTF();
		frameWidth = dis.readInt();
		frameHeight = dis.readInt();
		rotation = dis.readInt();
	}
}
