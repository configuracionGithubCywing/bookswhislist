package com.mercadopago.bookswhislist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchBook {
	private String apigoogle;
	private String filter;
	private String parameter;
}
