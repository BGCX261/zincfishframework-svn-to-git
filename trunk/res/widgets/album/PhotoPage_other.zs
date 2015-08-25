# 弹出评论列表
def ClickPhoto()
	_zssShowPopWindow("comments")
end

# 查看某条评论
def LookComment(string id)
	string content = _zsdGetAttValue(id,"content")
	_zsnPopDlg(content)
end

# 查看照片信息
def PhotoInfo()

end

# 照片所在专辑
def GoAlbum()
	string albumId = _zsdGetDataValue("albumlist","id")
	string mid = _zsdGetDataValue("albumlist","mid")
	if mid==""
		_zsnPopDlg("页面数据错误")
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx"
	url = url + "?specid=" + albumId
	url = url + "&mids=" + mid
	_zsnSend("/widgets/album/albumpage_other.xml","_zsnSend",url)
end

# 别人的相册首页
def OtherPhotoHome()
	string mid = _zsdGetDataValue("albumlist","mid")
	if mid==""
		_zsnPopDlg("页面数据错误")
		return
	end
	
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList.aspx"
	url = url + "?mids=" + mid
	
	_zsnSend("/widgets/album/album_other.xml","_zsnSend",url)
end

# 别人的首页
def OtherHome

end

# 我的首页
def MyHome
	_zsnSend(&quot;/widgets/main/mainpage.xml&quot;)
end

# 提交评论
def SubmitComment()
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

# 查看更多评论
def MoreComment()

end