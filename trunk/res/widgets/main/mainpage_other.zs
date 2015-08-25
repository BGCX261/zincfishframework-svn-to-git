# 获取mid
def GetMid()
	string mid = _zsdGetAttValue("head","data")
	if mid==""
		_zsnPopDlg("页面数据错误")
		return ""
	end
	return mid
end

# 好友
def FriendPage()
	string mid = GetMid()
	if mid==""
		return
	end
	
	#string url = "http://test.3g.cn/sns/interface/dfmsg.aspx"
	string url = "/widgets/friends/friend_list_others_download.xml"
	#url = url + "?mids=" + mid
	_zsnSend("/widgets/friends/friend_list_others.xml","_zsnSend",url)
end

# 日记
def DiaryPage()
	string mid = GetMid()
	if mid==""
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/diaryList2.aspx"
	url = url + "?mids=" + mid
	_zsnSend("/widgets/diary/diary_list_others.xml","_zsnSend",url)
end

# 相册
def AlbumPage()
	string mid = GetMid()
	if mid==""
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList.aspx"
	url = url + "?mids=" + mid
	_zsnSend("/widgets/album/album_other.xml","_zsnSend",url)
end

# 点滴
def DropPage()
	_zsnPopDlg("木有点滴")
end

# 投票
def VotePage()
	string mid = GetMid()
	if mid==""
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/votelist.aspx?lt=df"
	url = url + "&mids=" + mid
	_zsnSend("/widgets/vote/voten_other.xml","_zsnSend",url)
end

# 礼物
def GiftPage()
	_zsnSend("/widgets/present/gift.xml")
end

# 点击了留言
def ClickLW()
	string curID = _zsdGetFocusID()
	string content = _zsdGetAttValue(curID,"content")
	if content==""
		return
	end
	_zsnPopDlg(content)
end

# 点了最近来访者
def GoAccHome()
	string curId = _zsdGetFocusID()
	string mid = _zsdGetAttValue(curId,"data")
	if mid==""
		return
	end
	string url = "http://test.3g.cn/sns/interface/dfmsg.aspx"
	url = url + "?mids=" + mid
	_zsnSend(url)
end

# 去留言人首页
def LWHome()
	string curId = _zsdGetFocusID()
	string mid = _zsdGetDataValue(curId,"mid")
	if mid==""
		_zsnPopDlg("请将焦点移至留言上")
		return
	end
	string url = "http://test.3g.cn/sns/interface/dfmsg.aspx"
	url = url + "?mids=" + mid
	_zsnSend(url)
end

# 显示留言框
def ShowLWPop()
	string name = _zsdGetAttValue("head","user-name")
	string content = "给 " + name + " 留言"
	_zsdSetAttValue("LWPL","text",content)
	_zssShowPopWindow("LWPop")
end

# 显示消息框
def SendMsg()
	string name = _zsdGetAttValue("head","user-name")
	string content = "给 " + name + " 发消息"
	_zsdSetAttValue("MsgPL","text",content)
	_zssShowPopWindow("MsgPop")
end

# 发送留言
def SureLW()
	string content = _zsdGetAttValue("LWPT","value")
	if content==""
		_zsnPopDlg("请输入留言内容")
		return
	end
	
	string mid = GetMid()
	if mid==""
		_zssHidePopWindow("LWPop")
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/MsgWordSend_packet.aspx"
	url = url + "?mids=" + mid
	url = url + "&contents=" + content
	
	_zsnSendLater(url)
	_zsnSendNow()
	_zssHidePopWindow("LWPop")
end

# 发送消息
def SureMsg()
	string content = _zsdGetAttValue("MsgPT","value")
		if content==""
		_zsnPopDlg("请输入消息内容")
		return
	end
	
	string mid = GetMid()
	if mid==""
		_zssHidePopWindow("MsgPop")
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/MsgSend_packet.aspx"
	url = url + "?mids=" + mid
	url = url + "&contents=" + content
	
	_zsnSendLater(url)
	_zsnSendNow()
	_zssHidePopWindow("MsgPop")
end
