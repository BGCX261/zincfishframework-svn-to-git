#弹出好友邀请
def PopInviteFriend()
	string focus=_zsdGetFocusID()
	friendsel=focus

	string frname
	string frvalue
	frname = _zsdGetAttValue(friendsel, "title")
	frvalue = _zsdGetAttValue(friendsel, "value")
	_zsdSetAttValue("frInvite", "text", frname)
	_zsdSetAttValue("frInvite", "value", frvalue)
	
	string seleteID="Invitechecks1"
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
	_zssShowPopWindow("InviteFriend")
end

#发送好友邀请
def InviteFriendRequest(string aUrl, string Request, string GroupId)
	string guid
	guid=_zsnGetGuid()
	string url
	url=aUrl
	url=url+"guid="
	url=url+guid
	string focus=friendsel
	string value=_zsdGetAttValue(focus, "value")
	string firstdata
	firstdata="mids="
	firstdata=firstdata+value
	firstdata=firstdata+"&contents="	
	string request=_zsdGetAttValue(Request, "value")	
	firstdata=firstdata+request	
	firstdata=firstdata+"&Group="
	string groupvalue=_zsdGetAttValue(GroupId, "value")	
	firstdata=firstdata+groupvalue
	_zsnSendData(url, firstdata, 1)
	_zssHidePopWindow("InviteFriendtoGroup")
end

#加载好友的好友
def refshfriendfriend()
	string guid
	guid=_zsnGetGuid()
	string url="http://test.3g.cn/sns/interface/FriFriList.aspx?guid="
	url=url+guid
	url=url+"&time="
	string time=_zsnGetFriendTime()	
	url=url+time
	_zsnSend(url)
end

#翻页
def prePage()
	string pageStr = _zsdGetDataValue("search_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn - 1

	string guid
	guid=_zsnGetGuid()
	string url="http://test.3g.cn/sns/interface/FriFriList.aspx?guid="
	url=url+guid
	url=url+"&time="
	string time=_zsnGetFriendTime()	
	url=url+time

	url = url + "&pn"
	url = url + pn

	_zsnSend(url)
end

def nextPage()
	string pageStr = _zsdGetDataValue("search_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn + 1

	string guid
	guid=_zsnGetGuid()
	string url="http://test.3g.cn/sns/interface/FriFriList.aspx?guid="
	url=url+guid
	url=url+"&time="
	string time=_zsnGetFriendTime()	
	url=url+time

	url = url + "&pn"
	url = url + pn
	_zsnSend(url)
end
