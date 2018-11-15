package cn.pyg.user.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heshuangjun
 * @date 2018-11-15 22:11
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //cas与springsecurity整合后，认证服务类主要做授权操作，认证操作有cas完成
        List<GrantedAuthority> autherios = new ArrayList<>();
        autherios.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username, "", autherios);
    }
}
