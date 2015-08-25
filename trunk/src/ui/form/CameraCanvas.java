package ui.form;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import screen.BrowserScreen;
import utils.ArrayList;
import utils.StringUtil;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zinclib.NetLib;
import zincfish.zincscript.ZSException;
import com.mediawoz.akebono.corerenderer.CRGraphics;

/**
 * <code>CameraCanvas</code>实现了拍照界面
 * 
 * @author Jarod Yv
 */
public class CameraCanvas extends Canvas implements CommandListener {

	private Player player;
	private VideoControl videoControl;
	CapturePicture capturePicture;
	String dot = "...";

	//FileDOM cameradom;
	Command takePicture; // 拍照
	Command back; // 返回

	/**
	 * @see zincfish.zincwidget.AbstractSNSComponent#init(zincfish.zincdom.AbstractSNSDOM)
	 */
	public CameraCanvas(AbstractSNSDOM dom) {
		setFullScreenMode(false);
		capturePicture = new CapturePicture();
		//cameradom = (FileDOM) dom;
		try {
			player = Manager.createPlayer("capture://video");
			player.realize();
			videoControl = (VideoControl) player.getControl("VideoControl");
			if (videoControl != null) {
				videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO,
						this);
			}
			videoControl.setDisplaySize(getWidth(), getHeight());
			videoControl.setDisplayFullScreen(true);
			videoControl.setVisible(true);
			player.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MediaException e) {
			e.printStackTrace();
		}

		takePicture = new Command("拍照", Command.SCREEN, 0);
		back = new Command("返回", Command.SCREEN, 1);

		addCommand(takePicture);
		addCommand(back);

		setCommandListener(this);
	}

	/**
	 * @see zincfish.zincwidget.AbstractSNSComponent#paintImpl(com.mediawoz.akebono.corerenderer.CRGraphics)
	 */
	protected void paint(Graphics g) {
		g.setColor(0xFFFFFF);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(0x595959);
		g.drawString("正在处理图片" + dot, (getWidth() - g.getFont().stringWidth(
				"正在处理图片")) / 2, getHeight() / 2, CRGraphics.TOP
				| CRGraphics.LEFT);
		// g.setColor(0x1B4F93);
		// g.drawString("拍照", 2, getHeight() - (20 - g.getFont().getHeight()) /
		// 2, Graphics.BOTTOM | Graphics.TOP);
		// g.drawString("返回", 2, getHeight() - (20 - g.getFont().getHeight()) /
		// 2, Graphics.BOTTOM | Graphics.TOP);
	}

	public void commandAction(Command c, Displayable d) {
		if (capturePicture == null) {
			return;
		}
		if (takePicture == c) {
			capturePicture.vc = videoControl;
			new Thread(capturePicture).start();
		} else if (back == c) {
			closeDiplay();
		}
	}

	/**
	 * @see com.mediawoz.akebono.ui.IKeyInputHandler#keyPressed(int)
	 */
	// public void keyPressed(int keyCode) {
	// int actionKey = CSDevice.getGameAction(keyCode);
	// if(actionKey == CSDevice.KEY_FIRE || keyCode == CSDevice.NK_LSOFT) {
	// capturePicture.vc = videoControl;
	// new Thread(capturePicture).start();
	// } else if(keyCode == CSDevice.NK_RSOFT) {
	// SNSMIDlet.crdisplay.setCurrent(SNSMIDlet.curdisplayable);
	// }
	// }

	public class CapturePicture implements Runnable {

		byte[] picdata;
		VideoControl vc;
		String path = null;

		public void setVideoControl(VideoControl vc) {
			this.vc = vc;
		}

		public void run() {
			try {
				try {
					picdata = vc.getSnapshot(null);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				String curtime = "" + System.currentTimeMillis();
				String imgname = "png"
						+ curtime.substring(curtime.length() - 4);
				// 判断目录是否存在
				FileConnection direxsit = (FileConnection) Connector.open(
						"file:///c:/", Connector.READ_WRITE);

				// 判断是否存在pictures目录
				String rootdir = StringUtil.getStartWithDir(direxsit.list(),
						"pictures", direxsit.getURL());
				if (rootdir == null) {
					rootdir = StringUtil.getStartWithDir(direxsit.list(),
							"camera", direxsit.getURL());
				}
				if (rootdir == null) {
					rootdir = StringUtil.getStartWithDir(direxsit.list(),
							"image", direxsit.getURL());
				}
				if (rootdir == null) {
					rootdir = StringUtil.getStartWithDir(direxsit.list(),
							"other", direxsit.getURL());
				}
				if (rootdir == null) {
					Enumeration rootList = FileSystemRegistry.listRoots();
					while (rootList.hasMoreElements()) {
						rootdir = (String) rootList.nextElement();
					}
					rootdir = "file:///" + rootdir;
					// return;
				}
				direxsit.close();
				path = rootdir + imgname + ".png";
				FileConnection filecono = (FileConnection) Connector.open(path,
						Connector.READ_WRITE);
				if (filecono.exists()) {
					filecono.delete();
					filecono.create();
				} else {
					filecono.create();
				}
				DataOutputStream dos = filecono.openDataOutputStream();
				dos.write(picdata);
				dos.flush();
				dos.close();
				filecono.close();
			} catch (Exception e) {
				// 发生异常返回到先前的unit
				closeDiplay();
				e.printStackTrace();
				return;
			}
			BrowserScreen.getInstance().loadUnit("/widgets/album/upload.xml",
					null); // phtext1
			String url = "http://test.3g.cn/sns/interface/AlbumSpecList2.aspx";
			ArrayList params = new ArrayList(1);
			params.add(url);
//			AbstractSNSDOM targetDom = DOMUtil.getDOMByID(BrowserScreen
//					.getInstance().getBuffer().getCurrentBuffer(),
//					cameradom.domid);
//			if (targetDom != null) {
//				targetDom.setCommonValue(cameradom.domattr, path);
//				targetDom.setSubAttributeValue(cameradom.domattr, path);
//				if (targetDom.getComponent() instanceof SNSTextFieldComponent) {
//					((SNSTextFieldComponent) targetDom.getComponent())
//							.setText(path);
//				}
//			}
			try {
				NetLib.getInstance().callFunction("_zsnSend", params);
			} catch (ZSException e) {
				e.printStackTrace();
			}
			closeDiplay();
		}
	}

	public void closeDiplay() {
		player.close();
		//SNSMIDlet.DISPLAY.setCurrent(SNSMIDlet.curdisplayable);
	}
}
