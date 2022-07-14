package com.mercadopago.bookswhislist.utils;

public class Validation {
	/*
	 * Clase de validación de Nulos
	 * 
	 * */
	public static boolean isEmpty(String value) {
		if (value.trim().equals("") || value.equals(null)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEmpty(Long value) {
		if(value<=0) {
			return true;
		}else {
			return false;
		}
	}
}
