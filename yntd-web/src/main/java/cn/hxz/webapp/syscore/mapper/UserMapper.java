package cn.hxz.webapp.syscore.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.entity.User;
import net.chenke.playweb.support.mybatis.DynaMapper;

/**
 * 
 * @author chenke
 * 
 */
public interface UserMapper extends DynaMapper<User> {
	
	User loadByUsernameOrEmail(@Param("param") String param);
	
	void connectRole(@Param("userId") Long id, @Param("roleId")Long roleId);

	void disconnectRoles(@Param("userId") Long id);
	
	List<Role> findConnectedRoles(@Param("userId") Long id);

	/**
	 * 查询用户的所有Role.code
	 * @param username
	 * @return
	 */
	List<String> findRoleCodeForUserByUsername(@Param("username")String username);
	
	/**
	 * 查询用户所在组的所有Role.code
	 * @param username
	 * @return
	 */
	List<String> findRoleCodeForGroupByUsername(@Param("username")String username);
	
	/**
	 * 查询用户的所有Permission.code
	 * @param username
	 * @return
	 */
	List<String> findPermissionCodeForUserByUsername(@Param("username")String username);
	
	/**
	 * 查询用户所在组的所有Permission.code
	 * @param username
	 * @return
	 */
	List<String> findPermissionCodeForGroupByUsername(@Param("username")String username);
	
	int queryStatusByUserName(@Param("cardNo")String cardNo, @Param("trashed")boolean trashed);
}