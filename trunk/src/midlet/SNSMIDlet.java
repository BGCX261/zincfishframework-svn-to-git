package midlet;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import screen.BrowserScreen;
import com.mediawoz.akebono.corerenderer.CRDisplay;

/**
 * 程序的主入口
 * 
 * @author Jarod Yv
 */
public class SNSMIDlet extends MIDlet {

	/** <code>{@link SNSMIDlet}</code>的静态实例 */
	public static final SNSMIDlet MIDLET = new SNSMIDlet();

	/** <code>{@link Display}</code>的静态实例 */
	public static final Display DISPLAY = Display.getDisplay(MIDLET);

	/**
	 * 构造函数
	 */
	public SNSMIDlet() {
		CRDisplay.init(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		CRDisplay.setCurrent(BrowserScreen.getInstance());
		BrowserScreen.getInstance()
				.loadUnit("/widgets/main/mainpage.xml", null);
	}

	/**
	 * 退出程序
	 */
	public static void Exit() {
		MIDLET.destroyApp(true);
		MIDLET.notifyDestroyed();
	}
}
