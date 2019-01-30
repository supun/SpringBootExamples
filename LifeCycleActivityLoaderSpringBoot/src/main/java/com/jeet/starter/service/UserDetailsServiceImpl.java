package com.jeet.starter.service;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jeet.starter.dao.AppRoleDAO;
import com.jeet.starter.dao.AppUserDAO;
import com.jeet.starter.entity.AppUser;
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private AppUserDAO appUserDAO;
	
	@Autowired
	private AppRoleDAO appRoleDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AppUser appUser=this.appUserDAO.findUserAccount(username);
		if(appUser==null) {
			System.out.println("User not  found! "+username);
			throw new UsernameNotFoundException("User "+username+" was not found in the database");
		}
		System.out.println("Found User :"+appUser);
		
		List<String> roleNames=this.appRoleDAO.getRoleNames(appUser.getUserId());
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		
		if(roleNames!=null) {
			for(String role:roleNames){
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}
		UserDetails userDetails=(UserDetails)new User(appUser.getUserName(),appUser.getEncrytedPassword(),grantList);
		return userDetails;
	}
}
