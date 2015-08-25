def changevisable(string id_show, string id_hidden)
	_zsdSetAttValue(id_show, "visible", "true")
	_zsdSetAttValue(id_hidden, "visible", "false")
	_zsdRefresh(id_show)
end

def PopFriendPanel(string panelID, string seleteID)
	_zsdPopFriendPanel(seleteID)
	_zssShowPopWindow(panelID)
end

def CancelFriendPanel(string panelID)
	_zssHidePopWindow(panelID)
	updateRcvrs()
end

def newGift()

end


def updateRcvrs2()
	int fricnt
	fricnt=_zsdGetChildrenNum("seleteF")
	int remainnum=fricnt
	string subID
	string recvrs
	string recvrtemp
	string isSelected
	while remainnum>0
		remainnum=remainnum-1
		subID="";
		subID=_zsdGetChildrenID("seleteF", remainnum)
		isSelected=_zsdGetAttValue(subID, "text")
			recvrtemp=_zsdGetAttValue(subID, "text")
 			recvrs=recvrs+recvrtemp
			recvrs=recvrs+","
	end
	_zsdSetAttValue("recvrs", "text")
	_zsdRefresh("recvrs")	
end

def updateRcvrs()
	int fricnt
	fricnt=_zsdGetChildrenNum("seleteF")
	int remainnum
	remainnum=0
	string subID
	string isSelected
	string recvrs
	string recvrtemp
	
	string recvrid
	
	while remainnum<fricnt
		subID=_zsdGetChildrenID("seleteF", remainnum)
		isSelected=_zsdGetAttValue(subID, "select")
		if isSelected=="true"
			recvrtemp=_zsdGetAttValue(subID, "text")
			recvrid="recvr"+remainnum
			_zsdAddDom("label", recvrid, "recvrs")
			_zsdSetAttValue(recvrid, "text", recvrtemp)
		end
		remainnum=remainnum+1
	end
	_zsdRefresh("recvrs")		
end

def SendGift(string url)
		string friend = _zsdFriendListToStr("FriendChecks")
		if _zsdIsNull(friend)
			_zsnPopDlg("请选择好友")
			return
		end

		string visible = _zsdGetAttValue("vip_gift_div", "visible")
		string giftId
		if visible=="false"
			giftId = _zsdGetSelectResult("com_gift_select")
		else
			giftId = _zsdGetSelectResult("vip_gift_select")
		end
		if _zsdIsNull(giftId)
			_zsnPopDlg("请选择礼物")
			return
		end
		
		string way = _zsdGetAttValue("wayBox","value")
		string content = _zsdGetAttValue("words","value")
		string SendContent = "contents=" + content
		
		string urlOut = url
		urlOut = urlOut + "?id=" + giftId
		urlOut = urlOut + "&mids=" + friend
		urlOut = urlOut + "&type=" + way
		
		_zsnSendData(urlOut,SendContent,1)
end

def LoadAllList()
		_zsnSend("http://test.3g.cn/sns/interface/giftlist.aspx?lt=all&pn=1")
end

def ShowRecvList(string url)
		_zsnSend(url)
end

def ShowComPage(string url)
		_zsnSend(url)
		_zsdMoveFocusTo("common_gift")
end

def ShowVipList(string url)
		_zsnSend(url)
		_zsdMoveFocusTo("vip_gift")
end

def PresentBack()

end

def DelRecord()
		string focusID = _zsdGetFocusID()
		if IsNull(focusID)
			return
		end
		
		string recordID = _zsdGetDataValue(focusID,"record")
		string url = "http://test.3g.cn/sns/interface/giftdel.aspx?id=" + recordID
		_zsnSendData(url)
end

def HomePage()

end