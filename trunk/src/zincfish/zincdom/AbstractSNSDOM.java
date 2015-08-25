package zincfish.zincdom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import utils.ArrayList;
import utils.StringUtil;
import zincfish.zinccss.ICSSConstants;
import zincfish.zinccss.model.Alignment;
import zincfish.zinccss.model.Coordinates;
import zincfish.zinccss.model.Insets;
import zincfish.zinccss.model.Metrics;
import zincfish.zinccss.style.StyleSet;
import zincfish.zinclayout.ILayout;
import zincfish.zinclayout.InlineLayout;
import zincfish.zincparser.zmlparser.ZMLTag;
import zincfish.zincwidget.AbstractSNSComponent;

/**
 * <p>
 * <code>Akebono</code>的UI组件是以树形结构组织的，xml文档也是树形结构。使用xml来定义<code>Akebono</code>
 * UI界面是一件简单又自然的做法。但是考虑到<code>Akebono</code>巨大的内存消耗，因此缓存解析好的<code>Akebono</code>
 * 组件是不可行的
 * 。同样是出于对性能和内存的考虑，在解析xml文件时，我们仍然采用<b>Pull</b>式解析,而非<b>DOM</b>式解析。尽管在解析方式上不采用
 * <b>DOM</b>式解析，但是上面已经提到，<code>Akebono</code>的UI组件是以树形结构组织的，为了方便xml到
 * <code>Akebono</code> UI界面的转换，我们仍然要将xml解析为一颗DOM Tree。
 * </p>
 * <p>
 * DOM Tree的存在主要是在内存和性能约束的条件下，在两者之间寻找的一种平衡。直接缓存<code>Akebono</code>
 * 组件可以很快的实现界面的切换(因为不需要再解析排版)，但是会造成较大的内存开销；缓存xml数据内存开销很小，但是用xml到UI界面需要经历
 * <em>解析</em>、<em>排版</em>等处理过程，因此会损失性能。在这种约束下，构建并缓存DOM Tree是一种折中的做法。DOM Tree比
 * <code>Akebono</code> 组件消耗更少的内存，又可以省去解析xml的过程，达到了内存和性能的平衡。
 * </p>
 * <p>
 * DOM Tree的另一个重要作用是实现Ajax。Ajax的实现很大程度上依靠对DOM Tree的操作。<code>ZincFish</code>
 * 引擎利用<b>Zinc Script</b>脚本实现了一种在移动设备上工作的ajax框架。我们提供了一套对DOM Tree进行操作的库供<b>Zinc
 * Script</b>脚本调用，从而完成对DOM Tree的操作，实现Ajax，完成对UI的更新。
 * </p>
 * 
 * @author Jarod Yv
 */
public abstract class AbstractSNSDOM {
	// ///////////////////////////// 默认的样式 /////////////////////////////
	protected static final ILayout DEFAULT_LAYOUT = new InlineLayout();
	protected static final Integer DEFAULT_COLOR = new Integer(0xFFFFFF);
	protected static final Insets DEFAULT_MARGIN = new Insets();
	protected static final Insets DEFAULT_BORDER = new Insets();
	protected static final Insets DEFAULT_PADDING = new Insets();
	protected static final Metrics DEFAULT_MIN_SIZE = new Metrics();
	protected static final Coordinates DEFAULT_GAP = new Coordinates();
	protected static final Coordinates DEFAULT_SPAN = new Coordinates(1, 1);
	protected static final Coordinates DEFAULT_WEIGHT = new Coordinates(0, 0);
	protected static final Alignment DEFAULT_ALIGN = Alignment.TOP_LEFT;
	public static final Alignment[] DEFAULT_BACKGROUND_ALIGN = new Alignment[] { Alignment.TOP_LEFT };
	public static final Coordinates[] DEFAULT_BACKGROUND_REPEAT = new Coordinates[] { new Coordinates() };
	// /////////////////////////////////////////////////////////////////////

	// /////////////////////// DOM节点类型 ////////////////////////
	/** 未知类型 */
	public static final byte TYPE_UNKNOWN = 0x00;
	/** 垂直列表类型 */
	public static final byte TYPE_LIST = 0x01;
	/** 列表项类型 */
	public static final byte TYPE_LIST_ITEM = 0x02;
	/** 按钮类型 */
	public static final byte TYPE_BUTTON = 0x03;
	/** 图片浏览器类型 */
	public static final byte TYPE_IMAGE_BOX = 0x04;
	/** DIV类型 */
	public static final byte TYPE_DIV = 0x06;
	/** BODY类型 */
	public static final byte TYPE_BODY = 0x07;
	/** 输入框类型 */
	public static final byte TYPE_TEXT_FIELD = 0x08;
	/** 分隔线类型 */
	public static final byte TYPE_HR = 0x09;
	/** 相册类型 */
	public static final byte TYPE_ALBUM = 0x0a;
	/** 普通文本类型 */
	public static final byte TYPE_LABEL = 0x0b;
	/** 图片类型 */
	public static final byte TYPE_IMAGE = 0x0c;
	/** Unit类型 */
	public static final byte TYPE_UNIT = 0x0d;
	/** 单/复选框 */
	public static final byte TYPE_CHECKBOX = 0x0e;
	/** 分页类型 */
	public static final byte TYPE_PAGING = 0x0f;
	public static final byte TYPE_CAR = 0x21; // 小车类型
	public static final byte TYPE_PARK = 0x22; // 停车场类型
	public static final byte TYPE_CARBOX = 0x23; // 小车信息描述

	/**
	 * 投票单/复选项
	 */
	public static final byte TYPE_VOTE_ITEM = 0x51;
	/**
	 * 可以滚动显示的单行文本
	 */
	public static final byte TYPE_SCROLL_TEXT = 0x52;
	/**
	 *普通单/复选项
	 */
	public static final byte TYPE_CHECK_ITEM = 0x53;
	public static final byte TYPE_PRESENT_ITEM = 0x54;
	/**
	 * 弹出窗口
	 */
	public static final byte TYPE_POP = 0x55;
	public static final byte TYPE_CHECK_COMMON_ITEM = 0x56;
	public static final byte TYPE_HEAD = 0x57;
	public static final byte TYPE_FILE = 0x58; // 文件浏览
	public static final byte TYPE_LOADING = 0x59; // 
	public static final byte TYPE_MIXTAG = 0x5A;
	public static final byte TYPE_TURNPAGE = 0x5B;

	/** 投票选项类型 @add by jiangwei */
	/** 横线导航选项 */
	public static final byte TYPE_OPTIONS_ITEM = 0x10;
	/** 下拉框选项 */
	public static final byte TYPE_COMBOX = 0x11;
	/** 日期弹出框 */
	public static final byte TYPE_DATE_PICKER = 0x12;
	/** 菜单导航 */
	public static final byte TYPE_NAVIGATION = 0x13;
	/** TAB 负责页面切换 */
	public static final byte TYPE_TAB = 0x31;
	public static final byte TYPE_TAB_LIST = 0x42; // 负责所有的 TAB 管理
	/** 导航菜单 */
	public static final byte TYPE_NMENU = 0x32;
	/** 子菜单菜单 */
	public static final byte TYPE_SUBMENU = 0x33;
	/** 子菜单菜单 */
	public static final byte TYPE_TABNEMU = 0x34;
	/** 好友管理标签 */
	public static final byte TYPE_FRIENDS = 0x35;
	/** 弹出面板 */
	public static final byte TYPE_POPPANEL = 0x39;

	public static final byte TYPE_LAYOUT = 0x60; // 排版组件
	public static final byte TYPE_SELECT = 0x70; // 排版组件

	public static final byte TYPE_MENU = 0x36;
	public static final byte TYPE_PLAIN_TEXT = 0x37;
	public static final byte TYPE_TEXT_EDITOR = 0x38;
	public static final byte TYPE_HITEM = 0x50;
	public static final byte TYPE_P = 0x65;
	public static final byte TYPE_BUBBLE = 0x71;
	// ///////////////////////////////////////////////////////////

	// //////////////////////// 水平对齐方式 ///////////////////////
	public static final byte BIND_NONE = 0x00;
	public static final byte BIND_NORTH = 0x01;
	public static final byte BIND_EAST = 0x02;
	public static final byte BIND_SOUTH = 0x03;
	public static final byte BIND_WEST = 0x04;
	// //////////////////////////////////////////////////////////

	/** 支持的伪类数组 */
	public static final String[] PSEUDO_CLASSES = new String[] {
			ICSSConstants.PSEUDO_CLASS_HOVER,
			ICSSConstants.PSEUDO_CLASS_DISABLED,
			ICSSConstants.PSEUDO_CLASS_SELECTED };

	/** DOM节点的唯一标识 */
	public String id = null;

	/** 当前聚焦的第几个孩子节点 */
	public int index = 0;

	/** 节点类型 */
	public byte type = TYPE_UNKNOWN;

	/** 标志是否可视 */
	public boolean isVisible = true;

	/** 标志是否有效 */
	public boolean isAvailable = true;

	/** 标志是否可聚焦 */
	public boolean canFocus = false;

	/** name表示拼接数据时的name字段 */
	public String name = null;

	/** value表示拼接数据时的value字段 */
	public String value = null;

	/** tips表示当用户在某个组件上停留太久是现实的提示 */
	public String tips = null;

	/** 表示该组件后跟的&lt;bt&gt;的个数 */
	public int brNum = 0;

	/** style表示xml作者定义的css样式 */
	public String style = null;

	/** class属性, 用于用个class属性选择CSS样式 */
	public String classes = null;

	/** data属性, 用于保存附加信息 */
	public String data = null;

	/** bind属性, 用于指定组件的绑定样式 */
	public byte bind = BIND_NONE;

	// ////////////////////////// DOM事件 //////////////////////////
	/** 生成组件时的响应函数，该方法在生成组件时调用，不需要保存 */
	// public String onInit = null;
	/** 点击事件的响应函数 */
	public String onClick = null;

	/** 获得焦点事件的响应函数 */
	public String onFocus = null;

	/** 失去焦点时的响应函数 */
	public String onLoseFocus = null;

	/** 组件的值发生改变时触发 */
	public String onChanged = null;

	/** 组件由隐藏到显示时触发 */
	public String onShow = null;

	/** 组件由显示到隐藏时触发 */
	public String onHide = null;

	/** 载入事件的响应函数 */
	// public String onLoad = null;
	// ///////////////////////////////////////////////////////////
	/** 父节点 */
	public AbstractSNSDOM father = null;

	/** 子节点 */
	public ArrayList children = null;

	/** DOM对应的标签名，用于通过标签名选择CSS样式 */
	public String tagName = null;

	/* DOM节点相关联的SNS UI组件 */
	private AbstractSNSComponent component = null;

	/* 与该组件绑定的样式集合 */
	private StyleSet styleSet = new StyleSet(this);

	/* 标识组件的位置是否已经确定 */
	private boolean invalidated = true;

	/**
	 * 释放资源
	 */
	public void release() {
		// 清理属性
		id = null;
		name = null;
		value = null;
		data = null;
		tips = null;
		style = null;
		classes = null;
		onClick = null;
		onFocus = null;
		onLoseFocus = null;
		onShow = null;
		onHide = null;
		tagName = null;
		if (style != null) {
			styleSet.clearCachedStyle();
			style = null;
		}
		// 清理节点
		father = null;
		if (children != null) {
			while (children.size() > 0) {
				AbstractSNSDOM child = (AbstractSNSDOM) children.remove(0);
				child.release();
				child = null;
			}
			children = null;
		}
		// 释放ViewTree节点
		if (component != null) {
			component.release();
			component = null;
		}
		// System.gc();// 在此调用gc()是必要的
	}

	/**
	 * 重写{@link java.lang.Object#equals(Object)}方法<br>
	 * 通过比较两个对象的id是否相同来判断两个DOM对象是否相同
	 * 
	 * @return
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return (this == obj)
				|| (id != null && id.equals(((AbstractSNSDOM) obj).id));
	}

	/**
	 * 获取与此DOM节点相关联的ViewTree节点
	 * 
	 * @return 与此DOM节点相关联的ViewTree节点
	 */
	public AbstractSNSComponent getComponent() {
		return component;
	}

	/**
	 * 设置与此DOM节点相关联的ViewTree节点
	 * 
	 * @param component
	 *            与此DOM节点相关联的ViewTree节点
	 */
	public void setComponent(AbstractSNSComponent component) {
		this.component = component;
	}

	/**
	 * 判断DOM是否有孩子
	 * 
	 * @return <ul>
	 *         <li><code>true</code> - 有子节点
	 *         <li><code>false</code> - 没有子节点
	 *         </ul>
	 */
	public boolean hasChildren() {
		return children != null && children.size() > 0;
	}

	/**
	 * 向当前节点追加一个子节点
	 * 
	 * @param child
	 *            子节点对象
	 */
	public void addChild(AbstractSNSDOM child) {
		addChild(Integer.MAX_VALUE, child);
	}

	/**
	 * 向当前节点的指定位置插入一个子节点
	 * 
	 * @param index
	 *            插入的位置
	 * @param child
	 *            子节点对象
	 */
	public void addChild(int index, AbstractSNSDOM child) {
		if (child == null)
			return;
		if (children == null)
			children = new ArrayList(10);
		if (index >= children.size())
			children.add(child);
		else if (index < 0) {
			children.add(0, child);
		} else {
			children.add(index, child);
		}

		child.father = this;
	}

	public int findIndex() {
		int index = 0;
		for (int i = 0; father != null && i < father.children.size(); i++) {
			AbstractSNSDOM dom = (AbstractSNSDOM) father.children.get(i);
			if (dom.isVisible) {
				if (dom == this)
					return index;
				index++;
			}
			dom = null;
		}
		return -1;
	}

	/**
	 * 设置DOM的公共属性值。
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 */
	public void setCommonValue(String name, String value) {
		if (name == null || name.equals(ZMLTag.NONE_VALUE))
			return;
		// FIXME 根据实际情况，可能改成Map映射或直接用整形选择
		if (ZMLTag.ID_ATTR.equals(name)) {
			this.id = value;
		} else if (ZMLTag.VISIBLE_ATTR.equals(name)) {
			this.isVisible = StringUtil.Str2Bool(value);
			if (component != null)
				component.setVisible(this.isVisible);
		} else if (ZMLTag.AVAILABLE_ATTR.equals(name)) {
			this.isAvailable = StringUtil.Str2Bool(value);
		} else if (ZMLTag.CLASS_ATTR.equals(name)) {
			this.classes = value;
		} else if (ZMLTag.DATA_ATTR.equals(name)) {
			this.data = value;
		} else if (ZMLTag.ON_FOCUS_ATTR.equals(name)) {
			this.onFocus = value;
		} else if (ZMLTag.ON_LOSE_FOCUS_ATTR.equals(name)) {
			this.onLoseFocus = value;
		} else if (ZMLTag.ON_CLICK_ATTR.equals(name)) {
			this.onClick = value;
		}
	}

	/**
	 * 获取DOM的公共属性值。
	 * 
	 * @param name
	 *            属性名
	 * @return 属性值
	 */
	public String getCommonValue(String name) {
		if (name == null || name.equals(ZMLTag.NONE_VALUE))
			return null;
		// FIXME 根据实际情况，可能改成Map映射或直接用整形选择
		if (ZMLTag.ID_ATTR.equals(name))
			return this.id;
		if (ZMLTag.VISIBLE_ATTR.equals(name))
			return String.valueOf(this.isVisible);
		if (ZMLTag.AVAILABLE_ATTR.equals(name))
			return String.valueOf(this.value);
		if (ZMLTag.ON_FOCUS_ATTR.equals(name))
			return String.valueOf(this.onFocus);
		if (ZMLTag.DATA_ATTR.equals(name))
			return String.valueOf(this.data);
		return null;
	}

	/**
	 * 获取DOM网络属性值<br>
	 * 这里的网络属性值指的是<code>name</code> 和 <code>value</code>
	 * 
	 * @param name
	 *            属性名
	 * @return 属性值
	 */
	public String getNetValue(String name) {
		if (ZMLTag.NAME_ATTR.equals(name)) {
			return this.name;
		} else if (ZMLTag.VALUE_ATTR.equals(name)) {
			return this.value;
		} else {
			return null;
		}
	}

	/**
	 * 设置DOM网络属性值<br>
	 * 这里的网络属性值指的是<code>name</code> 和 <code>value</code>
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 */
	public void setNetValue(String name, String value) {
		if (ZMLTag.NAME_ATTR.equals(name)) {
			this.name = value;
		} else if (ZMLTag.VALUE_ATTR.equals(name)) {
			this.value = value;
		}
	}

	/**
	 * 获取组件的网络POST字段<br>
	 * POST字段是一种"name-value"对的形式
	 * 
	 * @return "name=value"
	 */
	public String getNetString() {
		if (name == null || name.equals(ZMLTag.NONE_VALUE))
			return null;

		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append('=');
		sb.append(value == null ? ZMLTag.NONE_VALUE : StringUtil
				.encodeChineseCharacters(value));
		return sb.toString();
	}

	/**
	 * 子类需要根据自己的属性实现此抽象方法，用于设置自己特有的属性值
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 */
	public abstract void setSubAttributeValue(String name, String value);

	/**
	 * 子类需要根据自己的属性实现此抽象方法，用于获取自己特有的属性值
	 * 
	 * @param name
	 *            属性名
	 */
	public abstract String getSubAttributeValue(String name);

	/**
	 * 返回支持的伪类
	 * 
	 * @return 支持的伪类数组
	 */
	public String[] getAvailablePseudoClasses() {
		return PSEUDO_CLASSES;
	}

	/**
	 * 返回对伪类的支持情况
	 * 
	 * @param pseudoClass
	 *            伪类名
	 * @return 如果组件支持伪类，则返回<code>true</code>
	 */
	public boolean isPseudoClassCompatible(String pseudoClass) {
		if (ICSSConstants.PSEUDO_CLASS_HOVER.equals(pseudoClass)) {
			return component == null ? false : component.isFocused();
		}
		if (ICSSConstants.PSEUDO_CLASS_SELECTED.equals(pseudoClass)) {
			return component == null ? false : component.isSelected();
		}
		if (ICSSConstants.PSEUDO_CLASS_DISABLED.equals(pseudoClass)) {
			return !isAvailable;
		}
		return false;
	}

	/**
	 * 返回继承的标签
	 * 
	 * @return 继承的标签
	 */
	public String getInheritedTag() {
		return tagName;
	}

	/**
	 * 获取组件绑定的样式集合
	 * 
	 * @return {@link #styleSet}
	 */
	public StyleSet getStyleSet() {
		return styleSet;
	}

	/**
	 * 清理缓存样式。清理会使所有缓存的样式都失效，并且将其移除内存
	 * 
	 * @param propagateToChildren
	 *            标示是否同时清理子组件
	 */
	public void clearCachedStyle(boolean propagateToChildren) {
		styleSet.clearCachedStyle();
		if (propagateToChildren) {
			for (int i = 0; children != null && i < children.size(); i++) {
				AbstractSNSDOM dom = (AbstractSNSDOM) children.get(i);
				dom.clearCachedStyle(propagateToChildren);
				dom = null;
			}
		}
		invalidateStylePropertiesCache(!propagateToChildren); // 如果propagateToChildren为true，invalidateStylePropertiesCache已经执行，不需要执行。
		invalidate();
	}

	/**
	 * 使所有缓存的样式属性全部失效
	 * 
	 * @param propagateToChildren
	 *            标示是否同时清理子组件
	 */
	public void invalidateStylePropertiesCache(boolean propagateToChildren) {
		// 重置有效标志位
		styleSet.setValidCachedFlags(0);
		// 如果需要，子组件的样式一并失效
		if (propagateToChildren) {
			for (int i = 0; hasChildren() && i < children.size(); i++) {
				AbstractSNSDOM dom = (AbstractSNSDOM) children.get(i);
				dom.invalidateStylePropertiesCache(propagateToChildren);
				dom = null;
			}
		}

	}

	/**
	 * 重置组件的位置和高度宽度，并且如果有父组件，父组件的位置和尺寸一并失效。要求重新排版。<br>
	 * 调用该方法会在所有尺寸失效的组件中调用 <code>{@link AbstractSNSComponent#layout()}</code> 和
	 * </code>{@link AbstractSNSComponent#paint()}</code>
	 */
	protected void invalidate() {
		invalidated = true;
		AbstractSNSDOM parent = this.father;
		if (parent != null && !parent.invalidated) {
			parent.invalidate();
		}
		parent = null;
	}

	/**
	 * 设置组件的尺寸位置是否已经确定
	 * 
	 * @param isInvalidated
	 */
	public void setInvalidated(boolean isInvalidated) {
		invalidated = isInvalidated;
	}

	/**
	 * 判断组件的尺寸位置是否已经确定
	 * 
	 * @return {@link #invalidated}
	 *         <ul>
	 *         <li><code>true</code> - 组件的尺寸位置尚未确定</li>
	 *         <li><code>false</code> - 组件的尺寸位置已经确定</li>
	 *         </ul>
	 */
	public boolean isInvalidated() {
		return invalidated;
	}

	/**
	 * 获取默认样式<br>
	 * 子类可以根据自己的需要覆盖此方法，更改默认样式。
	 * 
	 * @param name
	 *            样式属性名
	 * @return 默认样式
	 */
	public Object getDefaultStylePropertyValue(String name) {
		if (ICSSConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return null;/* DEFAULT_LAYOUT; */
		}
		if (ICSSConstants.MARGIN_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_MARGIN;
		}
		if (ICSSConstants.BORDER_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_BORDER;
		}
		if (ICSSConstants.PADDING_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_PADDING;
		}
		if (ICSSConstants.MIN_SIZE_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_MIN_SIZE;
		}
		if (ICSSConstants.GAP_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_GAP;
		}
		if (ICSSConstants.SPAN_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_SPAN;
		}
		if (ICSSConstants.WEIGHT_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_WEIGHT;
		}
		if (ICSSConstants.ALIGN_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_ALIGN;
		}
		if (ICSSConstants.COLOR_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_COLOR;
		}
		return null;
	}

	/**
	 * 序列化
	 * 
	 * @param dos
	 *            输出流
	 */
	public void serialize(DataOutputStream dos) throws IOException {
		dos.writeByte(type);// 类型
		serializeCommonAttributes(dos);
		serializeSpecialAttributes(dos);
		if (hasChildren()) {
			dos.writeInt(children.size());
			for (int i = 0; i < children.size(); i++) {
				AbstractSNSDOM child = (AbstractSNSDOM) children.get(i);
				child.serialize(dos);
				child = null;
			}
		} else {
			dos.writeInt(0);
		}
	}

	/**
	 * 反序列化
	 * 
	 * @param dis
	 *            输入流
	 * @return 根据输入流数据建立起的DOM树
	 */
	public AbstractSNSDOM deserialize(DataInputStream dis) throws IOException {
		AbstractSNSDOM dom = DOMFactory.createDOM(dis.readByte());
		dom.deserializeCommonAttributes(dis);
		dom.deserializeSpecialAttributes(dis);
		int childrenNum = dis.readInt();
		for (int i = 0; i < childrenNum; i++) {
			AbstractSNSDOM child = deserialize(dis);
			addChild(child);
			child = null;
		}
		return dom;
	}

	/**
	 * 序列化公共属性
	 * 
	 * @param dos
	 *            输出流
	 * @throws IOException
	 */
	protected void serializeCommonAttributes(DataOutputStream dos)
			throws IOException {
		dos.writeUTF(tagName);// 标签名
		dos.writeUTF(id);// ID
		dos.writeUTF(classes);// class
		dos.writeUTF(style);// style
		dos.writeUTF(name);// name
		dos.writeUTF(value);// value
		dos.writeBoolean(isVisible);// visible
		dos.writeBoolean(isAvailable);// available
		dos.writeUTF(tips);// tips
		dos.writeUTF(data);// data
		dos.writeUTF(onClick);
		dos.writeUTF(onFocus);
		dos.writeUTF(onLoseFocus);
		dos.writeUTF(onShow);
		dos.writeUTF(onChanged);
	}

	/**
	 * 反序列化公共属性
	 * 
	 * @param dis
	 *            输出流
	 * @throws IOException
	 */
	protected void deserializeCommonAttributes(DataInputStream dis)
			throws IOException {
		tagName = dis.readUTF();
		id = dis.readUTF();
		classes = dis.readUTF();
		style = dis.readUTF();
		name = dis.readUTF();
		value = dis.readUTF();
		isVisible = dis.readBoolean();
		isAvailable = dis.readBoolean();
		tips = dis.readUTF();
		data = dis.readUTF();
		onClick = dis.readUTF();
		onFocus = dis.readUTF();
		onLoseFocus = dis.readUTF();
		onShow = dis.readUTF();
		onChanged = dis.readUTF();
	}

	/**
	 * 序列化子类中的特殊属性
	 * 
	 * @param dos
	 *            输出流
	 * @throws IOException
	 */
	protected abstract void serializeSpecialAttributes(DataOutputStream dos)
			throws IOException;

	/**
	 * 反序列化子类中的特殊属性
	 * 
	 * @param dis
	 *            输入流
	 * @throws IOException
	 */
	protected abstract void deserializeSpecialAttributes(DataInputStream dis)
			throws IOException;

	/**
	 * 获取整棵树的根
	 * 
	 * @return
	 */
	public AbstractSNSDOM getRoot() {
		return father == null ? this : father.getRoot();
	}
}
