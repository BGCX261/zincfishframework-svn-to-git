# 弹出评论列表
def ClickPhoto()
	_zssShowPopWindow("comments")
end

# 查看某条评论
def LookComment(string id)
	string content = _zsdGetAttValue(id,"content")
	_zsnPopDlg(content)
end

# 显示确认删除框
def ShowDeletePhoto()
	string photoId = _zsdGetFocusID()
	_zsdSaveTempString("Del_Photo_ID",photoId)
	
	string name = _zsdGetAttValue(photoId,"title")
	string show = "是否删除\"" + name + "\"?"
	_zsdSetAttValue("DelLable","text",show)
	_zsdRefresh("DelPop")
	_zssShowPopWindow("DelPop")
end

# 确认删除
def SureDel()
	string curId = _zsdLoadTempString("Del_Photo_ID")
	_zsdRemoveTempString("Del_Photo_ID")
	string album = _zsdGetDataValue("albumlist","id")
	string photo = _zsdGetAttValue(curId, "data")
	
	string url = "http://test.3g.cn/sns/interface/AlbumPicDel_Packet.aspx"
	url = url + "?picid=" + photo
	url = url + "&specid=" + album
	_zsnSendLater(url)
	_zsnSendNow()
	_zssHidePopWindow("DelPop")
end

# 取消删除对话框
def CancelDel()
	_zsdRemoveTempString("Del_Photo_ID")
	_zssHidePopWindow("DelPop")
end

# 照片所在专辑
def AlbumWithPhoto()
	string albumID = _zsdGetDataValue("albumlist","id")
	if albumID==""
		_zsnPopDlg("页面数据错误")
		return
	end

	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx"
	url = url + "?specid=" + albumID
	_zsnSend("/widgets/album/albumpage.xml","_zsnSend",url)
end

# 设为专辑封面
def ShowSetCover()
	string curId = _zsdGetFocusID()
	if curId==""
		_zsnPopDlg("页面数据错误")
		return
	end

	string name = _zsdGetAttValue(curId,"title")
	string show = "是否将\"" + name + "\"设为专辑封面?"
	_zsdSetAttValue("CoverLable","text",show)
	_zsdRefresh("CoverPop")
	_zssShowPopWindow("CoverPop")
end

# 确认设置为封面
def SureCover()
	string curId = _zsdGetFocusID()
	if curId==""
		_zsnPopDlg("页面数据错误")
		return
	end
	
	string photo = _zsdGetAttValue(curId,"data")
	string url = "http://test.3g.cn/sns/interface/AlbumCoverSet_Packet.aspx"
	url = url + "?picid=" + photo
	_zsnSendLater(url)
	_zsnSendNow()
	_zssHidePopWindow("CoverPop")
end

# 取消设置封面对话框
def CancelCover()
	_zsdRemoveTempString("SetCover_Photo_ID")
	_zssHidePopWindow("CoverPop")
end

# 编辑照片属性
def EditPhoto()
	string curId = _zsdGetFocusID()
	if curId==""
		_zsnPopDlg("页面数据错误")
		return
	end
	
	string photo = _zsdGetAttValue(curId, "data")
	string url = "http://test.3g.cn/sns/interface/AlbumPicEdit.aspx"
	url = url + "?picid=" + photo
	#_zsnSend("/widgets/album/EditPhoto.xml","_zsnSend",url)
end

# 提交评论
def SubComment()
	string curId = _zsdGetFocusID()
	if curId==""
		_zsnPopDlg("页面数据错误")
		return
	end
	
	string content = _zsdGetAttValue("commentT","value")
	if content==""
		_zsnPopDlg("请输入评论")
		return
	end
	
	string photo = _zsdGetAttValue(curId, "data")
	string url = "http://test.3g.cn/sns/interface/.aspx"
	url = url + "?picid=" + photo
	url = url + "&contents=" + content
	_zsnPopDlg(url)
	# _zsnSendLater(url)
	# _zsnSendNow()
end

# 去到评论页面
def MoreComment()
	
end