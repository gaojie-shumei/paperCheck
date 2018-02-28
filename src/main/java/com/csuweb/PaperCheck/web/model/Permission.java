package com.csuweb.PaperCheck.web.model;

import java.io.Serializable;

public class Permission implements Serializable{
    private String id;

    private String permissionevent;

    private String permissionname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPermissionevent() {
        return permissionevent;
    }

    public void setPermissionevent(String permissionevent) {
        this.permissionevent = permissionevent == null ? null : permissionevent.trim();
    }

    public String getPermissionname() {
        return permissionname;
    }

    public void setPermissionname(String permissionname) {
        this.permissionname = permissionname == null ? null : permissionname.trim();
    }
}