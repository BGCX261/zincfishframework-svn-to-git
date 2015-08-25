# 获取评论
def GetComments(string albumid)
    _zssShowPopWindow("comments")
end

# 查看评论内容
def LookComment(string listid)
    _zssHidePopWindow(\"comments\")
    _zssShowPopWindow(\"popreplay\")
end

# 拍照界面
def Snap()
	_zsnEntryCameraScreen("SnapCallBack")
end	

# 拍照回调
def SnapCallBack(string aPath)
	if aPath==""
		_zsnPopDlg("没有照片!")
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList2.aspx"
	_zsnSend("/widgets/album/upload.xml","_zsnSend",url)
	_zsdSetAttValue("phtext1","value",aPath)
	_zsdRefresh()
end

# 判断专辑的可访问性、是否有照片
def GoAlbum()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="myAList"
			_zsnPopDlg("请将焦点移到专辑上")
		return ""
	end
	
	string num = _zsdGetAttValue(curId,"ltail1")
	if num=="0"
		_zsnPopDlg("该专辑中没有照片")
		return ""
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
	string mid = _zsdGetDataValue(curId,"mid")

	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx?specid=" + albumId
	url = url + "&mids=" + mid

	_zsnSend( "/widgets/album/albumpage.xml", "_zsnSend", url )
end

# 打开照片
def GoPhoto()
	string curId = _zsdGetFocusID()
    string mid = _zsdGetDataValue(curId,"mid")
    string picid = _zsdGetDataValue(curId,"id")

	string url = "http://test.3g.cn/sns/interface/Albumpicmsg2.aspx"    
	url = url + "?picid=" + picid
	url = url + "&mids=" + mid
	_zsnSend("/widgets/album/photopage_other.xml","_zsnSend",url)
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

# tag 切换
def SwitchView(string tabid,string menu)
	_zsdSetAttValue("myAlbum", "visible", "false")
	_zsdSetAttValue("myPhoto", "visible", "false")
	_zsdSetAttValue("friPhoto", "visible", "false")
	_zsdSetAttValue(tabid, "visible", "true")
	_zsdRefresh()
	_zsdSwitchLeftMenu(menu)
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

# 圈有我的照片，下一页
def MPN()
	string pageStr = _zsdGetDataValue("myP_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn + 1
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx?ty=myP&pn=" + pn
	_zsnSend(url)
end

# 圈有我的照片，上一页
def MPP()
	string pageStr = _zsdGetDataValue("myP_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn - 1
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx?ty=myP&pn=" + pn
	_zsnSend(url)
end

# 圈有好友的照片，上一页
def FPN()
	string pageStr = _zsdGetDataValue("friP_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn + 1
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx?ty=friP&pn=" + pn
	_zsnSend(url)
end

# 圈有好友的照片，下一页
def FPP()
	string pageStr = _zsdGetDataValue("friP_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn - 1
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx?ty=friP&pn=" + pn
	_zsnSend(url)
end

# 显示上传照片页面，默认到选中专辑
def ShowUploadPage()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="myAList"
			_zsnPopDlg("请将焦点移到专辑上")
		return
	end
	string specID = _zsdGetDataValue(curId,"id")
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList2.aspx?specid=" + specID
	_zsnSend("/widgets/album/upload.xml","_zsnSend",url)
end

# 弹出创建专辑框
def ShowCreateAlbumPage()
	_zssShowPopWindow("CreateAlbumPop")
end

# 取消创建专辑弹出框
def HideCreateAlbumPage()
	_zssHidePopWindow("CreateAlbumPop")
end

# 创建专辑
def CreateAlbum()
	string name = _zsdGetAttValue("CA_NameT","value")
	if name==""
		_zsnPopDlg("请输入专辑名")
		return
	end
	
	string privacy = _zsdGetAttValue("CA_Level","value")
	string url = "http://test.3g.cn/sns/interface/AlbumSpecAdd_Packet.aspx"
	url = url + "?specname=" + name
	url = url + "&privacy=" + privacy
	
	string password
	if privacy=="2"
		password = _zsdGetAttValue("CA_Password_T","value")
		if password==""
			_zsnPopDlg("请输入访问密码")
			return
		else
			url = url + "&password=" + password
		end
	end
	
	_zsnSendLater(url)
	_zssHidePopWindow("CreateAlbumPop")
	_zsnSendNow()
end

# 创建专辑弹出框 的密码输入
def CAPassWord(string isShow)
	_zsdSetAttValue("CA_Password_L","visible",isShow)
	_zsdSetAttValue("CA_Password_T","visible",isShow)
	_zsdRefresh("CreateAlbumPop")
end

# 弹出编辑专辑属性框
def ShowEditAlbumPage()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="myAList"
			_zsnPopDlg("请将焦点移到专辑上")
		return
	end
	
	string albumId = _zsdGetDataValue(curId,"id")
	_zsdSaveTempString("Edit_Album_ID",albumId)
	
	string name = _zsdGetAttValue(curId,"title")
	_zsdSetAttValue("MA_NameT","value",name) 
	
	_zssShowPopWindow("ModifyAlbumPop")
end

# 取消编辑专辑弹出框
def HideEditAlbumPage()
	_zsdRemoveTempString("Edit_Album_ID")
	_zssHidePopWindow("ModifyAlbumPop")
end

# 编辑专辑属性
def EditAlbum()
	string name = _zsdGetAttValue("MA_NameT","value")
	if name==""
		_zsnPopDlg("请输入专辑名")
		return
	end
	
	string privacy = _zsdGetAttValue("MA_Level","value")
	string url = "http://test.3g.cn/sns/interface/AlbumSpecEditTrue_Packet.aspx"
	url = url + "?specname=" + name
	url = url + "&privacy=" + privacy
	
	string password
	if privacy=="2"
		password = _zsdGetAttValue("MA_Password_T","value")
		if password==""
			_zsnPopDlg("请输入访问密码")
			return
		else
			url = url + "&password=" + password
		end
	end
	
	string albumId = _zsdLoadTempString("Edit_Album_ID")
	_zsdRemoveTempString("Edit_Album_ID")
	url = url + "&Specid=" + albumId 
	
	_zsnSendLater(url)
	_zssHidePopWindow("ModifyAlbumPop")
end

# 修改专辑属性弹出框 的密码输入
def MAPassWord(string isShow)
	_zsdSetAttValue("MA_Password_L","visible",isShow)
	_zsdSetAttValue("MA_Password_T","visible",isShow)
	_zsdRefresh("ModifyAlbumPop")
end

# 执行当前节点的onclick
def GoIn()
	_zsnOpenCurNode()
end

# 显示删除专辑提示框
def ShowDelAlbumPop()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="myAList"
			_zsnPopDlg("请将焦点移到专辑上")
		return
	end
	
	string albumId = _zsdGetDataValue(curId,"id")
	_zsdSaveTempString("Del_Album_ID",curId)
	
	string name = _zsdGetAttValue(curId,"title")
	string hint = "是否删除\"" + name + "\"?"
	_zsdSetAttValue("DelLable","text",hint)
	_zsdRefresh("DelPop")
	_zssShowPopWindow("DelPop")
end

# 删除专辑
def SureDel()
	string curId = _zsdLoadTempString("Del_Album_ID")
	_zsdRemoveTempString("Del_Album_ID")
	string albumId = _zsdGetDataValue(curId,"id")
	
	string url = "http://test.3g.cn/sns/interface/AlbumSpecDel_Packet.aspx"
	url = url + "?specid=" + albumId
	_zsnSendLater(url)
	_zssHidePopWindow("DelPop")
	_zsdDelDom(curId)
	_zsdRefresh()
	_zsnSendNow()
end

# 取消删除专辑对话框
def CancelDel()
	_zsdRemoveTempString("Del_Album_ID")
	_zssHidePopWindow("DelPop")
end

# 查看专辑访问权限
def ShowLevel()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="myAList"
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

# 照片所在专辑
def AlbumWithPhoto()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="myPList"
		if	parent != "friPList"
			_zsnPopDlg("请将焦点移到专辑上")
			return
		end
	end

	string albumId = _zsdGetDataValue(curId,"album")
	string mid = _zsdGetDataValue(curId,"mid")
	if albumId==""
		_zsnPopDlg("页面数据错误")
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx"
	url = url + "?specid=" + albumId
	url = url + "&mids=" + mid
	_zsnSend("/widgets/album/albumpage_other.xml","_zsnSend",url)
end

# 到别人的相册首页
def GoOtherAlbum()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="myPList"
		if	parent != "friPList"
			_zsnPopDlg("请将焦点移到专辑上")
			return
		end
	end
	
	string album = _zsdGetDataValue(curId,"album")
	string mid = _zsdGetDataValue(curId,"mid")
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList.aspx"
	url = url + "?mids=" + mid
	# url = url + "&specid=" + album
	
	_zsnSend("/widgets/album/album_other.xml","_zsnSend",url)
	# _zsnSend("/widgets/album/album_other.xml","_zsnSend","/widgets/album/album_other_down.xml")
end

# 到别人的首页
def GoOtherHome()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="myPList"
		if	parent != "friPList"
			_zsnPopDlg("请将焦点移到专辑上")
			return
		end
	end
	
	string mid = _zsdGetDataValue(curId,"mid")
	string url = "http://test.3g.cn/sns/interface/.aspx"
	url = url + "?mid=" + mid
	_zsnPopDlg(url)
	# _zsnSend()
end
