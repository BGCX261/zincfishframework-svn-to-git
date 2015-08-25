# 回复   mids(用户加密id)  msgid(消息的ID)  fr 所在页面
string mids
string msgid
string fr
# tab 短消息页面切换

# tab 评论页面切换
def CSwitchView(string tabidc)
	_zsdSetAttValue("comment_msg_in", "visible", "false")
	_zsdSetAttValue("comment_msg_out", "visible", "false")
	_zsdSetAttValue(tabidc, "visible", "true")
	_zsdRefresh()
end

# 下载上一页 下一页发件箱和收件箱消息
def DownloadMessage(string url)
	string guid = _zsnGetGuid()
	_zssprintln(url + "&guid=" + guid)
	_zsnSend(url)
end

#消息中心
def MsgCenter()
	_zssSwitch("/widgets/message/msgmain.xml")
end



# 删除评论
def DeleteComMessage()
	string focusid=_zsdGetFocusID()
	string msgId =  _zsdGetAttValue(focusid, "value")
	string url = "http://test.3g.cn/sns/interface/CommentDel_Packet.aspx?"
	url = url + "idtodel=" + msgId
	_zsnSendLater(url)
	_zsdDelDom(focusid)
	#_zsnSendNow()
	return
end




# 进入主页
def viewMainpage(string url)
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
end

# 进入评论
def viewComment(string url)
	string focusid
	focusid=_zsdGetFocusID()
	string popid=focusid+"_pop"
	_zssHidePopWindow(popid)
	_zsnSend("/widgets/common/comment/viewcomment.xml", "_zsnSend", url)
end

# 进入日记
def viewDiary(string url)
	_zsnSend("/widgets/diary/diary_page_normal.xml", "_zsnSend", url)
end

# 进入相册
def viewAlbum(string url)
	_zsnSend("/widgets/album/photopage.xml")
end

# 进入投票
def viewVote(string url)
	_zsnSend("/widgets/vote/voting.xml","_zsnSend",url)
end

