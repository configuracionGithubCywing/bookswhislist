package com.mercadopago.bookswhislist.utils;

public class ApiConstants {
    public static final String CONTENT_TYPE_JSON_APPLICATION = "application/json";
    public static final String RESPONSE_CODE_200 = "200";
    public static final String RESPONSE_CODE_200_DESCRIPTION = "Solicitud Procesada Correctamente.";
    public static final String RESPONSE_CODE_400 = "400";
    public static final String RESPONSE_CODE_400_DESCRIPTION = "Formato de solicitud invalida.";
    public static final String RESPONSE_CODE_403 = "403";
    public static final String RESPONSE_CODE_403_DESCRIPTION = "Solicitud no procesada.";
    public static final String RESPONSE_CODE_404 = "404";
    public static final String RESPONSE_CODE_404_DESCRIPTION = "Recurso no encontrado.";
    public static final String RESPONSE_CODE_500 = "500";
    public static final String RESPONSE_CODE_500_DESCRIPTION = "El proceso generó una respuesta no controlada.";
    public static final String APPNAME = "bookswhislist1.0";

    public static class UsersControllerConstants {
        public static final String URL = "/users";

        public static class Alive {
            public static final String URL = "/isalive";
            public static final String URL_SUMMARY = "IsAlive";
            public static final String URL_DESCRIPTION = "IsAlive";
        }
        
        public static class AddItemWishlist {
            public static final String URL = "/additemwishlist/";
            public static final String URL_SUMMARY = "Agregar Lista de Deseos";
            public static final String URL_DESCRIPTION = "Agregar Lista de Deseos a Usuario";
        }
        public static class DelItemWishlist {
            public static final String URL = "/deleteitemwishlist/{idwhislist}";
            public static final String URL_SUMMARY = "Eliminar Lista de Deseos de Usuario";
            public static final String URL_DESCRIPTION = "Eliminar Lista de Deseos de Usuario";
        }
        public static class ListWhislist {
            public static final String URL = "/listWhislist/";
            public static final String URL_SUMMARY = "Lista de Deseos";
            public static final String URL_DESCRIPTION = "Se muestra Lista de Deseos por Usuario Registrado";
        }

        public static class ListUsers {
            public static final String URL = "/list";
            public static final String URL_SUMMARY = "Lista de Usuarios";
            public static final String URL_DESCRIPTION = "Se muestra lista de Usuarios registrados";
        }
    }
    
    public static class BooksControllerConstants {
        public static final String URL = "/books";

    	public static class SearchBook {
            public static final String URL = "/searchbook";
            public static final String URL_SUMMARY = "Buscar Libro";
            public static final String URL_DESCRIPTION = "Se muestran lista de libros apartir de filtro titulo";
        }	
    	public static class AddBook {
            public static final String URL = "/addbook";
            public static final String URL_SUMMARY = "Agregar Libro";
            public static final String URL_DESCRIPTION = "Agregar libro a lista de deseos";
        }
    	public static class DeleteBook {
            public static final String URL = "/deletebook/{idbook}/{idwhislist}";
            public static final String URL_SUMMARY = "Eliminar Libro";
            public static final String URL_DESCRIPTION = "Elminar libro de lista de deseos";
        }
    	 public static class ListBooks {
             public static final String URL = "/list/{idwhislist}";
             public static final String URL_SUMMARY = "Lista de Libros";
             public static final String URL_DESCRIPTION = "Se muestra lista de Libros registrados por lista de deseos";
         }
    }
    
    public static class LoginControllerConstants {
        public static final String URL = "/access";
        
        public static class SingUp {
            public static final String URL = "/login";
            public static final String URL_SUMMARY = "Acceso";
            public static final String URL_DESCRIPTION = "Acceso de Usuario";
        }
        public static class Register {
            public static final String URL = "/register";
            public static final String URL_SUMMARY = "Registro";
            public static final String URL_DESCRIPTION = "Registro de Usuario";
        }
    }
  }
