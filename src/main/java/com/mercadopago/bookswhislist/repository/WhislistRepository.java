package com.mercadopago.bookswhislist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercadopago.bookswhislist.entity.Whislist;

public interface WhislistRepository extends JpaRepository<Whislist, Long> {
	
}
