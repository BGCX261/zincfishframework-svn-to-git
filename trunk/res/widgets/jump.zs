
################## ��ҳ ####################

# 1.�ҵ���ҳ
# ��������
def GoMyHome()
	string url = "http://test.3g.cn/sns/interface/mymsg.aspx"
	_zsnSend("/widgets/main/mainpage.xml","_zsnSend",url)
end

# 2.���˵���ҳ
# ���������˵�id
def GoHome(string mid)
	string url = "http://test.3g.cn/sns/interface/dfmsg.aspx?mids=" + mid
	_zsnSend("/widgets/main/mainpage_other.xml","_zsnSend",url)
end


################## ��� ####################

# 1.�ҵ������ҳ(ר���б�)
# ��������
def GoMyAlbumList()
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx"
	_zsnSend("/widgets/album/album.xml","_zsnSend",url)
end

# 2.���˵������ҳ(ר���б�)
# ���������˵�mid
def GoAlbumList(string mid)
	string url = "http://test.3g.cn/sns/interface/Albumspeclist.aspx?mids=" + mid
	_zsnSend("/widgets/album/album_other.xml","_zsnSend",url)
end

# 3.�ҵ�ר��ҳ��(��Ƭ�б�)
# ������ר��ID
def GoMyPhotoList(string AlbumID)
	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx?specid=" + AlbumID
	_zsnSend("/widgets/album/albumpage.xml","_zsnSend",url)
end

# 4.���˵�ר��ҳ��(��Ƭ�б�)
# ���������˵�mid��ר��ID
def GoPhotoList(string Mid, string AlbumID)
	string url = "http://test.3g.cn/sns/interface/AlbumPicList.aspx?specid=" + AlbumID + "&mids=" + Mid
	_zsnSend("/widgets/album/albumpage_other.xml","_zsnSend",url)
end

# 5.�ҵ�ĳ����Ƭ
# ��������ƬID
def GoMyPhoto(string PicID)
	string url = "http://test.3g.cn/sns/interface/Albumpicmsg2.aspx?picid=" + PicID
	_zsnSend("/widgets/album/photopage.xml","_zsnSend",url)
end

# 6.���˵�ĳ����Ƭ
# ���������˵�mid����ƬID
def GoPhoto(string Mid, string PicID)
	string url = "http://test.3g.cn/sns/interface/Albumpicmsg2.aspx?picid=" + PicID + "&mids=" + Mid
	_zsnSend("/widgets/album/photopage_other.xml","_zsnSend",url)
end

# 7.�༭�ҵ���Ƭ������
# ��������ƬID
def GoEditMyPhoto(string PicID)
	string url = "http://test.3g.cn/sns/interface/AlbumPicEdit.aspx?picid=" + PicID
	_zsnSend("/widgets/album/EditPhoto.xml","_zsnSend",url)
end

# 8.�鿴���˵���Ƭ������
# ��������ƬID
def GoEditPhoto(string PicID)
	string url = "http://test.3g.cn/sns/interface/AlbumPicEdit.aspx?picid=" + PicID
	_zsnSend("/widgets/album/EditPhoto_other.xml","_zsnSend",url)
end

# 9.�ϴ���Ƭҳ��
# ������Ĭ��ר��ID
def UpLoadPic(string AlbumId)
	string url = "http://test.3g.cn/sns/interface/AlbumSpecList2.aspx?specid=" + AlbumId
	_zsnSend("/widgets/album/upload.xml","_zsnSend",url)
end

################## ͶƱ ####################

# 1.�ҵ�ͶƱ�б�
# ��������
def GoMyVoten()
	string url = "http://test.3g.cn/sns/interface/votelist.aspx?lt=all"
	_zsnSend("/widgets/vote/voten.xml","_zsnSend",url)
end

# 2.���˵�ͶƱ�б�
# ���������˵�mid
def GoVoten(string Mid)
	string url = "http://test.3g.cn/sns/interface/votelist.aspx?lt=df&mids=" + Mid
	_zsnSend("/widgets/vote/voten_other.xml","_zsnSend",url)
end

# 3.�ҵ�ĳ��ͶƱ
# ������ͶƱID
def GoMyVoting(string VoteId)
	string url = "http://test.3g.cn/sns/interface/voteMsg.aspx?id=" + VoteId
	_zsnSend("/widgets/vote/voting.xml","_zsnSend",url)
end

# 4.���˵�ĳ��ͶƱ
# ������ͶƱID
def GoVoting(string VoteId)
	string url = "http://test.3g.cn/sns/interface/voteMsg.aspx?id=" + VoteId
	_zsnSend("/widgets/vote/voting_other.xml","_zsnSend",url)
end

# 5.����ͶƱ
# ��������
def StartVote()
	_zsnSend("/widgets/vote/newvote.xml")
end

################### �ռ� ######################
# 1.�ҵ��ռ��б�
# ��������
def GoMyDiaryList()
	string url="http://test.3g.cn/sns/interface/diaryList.aspx?lt=1"
	_zsnSend("/widgets/diary/diary_list.xml","_zsnSend",url)
end

# 2.���˵��ռ��б�
# ���������˵�mid
def ViewDiaryList(string mid)
	string url = "http://test.3g.cn/sns/interface/diaryList2.aspx"
	url = url + "?mids=" + mid
	_zsnSend("/widgets/diary/diary_list_others.xml","_zsnSend",url)
end

# 3.�鿴�ҵ��ռ�(ҳ���б༭�ϴ��ȹ���)
# �������ռ�id
def ViewMyDiary(string DiaryId)
	string url = "http://test.3g.cn/sns/interface/diarymsg.aspx"
	url = url + "?id=" + diaryId
	_zsnSend("/widgets/diary/diary_page_mine.xml", "_zsnSend", url)
end

# 4.�鿴�ռ�
# �������ռ�id
def ViewDiary(string DiaryId)
	string url = "http://test.3g.cn/sns/interface/diarymsg.aspx"
	url = url + "?id=" + diaryId
	_zsnSend("/widgets/diary/diary_page_normal.xml", "_zsnSend", url)
end

# 5.д�ռ�
# ��������
def GoWriteDiary()
	_zsnSend("/widgets/diary/write_diary.xml")
end

################### ���� ######################
# 1.���غ��ѹ������
# ��������
def GoLocalFrndList()
	_zsnSend("/widgets/friends/friend.xml")
end

# 2.�鿴ָ���û��ĺ���	TODO
# �������û�id
def ViewFrndList(string mid)
	_zsnSend("/widgets/friends/friend_list_others.xml")
end

# 3.�����������
# ��������
def GoFrndSearch()
	_zsnSend("/widgets/friends/searchFriend.xml")
end

# 4.�鿴���к��ѵĺ���
# ��������
def GoFrndFrnd()
	string url="http://test.3g.cn/sns/interface/FriFriList.aspx?time="
	string time=_zsnGetFriendTime()	
	url=url+time
	_zsnSend("/widgets/friends/friend_friend.xml", "_zsnSend", url)
end

################### ��Ϣ ######################
# 1.������Ϣ��ҳ
# ��������
def GoMsgMainPage()
	_zsnSend("/widgets/message/msgmain.xml")
end

# 2.�������Ϣ�б�
# ��������
def GoShortMsgList()
	string url = "http://test.3g.cn/sns/interface/msgrecsend.aspx"
	_zsnSend("/widgets/message/rec_msg_list.xml", "_zsnSend", url)
end

# 3.����ϵͳ��Ϣ�б�
# ��������
def GoSysMsgList()
	string url = "http://test.3g.cn/sns/interface/MsgSysListAll.aspx"
	_zsnSend("/widgets/message/sys_msg_list.xml", "_zsnSend", url)
end

# 4.����������Ϣ�б�
# ��������
def GoWordMsgList()
	string url = "http://test.3g.cn/sns/interface/MsgWordRecSend.aspx"
	_zsnSend("/widgets/message/word_msg_list.xml", "_zsnSend", url)
end

# 5.����������Ϣ�б�
# ��������
def GoCommentMsgList()
	string url = "http://test.3g.cn/sns/interface/CommentRecSend.aspx"
	_zsnSend("/widgets/message/comment_msg_list.xml", "_zsnSend", url)
end
################### ���� ######################
# 1.�鿴ĳ������������б� TODO
# ���������id
def ViewCommentList(string id)
	
end

# 2.�鿴����
# ������commentId:����id
# ������fr: =1���˸��ҵ������б� =2�Ҹ����˵������б�
def ViewComment(string commentId, string fr)
	string url = "http://test.3g.cn/sns/Interface/CommentMsg.aspx"
	url = url + "?id=" + commentId
	url = url + "&fr=" + fr
	_zsnSend("/widgets/message/viewcomment.xml", "_zsnSend", url)	
end

################### ���� ######################
# 1.�û��������б� TODO
# �������û�id
def ViewWordList(string mid)
	
end

# 2.�鿴����
# ����������id
# ������fr: =1���˸��ҵ�����ҳ�� =2�Ҹ����˵�����ҳ�� =3��ҳ�����б�
def ViewWord(string wordId, string fr)
	string url = "http://test.3g.cn/sns/Interface/MsgWordMsg.aspx"
	url = url + "?id=" + wordId
	url = url + "&fr=" + fr
	_zsnSend("/widgets/message/viewword.xml", "_zsnSend", url)
end

