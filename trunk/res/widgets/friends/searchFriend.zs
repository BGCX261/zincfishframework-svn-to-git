#搜索同学
def SearchClassmate()
	string college = _zsdGetAttValue("college", "value")
	string class = _zsdGetAttValue("class", "value")	
	string yearen = _zsdGetAttValue("yearen", "value")
	string url = "http://test.3g.cn/sns/interface/Frisearchresult.aspx"
	url = url + "?St=0"
	url = url + "&Words1="
	url = url + college
	url = url + "&Words2="
	url = url + class
	url = url + "&Words3="
	url = url + yearen
	_zsdSaveTempString("frsearch", url)
	_zsnSend( "/widgets/friends/searchResult.xml", "_zsnSend", url )
end

#搜索同事
def SearchWorkmate()
	string college = _zsdGetAttValue("college", "value")
	string company = _zsdGetAttValue("company", "value")	
	string url = "http://test.3g.cn/sns/interface/Frisearchresult.aspx"
	url = url + "?St=1"
	url = url + "&Words1="
	url = url + company
	_zsdSaveTempString("frsearch", url)
	_zsnSend( "/widgets/friends/searchResult.xml", "_zsnSend", url )
end

#名称信息搜索
def SearchName()
	string friname = _zsdGetAttValue("friname", "value")
	string cityin = _zsdGetAttValue("cityin", "value")	
	string sex = _zsdGetAttValue("sex", "value")
	string url = "http://test.3g.cn/sns/interface/Frisearchresult.aspx"
	url = url + "?St=2" + "&Words1="
	url = url + friname
	url = url + "&Words2="
	url = url + cityin
	url = url + "&Words3=" + sex
	_zsdSaveTempString("frsearch", url)
	_zsnSend( "/widgets/friends/searchResult.xml", "_zsnSend", url )
end

#精确查找
def SearchExact()
	string phonenum = _zsdGetAttValue("phonenum", "value")
	string url = "http://test.3g.cn/sns/interface/Frisearchresult.aspx"
	url = url + "?St=3"
	url = url + "&Words2="
	url = url + phonenum
	_zsdSaveTempString("frsearch", url)
	_zsnSend( "/widgets/friends/searchResult.xml", "_zsnSend", url )
end

#切换
def changeSearchvisable(string id_show, string id_hidden1, string id_hidden2, string id_hidden3)
	_zsdSetAttValue(id_show, "visible", "true")
	_zsdSetAttValue(id_hidden1, "visible", "false")
	_zsdSetAttValue(id_hidden2, "visible", "false")
	_zsdSetAttValue(id_hidden3, "visible", "false")
	_zsdRefresh()
	_zsdSaveTempString("frsearch", url)
end