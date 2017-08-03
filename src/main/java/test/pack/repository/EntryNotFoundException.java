package test.pack.repository;

public class EntryNotFoundException extends RepositoryException {
    public EntryNotFoundException() {
        super();
    }

    public EntryNotFoundException(String message) {
        super(message);
    }

    public EntryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
