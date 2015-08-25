def submitVote()
end

# TAB 转换
def SwitchView(string tabid)
	_zsdSetAttValue("voting", "visible", "false")
	_zsdSetAttValue("comments", "visible", "false")
	_zsdSetAttValue(tabid, "visible", "true")
	_zsdRefresh(tabid);
end