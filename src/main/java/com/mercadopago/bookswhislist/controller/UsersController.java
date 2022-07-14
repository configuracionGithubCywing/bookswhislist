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

import com.mercadopago.bookswhislist.service.UsersService;
import com.mercadopago.bookswhislist.utils.ApiConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

import com.mercadopago.bookswhislist.dto.CreateWhislist;
import com.mercadopago.bookswhislist.entity.Users;
import com.mercadopago.bookswhislist.entity.Whislist;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ApiConstants.UsersControllerConstants.URL)
@Tag(name = "Usuarios", description = "API de Usuarios")
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
                        responseCode = ApiConstants.RESPONSE_CODE_403,
                        description = ApiConstants.RESPONSE_CODE_403_DESCRIPTION,
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
public class UsersController {
	@Autowired
	private UsersService usersService;
	
	@PostMapping(ApiConstants.UsersControllerConstants.AddItemWishlist.URL)
    @Operation(
            summary = ApiConstants.UsersControllerConstants.AddItemWishlist.URL_SUMMARY,
            description = ApiConstants.UsersControllerConstants.AddItemWishlist.URL_DESCRIPTION
    )
    @ApiResponse(
            content = {
                    @Content(schema = @Schema(implementation = String.class))}
    )
	public ResponseEntity<Object> addItemWhislist(@RequestBody CreateWhislist wishlist){
		return usersService.addItemWhislist(wishlist);
	}
	
	@DeleteMapping(ApiConstants.UsersControllerConstants.DelItemWishlist.URL)
    @Operation(
            summary = ApiConstants.UsersControllerConstants.AddItemWishlist.URL_SUMMARY,
            description = ApiConstants.UsersControllerConstants.AddItemWishlist.URL_DESCRIPTION
    )
    @ApiResponse(
            content = {
                    @Content(schema = @Schema(implementation = String.class))}
    )
	public ResponseEntity<Object> delItemWhislist(@PathVariable(name = "idwhislist") Long idwhislist){
		return usersService.deleteItemWhislist(idwhislist);
	}
	
	@GetMapping(ApiConstants.UsersControllerConstants.ListWhislist.URL)
    @Operation(
            summary = ApiConstants.UsersControllerConstants.ListWhislist.URL_SUMMARY,
            description = ApiConstants.UsersControllerConstants.ListWhislist.URL_DESCRIPTION
    )
    @ApiResponse(
            content = {
                    @Content(schema = @Schema(implementation = Users.class))}
    )
	public ArrayList<Object> listWhislist(){
		return usersService.listWhislist();
	}
	
	
}
