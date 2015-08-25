
################## 主页 ####################

# 1.我的主页
# 参数：无
def GoMyHome()
	string url = "http://test.3g.cn/sns/interface/mymsg.aspx"
	_zsnSend("/widgets/main/mainpage.xml","_zsnSend",url)
end

# 2.别人的主页
# 参数：别人的id
def GoHome(string mid)
	string url = "http://test.3g.cn/sns/interface/dfmsg.aspx?mids=" + mid
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
end


################## 相册 ####################

# 1.我的相册主页(专辑列表)
# 参数：无
def GoMyAlbumList()
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx"
	_zsnSend("/widgets/album/album.xml","_zsnSend",url)
end

# 2.别人的相册首页(专辑列表)
# 参数：别人的mid
def GoAlbumList(string mid)
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx?mids=" + mid
	_zsnSend("/widgets/album/album_other.xml","_zsnSend",url)
end

# 3.我的专辑页面(照片列表)
# 参数：专辑ID
def GoMyPhotoList(string AlbumID)
	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx?specid=" + AlbumID
	_zsnSend("/widgets/album/albumpage.xml","_zsnSend",url)
end

# 4.别人的专辑页面(照片列表)
# 参数：别人的mid、专辑ID
def GoPhotoList(string Mid, string AlbumID)
	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx?specid=" + AlbumID + "&mids=" + Mid
	_zsnSend("/widgets/album/albumpage_other.xml","_zsnSend",url)
end

# 5.我的某张照片
# 参数：照片ID
def GoMyPhoto(string PicID)
	string url = "http://test.3g.cn/sns/interface/Albumpicmsg2.aspx?picid=" + PicID
	_zsnSend("/widgets/album/photopage.xml","_zsnSend",url)
end

# 6.别人的某张照片
# 参数：别人的mid、照片ID
def GoPhoto(string Mid, string PicID)
	string url = "http://test.3g.cn/sns/interface/Albumpicmsg2.aspx?picid=" + PicID + "&mids=" + Mid
	_zsnSend("/widgets/album/photopage_other.xml","_zsnSend",url)
end

# 7.编辑我的照片的属性
# 参数：照片ID
def GoEditMyPhoto(string PicID)
	string url = "http://test.3g.cn/sns/interface/AlbumPicEdit.aspx?picid=" + PicID
	_zsnSend("/widgets/album/EditPhoto.xml","_zsnSend",url)
end

# 8.查看别人的照片的属性
# 参数：照片ID
def GoEditPhoto(string PicID)
	string url = "http://test.3g.cn/sns/interface/AlbumPicEdit.aspx?picid=" + PicID
	_zsnSend("/widgets/album/EditPhoto_other.xml","_zsnSend",url)
end

# 9.上传照片页面
# 参数：默认专辑ID
def UpLoadPic(string AlbumId)
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList2.aspx?specid=" + AlbumId
	_zsnSend("/widgets/album/upload.xml","_zsnSend",url)
end

################## 投票 ####################

# 1.我的投票列表
# 参数：无
def GoMyVoten()
	string url = "http://test.3g.cn/sns/interface/votelist.aspx?lt=all"
	_zsnSend("/widgets/vote/voten.xml","_zsnSend",url)
end

# 2.别人的投票列表
# 参数：别人的mid
def GoVoten(string Mid)
	string url = "http://test.3g.cn/sns/interface/votelist.aspx?lt=df&mids=" + Mid
	_zsnSend("/widgets/vote/voten_other.xml","_zsnSend",url)
end

# 3.我的某项投票
# 参数：投票ID
def GoMyVoting(string VoteId)
	string url = "http://test.3g.cn/sns/interface/voteMsg.aspx?id=" + VoteId
	_zsnSend("/widgets/vote/voting.xml","_zsnSend",url)
end

# 4.别人的某项投票
# 参数：投票ID
def GoVoting(string VoteId)
	string url = "http://test.3g.cn/sns/interface/voteMsg.aspx?id=" + VoteId
	_zsnSend("/widgets/vote/voting_other.xml","_zsnSend",url)
end

# 5.发起投票
# 参数：无
def StartVote()
	_zsnSend("/widgets/vote/newvote.xml")
end

################### 日记 ######################
# 1.我的日记列表
# 参数：无
def GoMyDiaryList()
	string url="http://test.3g.cn/sns/interface/diaryList.aspx?lt=1"
	_zsnSend("/widgets/diary/diary_list.xml","_zsnSend",url)
end

# 2.别人的日记列表
# 参数：别人的mid
def ViewDiaryList(string mid)
	string url = "http://test.3g.cn/sns/interface/diaryList2.aspx"
	url = url + "?mids=" + mid
	_zsnSend("/widgets/diary/diary_list_others.xml","_zsnSend",url)
end

# 3.查看我的日记(页面有编辑上传等功能)
# 参数：日记id
def ViewMyDiary(string DiaryId)
	string url = "http://test.3g.cn/sns/interface/diarymsg.aspx"
	url = url + "?id=" + diaryId
	_zsnSend("/widgets/diary/diary_page_mine.xml", "_zsnSend", url)
end

# 4.查看日记
# 参数：日记id
def ViewDiary(string DiaryId)
	string url = "http://test.3g.cn/sns/interface/diarymsg.aspx"
	url = url + "?id=" + diaryId
	_zsnSend("/widgets/diary/diary_page_normal.xml", "_zsnSend", url)
end

# 5.写日记
# 参数：无
def GoWriteDiary()
	_zsnSend("/widgets/diary/write_diary.xml")
end

################### 好友 ######################
# 1.本地好友管理界面
# 参数：无
def GoLocalFrndList()
	_zsnSend("/widgets/friends/friend.xml")
end

# 2.查看指定用户的好友	TODO
# 参数：用户id
def ViewFrndList(string mid)
	_zsnSend("/widgets/friends/friend_list_others.xml")
end

# 3.进入好友搜索
# 参数：无
def GoFrndSearch()
	_zsnSend("/widgets/friends/searchFriend.xml")
end

# 4.查看所有好友的好友
# 参数：无
def GoFrndFrnd()
	string url="http://test.3g.cn/sns/interface/FriFriList.aspx?time="
	string time=_zsnGetFriendTime()	
	url=url+time
	_zsnSend("/widgets/friends/friend_friend.xml", "_zsnSend", url)
end

################### 消息 ######################
# 1.进入消息首页
# 参数：无
def GoMsgMainPage()
	_zsnSend("/widgets/message/msgmain.xml")
end

# 2.进入短消息列表
# 参数：无
def GoShortMsgList()
	string url = "http://test.3g.cn/sns/interface/msgrecsend.aspx"
	_zsnSend("/widgets/message/rec_msg_list.xml", "_zsnSend", url)
end

# 3.进入系统消息列表
# 参数：无
def GoSysMsgList()
	string url = "http://test.3g.cn/sns/interface/MsgSysListAll.aspx"
	_zsnSend("/widgets/message/sys_msg_list.xml", "_zsnSend", url)
end

# 4.进入留言消息列表
# 参数：无
def GoWordMsgList()
	string url = "http://test.3g.cn/sns/interface/MsgWordRecSend.aspx"
	_zsnSend("/widgets/message/word_msg_list.xml", "_zsnSend", url)
end

# 5.进入评论消息列表
# 参数：无
def GoCommentMsgList()
	string url = "http://test.3g.cn/sns/interface/CommentRecSend.aspx"
	_zsnSend("/widgets/message/comment_msg_list.xml", "_zsnSend", url)
end
################### 评论 ######################
# 1.查看某个物件的评论列表 TODO
# 参数：物件id
def ViewCommentList(string id)
	
end

# 2.查看评论
# 参数：commentId:评论id
# 参数：fr: =1别人给我的评论列表 =2我给别人的评论列表
def ViewComment(string commentId, string fr)
	string url = "http://test.3g.cn/sns/Interface/CommentMsg.aspx"
	url = url + "?id=" + commentId
	url = url + "&fr=" + fr
	_zsnSend("/widgets/message/viewcomment.xml", "_zsnSend", url)	
end

################### 留言 ######################
# 1.用户的留言列表 TODO
# 参数：用户id
def ViewWordList(string mid)
	
end

# 2.查看留言
# 参数：留言id
# 参数：fr: =1别人给我的留言页面 =2我给别人的留言页面 =3主页留言列表
def ViewWord(string wordId, string fr)
	string url = "http://test.3g.cn/sns/Interface/MsgWordMsg.aspx"
	url = url + "?id=" + wordId
	url = url + "&fr=" + fr
	_zsnSend("/widgets/message/viewword.xml", "_zsnSend", url)
end

