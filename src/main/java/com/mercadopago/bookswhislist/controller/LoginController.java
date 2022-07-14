package com.mercadopago.bookswhislist.controller;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mercadopago.bookswhislist.dto.Authentication;
import com.mercadopago.bookswhislist.dto.Response;
import com.mercadopago.bookswhislist.dto.TokenInfo;
import com.mercadopago.bookswhislist.entity.Users;
import com.mercadopago.bookswhislist.service.JwtUtilService;
import com.mercadopago.bookswhislist.service.UsersService;
import com.mercadopago.bookswhislist.utils.ApiConstants;
import com.mercadopago.bookswhislist.utils.Validation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ApiConstants.LoginControllerConstants.URL)
@Tag(name = "Acceso", description = "Acceso a Usuarios")
@Slf4j
@ApiResponses(value = {
		@ApiResponse(responseCode = ApiConstants.RESPONSE_CODE_200, description = ApiConstants.RESPONSE_CODE_200_DESCRIPTION, content = {
				@Content(mediaType = ApiConstants.CONTENT_TYPE_JSON_APPLICATION), }),
		@ApiResponse(responseCode = ApiConstants.RESPONSE_CODE_400, description = ApiConstants.RESPONSE_CODE_400_DESCRIPTION, content = @Content),
		@ApiResponse(responseCode = ApiConstants.RESPONSE_CODE_403, description = ApiConstants.RESPONSE_CODE_403_DESCRIPTION, content = @Content),
		@ApiResponse(responseCode = ApiConstants.RESPONSE_CODE_404, description = ApiConstants.RESPONSE_CODE_404_DESCRIPTION, content = @Content),
		@ApiResponse(responseCode = ApiConstants.RESPONSE_CODE_500, description = ApiConstants.RESPONSE_CODE_500_DESCRIPTION, content = {
				@Content(mediaType = ApiConstants.CONTENT_TYPE_JSON_APPLICATION), }) })
public class LoginController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	UserDetailsService usuarioDetailsService;

	@Autowired
	private JwtUtilService jwtUtilService;
	
	@Autowired
	private UsersService usersService;	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping("/")
	String home() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		log.info("-" + auth.getPrincipal());
		log.info("-" + auth.getAuthorities());
		log.info("-" + auth.isAuthenticated());
		log.info("-" + auth.getName());
		log.info("----" + auth.getCredentials());
		return "Hola Mundo";
	}

	@PostMapping(ApiConstants.LoginControllerConstants.SingUp.URL)
	@Operation(summary = ApiConstants.LoginControllerConstants.SingUp.URL_SUMMARY, description = ApiConstants.LoginControllerConstants.SingUp.URL_DESCRIPTION)
	@ApiResponse(content = { @Content(schema = @Schema(implementation = String.class)) })
	public ResponseEntity<Object> authenticate(@RequestBody Authentication authenticationReq) throws Exception {
		/*
		 * Autenticación de Usuario y generación de Tocken de acceso
		 * 
		 * */
		if(Validation.isEmpty(authenticationReq.getPswd())||Validation.isEmpty(authenticationReq.getUsername())) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}
		TokenInfo tokenInfo = null;
		final UserDetails userDetails;
		try {
			authenticationManager.authenticate(	new UsernamePasswordAuthenticationToken(authenticationReq.getUsername(), authenticationReq.getPswd()));
			userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUsername());
		}catch(Exception e) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}
		try {
			final String jwt = jwtUtilService.generateToken(userDetails);	
		    tokenInfo = new TokenInfo(jwt);
		    return ResponseEntity.ok(tokenInfo);
		}catch(Exception e) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}
		
	}
	
	@PostMapping(ApiConstants.LoginControllerConstants.Register.URL)
	@Operation(summary = ApiConstants.LoginControllerConstants.Register.URL_SUMMARY, description = ApiConstants.LoginControllerConstants.Register.URL_DESCRIPTION)
	@ApiResponse(content = { @Content(schema = @Schema(implementation = String.class)) })
	public ResponseEntity<Object> register(@RequestBody Authentication authenticationReq){
		/*
		 * Registro de Usuario y generación de Tocken de acceso
		 * */
		if(Validation.isEmpty(authenticationReq.getPswd())||Validation.isEmpty(authenticationReq.getUsername())) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}
		try {
			if(usersService.findUser(authenticationReq.getUsername()) != null) {
				Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
				return ResponseEntity.badRequest().body(response);	
			}
		}catch(Exception e) {
			log.info("Usuario No encontrado");
		}
		try {
			String psw = authenticationReq.getPswd();
			String passEncrip = passwordEncoder.encode(psw);
			authenticationReq.setPswd(passEncrip);
			usersService.createUser(authenticationReq);
			authenticationReq.setPswd(psw);
			authenticationManager.authenticate(	new UsernamePasswordAuthenticationToken(authenticationReq.getUsername(), authenticationReq.getPswd()));
			final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUsername());
			final String jwt = jwtUtilService.generateToken(userDetails);
			TokenInfo tokenInfo = new TokenInfo(jwt);
			return ResponseEntity.ok(tokenInfo);
		}catch(Exception e) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}
	}
}
