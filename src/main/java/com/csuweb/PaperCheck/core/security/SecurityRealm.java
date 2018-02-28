package com.csuweb.PaperCheck.core.security;

import javax.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.csuweb.PaperCheck.web.biz.PermissionBiz;
import com.csuweb.PaperCheck.web.biz.RoleBiz;
import com.csuweb.PaperCheck.web.biz.UserBiz;
import com.csuweb.PaperCheck.web.model.Permission;
import com.csuweb.PaperCheck.web.model.Role;
import com.csuweb.PaperCheck.web.model.User;


@Component(value = "securityRealm")
public class SecurityRealm extends AuthorizingRealm {

    @Resource
    private UserBiz userbiz;
    @Resource
    private RoleBiz rolebiz;
    @Resource
    private PermissionBiz permissionbiz;

    /**
     * 用于授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {    
    	SimpleAuthorizationInfo authorizationInfo = null;
    	User user = (User) principals.getPrimaryPrincipal();//获取登录的用户对象
        String roleid = user.getRoleid();
        if(roleid!=null&&roleid!=""){
        	authorizationInfo = new SimpleAuthorizationInfo();
    		//通过数据库获取用户所对应的角色
			Role role = rolebiz.selRoleByPrimaryKey(roleid);
			if(role!=null){
				authorizationInfo.addRole(role.getRolename());
				String permissionids = role.getPermissionids();
				if(permissionids!=null&&!permissionids.equals("")){
					String[] permissionidarr = permissionids.split(",");
					for (String s : permissionidarr) {
						Permission permission = permissionbiz.selPermissionByPrimaryKey(s);//获取对应权限
						if(permission!=null){
							authorizationInfo.addStringPermission(permission.getPermissionevent());
						}
					}
				}
			}
        }
        return authorizationInfo;
    }

    /**
     * 用于登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = String.valueOf(token.getPrincipal());
        String password = new String((char[]) token.getCredentials());
        //通过数据库进行验证
        final User authentication = userbiz.authentication(new User(username, password));
        if (authentication == null) {
            throw new AuthenticationException("该用户不存在，请注册后再登录！");
        }else if(!userbiz.makeMD5(password).equals(authentication.getPwd())){
        	throw new AuthenticationException("密码输入错误，请重新输入！");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(authentication, password, getName());
        return authenticationInfo;
    }

}
