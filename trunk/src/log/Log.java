package log;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

/**
 * 一个通用的日志工具。可以将信息输出到控制台、Form或者写入文件 <br>
 * 方便在真机上调试
 * 
 * @author Jarod Yv
 */
public class Log {
	// //////////////// 日志的输出级别 ////////////////////
	public static final byte LEVEL_CLOSE = 0;
	public static final byte LEVEL_INFO = 1;
	public static final byte LEVEL_DEBUG = 2;
	public static final byte LEVEL_ERROR = 3;
	public static final byte LEVEL_FATAL = 4;
	public static final byte LEVEL_ALL = 5;
	// //////////////////////////////////////////////////
	// //////////////// 日志的显示方式 ////////////////////
	public static final byte MODE_CONSOLE = 1;
	public static final byte MODE_FORM = 2;
	public static final byte MODE_FILE = 3;
	// //////////////////////////////////////////////////

	/** 日志的输出级别 */
	private byte level;

	/** 日志的输出模式 */
	private byte outputMode;

	/**
	 * 每一条日志的内容<br>
	 * 此处用StringBuffer方便对字符进行连接
	 */
	private StringBuffer log;

	/** 日志的名称 */
	private String name;

	/** 日志的数量 */
	private static int infoNums = 0;

	/** 显示日志的窗口。当且仅当outputMode == MODE_FORM时才被初始化 */
	private static LogForm form = null;

	// private static Log instance = null;

	public static LogForm DebugForm() throws NullPointerException {
		if (form == null)
			throw new NullPointerException();
		return form;
	}

	private Log(String className) {
		// this(className, LEVEL_ALL, MODE_FILE);
		// this(className, LEVEL_ALL, MODE_CONSOLE);
		this(className, LEVEL_DEBUG, MODE_CONSOLE);
		// this(className, LEVEL_DEBUG, MODE_FILE);
		// this(className, LEVEL_DEBUG, MODE_FORM);
	}

	private Log(String className, byte level, byte mode) {
		this.name = className;
		if (this.log == null) {
			this.log = new StringBuffer();
		}
		setLevel(level);
		setOutputMode(mode);
	}

	public static Log getLog(String className) {
		// if (instance == null)
		// instance = new Log(className);
		// return instance;
		return new Log(className);
	}

	public static Log getLog(String className, byte level, byte mode) {
		return new Log(className, level, mode);
	}

	public void info(Object mg) {
		if (level >= LEVEL_INFO) {
			log.append("Info:<");
			log.append(name);
			log.append(">:");
			log.append(mg);
			log.append('\n');
			println(log.toString());
		}
	}

	public void debug(Object mg) {
		if (level >= LEVEL_DEBUG) {
			log.append("debug:<");
			log.append(name);
			log.append(">:");
			log.append(mg);
			log.append('\n');
			println(log.toString());
		}
	}

	public void debug(boolean mg) {
		if (level >= LEVEL_DEBUG) {
			log.append("debug:<");
			log.append(name);
			log.append(">:");
			log.append(mg);
			log.append('\n');
			println(log.toString());
		}
	}

	public void debug(int mg) {
		if (level >= LEVEL_DEBUG) {
			log.append("debug:<");
			log.append(name);
			log.append(">:");
			log.append(mg);
			println(log.toString());
		}
	}

	public void error(Object mg) {
		if (level >= LEVEL_ERROR) {
			log.append("Error:<");
			log.append(name);
			log.append(">:");
			log.append(mg);
			log.append('\n');
			println(log.toString());
		}
	}

	public void error(Exception e, Object mg) {
		if (level >= LEVEL_ERROR) {
			log.append("Error:<");
			log.append(name);
			log.append(">:");
			log.append(mg);
			log.append('\n');
			println(e, log.toString());
		}
	}

	public void fatal(Exception e, Object mg) {
		if (level >= LEVEL_FATAL) {
			log.append("Fatal:<");
			log.append(name);
			log.append(">:");
			log.append(mg);
			log.append('\n');
			println(e, log.toString());
		}
	}

	private void println(Exception e, Object mg) {
		println(mg);
		if (e != null) {
			e.printStackTrace();
		}
	}

	private void println(Object mg) {
		infoNums++;
		log.insert(0, '\n');
		// log.insert(0, Runtime.getRuntime().freeMemory());
		// log.insert(0, " FreeMemory = ");
		log.insert(0, infoNums);
		switch (outputMode) {
		case MODE_CONSOLE:
			System.out.println(log.toString());
			break;
		case MODE_FORM:
			form.append(log.toString());
			break;
		case MODE_FILE:
			write2File(log.toString());
			break;
		}
		log.delete(0, log.length());
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	private static FileConnection fc = null;
	private static DataOutputStream ous = null;
	private static final String FILE_URL = "file:///";
	// private static String root = null;
	private static String root = "file:///c:/Data/";
	private static final String FOLDER = "sns";
	private static final String FILE_NAME = "snslog";

	private void createFile() {
		System.out.println("Log=======createFile start======================");
		if (ous != null)
			return;
		String version = System
				.getProperty("microedition.io.file.FileConnection.version");
		if (version != null) {
			if (root == null) {
				root = FILE_URL + getDefaultRoot();
			}
			System.out.println("Log=======createFile 0======================"
					+ getDefaultRoot());
			try {
				fc = (FileConnection) Connector.open(root + FOLDER + "/",
						Connector.READ_WRITE);
				if (!fc.exists()) {
					fc.mkdir();
				}
				fc.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.out.println("Log=======createFile 1======================");

			try {
				fc = (FileConnection) Connector.open(root + FOLDER + "/"
						+ FILE_NAME + ".txt", Connector.READ_WRITE);
				if (fc.exists()) {
					fc.delete();
				}
				fc.create();
				ous = fc.openDataOutputStream();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			System.out
					.println("Log=======createFile  else 0======================");
			setOutputMode(MODE_FORM);
			System.out
					.println("Log=======createFile  else 1======================");
		}
		System.out.println("Log=======createFile End======================");
	}

	private String getDefaultRoot() {
		String sRoot = null;
		Enumeration enumDrive = FileSystemRegistry.listRoots();
		while (enumDrive.hasMoreElements())
			sRoot = (String) enumDrive.nextElement();
		return sRoot;
	}

	private void write2File(String str) {
		if (ous == null)
			return;
		try {
			// byte[] strArrayByte = (String2ByteArray4BigEncode(str));
			// ous.write(strArrayByte, 0, strArrayByte.length);
			ous.write(str.getBytes());
			ous.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public byte getOutputMode() {
		return outputMode;
	}

	public void setOutputMode(byte outputMode) {
		if (this.outputMode != outputMode) {
			this.outputMode = outputMode;
			if (this.outputMode == MODE_FORM) {
				if (form == null)
					form = new LogForm();
			} else if (this.outputMode == MODE_FILE) {
				createFile();
			}
		}
	}

	/**
	 * 显示日志的Form类，简单的提供了退出和清空的功能
	 * 
	 * @author Jarod Yv
	 */
	public class LogForm extends Form implements CommandListener {

		// private Displayable displayable;

		private Command cmd_exit = new Command("退出", Command.EXIT, Command.EXIT);
		private Command cmd_write = new Command("清空", Command.ITEM,
				Command.SCREEN);

		public LogForm() {
			super("Log Form");
			this.addCommand(cmd_exit);
			this.addCommand(cmd_write);
			this.setCommandListener(this);
		}

		public void commandAction(Command c, Displayable d) {
			if (c == cmd_exit) {
				// GGMidlet.display.setCurrent(displayable);
			} else if (c == cmd_write) {
				this.deleteAll();
			}
		}

		public void setDisplayable(Displayable displayable) {
			// this.displayable = displayable;
		}
	}
}
