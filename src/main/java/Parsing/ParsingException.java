package Parsing;

public class ParsingException extends RuntimeException {

    private static final long serialVersionUID = 118998732159746722L;

    public ParsingException() {
    }

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }

    public ParsingException(String message,
            Throwable cause) {
        super(message, cause);
    }

    public ParsingException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression,
                writableStackTrace);
    }

}
