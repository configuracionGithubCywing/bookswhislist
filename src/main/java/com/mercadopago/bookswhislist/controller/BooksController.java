package com.mercadopago.bookswhislist.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.bookswhislist.dto.CreateBook;
import com.mercadopago.bookswhislist.dto.CreateBooks;
import com.mercadopago.bookswhislist.entity.Book;
import com.mercadopago.bookswhislist.entity.Users;
import com.mercadopago.bookswhislist.entity.Whislist;
import com.mercadopago.bookswhislist.service.BooksService;
import com.mercadopago.bookswhislist.utils.ApiConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ApiConstants.BooksControllerConstants.URL)
@Tag(name = "Libros", description = "API de Libros")
@Slf4j
@ApiResponses(
        value = {
                @ApiResponse(
                        responseCode = ApiConstants.RESPONSE_CODE_200,
                        description = ApiConstants.RESPONSE_CODE_200_DESCRIPTION,
                        content = {
                                @Content(mediaType = ApiConstants.CONTENT_TYPE_JSON_APPLICATION),}
                ),
                @ApiResponse(
                        responseCode = ApiConstants.RESPONSE_CODE_400,
                        description = ApiConstants.RESPONSE_CODE_400_DESCRIPTION,
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = ApiConstants.RESPONSE_CODE_404,
                        description = ApiConstants.RESPONSE_CODE_404_DESCRIPTION,
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = ApiConstants.RESPONSE_CODE_500,
                        description = ApiConstants.RESPONSE_CODE_500_DESCRIPTION,
                        content = {
                                @Content(mediaType = ApiConstants.CONTENT_TYPE_JSON_APPLICATION),}
                )}
)
public class BooksController {
	@Autowired
	private BooksService booksservice;
	
	@PostMapping(ApiConstants.BooksControllerConstants.SearchBook.URL)
    @Operation(
            summary = ApiConstants.BooksControllerConstants.SearchBook.URL_SUMMARY,
            description = ApiConstants.BooksControllerConstants.SearchBook.URL_DESCRIPTION
    )
    @ApiResponse(
            content = {
                    @Content(schema = @Schema(implementation = Users.class))}
    )
	public ArrayList<Object> searchBook(@RequestBody com.mercadopago.bookswhislist.dto.SearchBook searchbook){
		return booksservice.searchBookUser(searchbook);
	}
	@PostMapping(ApiConstants.BooksControllerConstants.AddBook.URL)
    @Operation(
            summary = ApiConstants.BooksControllerConstants.AddBook.URL_SUMMARY,
            description = ApiConstants.BooksControllerConstants.AddBook.URL_DESCRIPTION
    )
    @ApiResponse(
            content = {
                    @Content(schema = @Schema(implementation = String.class))}
    )
	public ResponseEntity<Object> addItemWhislist(@RequestBody CreateBook createBook){
		return booksservice.addBook(createBook);
	}
	@DeleteMapping(ApiConstants.BooksControllerConstants.DeleteBook.URL)
    @Operation(
            summary = ApiConstants.BooksControllerConstants.DeleteBook.URL_SUMMARY,
            description = ApiConstants.BooksControllerConstants.DeleteBook.URL_DESCRIPTION
    )
    @ApiResponse(
            content = {
                    @Content(schema = @Schema(implementation = String.class))}
    )
	public ResponseEntity<Object> delItemWhislist(@PathVariable(name = "idbook") Long idbook,@PathVariable(name = "idwhislist") Long idwhislist){
		return booksservice.deletebook(idbook,idwhislist);
	}
	
	@GetMapping(ApiConstants.BooksControllerConstants.ListBooks.URL)
    @Operation(
            summary = ApiConstants.BooksControllerConstants.ListBooks.URL_SUMMARY,
            description = ApiConstants.BooksControllerConstants.ListBooks.URL_DESCRIPTION
    )
    @ApiResponse(
            content = {
                    @Content(schema = @Schema(implementation = Users.class))}
    )
	public ArrayList<Object> listWhislist(@PathVariable(name = "idwhislist") Long idwhislist){
		return booksservice.listBooks(idwhislist);
	}
}
