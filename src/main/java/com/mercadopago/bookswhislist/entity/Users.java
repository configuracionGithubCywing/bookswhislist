package com.mercadopago.bookswhislist.entity;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.TypeDef;

import lombok.*;

@Setter
@Getter
@Entity
@ToString
@Table(name="Users")
@TypeDef(name="pgsql_feel_type", typeClass = Users.class)
public class Users {
	@Id
	@SequenceGenerator(name = "idUser_seq",sequenceName="idUser_id_seq",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="idUser_seq")
	@Basic(optional=false)
	@Column(name="id_User")
	Long id_User;
	
	@Basic(optional=false)
	@Column(name="username")
	String username;
	
	@Basic(optional=false)
	@Column(name="pswd")
	String pswd;
	
	@OneToMany(cascade=CascadeType.ALL)
	List<Whislist> wishlist;
}
