def changevisable(string id_show, string id_hidden)
	_zsdSetAttValue(id_show, "visible", "true")
	_zsdSetAttValue(id_hidden, "visible", "false")
	_zsdRefresh(id_show)
end

def PopFriendPanel(string panelID, string seleteID)
  _zssShowPopWindow(panelID)
	_zsdPopFriendPanel(panelID, seleteID)
end

def CancelFriendPanel(string panelID)
	_zsdHidePopPanel(panelID)
end

def LoadList()
	_zsnSend("http://test.3g.cn/sns/interface/TouchList.aspx?LT=all")
end

def AddCustom(string url)
	string word1 = _zsdGetAttValue("slefAction1", "value")
	if _zsdIsNull(word1)
		_zsnPopDlg("请输入动词")
		return
	end

	string word2 = _zsdGetAttValue("slefAction2", "value")
	if _zsdIsNull(word2)
		_zsnPopDlg("请输入量词")
		return
	end
	
	string urlOut = url
	url = url + "?word1=" + word1
	url = url + "&word2=" + word2
	
	_zsnSendData(url)
end

def Start(string url)
	string friend = _zsdFriendListToStr("FriendChecks")
	if _zsdIsNull(friend)
		_zsnPopDlg("请选择至少一位好友")
		return
	end
	
	string action = _zsdGetSelectResult("dt_select")
	if _zsdIsNull(action)
		_zsnPopDlg("请选择一个动作")
		return
	end
	
	string urlOut = url + "?mids=" + friend
	urlOut = urlOut + "&id=" + action
	
	_zsnSendData(urlOut)
end

def ShouDefaultPage(string url)
	_zsnSend(url)
end

def DelCustom(string url)
	string result = _zsdGetSelectResult("ct_select")
	if _zsdIsNull(result)
		_zsnPopDlg("请选择至少一个自定义动作")
		return
	end

	string urlOut = url
	urlOut = urlOut + "?ids=" + result
	_zsnSendData(urlOut)
end