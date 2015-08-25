# 回复   mids(用户加密id)  msgid(消息的ID)  fr 所在页面
string mids
string msgid
string fr


# tab 留言板页面切换
def WSwitchView(string tab)
	_zsdSetAttValue("word_msg_in", "visible", "false")
	_zsdSetAttValue("word_msg_out", "visible", "false")
	_zsdSetAttValue(tab, "visible", "true")
	_zsdRefresh()
end


# 进入好友的主页
def EntryHome(string url)
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
end
def viewMainpage(string url)
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
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

# 进入留言
def viewWord(string url)
	string focusid
	focusid=_zsdGetFocusID()
	string popid=focusid+"_pop"
	_zssHidePopWindow(popid)
	_zsnSend("/widgets/common/leaveword/viewword.xml", "_zsnSend", url)	
end


# 添加留言回复
def  AddWordReplay(string frvalue)
	string content = _zsdGetAttValue("reply_text", "value")
	if content == ""
		_zsnPopDlg("请输入回复内容！")	
		return
	end

	string url = "http://test.3g.cn/sns/interface/MsgWordMsgSend.aspx?"
	string guid = _zsnGetGuid()
	string wordid = _zsdGetAttValue("word_content", "value")

	url = url + "guid="
	url = url + guid
	url = url + "&Id="
	url = url + wordid
	url = url + "&contents="
	url = url + content
	_zsnSendLater(url)
	_zsnPopDlg("回复已发送")	
end

# 删除留言
def DeleteWordMessage()
	string focusid=_zsdGetFocusID()
	string msgId =  _zsdGetAttValue(focusid, "value")
	string url = "http://test.3g.cn/sns/interface/MsgWordDel_Packet.aspx?"
	url = url + "idtodel=" + msgId
	_zsnSendLater(url)
	_zsdDelDom(focusid)
	return
end



























# 回复留言
def OnWordReplay(string _mids, string _msgid)
    mids = _mids
	msgid = _msgid
	string domid = _zsdGetCurDomID()
	_zssprintln("OnWordReplay----domid-->" + domid)
	if domid == "word_msg_in"
	    fr = "1"
	    # 加载本地评论回复页面
		_zssSwitch("/widgets/message/viewcomment_replay_in.xml")
	elsif domid == "word_msg_out"
	    fr = "2"
		# 加载本地我给别人评论页面
		_zssSwitch("/widgets/message/viewcomment_replay_out.xml")
	else
		return ""
	end
	string guid = _zsnGetGuid()
	string id = _zsdGetAttValue(domid, "value")
	_zssprintln("<-OnWordReplay=======guid==" + guid + "-ID->" + id)
	# 加载网络数据
    string url = "http://test.3g.cn/sns/interface/MsgWordMsg.aspx"
	string data = "id=" + id + "&fr=" + fr + "&guid=" + guid
	_zssprintln("<-OnWordReplay=======data==" + data)
    
end

