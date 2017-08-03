package test.pack.service;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
