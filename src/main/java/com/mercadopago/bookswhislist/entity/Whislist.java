package com.mercadopago.bookswhislist.entity;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.TypeDef;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="Wishlist")
@TypeDef(name="pgsql_feel_type", typeClass = Whislist.class)
public class Whislist {
	
	@Id
	@SequenceGenerator(name = "idWishlist_seq",sequenceName="idWishlist_id_seq",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="idWishlist_seq")
	@Basic(optional=false)
	@Column(name="id_Wishlist")
	Long id_Wishlist;
	
	@Basic(optional=false)
	@Column(name="name")
	String name;

	@Basic(optional=false)
	@Column(name="category")
	String category;	
	
	@OneToMany(cascade=CascadeType.ALL)
	List<Book> books;
}
