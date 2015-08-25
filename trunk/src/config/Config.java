package config;

import com.mediawoz.akebono.corerenderer.CRFont;
import com.mediawoz.akebono.corerenderer.fonts.CRSystemFont;

public final class Config {
	public static final CRFont PLAIN_SMALL_FONT = new CRSystemFont(
			CRSystemFont.FACE_SYSTEM, CRSystemFont.STYLE_PLAIN,
			CRSystemFont.SIZE_SMALL);
	public static final CRFont BOLD_SMALL_FONT = new CRSystemFont(
			CRSystemFont.FACE_SYSTEM, CRSystemFont.STYLE_BOLD,
			CRSystemFont.SIZE_SMALL);
	public static final CRFont UNDERLINE_SMALL_FONT = new CRSystemFont(
			CRSystemFont.FACE_SYSTEM, CRSystemFont.STYLE_UNDERLINED,
			CRSystemFont.SIZE_SMALL);

	public static final CRFont BOLD_MEDIUM_FONT = new CRSystemFont(
			CRSystemFont.FACE_SYSTEM, CRSystemFont.STYLE_BOLD,
			CRSystemFont.SIZE_MEDIUM);

	// 按键值
	public final static int KEY_DEL = -8; // <- 删除键
	public final static int KEY_SWITCH = 42; // <- *键
	public final static int KEY_UP = -1; // 上按键
	public final static int KEY_LEFT = -3; // 左按键
	public final static int KEY_RIGHT = -4; // 右按键
	public final static int KEY_DOWN = -2; // 下按键
	public final static int KEY_FIRE = -5; // fire按键
	public final static int KEY_0 = 48; // 0 --> 48
	public final static int KEY_1 = 49; // 1 --> 49 符号键
	public final static int KEY_2 = 50; // 2 --> 50 abc2
	public final static int KEY_3 = 51; // 3 --> 51 def3
	public final static int KEY_4 = 52; // 4 --> 52 ghi4
	public final static int KEY_5 = 53; // 5 --> 53 jkl5
	public final static int KEY_6 = 54; // 6 --> 54 mno6
	public final static int KEY_7 = 55; // 7 --> 55 pqrs7
	public final static int KEY_8 = 56; // 8 --> 56 tuv8
	public final static int KEY_9 = 57; // 9 --> 57 wxyz9

}
