package com.mercadopago.bookswhislist.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;
import com.mercadopago.bookswhislist.dto.Authentication;
import com.mercadopago.bookswhislist.dto.CreateWhislist;
import com.mercadopago.bookswhislist.dto.Response;
import com.mercadopago.bookswhislist.entity.Users;
import com.mercadopago.bookswhislist.entity.Whislist;
import com.mercadopago.bookswhislist.mapper.UserMapper;
import com.mercadopago.bookswhislist.repository.UserRepository;
import com.mercadopago.bookswhislist.repository.WhislistRepository;
import com.mercadopago.bookswhislist.service.UsersService;
import com.mercadopago.bookswhislist.utils.ApiConstants;
import com.mercadopago.bookswhislist.utils.Validation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsersService  {
	@Autowired
	private UserMapper usermapper;
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private WhislistRepository whislistrepository;

	
	public Optional<Users> createUser(Authentication user){
		Users users = usermapper.fromCreateUserToUsers(user);
		users.setWishlist(new ArrayList<>());
		userrepository.save(users);
		return Optional.of(users);
	}
	
	public Users findUser(String username){
		Users users = userrepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró al usuario"));
		return users;
	}
	
	public ResponseEntity<Object> addItemWhislist(CreateWhislist createWhislist){
		/*
		 * Se agrega Nueva Lista de Deseos al Usuario
		 * 
		 * */
		try {
			if(Validation.isEmpty(createWhislist.getName())||Validation.isEmpty(createWhislist.getCategory())) {
				Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
				return ResponseEntity.badRequest().body(response);
			}
			Long idUser=findUser(SecurityContextHolder.getContext().getAuthentication().getName()).getId_User();
			Whislist whislist = usermapper.fromCreateWhislistToWhislist(createWhislist);
			Users users = userrepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró al usuario"));
			users.getWishlist().add(whislist);
			userrepository.save(users);
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_200,ApiConstants.RESPONSE_CODE_200_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}catch (Exception e) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}
	}
	public ResponseEntity<Object> deleteItemWhislist(Long idWhislist){
		/*
		 * Eliminación de Lista de Deseos por identificador de Lista
		 * 
		 * */
		try {
			if(Validation.isEmpty(idWhislist)) {
				Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
				return ResponseEntity.badRequest().body(response);
			}
			Long idUser=findUser(SecurityContextHolder.getContext().getAuthentication().getName()).getId_User();
			Users users = userrepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró al usuario"));
			Whislist whislist = whislistrepository.findById(idWhislist).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró la Lista de Deseos"));
			users.getWishlist().remove(whislist);
			whislistrepository.delete(whislist);		
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_200,ApiConstants.RESPONSE_CODE_200_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}catch (Exception e) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}
	}

	public ArrayList<Object>  listWhislist() {
		/*
		 * Generación de Lista de Lista de Deseos
		 * 
		 * */
		try {
			Long idUser=findUser(SecurityContextHolder.getContext().getAuthentication().getName()).getId_User();
			Users users = userrepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró al usuario"));
			ArrayList<Object> response = new ArrayList<Object>(users.getWishlist());
			return response;
		}catch(Exception e) {
			ArrayList<Object> response = new ArrayList<Object>();
			Response re = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			response.add(re);
			return response;
		}
	}
	
}
