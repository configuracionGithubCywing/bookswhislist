package com.mercadopago.bookswhislist.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TokenInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String Token;

}
