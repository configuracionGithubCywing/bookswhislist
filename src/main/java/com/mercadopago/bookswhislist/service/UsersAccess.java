package com.mercadopago.bookswhislist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mercadopago.bookswhislist.entity.Users;

@Service
public class UsersAccess implements UserDetailsService {
	@Autowired
	private UsersService usersService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * Validación de acceso de usuario psw y username
		 * 
		 * */
		try {
			Users usuarios = usersService.findUser(username);
			var rol = usuarios.getUsername();
			if (rol != null) {
				User.UserBuilder userBuilder = User.withUsername(usuarios.getUsername());
				userBuilder.password(usuarios.getPswd()).roles("ADMIN");
				return userBuilder.build();
			} else {
				System.err.println("No est");
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException(username);
		}
		throw new UsernameNotFoundException(username);
	}
}