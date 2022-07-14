package com.mercadopago.bookswhislist.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.google.api.services.books.model.Volume.VolumeInfo;
import com.mercadopago.bookswhislist.dto.CreateBook;
import com.mercadopago.bookswhislist.dto.CreateBooks;
import com.mercadopago.bookswhislist.dto.Response;
import com.mercadopago.bookswhislist.dto.SearchBook;
import com.mercadopago.bookswhislist.entity.Book;
import com.mercadopago.bookswhislist.entity.Users;
import com.mercadopago.bookswhislist.entity.Whislist;
import com.mercadopago.bookswhislist.mapper.BookMapper;
import com.mercadopago.bookswhislist.repository.BookRepository;
import com.mercadopago.bookswhislist.repository.UserRepository;
import com.mercadopago.bookswhislist.repository.WhislistRepository;
import com.mercadopago.bookswhislist.utils.ApiConstants;
import com.mercadopago.bookswhislist.utils.Validation;

import lombok.extern.slf4j.Slf4j;
import com.google.api.services.books.Books;

@Slf4j
@Service
public class BooksService {
	@Autowired
	private BookMapper bookmapper;
	@Autowired
	private BookRepository bookrepository;
	@Autowired
	private WhislistRepository whislistrepository;
	@Autowired
	private UserRepository userrepository;
	
	public  ArrayList<Object> searchBookUser(SearchBook searchbook) {	
		/*
		 * Busqueda de Libro con Api Google
		 * 
		 * */
		if(Validation.isEmpty(searchbook.getFilter()) || Validation.isEmpty(searchbook.getParameter())) {
			ArrayList<Object> response = new ArrayList<Object>();
			Response re = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			response.add(re);
			return response;
		}
		if(searchbook.getApigoogle()==null || searchbook.getApigoogle().trim().equals("")) {
			ArrayList<Object> response = new ArrayList<Object>();
			Response re = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			response.add(re);
			return response;
		}
		String query="";
		if(searchbook.getFilter().equals("Titulo")) {
			query+="intitle:"+searchbook.getParameter();
		}else if (searchbook.getFilter().equals("Autor")){
			query+="inauthor:"+searchbook.getParameter();
		}else if (searchbook.getFilter().equals("Categoria")){
			query+="subject:"+searchbook.getParameter();
		}
		if(query.trim().equals("")) {
			ArrayList<Object> response = new ArrayList<Object>();
			Response re = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			response.add(re);
			return response;
		}
		ArrayList<Object> book = getBooks(query,searchbook.getApigoogle());
		return book;
	}
	
	public Users findUser(String username){
		/*
		 * Busqueda de Usuario por Nombre
		 * 
		 * */
		Users users = userrepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró al usuario"));
		return users;
	}

	public ResponseEntity<Object> addBook(CreateBook createBook){
		/*
		 * Agregar libro por identificador ISB y Api Google
		 * 
		 * */
		try {
			Long idUser=findUser(SecurityContextHolder.getContext().getAuthentication().getName()).getId_User();
			Users users = userrepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró al usuario"));
			
			if(Validation.isEmpty(createBook.getApigoogle())||Validation.isEmpty(createBook.getIdApiBook())||Validation.isEmpty(createBook.getApigoogle())) {
				Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
				return ResponseEntity.badRequest().body(response);
			}
			
			Whislist whislist = whislistrepository.findById(createBook.getIdWhislist()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró la Lista de Deseos"));
			ArrayList<CreateBooks> books = getBook("ISBN:"+createBook.getIdApiBook(),createBook.getApigoogle());
			for (int i = 0; i < books.size(); i++){
				CreateBooks createabooks = new CreateBooks(books.get(i).getIdApiBook(),books.get(i).getTitle(),books.get(i).getEditorial(),
						books.get(i).getAuthor(),books.get(i).getPublishedDate());
				Book book = bookmapper.fromCreateBookToBook(books.get(i));
				whislist.getBooks().add(book);
			}		
			whislistrepository.save(whislist);
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_200,ApiConstants.RESPONSE_CODE_200_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}catch (Exception e) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}	
	}
	
	public ResponseEntity<Object> deletebook(Long idBook,Long idWhislist){
		/*
		 * Eliminación de libro de lista de deseos
		 * 
		 * */
		try {
			if(Validation.isEmpty(idBook)||Validation.isEmpty(idWhislist)){
				Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
				return ResponseEntity.badRequest().body(response);
			}
			Long idUser = findUser(SecurityContextHolder.getContext().getAuthentication().getName()).getId_User();
			Users users = userrepository.findById(idUser).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró al usuario"));
			Book book = bookrepository.findById(idBook)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró el Libro"));
			Whislist whislist = whislistrepository.findById(idWhislist).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró la Lista de Deseos"));
			whislist.getBooks().remove(book);
			
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_200,ApiConstants.RESPONSE_CODE_200_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			Response response = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	public ArrayList<CreateBooks> getBook(String query,String apikey){
		/*
		 * Consulta de query a ApiGoogle con llave de desarrollo
		 * 
		 * */
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		int i = 0;
		ArrayList<CreateBooks> book = new ArrayList<CreateBooks>();
		try {
			Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
					.setApplicationName(ApiConstants.APPNAME)
					.setGoogleClientRequestInitializer(new BooksRequestInitializer(apikey)).build();
			List volumesList = books.volumes().list(query);
			volumesList.setFilter("ebooks");
			Volumes volumes = volumesList.execute();
			for (Volume volume : volumes.getItems()) {
				VolumeInfo volumeInfomation = volume.getVolumeInfo();
				i++;
				book.add(new CreateBooks(volumeInfomation.getIndustryIdentifiers().get(0).getIdentifier(),volumeInfomation.getTitle(),
						volumeInfomation.getPublisher(), volumeInfomation.getAuthors().toString(),
						volumeInfomation.getPublishedDate()));
			}
		} catch (Exception e) {
			new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró información del libro");
		}
		return book;
	}
	
	public ArrayList<Object> getBooks(String query,String apikey){
		/*
		 * Consulta de query a ApiGoogle con llave de desarrollo
		 * 
		 * */
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		int i = 0;
		ArrayList<Object> book = new ArrayList<Object>();
		try {
			Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
					.setApplicationName(ApiConstants.APPNAME)
					.setGoogleClientRequestInitializer(new BooksRequestInitializer(apikey)).build();
			List volumesList = books.volumes().list(query);
			volumesList.setFilter("ebooks");
			Volumes volumes = volumesList.execute();
			for (Volume volume : volumes.getItems()) {
				VolumeInfo volumeInfomation = volume.getVolumeInfo();
				i++;
				book.add(new CreateBooks(volumeInfomation.getIndustryIdentifiers().get(0).getIdentifier(),volumeInfomation.getTitle(),
						volumeInfomation.getPublisher(), volumeInfomation.getAuthors().toString(),
						volumeInfomation.getPublishedDate()));
			}
		} catch (Exception e) {
			new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró información del libro");
		}
		return book;
	}


	public ArrayList<Object> listBooks(Long idwhislist) {
		/*
		 * Lista de libro por lista de deseos
		 * 
		 * */
		try {
			if(Validation.isEmpty(idwhislist)) {
				ArrayList<Object> response = new ArrayList<Object>();
				Response re = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
				response.add(re);
				return response;
			}
			Long idUser=findUser(SecurityContextHolder.getContext().getAuthentication().getName()).getId_User();
			Users users = userrepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró al usuario"));
			Whislist whislist = whislistrepository.findById(idwhislist).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontró la Lista de Deseos"));
			ArrayList<Object> response = new ArrayList<Object>(whislist.getBooks());
			return response;
		}catch(Exception e) {
			ArrayList<Object> response = new ArrayList<Object>();
			Response re = new Response(new Timestamp(System.currentTimeMillis()),ApiConstants.RESPONSE_CODE_400,ApiConstants.RESPONSE_CODE_400_DESCRIPTION,"");
			response.add(re);
			return response;
		}
		
	}
}
