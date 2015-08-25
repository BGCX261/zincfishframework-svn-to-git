
# TAB 转换
def SwitchView(string tabid)
	_zsdSetAttValue("diary_ownd", "visible", "false")
	_zsdSetAttValue("diary_related", "visible", "false")
	_zsdSetAttValue(tabid, "visible", "true")
	_zsdRefresh(tabid);
end

def LoadOthersDiaryList()
	_zsnSend("/widgets/diary/diary_downloadpages/diary_list_others_download.xml")

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
