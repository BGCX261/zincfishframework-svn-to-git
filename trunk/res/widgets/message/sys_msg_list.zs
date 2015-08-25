# 回复   mids(用户加密id)  msgid(消息的ID)  fr 所在页面
string mids
string msgid
string fr

# tab 系统消息页面切换
def SSwitchView(string tabids)
	_zsdSetAttValue("sys_msg_all", "visible", "false")
	_zsdSetAttValue("sys_msg_req", "visible", "false")
	_zsdSetAttValue("sys_msg_day", "visible", "false")
	_zsdSetAttValue(tabids, "visible", "true")
	_zsdRefresh()
end



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

# 删除消息
def DeleteMessage()
	string url = "http://test.3g.cn/sns/interface/msgdelone_packet.aspx"
	# 获取当前容器组件的ID
	string domid = _zsdGetFocusID()
	string focustype = _zsdGetDomType(domid)
	if focustype != "item"
		return
	end
	fr = "2"
	string idtodel = _zsdGetAttValue(domid, "value")
	string guid = _zsnGetGuid()
	url=url + "?guid="
	url=url + guid
	url=url + "&idtodel="
	url=url + idtodel
	url=url + "&fr="
	url=url + fr
	_zsnSendLater(url)
	#_zsnSendNow()
	#_zssprintln("DeleteMessage---url--->" + url)
	# 删除dom
	_zsdDelDom(domid)
	# 刷新页面
	_zsdRefresh()
	return
end

