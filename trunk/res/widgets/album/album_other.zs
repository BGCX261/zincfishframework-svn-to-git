# 判断专辑的可访问性、是否有照片
def GoAlbum()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="AList"
			_zsnPopDlg("请将焦点移到专辑上")
		return
	end
	
	string num = _zsdGetAttValue(curId,"ltail1")
	if num=="0"
		_zsnPopDlg("该专辑中没有照片")
		return
	end
	
	string level = _zsdGetAttValue(curId,"level")
	_zsdSaveTempString("PW_Album_ID",curId)
	
	if level=="2"
		_zsdSetAttValue("ciper","value","")
		_zssShowPopWindow("showciper")
	elsif level=="1"
		_zsnPopDlg("该专辑仅有好可访问")
	else
		EnterAlbum()
	end
end

# 打开专辑
def EnterAlbum()
	string curId = _zsdLoadTempString("PW_Album_ID")
	_zsdRemoveTempString("PW_Album_ID")
	string albumId = _zsdGetDataValue(curId,"id")
	string mid = _zsdGetDataValue("pages","mid")

	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx?specid=" + albumId
	url = url + "&mids=" + mid

	_zsnSend( "/widgets/album/albumpage_other.xml", "_zsnSend", url )
end

# 密码确认
def CiperConfirm()
	string id = _zsdLoadTempString("PW_Album_ID")
	string pw = _zsdGetDataValue(id,"pw")
	string inputPW = _zsdGetAttValue("ciper", "value")
	
	if pw==inputPW
		_zssHidePopWindow("showciper")
		EnterAlbum()
	else
		_zsnPopDlg("密码错误!")
	end
end

# 我的专辑，下一页
def MAN()
	string pageStr = _zsdGetDataValue("myA_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn + 1
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx?ty=myA&pn=" + pn
	_zsnSend(url)
end

# 我的专辑，上一页
def MAP()
	string pageStr = _zsdGetDataValue("myA_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn - 1
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx?ty=myA&pn=" + pn
	_zsnSend(url)
end

# 执行当前节点的onclick
def GoIn()
	_zsnOpenCurNode()
end

# 查看访问级别
def ShowLevel()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="AList"
			_zsnPopDlg("请将焦点移到专辑上")
		return
	end
	string level = _zsdGetDataValue(curId,"level")
	string show
	if level=="0"
		show = "任何人可访问"
	elsif level=="1"
		show = "仅好友可访问"
	elsif level=="2"
		show = "凭密码访问"
	end
	
	_zsnPopDlg(show)
end

# 去相册主人首页
def OtherHome()	
	string mid = _zsdGetDataValue("pages","mid")
	string url = "http://test.3g.cn/sns/interface/.aspx"
	url = url + "?mid=" + mid
	_zsnPopDlg(url)
end

# 去我的首页
def MyHome()

end