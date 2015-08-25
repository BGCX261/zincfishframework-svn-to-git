package ui.form;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.TextBox;

/**
 * <code>TextBoxForm</code> 是高级界面输入框。当页面上的输入框点击后会激活高级界面输入框。
 * 
 * @author Jarod Yv
 */
public class TextBoxForm extends TextBox {
	/** 简单文本框类型--只能输入文字 */
	public static final byte TYPE_SIMPLE = 0x00;
	/** 点滴文本框类型--可以插入表情 */
	public static final byte TYPE_TWITTER = 0x01;
	/** 日记文本框类型--可以插入表情和附件 */
	public static final byte TYPE_DIARY = 0x02;
	/** 全功能文本框类型--全功能 */
	public static final byte TYPE_FULL = 0x03;

	/** 确定按钮 */
	private static final Command CMD_OK = new Command("确定", Command.OK, 0);
	/** 取消按钮 */
	private static final Command CMD_CANCEL = new Command("取消", Command.CANCEL,
			0);
	/** 添加表情按钮 */
	private static final Command CMD_SMILE = new Command("添加表情", Command.ITEM,
			Command.SCREEN);
	/** 添加附件按钮 */
	private static final Command CMD_ATTECH = new Command("添加附件", Command.ITEM,
			Command.SCREEN);
	/** 拍照上传按钮 */
	private static final Command CMD_CAMERA = new Command("拍照上传", Command.ITEM,
			Command.SCREEN);
	/** 录音上传按钮 */
	private static final Command CMD_RECORD = new Command("录音上传", Command.ITEM,
			Command.SCREEN);

	/** <code>{@link TextBoxForm}</code>静态实例 */
	private static final TextBoxForm INSTANCE = new TextBoxForm();

	/**
	 * 私有构造函数<br>
	 * <code>TextBoxForm</code>采用单例模式
	 */
	private TextBoxForm() {
		super(null, null, 255, 0);
		this.addCommand(CMD_OK);
		this.addCommand(CMD_CANCEL);
	}

	/**
	 * 获取输入框的唯一实例
	 * 
	 * @param title
	 *            标题
	 * @param text
	 *            内容
	 * @param maxSize
	 *            最大字数
	 * @param constraints
	 *            约束
	 * @param type
	 *            文本框类型
	 * @return 文本框的唯一实例
	 */
	public TextBoxForm getTextBox(String title, String text, int maxSize,
			int constraints, byte type) {
		INSTANCE.setTitle(title);
		INSTANCE.setString(text);
		INSTANCE.setMaxSize(maxSize);
		INSTANCE.setConstraints(constraints);
		setType(type);
		return INSTANCE;
	}

	/**
	 * 设置文本框类型。文本框类型决定菜单项
	 * 
	 * @param type
	 *            文本框类型
	 */
	private void setType(byte type) {
		switch (type) {
		case TYPE_FULL:
			INSTANCE.addCommand(CMD_CAMERA);
			INSTANCE.addCommand(CMD_RECORD);
			break;
		case TYPE_DIARY:
			INSTANCE.removeCommand(CMD_CAMERA);
			INSTANCE.removeCommand(CMD_RECORD);
			INSTANCE.addCommand(CMD_ATTECH);
			INSTANCE.addCommand(CMD_SMILE);
			break;
		case TYPE_TWITTER:
			INSTANCE.removeCommand(CMD_CAMERA);
			INSTANCE.removeCommand(CMD_RECORD);
			INSTANCE.removeCommand(CMD_ATTECH);
			INSTANCE.addCommand(CMD_SMILE);
		default:
			INSTANCE.removeCommand(CMD_CAMERA);
			INSTANCE.removeCommand(CMD_RECORD);
			INSTANCE.removeCommand(CMD_ATTECH);
			INSTANCE.removeCommand(CMD_SMILE);
			break;
		}
	}
}
