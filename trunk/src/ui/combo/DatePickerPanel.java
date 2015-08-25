package ui.combo;

import java.util.Calendar;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import com.mediawoz.akebono.corerenderer.CRDisplay;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.coreservice.utils.CSDevice;
import config.Config;

/**
 * <code>DatePickerPanel</code>实现了弹出的日历组件
 * 
 * @author Jarod Yv
 */
public class DatePickerPanel extends AbstractComboPanel {
	/* 每一格的高度 */
	private static final int GRID_WIDTH = CRDisplay.getWidth() * 4 / 35;
	/* 每一格的宽度 */
	private static final int GRID_HEIGHT = Config.PLAIN_SMALL_FONT.getHeight() + 4;
	/* 每周对应的日子 */
	private static final String[] WEEKS = { "日", "一", "二", "三", "四", "五", "六" };
	/* 存储月份 */
	private static final String[] MONTHS = { "一月", "二月", "三月", "四月", "五月",
			"六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };
	private static final int ROW_MARGIN = 3;
	private static final int COL_MARGIN = 2;
	/* 背景颜色 */
	private static final int BG_COLOR = 0xFFFFFF;
	/* 外边框颜色 */
	private static final int BORDER_COLOR = 0xD7D7D7;
	/* 字体颜色 */
	private static final int FONT_COLOR = 0x363636;
	/* Cursor选中后的背景色 */
	private static final int SELECTED_COLOR = 0xC9E4D6;
	/* 选中日期后的背景色 */
	private static final int DATE_SELECTED_COLOR = 0x68838B;
	/* Week的背景色 */
	private static final int WEEK_COLOR = 0x99D1D3;
	/* 数字年, 月, 日 */
	private int nYear, nMonth, nToday;
	/* 整余 */
	private int remainder;
	/* 每月的一号是周几 */
	private int _1stDayOfWeek;
	/* 这个月有多少天 */
	private int daysOfMonth;
	/* 索引 当期Cursor落在的格子<li>-6为年份减 -5为月份减 -4为月份 -3为年份 -2为月份加 -1为年份加 0为日期 */
	private int index;
	/* 日期索引 日期导航指示标 */
	private int date_index;
	/* 下拉框Y轴, X轴 */
	private int cb_posX, cb_posY, cb_width;
	/* 下拉框的长度 */
	private int cb_length = 3;
	/* 换行标记 */
	private int minLineDate, maxLineDate;
	/* 中间确定键 确定键是否为激活状态 */
	private boolean _FIRE;
	/* 左右月份和年份加减图标 */
	private CRImage arrowLeftOfMonth;
	private CRImage arrowLeftOfYear;
	private CRImage arrowRightOfMonth;
	private CRImage arrowRightOfYear;

	/* 点的定位 */
	private int iPosY, iPosX;
	/* 行之间的距离 */
	private int iSpace;

	/* 时间值分隔符 */
	private static final String DATE_VALUE_SEPARATOR = "/";

	/* 该组件的唯一实例 */
	private static DatePickerPanel instance = null;

	/**
	 * 获取该组件的唯一实例
	 * 
	 * @return {@link #instance}
	 */
	public static DatePickerPanel getInstance() {
		if (instance == null)
			instance = new DatePickerPanel();
		instance.initDate();
		return instance;
	}

	private DatePickerPanel() {
		super(300, 0, 0, GRID_WIDTH * 7 + 6, GRID_HEIGHT * 8 + 8);
		index = -4;
		_FIRE = false;
		date_index = -1;
		// 加载图标
		arrowLeftOfMonth = CRImage
				.loadFromResource("/widgets/vote/img/arrowleft.png");
		arrowLeftOfYear = CRImage
				.loadFromResource("/widgets/vote/img/arrowdoubleleft.png");
		arrowRightOfMonth = CRImage
				.loadFromResource("/widgets/vote/img/arrowright.png");
		arrowRightOfYear = CRImage
				.loadFromResource("/widgets/vote/img/arrowdoubleright.png");
	}

	private void initDate() {
		Calendar cal = Calendar.getInstance();
		nYear = cal.get(Calendar.YEAR);
		nMonth = cal.get(Calendar.MONTH);
		nToday = cal.get(Calendar.DAY_OF_MONTH) - 1;
		cal = null;
		reviseDate();
	}

	protected void drawCurrentFrame(CRGraphics g) {
		// 防止月份的天数由大变小后 光标定位看不到
		nToday = (nToday >= daysOfMonth) ? daysOfMonth - 1 : nToday;
		// 画背景
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
		// 画边框
		g.setColor(BORDER_COLOR);
		g.drawRect(0, 0, getWidth(), getHeight());
		// 第一排 年份和月份 图标分配行距为20
		iPosY = COL_MARGIN;
		iPosX = ROW_MARGIN;
		if (index == -6) {
			g.setColor(SELECTED_COLOR);
			g.fillRect(iPosX, iPosY + 3, 20, GRID_HEIGHT - 6);
		}
		arrowLeftOfYear.draw(g, iPosX + (20 - arrowLeftOfYear.getWidth()) / 2,
				iPosY + (GRID_HEIGHT - arrowLeftOfYear.getHeight()) / 2,
				CRGraphics.TOP | CRGraphics.LEFT);
		iPosX = ROW_MARGIN + 20;
		if (index == -5) {
			g.setColor(SELECTED_COLOR);
			g.fillRect(iPosX, iPosY + 3, 20, GRID_HEIGHT - 6);
		}
		arrowLeftOfMonth.draw(g,
				iPosX + (20 - arrowLeftOfMonth.getWidth()) / 2, iPosY
						+ (GRID_HEIGHT - arrowLeftOfMonth.getHeight()) / 2,
				CRGraphics.TOP | CRGraphics.LEFT);
		iPosX = iPosX + 20;
		iSpace = (getWidth() - iPosX * 2 - Config.PLAIN_SMALL_FONT
				.stringWidth(MONTHS[nMonth] + nYear)) / 3;
		iPosX = iSpace + iPosX;
		if (index == -4) {
			g.setColor(BORDER_COLOR);
			cb_width = Config.PLAIN_SMALL_FONT.stringWidth(MONTHS[nMonth]) + 20;
			cb_posX = iPosX - 10;
			cb_posY = iPosY + GRID_HEIGHT;
			g.drawRect(cb_posX, iPosY + 1, cb_width, GRID_HEIGHT - 2);
			g.setColor(SELECTED_COLOR);
			g.fillRect(iPosX - 1, iPosY
					+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight() - 2)
					/ 2,
					Config.PLAIN_SMALL_FONT.stringWidth(MONTHS[nMonth]) + 2,
					Config.PLAIN_SMALL_FONT.getHeight() + 2);
		}
		g.setColor(FONT_COLOR);
		Config.PLAIN_SMALL_FONT.drawString(g, MONTHS[nMonth], iPosX, iPosY
				+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight()) / 2,
				CRGraphics.TOP | CRGraphics.LEFT);
		iPosX = iPosX + iSpace
				+ Config.PLAIN_SMALL_FONT.stringWidth(MONTHS[nMonth]);
		if (index == -3) {
			g.setColor(BORDER_COLOR);
			cb_width = Config.PLAIN_SMALL_FONT.stringWidth(nYear + "") + 20;
			cb_posX = iPosX - 10;
			cb_posY = iPosY + GRID_HEIGHT;
			g.drawRect(cb_posX, iPosY + 1, cb_width, GRID_HEIGHT - 2);
			g.setColor(SELECTED_COLOR);
			g.fillRect(iPosX - 1, iPosY
					+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight() - 2)
					/ 2, Config.PLAIN_SMALL_FONT.stringWidth(nYear + "") + 2,
					Config.PLAIN_SMALL_FONT.getHeight() + 2);
		}
		g.setColor(FONT_COLOR);
		Config.PLAIN_SMALL_FONT.drawString(g, nYear + "", iPosX, iPosY
				+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight()) / 2,
				CRGraphics.TOP | CRGraphics.LEFT);

		iPosX = getWidth() - ROW_MARGIN - 40;
		if (index == -2) {
			g.setColor(SELECTED_COLOR);
			g.fillRect(iPosX, iPosY + 3, 20, GRID_HEIGHT - 6);
		}
		arrowRightOfMonth.draw(g, iPosX + (20 - arrowRightOfMonth.getWidth())
				/ 2, iPosY + (GRID_HEIGHT - arrowRightOfMonth.getHeight()) / 2,
				CRGraphics.TOP | CRGraphics.LEFT);
		iPosX = iPosX + 20;
		if (index == -1) {
			g.setColor(SELECTED_COLOR);
			g.fillRect(iPosX, iPosY + 3, 20, GRID_HEIGHT - 6);
		}
		arrowRightOfYear.draw(g,
				iPosX + (20 - arrowRightOfYear.getWidth()) / 2, iPosY
						+ (GRID_HEIGHT - arrowRightOfYear.getHeight()) / 2,
				CRGraphics.TOP | CRGraphics.LEFT);
		// 画第二排 中文星期
		iPosY = iPosY + GRID_HEIGHT;
		iPosX = ROW_MARGIN;
		g.setColor(BORDER_COLOR);
		g.drawRect(iPosX, iPosY + 1, getWidth() - ROW_MARGIN * 2,
				GRID_HEIGHT * 7);
		g.setColor(WEEK_COLOR);
		g.fillRect(iPosX + 1, iPosY + 2, getWidth() - ROW_MARGIN * 2 - 2,
				GRID_HEIGHT - 4);
		g.setColor(FONT_COLOR);
		for (int i = 0; i < WEEKS.length; i++) {
			Config.PLAIN_SMALL_FONT.drawString(g, WEEKS[i], iPosX
					+ (GRID_WIDTH - Config.PLAIN_SMALL_FONT
							.stringWidth(WEEKS[i])) / 2, iPosY
					+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight()) / 2,
					CRGraphics.TOP | CRGraphics.LEFT);
			iPosX = iPosX + GRID_WIDTH;
		}
		// 第三排 日期
		iPosY = iPosY + GRID_HEIGHT;
		for (int i = 0; i < daysOfMonth; i++) {
			remainder = (i + _1stDayOfWeek) % 7;
			iPosX = ROW_MARGIN + remainder * GRID_WIDTH;
			if (i == nToday) {
				g.setColor(DATE_SELECTED_COLOR);
				g.fillRect(iPosX + 2, iPosY + 3, GRID_WIDTH - 4,
						GRID_HEIGHT - 6);
				g.setColor(FONT_COLOR);
			}
			if (nToday != date_index && date_index == i) {
				g.setColor(SELECTED_COLOR);
				g.fillRect(iPosX + 2, iPosY + 3, GRID_WIDTH - 4,
						GRID_HEIGHT - 6);
				g.setColor(FONT_COLOR);

			}
			Config.PLAIN_SMALL_FONT.drawString(g, (i + 1) + "", iPosX
					+ (GRID_WIDTH - Config.PLAIN_SMALL_FONT.stringWidth((i + 1)
							+ "")) / 2, iPosY
					+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight()) / 2,
					CRGraphics.TOP | CRGraphics.LEFT);
			if (remainder == 6 && i != daysOfMonth - 1) {
				iPosY = iPosY + GRID_HEIGHT;
			}
			if (i < 7 && remainder == 6) {
				minLineDate = i;
			}
			if (i >= daysOfMonth - 7 && remainder == 6) {
				if (i == daysOfMonth - 1) {
					maxLineDate = i - 7;
				} else {
					maxLineDate = i;
				}
			}
		}
		// 第四排 按钮
		// iPosY = gridHeight * 8 + colMargin;
		// iPosX = (getWidth() - Config.PLAIN_SMALL_FONT.stringWidth("确  定")) /
		// 2;
		// if (index == 1) {
		// crg.setColor(selectedColor);
		// } else {
		// crg.setColor(0xD7D7D5);
		// }
		// crg.fillRect(iPosX - 10, iPosY + (gridHeight + 4 -
		// Config.PLAIN_SMALL_FONT.getHeight()
		// - 2) / 2, Config.PLAIN_SMALL_FONT.stringWidth("确  定") + 20,
		// Config.PLAIN_SMALL_FONT.getHeight() + 2);
		// crg.setColor(textColor);
		// Config.PLAIN_SMALL_FONT.drawString(crg, "确  定", iPosX, iPosY +
		// (gridHeight + 4 -
		// Config.PLAIN_SMALL_FONT.getHeight()) / 2, CRGraphics.TOP |
		// CRGraphics.LEFT);
		// 触发月份事件
		if (index == -4 && _FIRE) {
			g.setColor(BG_COLOR);
			g.fillRect(cb_posX, cb_posY + 2, cb_width, GRID_HEIGHT * cb_length);
			g.setColor(BORDER_COLOR);
			g.drawRect(cb_posX, cb_posY + 2, cb_width, GRID_HEIGHT * cb_length);
			drawCombox(g, 0, cb_posX, (cb_posY + 2));
		}
		if (index == -3 && _FIRE) {
			g.setColor(BG_COLOR);
			g.fillRect(cb_posX, cb_posY + 2, cb_width, GRID_HEIGHT * cb_length);
			g.setColor(BORDER_COLOR);
			g.drawRect(cb_posX, cb_posY + 2, cb_width, GRID_HEIGHT * cb_length);
			drawCombox(g, 1, cb_posX, (cb_posY + 2));
		}
	}

	/** 画下拉框选项 <li>monthOryear 0为月 1 为年 */
	private void drawCombox(CRGraphics crg, int monthOryear, int oStartX,
			int oStartY) {
		if (monthOryear == 1) {
			crg.setColor(FONT_COLOR);
			Config.PLAIN_SMALL_FONT.drawString(crg, (nYear - 1) + "", oStartX
					+ (cb_width - Config.PLAIN_SMALL_FONT
							.stringWidth((nYear - 1) + "")) / 2, oStartY
					+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight()) / 2,
					Graphics.TOP | Graphics.LEFT);
			crg.setColor(SELECTED_COLOR);
			crg.fillRect(oStartX
					+ (cb_width - Config.PLAIN_SMALL_FONT
							.stringWidth((nYear - 1) + "")) / 2 - 1, oStartY
					+ GRID_HEIGHT
					+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight() - 2)
					/ 2,
					Config.PLAIN_SMALL_FONT.stringWidth((nYear - 1) + "") + 2,
					Config.PLAIN_SMALL_FONT.getHeight() + 2);
			crg.setColor(FONT_COLOR);
			Config.PLAIN_SMALL_FONT.drawString(crg, nYear + "", oStartX
					+ (cb_width - Config.PLAIN_SMALL_FONT
							.stringWidth((nYear - 1) + "")) / 2, oStartY
					+ GRID_HEIGHT
					+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight()) / 2,
					Graphics.TOP | Graphics.LEFT);
			Config.PLAIN_SMALL_FONT.drawString(crg, (nYear + 1) + "", oStartX
					+ (cb_width - Config.PLAIN_SMALL_FONT
							.stringWidth((nYear - 1) + "")) / 2, oStartY
					+ GRID_HEIGHT * 2
					+ (GRID_HEIGHT - Config.PLAIN_SMALL_FONT.getHeight()) / 2,
					Graphics.TOP | Graphics.LEFT);
		} else {
			crg.setColor(FONT_COLOR);
			if (nMonth < 2) {
				for (int i = 0; i < cb_length; i++) {
					if (i == nMonth) {
						crg.setColor(SELECTED_COLOR);
						crg.fillRect(oStartX
								+ (cb_width - Config.PLAIN_SMALL_FONT
										.stringWidth(MONTHS[nMonth])) / 2 - 1,
								oStartY
										+ GRID_HEIGHT
										* i
										+ (GRID_HEIGHT
												- Config.PLAIN_SMALL_FONT
														.getHeight() - 2) / 2,
								Config.PLAIN_SMALL_FONT
										.stringWidth(MONTHS[nMonth]) + 2,
								Config.PLAIN_SMALL_FONT.getHeight() + 2);
						crg.setColor(FONT_COLOR);
					}
					Config.PLAIN_SMALL_FONT.drawString(crg, MONTHS[nMonth],
							oStartX
									+ (cb_width - Config.PLAIN_SMALL_FONT
											.stringWidth(MONTHS[nMonth])) / 2,
							oStartY
									+ (GRID_HEIGHT * i)
									+ (GRID_HEIGHT - Font.getDefaultFont()
											.getHeight()) / 2, Graphics.TOP
									| Graphics.LEFT);
				}
			} else if (nMonth > 9) {
				for (int i = 9; i < 12; i++) {
					if (i == nMonth) {
						crg.setColor(SELECTED_COLOR);
						crg.fillRect(oStartX
								+ (cb_width - Config.PLAIN_SMALL_FONT
										.stringWidth(MONTHS[nMonth])) / 2 - 1,
								oStartY
										+ GRID_HEIGHT
										* (i - 9)
										+ (GRID_HEIGHT
												- Config.PLAIN_SMALL_FONT
														.getHeight() - 2) / 2,
								Config.PLAIN_SMALL_FONT
										.stringWidth(MONTHS[nMonth]) + 2,
								Config.PLAIN_SMALL_FONT.getHeight() + 2);
						crg.setColor(FONT_COLOR);
					}
					Config.PLAIN_SMALL_FONT.drawString(crg, MONTHS[nMonth],
							oStartX
									+ (cb_width - Config.PLAIN_SMALL_FONT
											.stringWidth(MONTHS[nMonth])) / 2,
							oStartY
									+ GRID_HEIGHT
									* (i - 9)
									+ (GRID_HEIGHT - Font.getDefaultFont()
											.getHeight()) / 2, Graphics.TOP
									| Graphics.LEFT);
				}
			} else {
				for (int i = nMonth - 1; i < nMonth + 2; i++) {
					if (i == nMonth) {
						crg.setColor(SELECTED_COLOR);
						crg.fillRect(oStartX
								+ (cb_width - Config.PLAIN_SMALL_FONT
										.stringWidth(MONTHS[nMonth])) / 2 - 1,
								oStartY
										+ GRID_HEIGHT
										* (i + 1 - nMonth)
										+ (GRID_HEIGHT
												- Config.PLAIN_SMALL_FONT
														.getHeight() - 2) / 2,
								Config.PLAIN_SMALL_FONT
										.stringWidth(MONTHS[nMonth]) + 2,
								Config.PLAIN_SMALL_FONT.getHeight() + 2);
						crg.setColor(FONT_COLOR);
					}
					Config.PLAIN_SMALL_FONT.drawString(crg, MONTHS[nMonth],
							oStartX
									+ (cb_width - Config.PLAIN_SMALL_FONT
											.stringWidth(MONTHS[nMonth])) / 2,
							oStartY
									+ GRID_HEIGHT
									* (i + 1 - nMonth)
									+ (GRID_HEIGHT - Font.getDefaultFont()
											.getHeight()) / 2, Graphics.TOP
									| Graphics.LEFT);
				}
			}
		}
	}

	/**
	 * @see com.mediawoz.akebono.coreanimation.CAAnimator#animate()
	 */
	protected boolean animate() {
		return false;
	}

	public boolean keyPressed(int iKeyCode) {
		int keyCode = CSDevice.getGameAction(iKeyCode);
		if (_FIRE == true) {
			switch (index) {
			case -4:
				if (keyCode == CSDevice.KEY_DOWN) {
					if (nMonth < 11) {
						nMonth = nMonth + 1;
					}
				}
				if (keyCode == CSDevice.KEY_UP) {
					if (nMonth > 0) {
						nMonth = nMonth - 1;
					}
				}
				break;
			case -3:
				if (keyCode == CSDevice.KEY_DOWN) {
					nYear = nYear + 1;
				}
				if (keyCode == CSDevice.KEY_UP) {
					nYear = nYear - 1;
				}
				break;
			}
			if (keyCode == CSDevice.KEY_FIRE) {
				_FIRE = (_FIRE == true) ? false : true;
			}
			reviseDate();
		} else {
			if (index < 0) {
				if (keyCode == CSDevice.KEY_LEFT) {
					if (index > -6) {
						index = index - 1;
					}
				}
				if (keyCode == CSDevice.KEY_RIGHT) {
					if (index < -1) {
						index = index + 1;
					}
				}
				if (keyCode == CSDevice.KEY_DOWN) {
					index = 0;
					date_index = 0;
				}
				if (keyCode == CSDevice.KEY_FIRE) {
					switch (index) {
					case -6:
						nYear = nYear - 1;
						break;
					case -5:
						if (nMonth > 0) {
							nMonth = nMonth - 1;
						}
						break;
					case -4:
					case -3:
						_FIRE = true;
						break;
					case -2:
						if (nMonth < 11) {
							nMonth = nMonth + 1;
						}
						break;
					case -1:
						nYear = nYear + 1;
						break;
					}
				}
				reviseDate();
			} else if (index == 0) {
				if (keyCode == CSDevice.KEY_LEFT) {
					if (date_index > 0) {
						date_index = date_index - 1;
					}
				}
				if (keyCode == CSDevice.KEY_RIGHT) {
					if (date_index < daysOfMonth - 1) {
						date_index = date_index + 1;
					}
				}
				if (keyCode == CSDevice.KEY_UP) {
					if (date_index <= minLineDate) {
						index = -4;
						date_index = -1;
					} else {
						date_index = date_index - 7;
						if (date_index < 0) {
							date_index = 0;
						}
					}
				}
				if (keyCode == CSDevice.KEY_DOWN) {
					if (date_index <= maxLineDate) {
						// index = 1;
						// date_index = -1;
						date_index = date_index + 7;
						if (date_index >= daysOfMonth) {
							date_index = daysOfMonth - 1;
						}
					}
				}
				if (keyCode == CSDevice.KEY_FIRE) {
					nToday = date_index;
					value = nYear + DATE_VALUE_SEPARATOR
							+ getBitDate((nMonth + 1), 2)
							+ DATE_VALUE_SEPARATOR
							+ getBitDate((nToday + 1), 2);
					return false;
					// if (keyCode == CSDevice.KEY_FIRE) {
					// }
				}
			}
			// else {
			// if (keyCode == CSDevice.KEY_UP) {
			// if (date_index <= minLineDate) {
			// index = 0;
			// date_index = lengthOfMonthDay - 1;
			// }
			// }
			// if (keyCode == CSDevice.KEY_FIRE) {
			// dateValue = nYear + dateValueSeparator + getBitDate((nMonth + 1),
			// 2) + dateValueSeparator + getBitDate((nToday + 1), 2);
			// return false;
			// }
			// }
			// 修改日历的日期
		}
		return true;
	}

	/**
	 * 日期补位
	 */
	private String getBitDate(int _date, int bit) {
		String str = String.valueOf(_date);
		if (str.length() < bit) {
			for (int i = 0; i < (bit - str.length()); i++) {
				str = "0" + str;
			}
		}
		return str;
	}

	/**
	 * 年份和月份修改核对
	 */
	private void reviseDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, nYear);
		cal.set(Calendar.MONTH, nMonth);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		_1stDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		daysOfMonth = getMonthDays(nYear, nMonth);
	}

	/**
	 * 获取每个月月份的天数
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return 该月的天数
	 */
	private int getMonthDays(int year, int month) {
		if (month == 1)
			return isLeapYear(year) ? 29 : 28;
		if (month < 7 && month % 2 == 0 || month > 6 && month % 2 == 1)
			return 31;
		return 30;
	}

	/**
	 * 判断是否是闰年
	 * 
	 * @param year
	 *            年份
	 * @return <ul>
	 *         <li><code>true</code> - 是闰年
	 *         <li><code>false</code> - 不是闰年
	 *         </ul>
	 */
	private boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (nYear % 400 == 0);
	}

	public boolean keyReleased(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean keyRepeated(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
