package com.mercadopago.bookswhislist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercadopago.bookswhislist.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
