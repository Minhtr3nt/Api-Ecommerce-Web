package com.example.ProjectEcommerce.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String mess){
        super(mess);
    }
}
