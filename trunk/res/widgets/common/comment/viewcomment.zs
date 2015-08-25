
# 删除评论回复
def DeleteComReplay()
	string focusid=_zsdGetFocusID()
	string replyId =  _zsdGetAttValue(focusid, "value")
	string commentid =  _zsdGetAttValue("comment_content", "value")
	string url = "http://test.3g.cn/sns/interface/CommentMsgDel_Packet.aspx?"
	url = url + "idtodel=" + replyId
	url = url + "&Id=" + commentid
	_zsnSendLater(url)
	_zsdDelDom(focusid)
	#_zsnSendNow()
	return
end

# 下载上一页 下一页发件箱和收件箱消息
def DownloadMessage(string url)
	string guid = _zsnGetGuid()
	_zssprintln(url + "&guid=" + guid)
	_zsnSend(url)
end


# 添加评论回复
def  AddCommentReplay()
	string content = _zsdGetAttValue("reply_text", "value")
	if content == ""
		_zsnPopDlg("请输入回复内容！")	
		return
	end

	string url = "http://test.3g.cn/sns/interface/CommentMsgSend_Packet.aspx?"
	string guid = _zsnGetGuid()
	string commentid = _zsdGetAttValue("comment_content", "value")

	url = url + "guid="
	url = url + guid
	url = url + "&Id="
	url = url + commentid
	url = url + "&contents="
	url = url + content
	_zsnSendLater(url)
	#_zsnSendNow()
	_zsnPopDlg("回复已发送")
end
