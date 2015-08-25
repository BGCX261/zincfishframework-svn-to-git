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
	_zsdSetAttValue("diary_mine", "visible", "false")
	_zsdSetAttValue("diary_fr", "visible", "false")
	_zsdSetAttValue("diary_relate_me", "visible", "false")
	_zsdSetAttValue("diary_relate_fr", "visible", "false")
	_zsdSetAttValue("writediary", "visible", "false")
	_zsdSetAttValue(tabid, "visible", "true")
	_zsdRefresh(tabid);
end

def LoadDiaryList()
	#_zsnSend("/widgets/diary/diary_downloadpages/diary_list_download.xml")
	
	_zsnSend("http://test.3g.cn/sns/interface/diaryList.aspx?lt=1")

end

# 进入自己的一篇日记
def viewMyDiary()
	string focus=_zsdGetFocusID()
	string diaryId=_zsdGetAttValue(focus, "value")
	string url = "http://test.3g.cn/sns/interface/diarymsg.aspx"
	url = url + "?id=" + diaryId
	_zsnSend("/widgets/diary/diary_page_mine.xml", "_zsnSend", url)
end

# 进入一篇日记
def viewDiary()
	string focus2=_zsdGetFocusID()
	string diaryId=_zsdGetAttValue(focus2, "value")
	string url = "http://test.3g.cn/sns/interface/diarymsg.aspx"
	url = url + "?id=" + diaryId
	_zsnSend("/widgets/diary/diary_page_normal.xml", "_zsnSend", url)
end

# 开始写日记
def WriteDiary()
	_zssSwitch("/widgets/diary/write_diary.xml")
end






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

def OnAddImg(string imgpath)
	string id = "imageAttach" + imgAddtachIndex
	string iddesc = "attachDesc" + imgAddtachIndex
	_zsdAddDom("img", id, "attaches")
	_zsdSetAttValue(id, "src", imgpath)
	_zsdSetAttValue(id, "available", "true")
	_zsdSetAttValue(id, "style", "align:right")

	_zsdAddDom("label", iddesc, "attaches")
	_zsdSetAttValue(iddesc, "text", imgpath)
	_zsdSetAttValue(iddesc, "available", "true")
	_zsdSetAttValue(iddesc, "wrap", "false")
	imgAddtachIndex = imgAddtachIndex + 1
	_zsdRefresh()
end

def addPic()
	_zssOnVoiceRecording("reocrdtemp", "2")
	#_zssOnPathLocating("file", "OnAddImg")
end

int imgAddtachIndex = 1