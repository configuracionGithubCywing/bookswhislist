package com.mercadopago.bookswhislist.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Authentication implements Serializable {

  private static final long serialVersionUID = 1L;
  private String username;
  private String pswd;

}

