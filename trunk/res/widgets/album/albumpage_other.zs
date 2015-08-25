# 查看具体照片
def BrowserAlbum()
    string curId = _zsdGetFocusID()
    string mid = _zsdGetDataValue("headtext","mid")
    string picid = _zsdGetDataValue(curId,"id")

	string url = "http://test.3g.cn/sns/interface/Albumpicmsg2.aspx"    
	url = url + "?picid=" + picid
	url = url + "&mids=" + mid
	_zsnSend("/widgets/album/photopage_other.xml","_zsnSend",url)
end

# 照片属性
def ShowPicAttr()
	string curId = _zsdGetFocusID()
	string parent = _zsdGetParentID(curId)
	if parent!="photolist"
		_zsnPopDlg("请将焦点移到一张照片上")
		return
	end
	string photo = _zsdGetDataValue(curId, "id")
	string url = "http://test.3g.cn/sns/interface/AlbumPicEdit.aspx"
	string mid = _zsdGetDataValue("headtext","mid")
	url = url + "?picid=" + photo
	url = url + "&mids=" + mid
	_zsnSend("/widgets/album/EditPhoto_other.xml","_zsnSend",url)
end

# Ta的相册首页
def HisHome()
	string mid = _zsdGetDataValue("headtext","mid")
	if mid==""
		_zsnPopDlg("页面数据错误")
	end
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList.aspx?mids=" + mid
	_zsnSend("/widgets/album/album_other.xml","_zsnSend",url)
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

# 执行当前选中项
def OpenCur()
	_zsnOpenCurNode()
end