package ui.form;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;

/**
 * <code>RecordCanvas</code>是录音界面
 * 
 * @author Jarod Yv
 */
public class RecordCanvas extends Canvas implements Runnable {
	private static byte STATUS_STOP = 0x00;
	private static byte STATUS_RECORDING = 0x01;
	private static byte STATUS_PAUSE = 0x02;

	/** <code>RecordCanvas</code> 的静态实例 */
	private static RecordCanvas instance = null;

	/** <code>RecordCanvas</code> 的静态实例 */
	private boolean isRecording = false;
	private Player player;
	private RecordControl recordControl;

	private String text;
	private int hour;
	private int minute;
	private int second;
	private String audiourl;

	/**
	 * 获取唯一实例
	 * 
	 * @return
	 */
	public static RecordCanvas getInstance() {
		if (instance == null)
			instance = new RecordCanvas();
		return instance;
	}

	/**
	 * 私有构造函数
	 */
	private RecordCanvas() {
		setFullScreenMode(true);
	}

	/**
	 * 开始录音
	 */
	public void startRecord() {
		Thread thread = new Thread(this);
		thread.start();
		thread = null;
	}

	public void calTimes(int s) {
		second = s % 60;
		minute = (s / 60) % 60;
		hour = (s / (60 * 60)) % 60;
	}

	public void stopRecord() {

	}

	/**
	 * @see javax.microedition.lcdui.Canvas#paint(javax.microedition.lcdui.Graphics)
	 */
	protected void paint(Graphics g) {
		g.setColor(0xFFFFFF);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(0x292929);
		g.drawString("录音界面",
				(getWidth() - g.getFont().stringWidth("录音界面")) / 2, (20 - g
						.getFont().getHeight()) / 2, Graphics.TOP
						| Graphics.LEFT);

		// 绘制录音时间
		String times = null;
		if (hour > 0) {
			times = getIString(hour) + "小时" + getIString(minute) + "分"
					+ getIString(second) + "秒";
			g.drawString(times,
					(getWidth() - g.getFont().stringWidth(times)) / 2,
					getHeight() / 2, Graphics.BOTTOM | Graphics.LEFT);
		} else {
			times = getIString(minute) + "分" + getIString(second) + "秒";
			g.drawString(times,
					(getWidth() - g.getFont().stringWidth(times)) / 2,
					getHeight() / 2, Graphics.BOTTOM | Graphics.LEFT);
		}
		if (audiourl != null) {
			g.drawString(audiourl, (getWidth() - g.getFont().stringWidth(
					audiourl)) / 2, getHeight() / 2 + 30, Graphics.BOTTOM
					| Graphics.LEFT);
		}

		text = isRecording ? "停止" : "启动";
		g.drawString(text, (getWidth() - g.getFont().stringWidth("启动")) / 2,
				getHeight() - (20 - g.getFont().getHeight()) / 2,
				Graphics.BOTTOM | Graphics.LEFT);

		g.drawString("返回", getWidth() - g.getFont().stringWidth("启动") - 2,
				getHeight() - (20 - g.getFont().getHeight()) / 2,
				Graphics.BOTTOM | Graphics.LEFT);
	}

	protected void keyPressed(int keyCode) {
		if (keyCode == 5 || keyCode == -5) {
			if (isRecording) {
				minute = 0;
				second = 0;
				hour = 0;
				startRecord();
			} else {
				stopRecord();
			}
		}
		if (keyCode == -7 || keyCode == -7) {

		}
		repaint();
	}

	public String getIString(int num) {
		if (num < 10) {
			return "0" + num;
		}
		return "" + num;
	}

	public String getDir(Enumeration _enum, String dir, String prefix) {
		String elements;
		while (_enum.hasMoreElements()) {
			elements = (String) _enum.nextElement();
			System.out.println(elements + "--->" + dir);
			if (elements.startsWith(dir) && elements.endsWith("/")) {
				return prefix + elements;
			}
		}
		System.out.println("-----getDir End-----------");
		return null;
	}

	public void run() {
		long times = 0L;
		byte[] recorddata = null;
		try {
			player = Manager.createPlayer("capture://audio");
			player.realize();
			recordControl = (RecordControl) player.getControl("RecordControl");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			recordControl.setRecordStream(baos);
			recordControl.startRecord();
			// 开始计时
			times = 0;
			long startTimes = System.currentTimeMillis();
			// 开始录音
			player.start();
			while (isRecording) {
				Thread.sleep(1000L);
				times = System.currentTimeMillis() - startTimes;
				calTimes((int) (times / 1000));
				repaint();
			}
			// 录音停止
			recordControl.commit();
			// 存储数据
			recorddata = baos.toByteArray();
			// 关闭
			baos.close();
			player.close();

			// 写数据到文件
			String curtime = "" + System.currentTimeMillis();
			String audioname = "audio"
					+ curtime.substring(curtime.length() - 6);
			System.out.println("file name=" + audioname);
			// 判断目录是否存在
			FileConnection direxsit = (FileConnection) Connector.open(
					"file:///c:/", Connector.READ_WRITE);
			System.out.println("URL-->" + direxsit.getURL());
			String rootdir = getDir(direxsit.list(), "music", direxsit.getURL());
			if (rootdir == null) {
				System.out.println("URL-1->" + direxsit.getURL());
				rootdir = getDir(direxsit.list(), "videos", direxsit.getURL());
			}
			if (rootdir == null) {
				System.out.println("URL-3->" + direxsit.getURL());
				rootdir = getDir(direxsit.list(), "other", direxsit.getURL());
			}
			direxsit.close();
			if (rootdir == null) {
				Enumeration rootList = FileSystemRegistry.listRoots();
				while (rootList.hasMoreElements()) {
					rootdir = (String) rootList.nextElement();
				}
				rootdir = "file:///" + rootdir;
			}

			FileConnection filecono = (FileConnection) Connector.open(rootdir
					+ audioname + ".amr", Connector.READ_WRITE);
			if (filecono.exists()) {
				filecono.delete();
				filecono.create();
			} else {
				filecono.create();
			}
			DataOutputStream dos = filecono.openDataOutputStream();
			dos.write(recorddata);
			dos.flush();
			dos.close();
			audiourl = filecono.getURL();
			filecono.close();
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
