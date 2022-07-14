package com.mercadopago.bookswhislist.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class Response {
	Timestamp timestamp;
	String status;
	String error;
	String path;
	
}
