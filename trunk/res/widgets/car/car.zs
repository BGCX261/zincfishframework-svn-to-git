# TAB 转换
def SwitchView(string tabid)
	_zsdSetComValue("carlist", "visible", "false")
	_zsdSetComValue("recordlist", "visible", "false")
	_zsdSetComValue(tabid, "visible", "true")
	_zsdRefresh(tabid);
end

# 辅助变量   停车的车道  对应汽车item的ID
string itemid
# 停车-弹出框(我的汽车列表)
def ShowMyCars(string domid)
	itemid = domid
	_zssShowPopWindow("popmy")
end

# 贴条-弹出框
def TieTiao(string domid)
    itemid = domid
	string text = _zsdGetAttValue(domid, "describe")
	_zsdSetAttValue("tietiao", "text", text)
	_zssShowPopWindow("poptietiao")
end

# 贴条成功-移去汽车
def TieTiaoConfirm()
	_zsdDelDOM(itemid)
	_zssShowPopWindow("poptip")
end


# 停车
def ParkCar(string imgpath)
    _zssprintln("停车------itemid-" + itemid)
    # 如果该车位有预计收益 计算预计收益
	_zssHidePopWindow("popmy")
	_zsdSetAttValue(itemid, "src", imgpath)
	_zsdGetComValue(itemid, "onclick", "null")
end