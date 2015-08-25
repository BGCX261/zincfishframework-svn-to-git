

def Register()
	_zsnRegister()
end

def Login()
	
	_zsnSend("/widgets/main/mainpage.xml")
	#string phoneNum =_zsdGetAttValue("textphonenum","value")
	#string password = _zsdGetAttValue("textpwd","value")
	#_zsnLogin(phoneNum,password,"handlLogin")
end

def handlLogin()
	int loginStatus=_zsnGetLoginSuccessStatus()
	_zssprintln("loginStatus==="+loginStatus)
	string loginMsg = _zsnLoginFailureInfo()
	if loginStatus==1
		_zsnSend("/widgets/main/mainpage.xml")
	else 
		_zsnPopDlg(loginMsg)
	end
end

def initlogindata()
	#string strphonenum
	#strphonenum=_zsnGetPhoneNum()
	#_zsdSetAttValue("textphonenum", "value", strphonenum)
	
	#string strpwd
	#strpwd=_zsnGetPwd()
	#_zsdSetAttValue("textpwd", "value", strpwd)
	
	#string strloginself
	#strloginself=_zsnGetloginStatus()
	#_zsdSetAttValue("loginself", "select", strloginself)
	
	#string strpwdrember
	#strpwdrember=_zsnPwdRemberStatus()
	#_zsdSetAttValue("pwdrember", "select", strpwdrember)
	
	_zsnUpdateFriendsList()
end

def initchangepwdPage()
	string 	strphonenum=_zsnGetPhoneNum()	
	#_zsdSetAttValue("originalphoneNum", "text", strphonenum)
	
	string 	strpwd=_zsnGetPwd()
	#_zsdSetAttValue("originalpswd", "text", strpwd)
	
	_zsdRefresh()
end

def changepwdNextstep()
	_zsnChangePwd("Pwd1", "Pwd2")
	_zsnShowcontent("logreg\\regeditPage.xml")
end

def savePassword()
	string password1 =_zsdGetAttValue("newpswd","value")
	string password2 = _zsdGetAttValue("newpswdconfirm","value")
	
	if password1=="" || password2==""
		_zsnPopDlg("密码输入为空!")
	elsif password1!=password2
		_zsnPopDlg("两次密码输入不一致!")
	else
		_zsnChangePwd(password1)
	end
	
end

def skip()
	_zssShowPopWindow("msg")
end

def ok()
	_zssHidePopWindow("msg")
	string phoneNum =_zsnGetPhoneNum()
	string password = _zsnGetPwd()
	_zsnLogin(phoneNum,password,"handlLogin")
end

def cancel()
	_zssHidePopWindow("msg")
end
