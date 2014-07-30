package com.iana.ssh.exception;

public class WrongConfigurationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message = null;
	 
    public WrongConfigurationException() {
        super();
    }
 
    public WrongConfigurationException(String message) {
        super(message);
        this.message = message;
    }
 
    public WrongConfigurationException(Throwable cause) {
        super(cause);
    }
 
    @Override
    public String toString() {
        return message;
    }
 
    @Override
    public String getMessage() {
        return message;
    }
}
