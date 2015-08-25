# 进入好友的主页
def EntryHome(string url)
	_zssprintln("好友主页:" + url)
	_zssHidePopWindow(msgid + "_pop")
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


# 添加留言回复
def  AddWordReplay()
	string content = _zsdGetAttValue("reply_text", "value")
	if content == ""
		_zsnPopDlg("请输入回复内容！")	
		return
	end

	string url = "http://test.3g.cn/sns/interface/MsgWordMsgSend_Packet.aspx?"
	string guid = _zsnGetGuid()
	string wordid = _zsdGetAttValue("word_content", "value")

	url = url + "guid="
	url = url + guid
	url = url + "&Id="
	url = url + wordid
	url = url + "&contents="
	url = url + content
	_zsnSendLater(url)
	#_zsnSendNow()
	_zsnPopDlg("回复已发送")	
end


# 删除留言回复
def DeletewordReply()
	string focusid=_zsdGetFocusID()
	string replyId =  _zsdGetAttValue(focusid, "value")
	string wordId =  _zsdGetAttValue("word_content", "value")
	string url = "http://test.3g.cn/sns/interface/MsgWordMsgDel_Packet.aspx?"
	url = url + "idtodel=" + replyId
	url = url + "&Id=" + wordId
	_zsnSendLater(url)
	#_zsnSendNow()
	return
end





