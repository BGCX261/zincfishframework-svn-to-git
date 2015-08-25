
# 回复   mids(用户加密id)  msgid(消息的ID)  fr 所在页面
string mids
string msgid
string fr
# tab 短消息页面切换
def SwitchView(string tabid)
	_zsdSetAttValue("rec_msg_in", "visible", "false")
	_zsdSetAttValue("rec_msg_out", "visible", "false")
	_zsdSetAttValue("rec_msg_send", "visible", "false")
	_zsdSetAttValue(tabid, "visible", "true")
	_zsdRefresh()
end


# 回复
def OnReplay(string _mids, string _msgid)
	mids = _mids
	msgid = _msgid
	string msgtext = _zsdGetAttValue(msgid, "content")
	_zssprintln(msgid + "<-OnReplay=======msgtext==" + msgtext + "==mids=" + mids)
	# 设置label标签的text信息
	_zsdSetAttValue("msgtext_re", "text", msgtext)
	# 隐藏弹出框
	_zssHidePopWindow(msgid + "_pop")
	_zssShowPopWindow("replay_pop")
end

# 回复该消息
def ReplayMessage()
	string guid = _zsnGetGuid()
	# 获取回复内容
	string content = _zsdGetAttValue("content_re", "value")
	string url = "http://test.3g.cn/sns/interface/MsgSend_packet.aspx?"
	# 说明是收件箱
	string fr = "1" 
	url = url + "guid="
	url = url + guid
	url = url + "&mids="
	url = url + mids
	url = url + "&contents="
	url = url + content
	#_zssprintln("data===" + data)
	# string data = "guid="
	# 发送数据
	#_zsnSendData("", data)
	_zsnSendLater(url)
	#_zsnSendNow()
	_zssHidePopWindow("replay_pop")
	_zssShowPopWindow("message_pop")
	_zsnPopDlg("消息已发送")
end


# 进入好友的主页
def EntryHome(string url)
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
end



# 回复消息成功
def PeplayOK()
    # 上传消息数据
    _zssHidePopWindow("popreplay")
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
	string conid = _zsdGetParentID(domid)
	string fr = ""
	# 页码
	string pn = ""
	_zssprintln("<--DeleteMessage---conid--->" + conid)
	if conid == "rec_msg_out_list"  # 说明是发件箱
		fr = "1"
		pn = _zsdGetAttValue("rec_msg_out_page", "value")
	elsif conid=="rec_msg_in_list"  # 说明是收件箱
		fr = "0"
		pn = _zsdGetAttValue("rec_msg_in_page", "value")
	else 
		return ""
	end
	_zssprintln("DeleteMessage=======conid==" + conid)
	# 获取当前容器被选中的dom的ID
	
	_zssprintln("DeleteMessage=======domid==" + domid)
	string idtodel = _zsdGetAttValue(domid, "value")
	_zssprintln("DeleteMessage=======idtodel==" + idtodel)
	# string msgid = _zsdGetAttValue(domid, "value")
	# 获取GUID
	string guid = _zsnGetGuid()
	_zssprintln("DeleteMessage=======guid==" + guid)
	# 拼接URL
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
end


# 弹出好友列表
def PopRecvrs()
	_zsdClearSubDoms("FriSelect")
	int fricnt=_zsnGetFriendsListSize()
	array friinfo[][]=_zsnGetFriendsList()
	string midtemp
	string nametemp
	string descriptiontemp
	int friindex=0
	while friindex<fricnt
		midtemp=friinfo[friindex][0]
		nametemp=friinfo[friindex][1]
		descriptiontemp=friinfo[friindex][2]

		_zsdAddDom("checkbox", midtemp, "FriSelect")
		_zsdSetAttValue(midtemp, "value", midtemp)
		_zsdSetAttValue(midtemp, "text", nametemp)
		_zsdSetAttValue(midtemp, "type", "radio")
		friindex=friindex+1
	end
	_zssShowPopWindow("FriSelPop")
end

# 更新收信人
def updateRcvr()
	string PanelID = "FriSelPop"
	string SeleteID = "FriSelect"
	string labelID = "msg_recvr"

	_zssHidePopWindow(PanelID)

	int fricnt
	fricnt=_zsdGetChildrenNum(SeleteID)
	int remainnum
	remainnum=0
	string subID
	string isSelected
	string recvrtemp
	string recvrid
	string friID
	
	while remainnum<fricnt
		subID=_zsdGetChildrenID(SeleteID, remainnum)
		isSelected=_zsdGetAttValue(subID, "select")
		if isSelected=="true"
			recvrtemp=_zsdGetAttValue(subID, "text")
			friID = _zsdGetAttValue(subID, "value")
			_zsdSetAttValue(labelID, "text", recvrtemp)
			_zsdSetAttValue(labelID, "value", friID)
			_zsdRefresh()
			return
		end
		remainnum=remainnum+1
	end
	_zsdRefresh()		
end


# 发送消息 
def SendMessage()
	string mids = _zsdGetAttValue("msg_recvr", "value")
	if mids==""
		_zsnPopDlg("请指定收信人")	
		return
	end
	string guid = _zsnGetGuid()
	string content = _zsdGetAttValue("rec_msg_send_text", "value")
	# 获取mids
	# string _mids = ""
	string url = "http://test.3g.cn/sns/interface/MsgSend_packet.aspx?"
	#string url = "http://test.3g.cn/sns/interface/MsgWordSend_Packet.aspx?"
	# string data = "&guid=" + guid + "&fr=2&mids=" + mids + "&contents="+ content
	# _zssprintln("SendMessage-d->" + data)
	# send()
	url = url + "guid="
	url = url + guid
	url = url + "&mids="
	url = url + mids
	url = url + "&contents="
	url = url + content
	#_zssprintln("data===" + data)
	# string data = "guid="
	# 发送数据
	#_zsnSendData("", data)
	_zsnSendLater(url)
	#_zsnSendNow()
	_zsnPopDlg("消息已发送")
end


# 清空消息
def DeleteBoxMessage(string flag)
    string listid = ""
    string pnlabel = ""
    if flag == "0" # 说明是收件箱
    	listid="rec_msg_in_list"
    	pnlabel = "rec_msg_in_page"
    elsif flag == "1"  # 说明是发件箱
    	listid="rec_msg_out_list"
    	pnlabel = "rec_msg_out_page"
    else
    	return
    end
    # 删除list下所有孩子
    _zsdDelAllChildren(listid)
    if flag == "0"
        _zsdSetAttValue("rec_msg_in_next", "visible", "false")
        _zsdSetAttValue("rec_msg_in_page", "visible", "false")
        _zsdSetAttValue("rec_msg_in_prev", "visible", "false")
    elsif flag == "1"
        _zsdSetAttValue("rec_msg_out_next", "visible", "false")
        _zsdSetAttValue("rec_msg_out_page", "visible", "false")
        _zsdSetAttValue("rec_msg_out_prev", "visible", "false")
    end
    _zsdGetAttValue(pnlabel, "text","0/0")
	# flag = 0 清空  收件箱 =1 清空发件箱
	string url = "http://test.3g.cn/sns/interface/MsgDelAll.aspx?"
	string guid = _zsnGetGuid()
	string st = flag
	url = url + "&st=" + st + "&guid=" + guid
	_zsnSendLater(url)
	_zssprintln("清空消息url---->" + url)
    _zsdRefresh()
end
