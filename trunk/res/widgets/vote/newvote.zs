def showDescription()
	_zsdSetAttValue("editor","visible","true")
	_zsdDelDOM("description")
	_zsdRefresh()
end

int i=4
def addCandidate()
	i = i+1
	string t = _zssGetSystemTime()
	string divID = "div-"+t
	_zsdAddDOM("DivDOM",divID,"form",i)
	_zsdSetAttValue(divID,"class","horizontal")
	
	string labelID="l-"+t
	_zsdAddDOM("LabelDOM",labelID,divID,0)
	_zsdSetAttValue(labelID,"text","候选项：")
	
	string tfID="b-"+t
	_zsdAddDOM("TextFieldDOM",tfID,divID,1)
	_zsdSetAttValue(tfID,"bg","1")
	_zsdSetAttValue(tfID,"w","130")
	
	_zsdRefresh()
end

def submit()
end