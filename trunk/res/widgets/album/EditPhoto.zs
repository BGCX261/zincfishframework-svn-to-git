# 修改专辑属性
def ModifyPhotoAttr()
		string photoName = _zsdGetAttValue("nameT","value")
		string photoDes = _zsdGetAttValue("desT","value")
		string albumId = _zsdGetAttValue("albumC","value")
		string photoId = _zsdGetAttValue("photoI","data")
		
		string url="http://test.3g.cn/sns/interface/AlbumPicEditTrue_Packet.aspx"
		url = url + "?Picid=" + photoId
		url = url + "&title=" + photoName
		url = url + "&specid=" + albumId
		url = url + "&msg=" + photoDes
		
		int count = _zsdGetChildrenNum("friHL")
		if count > 0
			int i=0
			string fri = "&mids="
			while i<count
				string itemID = _zsdGetChildrenID("friHL",i)
				string friID = _zsdGetAttValue(itemID,"data")
				fri = fri + friID + ";"
				i = i + 1
			end
			url = url + fri
		end
		
		# _zsnPopDlg(url)
		_zsnSendLater(url)
		_zsnSendNow()
end

# 确定选择好友
def updateRcvrs()
	string PanelID = "FriSelPop"
	string SeleteID = "FriSelect"
	string ListID = "friHL"
	
	int childNum = _zsdGetChildrenNum(ListID)
	int i=childNum-1
	string childID
	while i>=0
		childID = _zsdGetChildrenID(ListID,i)
		if childID!=""
			_zsdDelDom(childID)
		end
		i=i-1
	end
	_zsdRefresh()

	_zssHidePopWindow(PanelID)

	int fricnt
	fricnt=_zsdGetChildrenNum(SeleteID)
	int remainnum
	remainnum=0
	string subID
	string isSelected
	string recvrs
	string recvrtemp
	
	string recvrid
	string friID
	
	while remainnum<fricnt
		subID=_zsdGetChildrenID(SeleteID, remainnum)
		isSelected=_zsdGetAttValue(subID, "select")
		if isSelected=="true"
			recvrtemp=_zsdGetAttValue(subID, "text")
			recvrid="recvr"+remainnum
			_zsdAddDom("label", recvrid, ListID)
			_zsdSetAttValue(recvrid, "text", recvrtemp)
			friID = _zsdGetAttValue(subID, "value")
			_zsdSetAttValue(recvrid, "data", friID)
		end
		remainnum=remainnum+1
	end
	_zsdRefresh()		
end

# 弹出选择好友对话框
def PopFriendPanel(string panelID, string seleteID)
	_zsdPopFriendPanel(seleteID)
	_zssShowPopWindow(panelID)
end