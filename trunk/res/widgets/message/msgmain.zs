
# 装载新消息数存在内存中
def LoadMSGData()
    # 短消息条数    ID:short_msg_b 写死
	string short_msg = _zssGetGlobalVarValue("short_msg")
	_zssprintln("-short_msg-->" + short_msg)
	_zsdSetAttValue("short_msg_b", "text", short_msg + "条新")
    # 系统消息条数
	string sys_msg = _zssGetGlobalVarValue("sys_msg")
	_zssprintln("-sys_msg-->" + sys_msg)
	_zsdSetAttValue("sys_msg_b", "text", sys_msg + "条新")
    # 留言板条数
	string leave_words = _zssGetGlobalVarValue("leave_words")
	_zssprintln("-leave_words-->" + leave_words)
	_zsdSetAttValue("leave_words_b", "text", leave_words + "条新")
    # 评论条数
	string comment = _zssGetGlobalVarValue("comment")
	_zsdSetAttValue("comment_b", "text", comment + "条新")
	_zsdRefresh()
end

# 1.进入消息首页
# 参数：无
def GoMsgMainPage()
	_zsnSend("/widgets/message/msgmain.xml")
end

# 2.进入短消息列表
# 参数：无
def GoShortMsgList()
	string url = "http://test.3g.cn/sns/interface/msgrecsend.aspx"
	_zsnSend("/widgets/message/rec_msg_list.xml", "_zsnSend", url)
end

# 3.进入系统消息列表
# 参数：无
def GoSysMsgList()
	string url = "http://test.3g.cn/sns/interface/MsgSysListAll.aspx"
	_zsnSend("/widgets/message/sys_msg_list.xml", "_zsnSend", url)
end

# 4.进入留言消息列表
# 参数：无
def GoWordMsgList()
	string url = "http://test.3g.cn/sns/interface/MsgWordRecSend.aspx"
	_zsnSend("/widgets/message/word_msg_list.xml", "_zsnSend", url)
end

# 5.进入评论消息列表
# 参数：无
def GoCommentMsgList()
	string url = "http://test.3g.cn/sns/interface/CommentRecSend.aspx"
	_zsnSend("/widgets/message/comment_msg_list.xml", "_zsnSend", url)
end

