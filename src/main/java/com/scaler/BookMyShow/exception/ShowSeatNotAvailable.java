package com.scaler.BookMyShow.exception;

public class ShowSeatNotAvailable extends RuntimeException{
    public ShowSeatNotAvailable() {
    }

    public ShowSeatNotAvailable(String message) {
        super(message);
    }

    public ShowSeatNotAvailable(String message, Throwable cause) {
        super(message, cause);
    }

    public ShowSeatNotAvailable(Throwable cause) {
        super(cause);
    }

    public ShowSeatNotAvailable(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
