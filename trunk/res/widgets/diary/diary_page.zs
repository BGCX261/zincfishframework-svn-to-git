def showTagManager()
	_zssprintln("###########--showTagManager---##############")
	_zssShowPopWindow("tagManager")
end
def hideTagManager()
	_zssHidePopWindow("tagManager")
end

def goComments()
    _zssSwitch("/widgets/diary/diaryncomments.xml")
end

# TAB 转换
def SwitchView(string tabid)
	_zsdSetAttValue("diary_content", "visible", "false")
	_zsdSetAttValue("diary_comments", "visible", "false")
	_zsdSetAttValue(tabid, "visible", "true")
	_zsdRefresh();
end

# 进入好友首页
def viewMainpage(string mid)
	string url = "http://test.3g.cn/sns/interface/dfmsg.aspx"
	url = url + "?mids=" + mid
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
end

# 开始处理附件
def OnAttachment(string url)
	_zsnPopDlg(url)
end


# 编辑日记正文
def OnEditText()
	string diaryTitle = _zsdGetAttValue("diary_head", "signature")
	string diaryText = _zsdGetAttValue("text", "text")
	#reload the edit panel
	_zsdSetAttValue("edit_title", "value", diaryTitle)
	_zsdSetAttValue("edit_text", "value", diaryText)
	_zssShowPopWindow("edit_text_pop")
end

# 保存正文
def SaveText()
	string newDiaryTitle = _zsdGetAttValue("edit_title", "value")
	if newDiaryTitle==""
		_zsnPopDlg("日记标题不能为空！")
		return
	end
	string newDiaryText = _zsdGetAttValue("edit_text", "value")
	if newDiaryText==""
		_zsnPopDlg("日记正文不能为空！")
		return
	end
	_zsdSetAttValue("diary_head", "signature", newDiaryTitle)
	_zsdSetAttValue("text", "text", newDiaryText)
	_zssHidePopWindow("edit_text_pop")
	_zsdRefresh()
end

# 返回
def ExitEdit(string popId)
	_zssHidePopWindow(popId)
end

# 编辑隐私权限
def OnPrivacyEdit()
	_zssShowPopWindow("edit_privacy_pop")
end

# 添加图片附件
def AddImgicAttachment()
	_zssOnPathLocating("file", "OnAddImg")
end

def OnAddImg(string imgpath, string imgname, string mimetype)
	int index = _zsdGetChildrenNum("attachment_img")
	index = index + 1
	string id = "attachment_img_" + index
	_zsdAddDom("img", id, "attachment_img")
	_zsdSetAttValue(id, "src", imgpath)
	_zsdSetAttValue(id, "available", "true")
	_zsdRefresh()
	_zsdMoveFocusTo(id)
	return

end

# 删除附件
def RemoveAttachment()
	string focusId = _zsdGetFocusID()
	if focusId == ""
		_zsnPopDlg("请选中要删除的附件")
		return
	end
	string focusContainerId = _zsdGetParentID(focusId)
	if focusContainerId == "attachment_img"
		_zsdDelDom(focusId)
		_zsdRefresh()
		return
	end
	if focusContainerId == "attachment_other"
		_zsdDelDom(focusId)
		_zsdRefresh()
		return
	end
end

# 添加附件




# 发表评论

def postComment()
	string content=_zsdGetAttValue("comment_text", "value")
	if content==""
		_zsnPopDlg("请输入评论内容！")
		return
	end
	
	string diaryId=_zsdGetAttValue("diary_head", "value")
	string url="http://test.3g.cn/sns/interface/CommentSend.aspx?zujian=2"
	url=url+"&Id="+diaryId
	url=url+"&Content="+content
	#_zsnPopDlg(url)
	_zsnSend(url)
	
end




# 未使用
def postDiary()
	string TitleStr = _zsdGetAttValue("title", "value")
	string Content = _zsdGetAttValue("editor", "value")
	string Privacy = _zsdGetAttValue("rights", "value")
	string FriendStr ="a;b;c"
	_zsnPostDiary(TitleStr,Content,Privacy,FriendStr)
end

def delDiary()
	string curID = _zsdGetCurDomID()
	string url = "http://test.3g.cn/sns/interface/diarydel.aspx?id="+curID
	_zsnSendLater(url)
	_zsdDelDOM(curID)
	_zsdRefresh(curID)
end

def addFriends()
	_zssShowPopWindow("friendspop")
end

def chooseFirends()
	_zssHidePopWindow("friendspop")
end



def addPic()
	_zssOnVoiceRecording("reocrdtemp", "2")
	#_zssOnPathLocating("file", "OnAddImg")
end
