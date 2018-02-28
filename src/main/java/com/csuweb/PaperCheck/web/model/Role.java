package com.csuweb.PaperCheck.web.model;

import java.io.Serializable;

public class Role implements Serializable{
    private String id;

    private String rolename;

    private String permissionids;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename == null ? null : rolename.trim();
    }

    public String getPermissionids() {
        return permissionids;
    }

    public void setPermissionids(String permissionids) {
        this.permissionids = permissionids == null ? null : permissionids.trim();
    }
}