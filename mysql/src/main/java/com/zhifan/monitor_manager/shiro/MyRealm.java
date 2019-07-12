package com.zhifan.monitor_manager.shiro;

import com.zhifan.monitor_manager.domain.entity.TMSystemAccountInfo;
import com.zhifan.monitor_manager.domain.repository.TMSSystemAccountInfoRepository;
import com.zhifan.monitor_manager.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;


public class MyRealm extends AuthorizingRealm {

    @Autowired
    TMSSystemAccountInfoRepository tmsSystemAccountInfoRepository;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        AccountInfoEntity accountInfo = (AccountInfoEntity)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        List<AccountRoleEntity> accountRoleEntityList = accountRoleRepository.findByUserId(accountInfo.getUserId());
//        List<String> roleStr = new ArrayList<>();
//        if (CollectionUtils.isEmpty(accountRoleEntityList)){
//            return simpleAuthorizationInfo;
//        }else {
//            List<Integer> roleList = new ArrayList<>();
//
//            for (AccountRoleEntity accountRoleEntity : accountRoleEntityList){
//                roleList.add(accountRoleEntity.getRoleId());
//            }
//            List<RoleEntity> roleEntityList = roleRepository.findByRoleIdIn(roleList);
//            if (!CollectionUtils.isEmpty(roleEntityList)){
//                for (int i = 0 ;i < roleEntityList.size(); i++){
//                    roleStr.add(roleEntityList.get(i).getRoleName());
//                }
//            }
//        }
//        simpleAuthorizationInfo.addRoles(roleStr);

        return  simpleAuthorizationInfo;

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比

        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token无效");
        }

        TMSystemAccountInfo tmSystemAccountInfo = tmsSystemAccountInfoRepository.findByDisplayName(username);
        if (ObjectUtils.isEmpty(tmSystemAccountInfo)) {
            throw new AuthenticationException("用户不存在!");
        }

        if (!JwtUtil.verify(token, username, tmSystemAccountInfo.getSecurityPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }

        if (ObjectUtils.isEmpty(tmSystemAccountInfo.getIsLogin())){
            throw new AuthenticationException("获取用户会话失败");
        }

        if (tmSystemAccountInfo.getIsLogin() != TMSystemAccountInfo.LOGIN_ONE){
            throw new AuthenticationException("获取用户会话失败");
        }

        return new SimpleAuthenticationInfo(tmSystemAccountInfo, token, "my_realm");

    }
}
