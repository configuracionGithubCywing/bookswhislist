package com.mercadopago.bookswhislist.mapper;

import org.mapstruct.Mapper;

import com.mercadopago.bookswhislist.dto.Authentication;
import com.mercadopago.bookswhislist.dto.CreateWhislist;
import com.mercadopago.bookswhislist.entity.Users;
import com.mercadopago.bookswhislist.entity.Whislist;

@Mapper(componentModel = "spring")
public interface UserMapper {
	Users fromCreateUserToUsers(Authentication createUser);
	
	Whislist fromCreateWhislistToWhislist(CreateWhislist createWhislist);
}
