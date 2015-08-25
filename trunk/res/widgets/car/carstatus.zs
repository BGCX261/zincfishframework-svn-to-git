def test()
end

def ShowSendCar()
	
end

# 给汽车加油
def AddOil()
    string surplus = _zsdGetConValue("park", "surplus")
    _zssprintln("surplus====" + surplus)
    surplus = "剩余油量: " + surplus + "升"
    _zssprintln("surplus=2===" + surplus)
    # 联网或者.. 获取目前的总油量
    string total = "油站油量:100升"
    _zsdSetAttValue("surplus", "text", surplus)
    _zsdSetAttValue("total", "text", total)
	_zssShowPopWindow("popoil")
end

# 加油成功
def AddOilOK()
	_zsdSetAttValue("result", "text", "加油成功")
	_zssHidePopWindow("popoil")
	_zssShowPopWindow("popresult")
end

# 送车成功
def SendCarOK()
	_zsdSetAttValue("result", "text", "已经将车送出.")
	_zssHidePopWindow("sendingcar")
	_zssShowPopWindow("popresult")
end