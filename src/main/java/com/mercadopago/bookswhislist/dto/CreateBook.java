package com.mercadopago.bookswhislist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateBook {
	String apigoogle;
	long idWhislist;
	String idApiBook;
}
