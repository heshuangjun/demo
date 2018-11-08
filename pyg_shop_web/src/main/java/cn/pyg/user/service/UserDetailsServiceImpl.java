package cn.pyg.user.service;

import cn.pyg.pojo.TbSeller;
import cn.pyg.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务类
 */
public class UserDetailsServiceImpl implements UserDetailsService {


    //添加属性和增加set方法
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    //修改loadUserByUsername方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("经过了UserDetailsServiceImpl!");

        TbSeller seller = sellerService.findOne(username);

        if (seller != null) {
            if ("1".equals(seller.getStatus())) {
                //用户具有的权限和角色的集合
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
                return new User(username, seller.getPassword(), authorities);
            } else {
                return null;
            }
        } else {
            return null;
        }


    }
}
