package com.mercadopago.bookswhislist.mapper;

import org.mapstruct.Mapper;

import com.mercadopago.bookswhislist.dto.CreateBooks;
import com.mercadopago.bookswhislist.entity.Book;


@Mapper(componentModel = "spring")
public interface BookMapper {
	Book fromCreateBookToBook(CreateBooks createbook);

}
