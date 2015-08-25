#扩展标签
def expendTag(string domid)
	string dom_visible = _zsdGetAttValue(domid, "visible")
	if dom_visible == "true"
		_zsdSetAttValue(domid, "visible", "false")
	elsif dom_visible == "false"
		_zsdSetAttValue(domid, "visible", "true")
	end
	_zsdRefresh()
end