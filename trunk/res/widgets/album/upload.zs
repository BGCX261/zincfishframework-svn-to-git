# 创建专辑弹出框
def CreateAlbum()
   _zseAlbumMNG(8)
end

# 浏览照片
def BrowserPictures()
   _zssHidePopWindow("imageedit")
   _zsnSend("/widgets/album/albumpage.xml")
   _zsdSetAttValue("0", "onclick", "SelectAlbumLogo(\"0\")")
   _zsdSetAttValue("1", "onclick", "SelectAlbumLogo(\"1\")")
   _zsdSetAttValue("2", "onclick", "SelectAlbumLogo(\"2\")")
   _zsdSetAttValue("3", "onclick", "SelectAlbumLogo(\"3\")")
end

# 弹出图片预览框
def PopPhotoview()
	_zssHidePopWindow("loading");
	_zssShowPopWindow("imageedit");
end

# 选择照片
def BrowserPhoto(string tfieldid)
	# 参数：textfield的ID，将选择的图片的路径放入该textfield
   _zsnSeletePhoto(tfieldid)
end

# 添加
int cur = 2
def AddSolt()
	cur = cur + 1
	if cur > 5
		_zsnPopDlg("一次最多上传5张照片")
		return
	end
	string divID = "phdiv" + cur
	_zsdSetAttValue(divID,"visible","true")
	_zsdRefresh("comdiv")
end

# 开始上传
def StartUpload()
		string album = _zsdGetAttValue("AlbumBox1", "value")
		
		string Path1 = _zsdGetAttValue("phtext1","value")
		string Path2 = _zsdGetAttValue("phtext2","value")
		string Path3 = _zsdGetAttValue("phtext3","value")
		string Path4 = _zsdGetAttValue("phtext4","value")
		string Path5 = _zsdGetAttValue("phtext5","value")
	
		string url = "http://test.3g.cn/sns/interface/AlbumUp.aspx"
		
		_zssShowPopWindow("loading")
		
		# 参数：(2+5*n)个参数  url + 回调函数 + n*（路径+名称+描述+专辑+好友）
		_zsnPostPhoto(url,"UploadOk()", Path1,"","",album,"",  Path2,"","",album,"",  Path3,"","",album,"",  Path4,"","",album,"",  Path5,"","",album,"" )
end

# 上传完成的回调
def UploadOk()
	_zssHidePopWindow("loading")
	_zsnPopDlg("上传完成")
end

# 取消上传
def CancelUpload()
	_zsnStopPostPhoto()
	_zssHidePopWindow("loading")
	_zsnPopDlg("上传已取消")
end

# 弹出创建专辑框
def ShowCreateAlbumPage()
	_zssShowPopWindow("CreateAlbumPop")
end

# 取消创建专辑弹出框
def HideCreateAlbumPage()
	_zssHidePopWindow("CreateAlbumPop")
end

# 新建相册
def NewAlbum()
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

# 显示删除专辑提示框
def ShowDelAlbumPop()
	int num = _zsdGetChildrenNum("AlbumBox1")
	if num<=0
		_zsnPopDlg("没有专辑被选中")
		return
	end
	
	string hint = "是否删除所选专辑?"
	_zsdSetAttValue("DelLable","text",hint)
	_zsdRefresh("DelPop")
	_zssShowPopWindow("DelPop")
end

# 删除专辑
def SureDel()
	string albumID = _zsdGetAttValue("AlbumBox1","value")
	if albumID==""
		_zsnPopDlg("没有选中专辑")
	end
	
	string url = "http://test.3g.cn/sns/interface/AlbumSpecDel_Packet.aspx"
	url = url + "?specid=" + albumID
	_zsnSendLater(url)
	_zssHidePopWindow("DelPop")
	_zsdRefresh()
	_zsnSendNow()
end

# 取消删除专辑对话框
def CancelDel()
	_zsdRemoveTempString("Del_Album_ID")
	_zssHidePopWindow("DelPop")
end