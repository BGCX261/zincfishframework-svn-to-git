def updateRecord()
    _zssprintln("###########--updateRecord--##############")
end

# 删除日记
def deleteRecord(string listid)
    _zssprintln("###########--开始删除点滴--##############" + listid);
    _zseRecordMNG(2, listid);
    _zssprintln("###########--结束删除点滴--##############");
end

# 添加日记
def addRecord(string listid)
    _zssprintln("###########--开始添加点滴--##############" + listid);
    _zseRecordMNG(1, listid);
    _zssprintln("###########--结束添加点滴--##############");
end

# 回到点滴评论
def goComments(string listid)
    _zseRecordMNG(3, listid, "/widgets/record/recordnlook.xml");
end

# 添加点滴评论
def addComment(string tfieldid, string listid)
    _zseRecordMNG(4, tfieldid, listid);
end

# TAB 转换
def SwitchView(string tabid)
	_zsdSetAttValue("myrecord", "visible", "false")
	_zsdSetAttValue("frirecord", "visible", "false")
	_zsdSetAttValue("othersrecord", "visible", "false")
	_zsdSetAttValue(tabid, "visible", "true")
	_zsdRefresh(tabid);
end