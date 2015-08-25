#在启动弹出框时缓存当前选中的好友item的id
string friendsel
#缓存当前操作的好友分组id
string currentfocusgroup

#切换好友组
def changevisable(string id_show, string id_hidden1, string id_hidden2)
	_zsdSetAttValue(id_show, "visible", "true")
	_zsdSetAttValue(id_hidden1, "visible", "false")
	_zsdSetAttValue(id_hidden2, "visible", "false")
	_zsdRefresh(id_show)
end

#关闭弹出框
def CancelFriendPanel(string panelID)
	_zssHidePopWindow(panelID)
end

#加载好友组
def getGroupList()
	int groupCnt=_zsnGetGroupsListSize()
	array groupinfo[][]=_zsnGetGroupsList()
	string idtemp
	string nametemp
	int groupindex=0
	while groupindex<groupCnt
		idtemp=groupinfo[groupindex][0]
		nametemp=groupinfo[groupindex][1]
		_zsdAddDom("button", idtemp, "grouplist")
		_zsdSetAttValue(idtemp, "value", idtemp)
		_zsdSetAttValue(idtemp, "name", nametemp)
		_zsdSetAttValue(idtemp, "text", nametemp)
		_zsdSetAttValue(idtemp, "onfocus", "reloadFriendList")
		groupindex=groupindex+1
	end
	_zsdRefresh()
end

#加载当前选中好友组的好友到vlist
def reloadFriendList()
	string currentfocusid
	currentfocusid=_zsdGetFocusID()
	currentfocusgroup=currentfocusid
	
	_zssprintln("currentfocusgroup========"+currentfocusgroup)
	
	_zsdClearSubDoms("friendlist")
	int fricnt=_zsnGetFriendsListSize(currentfocusid)
	array friinfo[][]=_zsnGetFriendsList(currentfocusid)
	string midtemp
	string nametemp
	string descriptiontemp
	int friindex=0
	while friindex<fricnt
		midtemp=friinfo[friindex][0]
		nametemp=friinfo[friindex][1]
		descriptiontemp=friinfo[friindex][2]
		_zssprintln("midtemp========"+midtemp)
		_zssprintln("nametemp========"+nametemp)
		_zsdAddDom("item", midtemp, "friendlist")
		_zsdSetAttValue(midtemp, "title", nametemp)
		_zsdSetAttValue(midtemp, "content", descriptiontemp)
		_zsdSetAttValue(midtemp, "value", midtemp)
		_zsdSetAttValue(midtemp, "name", nametemp)
		friindex=friindex+1
	end
	_zsdRefresh()
end


#弹出操作框，加载操作好友及分组选项
def PopGroupPanel(string panelID, string nameID, string seleteID)		
	string focus=_zsdGetFocusID()
	friendsel=focus

	string frname
	string frvalue
	frname = _zsdGetAttValue(friendsel, "title")
	frvalue = _zsdGetAttValue(friendsel, "value")
	_zsdSetAttValue(nameID, "text", frname)
	_zsdSetAttValue(nameID, "value", frvalue)
	
	_zsdClearSubDoms(seleteID)
	int groupCnt=_zsnGetGroupsListSize()
	array groupinfo[][]=_zsnGetGroupsList()
	string idtemp
	string nametemp
	string checksId
	int groupindex=0
	while groupindex<groupCnt
		idtemp=groupinfo[groupindex][0]
		nametemp=groupinfo[groupindex][1]
		checksId = seleteID + idtemp
		_zsdAddDom("checkbox", checksId, seleteID)
		_zsdSetAttValue(checksId, "value", idtemp)
		_zsdSetAttValue(checksId, "text", nametemp)
		_zsdSetAttValue(checksId, "type", "radio")
		groupindex=groupindex+1
	end
	_zssShowPopWindow(panelID)
end

#复制好友到组
def moveFriendFromGroup(string aUrl,string GroupId)
	string guid
	guid=_zsnGetGuid()
	string url
	url=aUrl
	url=url+"guid="
	url=url+guid
	string focus=friendsel
	string value=_zsdGetAttValue(focus, "value")
	string firstdata="mids="
	firstdata=firstdata+value	
	firstdata=firstdata+"&Groupid="
	string groupvalue=_zsdGetAttValue(GroupId, "value")
	firstdata=firstdata+groupvalue
	_zsnSendData(url, firstdata, 1)
	_zssHidePopWindow("MoveFriendtoGroup")
end

#剪贴好友到组
def CutFriendFromGroup(string aUrl,string GroupId)
	string guid
	guid=_zsnGetGuid()
	string url
	url=aUrl
	url=url+"guid="
	url=url+guid
	string firstdata="Groupid="
	string groupvalue=_zsdGetAttValue(GroupId, "value")
	firstdata=firstdata+groupvalue
	firstdata=firstdata+"&GroupOldId="	
	string parent=currentfocusgroup
	string parentval=_zsdGetAttValue(parent, "value")	
	firstdata=firstdata+parentval	
	string focus=friendsel
	string value=_zsdGetAttValue(focus, "value")
	firstdata=firstdata+"&mids="
	firstdata=firstdata+value
	_zsnSendData(url, firstdata, 1)
	_zssHidePopWindow("CutFriendtoGroup")
end

#删除分组
def DelCurGroup()	
	string val="Groupid="
	string id=currentfocusgroup
	string mids=_zsdGetAttValue(id, "id")
	val=val+mids
	string url="http://test.3g.cn/sns/interface/frigroupdel.aspx?"
	string guid
	guid=_zsnGetGuid()
	url=url+"&guid="
	url=url+guid
	_zsnSendData(url, val, 1)		
end

#从组中删除好友
def DelfriendFromGroup()
	string guid
	guid=_zsnGetGuid()
	string focus=_zsdGetFocusID()
	string parent=_zsdGetParentID(focus)
	if parent != "friendlist"
	    return 0
	end
	string val="mids="	
	string mids=_zsdGetAttValue(focus, "id")
	val=val+mids
	string parent=currentfocusgroup
	string parentval=_zsdGetAttValue(parent, "value")	
	val=val+"&groupid="
	val=val+parentval
	string url="http://test.3g.cn/sns/interface/fridel2.aspx?"
	_zsnSendData(url, val, 1)	
	return 1
end

#创建好友组
def CreateGroup(string aUrl, string aId, string amethod)
	string guid
	guid=_zsnGetGuid()
	string url
	url=aUrl	
	url=url+"&guid="
	url=url+guid
	url=url+"&time="
	string time=_zsnGetFriendTime()
	url=url+time

	string groupname = _zsdGetAttValue("groupname", "value")
	string val = "Groupname="
	val = val + groupname
	_zsnSendData(url, val, 1)
	_zssHidePopWindow("addGroup")
end

#删除好友
def delfriend()
	string guid
	guid=_zsnGetGuid()
	string focus=_zsdGetFocusID()
	string parent=_zsdGetParentID(focus)
	if parent != "friendlist"
	    return 0
	end
	string val="mids="
	string tmp=_zsdGetAttValue(focus, "id")
	val=val+tmp
	string url="http://test.3g.cn/sns/interface/FriDel.aspx?"
	url=url+"&guid="
	url=url+guid
	_zsnSendData(url, val, 1)
	return 1
end


def popuplist(string id) 
	_zssShowPopWindow(id)
end

def showcontent(string id)
    _zsnSend(id)
end



#以下部分暂时未使用
def getFriendList2()
	_zsnCreateFriendList(0)
end

def opennode()
	_zsnOpenCurNode()
end

def showdialog(string id)
	_zsnPopDlg(id)
end

def Search(string aId, string amethod, string first)
	_zsnXmlDomSubmit("http://test.3g.cn/sns/interface/Frisearchresult.aspx",aId, amethod, first,"friend\\searchResult.xml")	
end

def AgreeaddToFriendRequest(string aUrl, string Request, string GroupId)
	string guid
	guid=_zsnGetGuid()
	string url
	url=aUrl
	url=url+"group="
	string groupvalue=_zsdGetAttValue(GroupId, "value")	
	url=url+groupvalue
	url=url+"&guid="
	url=url+guid			
	 _zsnShowcontent(url)
	 _zsdHidePopPanel("AgreeAddFriendtoGroup")	 
end




