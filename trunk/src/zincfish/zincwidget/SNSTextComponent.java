package zincfish.zincwidget;

import utils.ArrayList;
import utils.DrawUtils;
import zincfish.zinccss.model.Alignment;
import zincfish.zinccss.model.Coordinates;
import zincfish.zinccss.model.Insets;
import zincfish.zinccss.model.Metrics;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSLabelDOM;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.fonts.CRSystemFont;

/**
 * <code>SNSTextComponent</code> 定义了SNS中的文字组件
 * 
 * @author Jarod Yv
 */
public class SNSTextComponent extends AbstractSNSComponent {
	private static final int STEP = 2;// 滚动的步长
	private static final int LINE_MARGIN = 2;// 行间距

	/** 文字绘制起始点的横坐标 */
	protected int textX;

	/** 文字绘制起始点的横坐标 */
	protected int textY;

	private int textWidth = 0;

	private int slideTextIncrement = 0;

	private int lineHight = 0;

	/* 显示的文字 */
	public String displayedString = null;

	private ArrayList displayStrings = null;

	protected byte wrap = SNSLabelDOM.WRAP_PLAIN;

	public void init(AbstractSNSDOM dom) {
		super.init(dom);
		SNSLabelDOM labelDOM = (SNSLabelDOM) this.dom;
		wrap = labelDOM.wrap;
		displayedString = labelDOM.text;
		if (labelDOM.onClick == null) {
			labelDOM.canFocus = false;
		} else if (labelDOM.classes == null)
			labelDOM.classes = "link";
		labelDOM = null;
	}

	public Metrics getPreferredSize(int preferredWidth, int nextLineWidth) {
		Metrics metrics = null;
		if (needToComputePreferredSize(preferredWidth)) {
			metrics = super.getPreferredSize(preferredWidth, nextLineWidth);
			StyleSet style = this.dom.getStyleSet();
			CRSystemFont font = style.getFont();
			Metrics minSize = style.getMinSize();
			if (displayedString != null) {
				metrics.firstLineWidth = metrics.width += Math.max(font
						.stringWidth(displayedString), minSize.width);
			} else {
				metrics.firstLineWidth = metrics.width;
			}
			metrics.lastLineHeight = metrics.height += Math.max(font
					.getHeight(), minSize.height);
			Coordinates gap = style.getGap();
			lineHight = font.getHeight() + (gap == null ? LINE_MARGIN : gap.Y);
			gap = null;
			minSize = null;
			style = null;

			// 当文字比给定宽度长时，进行相应处理
			if (metrics.width > preferredWidth) {
				if (wrap == SNSLabelDOM.WRAP_BREAK
						|| wrap == SNSLabelDOM.WRAP_MULTI) {// 文本要求折行时
					metrics.firstLineWidth = preferredWidth;
					if (font.charWidth(displayedString.charAt(0)) > preferredWidth) {
						metrics.firstLineWidth = nextLineWidth;
					}
					metrics.nextLineWidth = nextLineWidth;
					slideTextIncrement = metrics.nextLineWidth
							- metrics.firstLineWidth;
					// metrics.height += lineHight;
					int lineW = 0;
					boolean isFirst = true;
					for (int endIndex = 0; endIndex < displayedString.length(); endIndex++) {
						char ch = displayedString.charAt(endIndex);
						int charW = font.charWidth(ch);
						lineW += charW;
						if (lineW > (isFirst ? metrics.firstLineWidth
								: metrics.nextLineWidth)) {
							isFirst = false;
							lineW = charW;
							metrics.height += lineHight;
						}
					}
					metrics.width = lineW;
					metrics.lastLineHeight = lineHight;
				} else {
					metrics.width = preferredWidth;
				}
			}
			font = null;
		} else {
			metrics = getCachedMetrics();
		}
		return metrics;
	}

	private String getDisplayedString(int width) {
		if (wrap == SNSLabelDOM.WRAP_OMIT) {
			int tmpWidth = 0;
			CRSystemFont font = dom.getStyleSet().getFont();
			for (int i = 0; i < displayedString.length(); i++) {
				char ch = displayedString.charAt(i);
				tmpWidth += font.charWidth(ch);
				if (tmpWidth >= width) {
					displayedString = displayedString.substring(0, i) + "...";
					break;
				}
			}
			font = null;
		} else if (wrap == SNSLabelDOM.WRAP_BREAK
				|| wrap == SNSLabelDOM.WRAP_MULTI) {
			Metrics metrics = getCachedMetrics();
			if (metrics.nextLineWidth > 0) {
				int lineW = 0;
				int beginIndex = 0;
				boolean isFirst = true;
				CRSystemFont font = dom.getStyleSet().getFont();
				displayStrings = new ArrayList(2);
				for (int endIndex = 0; endIndex < displayedString.length(); endIndex++) {
					char ch = displayedString.charAt(endIndex);
					int charW = font.charWidth(ch);
					lineW += charW;
					if (lineW > (isFirst ? metrics.firstLineWidth
							: metrics.nextLineWidth)) {
						displayStrings.add(displayedString.substring(
								beginIndex, endIndex));
						beginIndex = endIndex;
						isFirst = false;
						lineW = charW;
					}
				}
				displayStrings.add(displayedString.substring(beginIndex));
				font = null;
			}
			metrics = null;
		}
		return displayedString;
	}

	protected void layout() {
		StyleSet style = dom.getStyleSet();
		Insets insets = style.getInsets();
		textX = insets.left;
		textY = insets.top;
		displayedString = getDisplayedString(iWidth - insets.left
				- insets.right);
		if (displayedString != null) {
			// 计算文字尺寸和位置
			CRSystemFont font = style.getFont();
			Alignment alignment = style.getAlign();
			textWidth = font.stringWidth(displayedString);
			int insetWidth = iWidth - insets.left - insets.right;
			int insetHeight = iHeight - insets.top - insets.bottom;
			if (alignment != null) {
				textX += alignment.alignX(insetWidth, textWidth);
				textY += alignment.alignY(insetHeight, font.getHeight());
			}
		}
		insets = null;
		style = null;
	}

	protected void paint(CRGraphics g) {
		StyleSet style = dom.getStyleSet();
		if (style != null) {
			if (displayStrings == null || wrap == SNSLabelDOM.WRAP_MULTI) {
				DrawUtils.paintBackground(g, iWidth, iHeight, style);// 绘制背景
				DrawUtils.paintBorder(g, iWidth, iHeight, style);// 绘制边框
			}
		}
		style = null;
		paintImpl(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincwidget.AbstractSNSComponent#paintImpl(com.mediawoz.akebono
	 * .corerenderer.CRGraphics)
	 */
	protected void paintImpl(CRGraphics g) {
		if (displayedString != null) {
			StyleSet style = dom.getStyleSet();
			Integer color = style.getColor();
			if (color != null) {
				g.setColor(color.intValue());
			} else {
				g.setColor(0x000000);
			}
			color = null;
			CRSystemFont font = style.getFont();
			if (displayStrings != null) {
				Metrics cachedMetrics = getCachedMetrics();
				for (int i = 0; i < displayStrings.size(); i++) {
					String str = (String) displayStrings.get(i);
					int x = textX - (i == 0 ? 0 : slideTextIncrement);
					int y = textY + lineHight * i;
					int w = (i == 0 ? cachedMetrics.firstLineWidth
							: cachedMetrics.nextLineWidth);
					w = (i == displayStrings.size() - 1) ? cachedMetrics.width
							: w;
					if (wrap == SNSLabelDOM.WRAP_BREAK) {
						DrawUtils.paintBackground(g, x, y, w, lineHight, style);
						DrawUtils.paintBorder(g, x, y, w, lineHight, style);
					}
					font.drawString(g, str, x, y, 20);
					str = null;
				}
				cachedMetrics = null;
			} else {
				int clipX = g.getClipX();
				int clipY = g.getClipY();
				int clipW = g.getClipWidth();
				int clipH = g.getClipHeight();
				g = DrawUtils.intersectG(g, textX, textY, textWidth, iHeight);
				font.drawString(g, displayedString, textX - slideTextIncrement,
						textY, 0);
				g.setClip(clipX, clipY, clipW, clipH);
				font = null;
				style = null;
				if (animate()) {
					slideTextIncrement += STEP;
					if (slideTextIncrement >= textWidth)
						slideTextIncrement = -iWidth;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.ui.UPanel#animate()
	 */
	protected boolean animate() {
		return isFocused && wrap == SNSLabelDOM.WRAP_SCROLL;
	}

	public void setMotion(int motionX, int motionY) {

	}
}
