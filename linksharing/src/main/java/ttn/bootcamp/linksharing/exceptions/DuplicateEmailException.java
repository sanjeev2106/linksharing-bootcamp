package ttn.bootcamp.linksharing.exceptions;

public class DuplicateEmailException extends Exception {

    public DuplicateEmailException(String message) {
        super(message);
    }
}
