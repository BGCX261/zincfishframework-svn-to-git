package zincfish.zincwidget;

import screen.BrowserScreen;
import utils.ArrayList;
import zincfish.zinccss.CSSParser;
import zincfish.zincdom.SNSUnitDOM;
import zincfish.zincscript.ZSException;
import zincfish.zincscript.ZincScript;
import com.mediawoz.akebono.corerenderer.CRGraphics;

/**
 * <code>SNSUnitComponent</code> 是所有显示组件的跟. 对所有独立界面单元具有管理作用.
 * 
 * @author Jarod Yv
 */
public class SNSUnitComponent extends AbstractSNSComponent {

	private boolean needLoadCSS = true;
	private boolean needLoadScript = true;

	public void loadCSS() {
		if (needLoadCSS) {
			// 解析CSS
			String cssFile = ((SNSUnitDOM) this.dom).getCSSFile();
			try {
				CSSParser.getInstance().parseCSS(cssFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			needLoadCSS = false;
			cssFile = null;
		}
	}

	public void loadScript() {
		if (needLoadScript) {
			ArrayList scriptFiles = ((SNSUnitDOM) this.dom).getScriptFiles();
			if (scriptFiles != null && scriptFiles.size() > 0) {
				ZincScript zinc = BrowserScreen.getInstance().getZinc();
				try {
					zinc.reset();
					for (int i = 0; i < scriptFiles.size(); i++) {
						String zs = (String) scriptFiles.get(i);
						zinc.loadScript(zs);
						zs = null;
					}
					zinc.executeScript();
				} catch (ZSException e) {
					e.printStackTrace();
				} finally {
					zinc = null;
				}
			}
			needLoadScript = false;
			scriptFiles = null;
		}
	}

	public void setMotion(int motionX, int motionY) {

	}

	// long curTime = 0;

	protected void paintImpl(CRGraphics g) {
		// long preTime = curTime;
		// curTime = System.currentTimeMillis();
		// g.setColor(0xff0000);
		// Config.BOLD_SMALL_FONT.drawString(g, "MSPF:" + (curTime - preTime),
		// 100, 10, 20);
	}
}
