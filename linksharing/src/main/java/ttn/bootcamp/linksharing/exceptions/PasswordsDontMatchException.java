package ttn.bootcamp.linksharing.exceptions;

public class PasswordsDontMatchException extends Exception {

    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
