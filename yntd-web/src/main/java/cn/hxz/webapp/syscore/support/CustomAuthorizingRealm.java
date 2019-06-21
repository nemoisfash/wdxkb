package cn.hxz.webapp.syscore.support;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.service.UserSerivce;
import net.chenke.playweb.support.spring.MessageSourceBean;

/**
 * 
 * @author chenke
 * 
 */
public class CustomAuthorizingRealm extends AuthorizingRealm {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserSerivce bizUser;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取当前登录的用户名
        String username = (String) super.getAvailablePrincipal(principals);
        
        if (logger.isDebugEnabled())
        	logger.info("当前登录用户名：" + username + " 加载权限数据");
        
        if(username != null){
        	List<String> roles = AuthorizingUtils.findRolesCode(username);
        	List<String> permissions = AuthorizingUtils.findPermissionsCode(username);
            authorizationInfo.addRoles(roles);
            authorizationInfo.addStringPermissions(permissions);
        }else{
            throw new AuthorizationException();
        }
        
        return authorizationInfo;
	}

	/**
     *  认证回调函数,登录时调用.
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
		
        if (logger.isDebugEnabled())
        	logger.info("请求登录用户名：" + authcToken.getUsername() + " 校验账号信息");
        
        User account = bizUser.load(authcToken.getUsername());
        if (account==null)
        	throw new AuthenticationException(MessageSourceBean.getMessage("err.login", "用户名或密码错误"));

        if (!account.getPassword().equals(new String(authcToken.getPassword())))
        	throw new AuthenticationException(MessageSourceBean.getMessage("err.login", "用户名或密码错误"));
        
        if (account.getEnabled()!=true)
        	throw new AuthenticationException("账号已停用");

		User login = new User();
		login.setId(account.getId());
		login.setLoginIp(authcToken.getHost());
		login.setLoginHits(account.getLoginHits()==null?1:account.getLoginHits()+1);
		login.setLoginTime(new Date());
		bizUser.update(login);
		
		return new SimpleAuthenticationInfo(account.getUsername(), account.getPassword(), account.getName());
	}
}
