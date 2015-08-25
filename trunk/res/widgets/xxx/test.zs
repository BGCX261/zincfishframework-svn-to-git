def loadFirendsList()
	string frtID
	string frtName
	string frtGroup
	string frtDes
	array fList[][]=_zsnGetFriendsList("0")
	int i=0	
	while i<_zsnGetFriendsListSize("0")	
		 frtID = fList[i][0]
		 frtName = fList[i][1]
		 frtGroup = fList[i][2]
		 frtDes = fList[i][3]
		_zssprintln(frtID+"$"+frtName+"$"+frtGroup+"$"+frtDes)
		i=i+1
	end
end

def updateFriendsList()
	_zsnUpdateFriendsList()
end

def delFriend()
	string mid="541840588092541437"
	string url="http://test.3g.cn/sns/interface/FriDel.aspx"
	string guid=_zsnGetGuid()
	_zsnSend(url+"?guid="+guid+"&mids="+mid)
end