package config;

import java.util.Hashtable;

import com.mediawoz.akebono.corerenderer.CRImage;

public final class Resources {
	private static Resources instance = null;

	public static Resources getInstance() {
		if (instance == null) {
			instance = new Resources();
		}
		return instance;
	}

	private CRImage list_selector = null;
	private CRImage list_cover = null;
	private CRImage list_ltail1 = null;
	private CRImage list_ltail2 = null;
	private CRImage[] iconList = null;
	private CRImage textfield_left = null;
	private CRImage textfield_right = null;
	private CRImage more = null;

	private CRImage texteditor_lt = null;
	private CRImage texteditor_ld = null;
	private CRImage texteditor_rt = null;
	private CRImage texteditor_rd = null;

	private CRImage t_l = null;
	private CRImage t_r = null;
	private CRImage b_l = null;
	private CRImage b_r = null;

	private CRImage[] emotion_faces = null;
	private CRImage default_album = null;
	private CRImage comment = null;
	private CRImage comment1 = null;
	private CRImage back = null;
	private CRImage back1 = null;

	private static final String[] EMOTION_SYMBOL = { ":-)", ":-(", ":-D",
			":-W", ":-8", ":-O", ":-P", ":-V", ":-Z", ":-C" };

	/* 左箭头 */
	private CRImage left_arrow = null;
	/* 高亮的左箭头 */
	private CRImage left_arrow_highlight = null;
	/* 右箭头 */
	private CRImage right_arrow = null;
	/* 高亮的右箭头 */
	private CRImage right_arrow_highlight = null;

	private CRImage point = null;

	private CRImage lightPoint = null;

	/**
	 * @return the list_select_lt
	 */
	public CRImage getListSelector() {
		if (list_selector == null)
			list_selector = CRImage
					.loadFromResource("/widgets/diary/img/selector.png");
		return list_selector;
	}

	public CRImage getListTail1() {
		if (list_ltail1 == null)
			list_ltail1 = CRImage
					.loadFromResource("/widgets/main/img/read.png");
		return list_ltail1;
	}

	public CRImage getListTail2() {
		if (list_ltail2 == null)
			list_ltail2 = CRImage
					.loadFromResource("/widgets/diary/img/replay.png");
		return list_ltail2;
	}

	public CRImage getListCover() {
		if (list_cover == null) {
			list_cover = CRImage.createImage(235, 10);
			list_cover.setAlpha(25);
		}
		return list_cover;
	}

	public CRImage getIcon(int index) {
		if (iconList == null) {
			iconList = new CRImage[6];
			iconList[0] = CRImage
					.loadFromResource("/widgets/diary/img/ico1.png");
			iconList[1] = CRImage
					.loadFromResource("/widgets/diary/img/ico2.png");
			iconList[2] = CRImage
					.loadFromResource("/widgets/diary/img/ico3.png");
			iconList[3] = CRImage
					.loadFromResource("/widgets/diary/img/ico4.png");
			iconList[4] = CRImage
					.loadFromResource("/widgets/diary/img/ico5.png");
			iconList[5] = CRImage
					.loadFromResource("/widgets/diary/img/ico7.png");
		}
		return iconList[index];
	}

	/**
	 * @return the textfield_left
	 */
	public CRImage getTextfield_left() {
		if (textfield_left == null)
			textfield_left = CRImage.loadFromResource("/img/tfl.png");
		return textfield_left;
	}

	/**
	 * @return the textfield_right
	 */
	public CRImage getTextfield_right() {
		if (textfield_right == null)
			textfield_right = CRImage.loadFromResource("/img/tfr.png");
		return textfield_right;
	}

	/**
	 * @return the texteditor_lt
	 */
	public CRImage getTexteditor_lt() {
		if (texteditor_lt == null)
			texteditor_lt = CRImage.loadFromResource("/img/telt.png");
		return texteditor_lt;
	}

	/**
	 * @return the texteditor_ld
	 */
	public CRImage getTexteditor_ld() {
		if (texteditor_ld == null)
			texteditor_ld = CRImage.loadFromResource("/img/teld.png");
		return texteditor_ld;
	}

	/**
	 * @return the texteditor_rt
	 */
	public CRImage getTexteditor_rt() {
		if (texteditor_rt == null)
			texteditor_rt = CRImage.loadFromResource("/img/tert.png");
		return texteditor_rt;
	}

	/**
	 * @return the texteditor_rd
	 */
	public CRImage getTexteditor_rd() {
		if (texteditor_rd == null)
			texteditor_rd = CRImage.loadFromResource("/img/terd.png");
		return texteditor_rd;
	}

	public CRImage[] getEmotion_faces() {
		if (emotion_faces == null)
			emotion_faces = new CRImage[10];
		for (int i = 0; i < emotion_faces.length; i++)
			emotion_faces[i] = CRImage.loadFromResource("/img/face/" + i
					+ ".png");
		return emotion_faces;
	}

	/**
	 * @return the left_arrow
	 */
	public CRImage getLeft_arrow() {
		if (left_arrow == null)
			left_arrow = CRImage.createImage(getRight_arrow(), 0, 0,
					getRight_arrow().getWidth(), getRight_arrow().getHeight(),
					CRImage.FLIP_ROT180);
		return left_arrow;
	}

	private CRImage loadingImage = null;

	public CRImage getLoadingImage() {
		if (loadingImage == null)
			loadingImage = CRImage.loadFromResource("/img/hourglass.png");
		return loadingImage;
	}

	/**
	 * @return the left_arrow_highlight
	 */
	public CRImage getLeft_arrow_highlight() {
		if (left_arrow_highlight == null)
			left_arrow_highlight = CRImage
					.createImage(getRight_arrow_highlight(), 0, 0,
							getRight_arrow_highlight().getWidth(),
							getRight_arrow_highlight().getHeight(),
							CRImage.FLIP_ROT180);
		return left_arrow_highlight;
	}

	/**
	 * @return the right_arrow
	 */
	public CRImage getRight_arrow() {
		if (right_arrow == null)
			right_arrow = CRImage
					.loadFromResource("/widgets/diary/img/right_arrow.png");
		return right_arrow;
	}

	/**
	 * @return the right_arrow_highlight
	 */
	public CRImage getRight_arrow_highlight() {
		if (right_arrow_highlight == null)
			right_arrow_highlight = CRImage
					.loadFromResource("/widgets/diary/img/right_arrow1.png");
		return right_arrow_highlight;
	}

	/**
	 * @return the default_album
	 */
	public CRImage getDefault_album() {
		if (default_album == null)
			default_album = CRImage
					.loadFromResource("/widgets/album/img/portrait.png");
		return default_album;
	}

	private CRImage pop = null;

	public CRImage getPop() {
		if (pop == null)
			pop = CRImage.loadFromResource("/widgets/album/img/popup.png");
		return pop;
	}

	private CRImage popTail = null;

	public CRImage getPopTail() {
		if (popTail == null)
			popTail = CRImage.loadFromResource("/widgets/album/img/dialog.png");
		return popTail;
	}

	private CRImage privacy_friendly = null;

	public CRImage getPrivacy_friendly() {
		if (privacy_friendly == null)
			privacy_friendly = CRImage
					.loadFromResource("/widgets/album/img/key.png");
		return privacy_friendly;
	}

	private CRImage privacy_private = null;

	public CRImage getPrivacy_private() {
		if (privacy_private == null)
			privacy_private = CRImage
					.loadFromResource("/widgets/album/img/lock.png");
		return privacy_private;
	}

	/**
	 * @return the t_l
	 */
	public CRImage getT_l() {
		if (t_l == null)
			t_l = CRImage.loadFromResource("/widgets/album/img/t_l.png");
		return t_l;
	}

	/**
	 * @return the t_r
	 */
	public CRImage getT_r() {
		if (t_r == null)
			t_r = CRImage.loadFromResource("/widgets/album/img/t_r.png");
		return t_r;
	}

	/**
	 * @return the b_l
	 */
	public CRImage getB_l() {
		if (b_l == null)
			b_l = CRImage.loadFromResource("/widgets/album/img/b_l.png");
		return b_l;
	}

	/**
	 * @return the b_r
	 */
	public CRImage getB_r() {
		if (b_r == null)
			b_r = CRImage.loadFromResource("/widgets/album/img/b_r.png");
		return b_r;
	}

	private Hashtable smiles = null;

	private void generateSmiles() {
		smiles = new Hashtable(10);
		for (int i = 0; i < EMOTION_SYMBOL.length; i++) {
			CRImage img = CRImage.loadFromResource("/img/face/" + i + ".png");
			smiles.put(EMOTION_SYMBOL[i], img);
			img = null;
		}
	}

	public CRImage getSmile(String symbol) {
		if (smiles == null)
			generateSmiles();
		return (CRImage) smiles.get(symbol);
	}

	private CRImage maskImage = null;

	public CRImage getMaskImage(int w) {
		if (maskImage == null || maskImage.getWidth() != w) {
			maskImage = null;
			maskImage = CRImage.createImage(w, w);
			maskImage.setAlpha(40);
		}
		return maskImage;
	}

	private CRImage chechedCheckBox = null;
	private CRImage checkedFocusedCheckBox = null;
	private CRImage unchechedCheckBox = null;
	private CRImage unchechFocusedCheckBox = null;

	private CRImage chechedRadioBox = null;
	private CRImage checkedFocusedRadioBox = null;
	private CRImage unchechedRadioBox = null;
	private CRImage unchechFocusedRadioBox = null;

	public CRImage getComment() {
		if (comment == null)
			comment = CRImage
					.loadFromResource("/widgets/diary/img/comment.png");
		return comment;
	}

	public CRImage getComment1() {
		if (comment1 == null)
			comment1 = CRImage
					.loadFromResource("/widgets/diary/img/comment1.png");
		return comment1;
	}

	public CRImage getBack() {
		if (back == null)
			back = CRImage.loadFromResource("/widgets/diary/img/back.png");
		return back;
	}

	public CRImage getBack1() {
		if (back1 == null)
			back1 = CRImage.loadFromResource("/widgets/diary/img/back1.png");
		return back1;
	}

	private CRImage bigRightArrow = null;

	public CRImage getBigRightArrow() {
		if (bigRightArrow == null)
			bigRightArrow = CRImage
					.loadFromResource("/widgets/album/img/bigarrow.png");
		return bigRightArrow;
	}

	/** @Add by jiangwei 下面是投票的图片资源 */
	/** 左圆角 */
	private CRImage smallleftImage;
	private CRImage smallselectedLeft;
	/** 中间部分 */
	private CRImage smallcenterImage;
	private CRImage smallselectedCenter;
	/** 右圆角 */
	private CRImage smallrightImage;
	private CRImage smallselectedRight;
	/** 下拉框图标 */
	private CRImage dropDownBG;
	private CRImage dropDownBGFocued;
	private CRImage dropDownFlag = null;
	private CRImage datePickerFlag = null;

	public CRImage getDropDownBG() {
		if (dropDownBG == null) {
			dropDownBG = CRImage.loadFromResource("/ui/dropdown.png");
		}
		return dropDownBG;
	}

	public CRImage getDropDownBGFocued() {
		if (dropDownBGFocued == null) {
			dropDownBGFocued = CRImage
					.loadFromResource("/ui/dropdown_focus.png");
		}
		return dropDownBGFocued;
	}

	public CRImage getDropDownFlag() {
		if (dropDownFlag == null) {
			dropDownFlag = CRImage.loadFromResource("/ui/combo.png");
		}
		return dropDownFlag;
	}

	public CRImage getDatePickerFlag() {
		if (datePickerFlag == null) {
			datePickerFlag = CRImage.loadFromResource("/ui/calendar.png");
		}
		return datePickerFlag;
	}

	public CRImage getVoteSmallLeftImage() {
		if (smallleftImage == null) {
			smallleftImage = CRImage
					.loadFromResource("/widgets/vote/img/leftsmall.png");
		}
		return smallleftImage;
	}

	public CRImage getVoteSmallselectedLeft() {
		if (smallselectedLeft == null) {
			smallselectedLeft = CRImage
					.loadFromResource("/widgets/vote/img/selectedleft.png");
		}
		return smallselectedLeft;
	}

	public CRImage getVoteSmallcenterImage() {
		if (smallcenterImage == null) {
			smallcenterImage = CRImage
					.loadFromResource("/widgets/vote/img/smallcenter.png");
		}
		return smallcenterImage;
	}

	public CRImage getVoteSmallselectedCenter() {
		if (smallselectedCenter == null) {
			smallselectedCenter = CRImage
					.loadFromResource("/widgets/vote/img/selectedcenter.png");
		}
		return smallselectedCenter;
	}

	public CRImage getVoteSmallrightImage() {
		if (smallrightImage == null) {
			smallrightImage = CRImage
					.loadFromResource("/widgets/vote/img/rightsmall.png");
		}
		return smallrightImage;
	}

	public CRImage getVoteSmallselectedRight() {
		if (smallselectedRight == null) {
			smallselectedRight = CRImage
					.loadFromResource("/widgets/vote/img/selectedright.png");
		}
		return smallselectedRight;
	}

	/** @Add by jiangwei 下面是消息资源图片, 包括上一级菜单图片选项 */

	// 下面是消息中心菜单图标
	/** 系统消息 */
	CRImage mSystemMessage;
	/** 站内消息 */
	CRImage mInstation;
	/** 评论 */
	CRImage mComments;
	/** 评论回复 */
	CRImage mCommentReply;
	/** 留言板 */
	CRImage mMessageBoard;
	/** 留言回复 */
	CRImage mMessageReply;

	public CRImage getMSystemMessageImage() {
		if (mSystemMessage == null) {
			mSystemMessage = CRImage
					.loadFromResource("/widgets/message/img/mSystemMessage.png");
		}
		return mSystemMessage;
	}

	public CRImage getMInstationImage() {
		if (mInstation == null) {
			mInstation = CRImage
					.loadFromResource("/widgets/message/img/mInstation.png");
		}
		return mInstation;
	}

	public CRImage getMCommentsImage() {
		if (mComments == null) {
			mComments = CRImage
					.loadFromResource("/widgets/message/img/mComments.png");
		}
		return mComments;
	}

	public CRImage getMCommentReplyImage() {
		if (mCommentReply == null) {
			mCommentReply = CRImage
					.loadFromResource("/widgets/message/img/mCommentReply.png");
		}
		return mCommentReply;
	}

	public CRImage getMMessageBoardImage() {
		if (mMessageBoard == null) {
			mMessageBoard = CRImage
					.loadFromResource("/widgets/message/img/mMessageBoard.png");
		}
		return mMessageBoard;
	}

	public CRImage getMMessageReplyImage() {
		if (mMessageReply == null) {
			mMessageReply = CRImage
					.loadFromResource("/widgets/message/img/mMessageReply.png");
		}
		return mMessageReply;
	}

	/**
	 * @return the more
	 */
	public CRImage getMore() {
		if (more == null)
			more = CRImage.loadFromResource("/widgets/diary/img/more.png");
		return more;
	}

	// 二期开发用到的图片资源 
	/** 下面是加载Tab组件的图片资源 {@link zincfish.zincwidget.SNSTabComponent} */
	private CRImage[] tabSlice;
	public CRImage[] getTabSclice() {
		if (tabSlice == null) {
			tabSlice = new CRImage[3];
			tabSlice[0] = CRImage.loadFromResource("/img/tab/tabl.png");
			tabSlice[1] = CRImage.loadFromResource("/img/tab/tabc.png");
			tabSlice[2] = CRImage.loadFromResource("/img/tab/tabr.png");
		}
		return tabSlice;
	}
	private CRImage[] tabnSlice;
	public CRImage[] getTabNSclice() {
		if (tabnSlice == null) {
			tabnSlice = new CRImage[3];
			tabnSlice[0] = CRImage.loadFromResource("/img/tab/bltext.png");
			tabnSlice[1] = CRImage.loadFromResource("/img/tab/bctext.png");
			tabnSlice[2] = CRImage.loadFromResource("/img/tab/brtext.png");
		}
		return tabnSlice;
	}
    // 选中图片后的背景
	private CRImage hbuttonbg;
	public CRImage getHButtonBG() {
		if(hbuttonbg == null) {
			hbuttonbg = CRImage.loadFromResource("/widgets/diary/img/hbfocus.png");
		}
		return hbuttonbg;
	}
	// 选中图片后的背景
	private CRImage hbuttonbg2;
	public CRImage getHButtonBG2() {
		if(hbuttonbg2 == null) {
			hbuttonbg2 = CRImage.loadFromResource("/widgets/diary/img/dselected.png");
		}
		return hbuttonbg2;
	}
	// 选中图片后的背景
	private CRImage hbuttonbg3;
	public CRImage getHButtonBG3() {
		if(hbuttonbg3 == null) {
			hbuttonbg3 = CRImage.loadFromResource("/widgets/diary/img/dselected3.png");
		}
		return hbuttonbg3;
	}
	// 选中图片后的背景
	private CRImage hbuttonbg4;
	public CRImage getHButtonBG4() {
		if(hbuttonbg4 == null) {
			hbuttonbg4 = CRImage.loadFromResource("/widgets/diary/img/dselected4.png");
		}
		return hbuttonbg4;
	}
	
	
	// 加载菜单ICON图片
	public CRImage getMenuIconImage(int i) {
		return CRImage.loadFromResource("/widgets/main/img/micon" + i + ".png");
	}

	// 多选框图标
	private CRImage check = null;
	private CRImage checked = null;

	public CRImage getCheckImage() {
		if (check == null) {
			check = CRImage.loadFromResource("/widgets/friends/img/check.png");
		}
		return check;
	}

	public CRImage getCheckedImage() {
		if (checked == null) {
			checked = CRImage
					.loadFromResource("/widgets/friends/img/checked.png");
		}
		return checked;
	}

	// 多选框图标
	private CRImage button = null;
	private CRImage buttonOK = null;

	public CRImage getButtonImage() {
		if (button == null) {
			button = CRImage.loadFromResource("/widgets/friends/img/fire.png");
		}
		return button;
	}

	public CRImage getButtonOKImage() {
		if (buttonOK == null) {
			buttonOK = CRImage
					.loadFromResource("/widgets/friends/img/fire_ok.png");
		}
		return buttonOK;
	}

	// 中文输入背景
	private CRImage pinyinbg;
	private CRImage pinyinbgabc;

	public CRImage getPinyinBG() {
		if (pinyinbg == null) {
			pinyinbg = CRImage.loadFromResource("/pinyin/pinyinbg.png");
		}
		return pinyinbg;
	}

	public CRImage getPinyinBGABC() {
		if (pinyinbgabc == null) {
			pinyinbgabc = CRImage.loadFromResource("/pinyin/pybgabc.png");
		}
		return pinyinbgabc;
	}

	// 主菜单背景图片
	CRImage[] homebg;
    // Vlist 箭头
	CRImage vlistArrow;
	public CRImage[] getHomeMenuBG() {
		if (homebg == null) {
			homebg = new CRImage[6];
			homebg[0] = CRImage.loadFromResource("/widgets/main/img/bgl.png");
			homebg[1] = CRImage.loadFromResource("/widgets/main/img/bgc.png");
			homebg[2] = CRImage.loadFromResource("/widgets/main/img/bgr.png");
			homebg[3] = CRImage.loadFromResource("/widgets/main/img/sbgl.png");
			homebg[4] = CRImage.loadFromResource("/widgets/main/img/sbgc.png");
			homebg[5] = CRImage.loadFromResource("/widgets/main/img/sbgr.png");
		}
		return homebg;
	}

	// 相册选中和没选中的背景
	
	public CRImage getVlistarrow() {
		if(vlistArrow == null) {
			vlistArrow = CRImage.loadFromResource("/widgets/main/img/arrow.png");
		}
		return vlistArrow;
	}
	
	public static final CRImage TOKEN = CRImage
			.loadFromResource("/menu/pop.png");

	/**
	 * @return the point
	 */
	public CRImage getPoint() {
		if (point == null)
			point = CRImage.loadFromResource("/img/point.png");
		return point;
	}

	/**
	 * @return the lightPoint
	 */
	public CRImage getLightPoint() {
		if (lightPoint == null)
			lightPoint = CRImage.loadFromResource("/img/light.png");
		return lightPoint;
	}

	private CRImage DivBG = null;

	public CRImage getDivBG() {
		if (DivBG == null)
			DivBG = CRImage.loadFromResource("/ui/divbg.png");
		return DivBG;
	}

	/**
	 * @return the chechedCheckBox
	 */
	public CRImage getChechedCheckBox() {
		if (chechedCheckBox == null)
			chechedCheckBox = CRImage.loadFromResource("/ui/check_on.png");
		return chechedCheckBox;
	}

	/**
	 * @return the unchechedCheckBox
	 */
	public CRImage getUnchechedCheckBox() {
		if (unchechedCheckBox == null)
			unchechedCheckBox = CRImage.loadFromResource("/ui/check_off.png");
		return unchechedCheckBox;
	}

	public CRImage getUnchechFocusedCheckBox() {
		if (unchechFocusedCheckBox == null)
			unchechFocusedCheckBox = CRImage
					.loadFromResource("/ui/check_off_focus.png");
		return unchechFocusedCheckBox;
	}

	public CRImage getChechedFocusedCheckBox() {
		if (checkedFocusedCheckBox == null)
			checkedFocusedCheckBox = CRImage
					.loadFromResource("/ui/check_on_foucs.png");
		return checkedFocusedCheckBox;
	}

	public CRImage getUnchechFocusedRadioBox() {
		if (unchechFocusedRadioBox == null)
			unchechFocusedRadioBox = CRImage
					.loadFromResource("/ui/radio_off_focus.png");
		return unchechFocusedRadioBox;
	}

	public CRImage getChechedFocusedRadioBox() {
		if (checkedFocusedRadioBox == null)
			checkedFocusedRadioBox = CRImage
					.loadFromResource("/ui/radio_on_focus.png");
		return checkedFocusedRadioBox;
	}

	/**
	 * @return the chechedRadioBox
	 */
	public CRImage getChechedRadioBox() {
		if (chechedRadioBox == null)
			chechedRadioBox = CRImage.loadFromResource("/ui/radio_on.png");
		return chechedRadioBox;
	}

	/**
	 * @return the unchechedRadioBox
	 */
	public CRImage getUnchechedRadioBox() {
		if (unchechedRadioBox == null)
			unchechedRadioBox = CRImage.loadFromResource("/ui/radio_off.png");
		return unchechedRadioBox;
	}

	private CRImage scrollBar = null;

	public CRImage getScrollBar() {
		if (scrollBar == null)
			scrollBar = CRImage.loadFromResource("/ui/scrollbar.png");
		return scrollBar;
	}

	private CRImage imageCorner = null;

	public CRImage getImageCorner() {
		if (imageCorner == null)
			imageCorner = CRImage.loadFromResource("/ui/imageblock_corner.png");
		return imageCorner;
	}

	private CRImage imageBorder = null;

	public CRImage getImageBorder() {
		if (imageBorder == null)
			imageBorder = CRImage.loadFromResource("/ui/imageblock_border.png");
		return imageBorder;
	}

	private CRImage formTick = null;

	public CRImage getFormTick() {
		if (formTick == null)
			formTick = CRImage.loadFromResource("/ui/form_tick.png");
		return formTick;
	}

	private CRImage tipsBG = null;

	public CRImage getTipsBG() {
		if (tipsBG == null)
			tipsBG = CRImage.loadFromResource("/ui/tips_bg.png");
		return tipsBG;
	}

	private CRImage buttonBG = null;

	public CRImage getButtonBG() {
		if (buttonBG == null)
			buttonBG = CRImage.loadFromResource("/ui/form_button.png");
		return buttonBG;
	}

}
