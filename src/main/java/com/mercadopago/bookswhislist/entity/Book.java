package com.mercadopago.bookswhislist.entity;

import javax.persistence.*;

import org.hibernate.annotations.TypeDef;

import lombok.*;

@Setter
@Getter
@Entity
@ToString
@Table(name="Books")
@TypeDef(name="pgsql_feel_type", typeClass = Book.class)
public class Book {
	@Id
	@SequenceGenerator(name = "idBook_seq",sequenceName="idBook_id_seq",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="idBook_seq")
	@Basic(optional=false)
	@Column(name="id_Book")
	Long id_Book;
	
	@Basic(optional=false)
	@Column(name="idApiBook")
	String idApiBook;
	
	@Basic(optional=false)
	@Column(name="title")
	String title;
	
	@Basic(optional=false)
	@Column(name="editorial")
	String editorial;
	
	@Basic(optional=false)
	@Column(name="author")
	String author;
	
	@Basic(optional=false)
	@Column(name="publishedDate")
	String publishedDate;
	
}
