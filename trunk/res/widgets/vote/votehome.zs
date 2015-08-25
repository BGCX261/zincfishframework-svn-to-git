def deletVote()
	string listID=_zsdGetCurDomID()
	string curID = _zsdGetCurVItemID(listID)
	_zsdDelDOM(curID)
	_zsdRefresh()
end

# TAB 转换
def SwitchView(string tabid)
	_zssprintln("=======SwitchView============>>>========"+tabid)
	_zsdSetAttValue("votem", "visible", "false")
	_zsdSetAttValue("votej", "visible", "false")
	_zsdSetAttValue("votef", "visible", "false")
	_zsdSetAttValue("voteo", "visible", "false")
	_zsdSetAttValue(tabid, "visible", "true")
	_zsdRefresh(tabid)
end