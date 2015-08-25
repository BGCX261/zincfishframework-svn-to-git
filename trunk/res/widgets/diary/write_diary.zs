# 弹出好友列表
def PopFrndList()
	_zsdClearSubDoms("FrndSelect")
	int frndcnt=_zsnGetFriendsListSize()
	array frndinfo[][]=_zsnGetFriendsList()
	string midtemp
	string nametemp
	string descriptiontemp
	int frndindex=0
	while frndindex<frndcnt
		midtemp=frndinfo[frndindex][0]
		nametemp=frndinfo[frndindex][1]
		descriptiontemp=frndinfo[frndindex][2]

		_zsdAddDom("checkbox", midtemp, "FrndSelect")
		_zsdSetAttValue(midtemp, "value", midtemp)
		_zsdSetAttValue(midtemp, "text", nametemp)
		frndindex=frndindex+1
	end
	_zssShowPopWindow("FrndSelPop")
end

# 更新提到的好友
def updateFrndList()
	string PanelID = "FrndSelPop"
	string SeleteID = "FrndSelect"

	_zssHidePopWindow(PanelID)

	int fricnt
	fricnt=_zsdGetChildrenNum(SeleteID)
	int remainnum
	remainnum=0
	string subID
	string isSelected

	string friname
	string friID
	string frRelateId

	string desContainerId="diary_friends"
	int exitcnt = _zsdGetChildrenNum(desContainerId)
	string exitFrndIdTemp
	string exitIdTemp
	int exitCheckIndex
	
	while remainnum<fricnt
		subID=_zsdGetChildrenID(SeleteID, remainnum)
		isSelected=_zsdGetAttValue(subID, "select")
		if isSelected=="true"
			friname=_zsdGetAttValue(subID, "text")
			friID = _zsdGetAttValue(subID, "value")
			exitCheckIndex = 0
			
			# clear exists
			while exitCheckIndex<exitcnt
				exitIdTemp = _zsdGetChildrenID(desContainerId)
				exitFrndIdTemp = _zsdGetAttValue(exitIdTemp, "value")
				if exitFrndIdTemp==friID
					_zsdDelDom(exitIdTemp)
					exitcnt=exitcnt-1
				end
				exitCheckIndex=exitCheckIndex+1
			end
			frRelateId = "fr"+friID
			_zsdAddDom("label", frRelateId, desContainerId)
			_zsdSetAttValue(frRelateId, "text", friname)
			_zsdSetAttValue(frRelateId, "value", friID)

		end
		remainnum=remainnum+1
	end
	_zssHidePopWindow(PanelID)
	_zsdRefresh()	
end



# 添加图片附件
def AddImgicAttachment()
	_zssOnPathLocating("file", "OnAddImg")
end

def OnAddImg(string imgpath, string imgName, string mimeType)
	int index = _zsdGetChildrenNum("attachment_img")
	index = index + 1
	string id = "attachment_img_" + index
	_zsdAddDom("img", id, "attachment_img")
	_zsdSetAttValue(id, "src", imgpath)
	_zsdSetAttValue(id, "available", "true")
	_zsdRefresh()
	_zsdMoveFocusTo(id)
	return

end

# 添加普通文件
def AddArchiveAttachment()
	_zssOnPathLocating("file", "OnAddArchive")
end

def OnAddArchive(string archivePath, string archiveName, string mimeType)
	int index = _zsdGetChildrenNum("attachment_other")
	index = index + 1
	string id = "attachment_other_" + index
	_zsdAddDom("button", id, "attachment_other")
	_zsdSetAttValue(id, "data", "archive")
	_zsdSetAttValue(id, "value", archivePath)
	_zsdSetAttValue(id, "bgpath", "/widgets/diary/img/dfile.png")
	_zsdSetAttValue(id, "hgpath", "/widgets/diary/img/dfilesel.png")
	_zsdRefresh()
	_zsdMoveFocusTo(id)
	return
end

# 添加视频文件
def AddVideoAttachment()
	_zssOnPathLocating("file", "OnAddVideo")
end

def OnAddVideo(string videoPath, string videoName, string mimeType)
	int index = _zsdGetChildrenNum("attachment_other")
	index = index + 1
	string id = "attachment_other_" + index
	_zsdAddDom("button", id, "attachment_other")
	_zsdSetAttValue(id, "data", "video")
	_zsdSetAttValue(id, "value", videoPath)
	_zsdSetAttValue(id, "bgpath", "/widgets/diary/img/dvideo.png")
	_zsdSetAttValue(id, "hgpath", "/widgets/diary/img/dvideosel.png")
	_zsdRefresh()
	_zsdMoveFocusTo(id)
	return
end

# 添加音频文件
def AddAudioAttachment()
	_zssOnPathLocating("file", "OnAddAudio")
end

def OnAddAudio(string audioPath, string audioName, string mimeType)
	int index = _zsdGetChildrenNum("attachment_other")
	index = index + 1
	string id = "attachment_other_" + index
	_zsdAddDom("button", id, "attachment_other")
	_zsdSetAttValue(id, "data", "audio")
	_zsdSetAttValue(id, "value", audioName)
	_zsdSetAttValue(id, "bgpath", "/widgets/diary/img/daudio.png")
	_zsdSetAttValue(id, "hgpath", "/widgets/diary/img/daudiosel.png")
	_zsdRefresh()
	_zsdMoveFocusTo(id)
	return
end

# 添加普通文件
def AddArchiveAttachment()
	_zssOnPathLocating("file", "OnAddArchive")
end

def OnAddArchive(string archivePath, string archiveName, string mimeType)
	int index = _zsdGetChildrenNum("attachment_other")
	index = index + 1
	string id = "attachment_other_" + index
	_zsdAddDom("button", id, "attachment_other")
	_zsdSetAttValue(id, "data", "archive")
	_zsdSetAttValue(id, "value", archivePath)
	_zsdSetAttValue(id, "bgpath", "/widgets/diary/img/dfile.png")
	_zsdSetAttValue(id, "hgpath", "/widgets/diary/img/dfilesel.png")
	_zsdRefresh()
	_zsdMoveFocusTo(id)
	return
end



# 删除附件
def RemoveAttachment()
	string focusId = _zsdGetFocusID()
	if focusId == ""
		_zsnPopDlg("请选中要删除的附件")
		return
	end
	string focusContainerId = _zsdGetParentID(focusId)
	if focusContainerId == "attachment_img"
		_zsdDelDom(focusId)
		_zsdRefresh()
		return
	end
	if focusContainerId == "attachment_other"
		_zsdDelDom(focusId)
		_zsdRefresh()
		return
	end
end

# 添加附件



# 发表日记
def PostDiary()
	string guid
	guid=_zsnGetGuid()
	string url="http://test.3g.cn/sns/interface/diaryup.aspx"
	#string url="test"
	string tmp="?guid="
	url=url+tmp
	url=url+guid
	_zsnPostDiary(url,"title","text","diary_priv","diary_friends","attachment_img","attachment_other","OnPostDiaryComplete")
end

def OnPostDiaryComplete(string bSuccess, string infos)
	if bSuccess=="true"
		_zsnPopDlg(infos)
		#_zsnSend("/widgets/diary/diary_page_mine.xml", "_zsnSend", infos)
		return
	end
	if bSuccess=="false"
		_zsnPopDlg(infos)
		return
	end
	_zsnPopDlg("出现问题！")
end
