# 查看具体照片
def BrowserAlbum()
    string curId = _zsdGetFocusID()
    string mid = _zsdGetDataValue("headtext","mid")
    string picid = _zsdGetDataValue(curId,"id")

		string url = "http://test.3g.cn/sns/interface/Albumpicmsg2.aspx"    
		url = url + "?picid=" + picid
		url = url + "&mids=" + mid
		_zsnSend("/widgets/album/photopage.xml","_zsnSend",url)
end

# 显示删除确认对话框
def DeletePhoto()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="photolist"
			_zsnPopDlg("请将焦点移到一张照片上")
		return
	end
	
	_zsdSaveTempString("Del_Photo_ID",curId)
	
	string name = _zsdGetAttValue(curId,"title")
	string show = "是否删除\"" + name + "\"?"
	_zsdSetAttValue("DelLable","text",show)
	_zsdRefresh("DelPop")
	_zssShowPopWindow("DelPop")
end

# 显示设为封面对话框
def SetCover()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="photolist"
			_zsnPopDlg("请将焦点移到一张照片上")
		return
	end
	
	_zsdSaveTempString("SetCover_Photo_ID",curId)
	
	string name = _zsdGetAttValue(curId,"title")
	string show = "是否将\"" + name + "\"设为专辑封面?"
	_zsdSetAttValue("CoverLable","text",show)
	_zsdRefresh("CoverPop")
	_zssShowPopWindow("CoverPop")
end

# 确认删除
def SureDel()
	string curId = _zsdLoadTempString("Del_Photo_ID")
	_zsdRemoveTempString("Del_Photo_ID")
	string mid = _zsdGetDataValue("headtext","mid")
	string album = _zsdGetDataValue("headtext","id")
	string photo = _zsdGetDataValue(curId, "id")
	
	string url = "http://test.3g.cn/sns/interface/AlbumPicDel_Packet.aspx"
	url = url + "?picid=" + photo
	url = url + "&specid=" + album
	# _zsnPopDlg(url)
	_zsnSendLater(url)
	_zssHidePopWindow("DelPop")
	
	_zsdDelDom(curId)
	_zsdRefresh()
	_zsnSendNow()
end

# 确认设置为封面
def SureCover()
	string curId = _zsdLoadTempString("SetCover_Photo_ID")
	_zsdRemoveTempString("SetCover_Photo_ID")
	string photo = _zsdGetDataValue(curId, "id")
	
	string url = "http://test.3g.cn/sns/interface/AlbumCoverSet_Packet.aspx"
	url = url + "?picid=" + photo
	# _zsnPopDlg(url)
	_zsnSendLater(url)
	_zssHidePopWindow("CoverPop")
end

# 取消删除对话框
def CancelDel()
	_zsdRemoveTempString("Del_Photo_ID")
	_zssHidePopWindow("DelPop")
end

# 取消设置封面对话框
def CancelCover()
	_zsdRemoveTempString("SetCover_Photo_ID")
	_zssHidePopWindow("CoverPop")
end

# 进入编辑照片属性页面
def ShowEditPhotoPage()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="photolist"
			_zsnPopDlg("请将焦点移到一张照片上")
		return
	end
	string photo = _zsdGetDataValue(curId, "id")
	string url = "http://test.3g.cn/sns/interface/AlbumPicEdit.aspx"
	url = url + "?picid=" + photo
	_zsnSend("/widgets/album/EditPhoto.xml","_zsnSend",url)
end

# 上一页
def PrePage()
	string pageStr = _zsdGetDataValue("in_gift_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn - 1
	string album = _zsdGetDataValue("headtext","id")
	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx?pn=" + pn
	url = url + "&specid=" + album
	_zsnSend(url)
end

# 下一页
def NextPage()
	string pageStr = _zsdGetDataValue("in_gift_pages","pn")
	int pn = _zssStr2Int(pageStr)
	pn = pn + 1
	string album = _zsdGetDataValue("headtext","id")
	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx?pn=" + pn
	url = url + "&specid=" + album
	_zsnSend(url)
end

# 查看
def OpenCur()
	_zsnOpenCurNode()
end

# 显示上传照片页面，默认到当前专辑
def ShowUploadPage()
	string specID = _zsdGetDataValue("headtext","id")
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList2.aspx?specid=" + specID
	_zsnSend("/widgets/album/upload.xml","_zsnSend",url)
end