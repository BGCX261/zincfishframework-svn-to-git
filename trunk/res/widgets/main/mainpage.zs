# 脚本测试
def textprint(string text)
	_zssprintln("脚本测试----->" + text)
end

# 设置背景颜色页面
def SetColorPage()
	_zsnSend("/widgets/main/setcolor.xml")
end

# 好友
def FriendPage()
	_zsnSend("/widgets/friends/friend.xml")
end

# 日记
def DiaryPage()
	_zsnSend("/widgets/diary/diary_list.xml")
end

# 相册
def AlbumPage()
	_zsnSend("/widgets/album/album.xml")
end

# 挣车位
def CarPage()
	_zsnSend("/widgets/car/car.xml")
end

# 导航
def NavPage()
	_zsnSend("/widgets/main/homenavigation.xml")
end

# 消息
def MsgPage()
	_zsnSend("/widgets/message/msgmain.xml")
end

# 点滴
def DropPage()
	_zsnSend("/widgets/record/recordn.xml")
end

# 投票
def VotePage()
	_zsnSend("/widgets/vote/voten.xml")
end

# 礼物
def GiftPage()
	_zsnSend("/widgets/present/gift.xml")
end

# 动他一下
def TouchPage()
	_zsnSend("/widgets/touch/touch.xml")
end

# 最近访客
def ResentPage()
	_zsnSend("/widgets/touch/touch-home.xml")
end

# 组件
def WidgetPage()
	_zsnPopDlg("去到组件管理界面")
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
	#if mid==""
		#return 0
	#end
	string url = "http://test.3g.cn/sns/interface/dfmsg.aspx"
	url = url + "?mids=" + mid
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
end

# 去留言人首页
def LWHome()
	string curId = _zsdGetFocusID()
	string mid = _zsdGetDataValue(curId,"mid")
	#if mid==""
		#zsnPopDlg("请将焦点移至留言上")
		#return
	#end
	string url = "http://test.3g.cn/sns/interface/dfmsg.aspx"
	url = url + "?mids=" + mid
	# _zsnPopDlg(url)
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
end


#去到某一篇日记
def goDiary(string did)
	string url="http://test.3g.cn/sns/interface/diaryMsg.aspx?id="+did
	_zsnSend("/widgets/diary/diary_page_normal.xml", "_zsnSend", url)
end

#去到日记评论
def goDiaryComment(string did)
	string url="http://test.3g.cn/sns/interface/diaryMsg.aspx?id="+did
	_zsnSend("/widgets/diary/diary_page_normal.xml", "_zsnSend", url,"SwitchView","diary_comments")
end

#去到相册评论
def goPhotoComment(string mid,string pid)
	string url="" 
	_zsnSend("/widgets/album/photopage.xml",url,"ClickPhoto")
end

#去到相册
def goAlbum(string mid,string pid)
	string url=""
	_zsnSend("/widgets/album/album.xml")
end