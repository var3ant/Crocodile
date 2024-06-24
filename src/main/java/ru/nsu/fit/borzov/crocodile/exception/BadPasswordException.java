
package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class BadPasswordException extends HttpException {
    public BadPasswordException() {
        super(HttpStatus.BAD_REQUEST, ExceptionMessage.BAD_PASSWORD_MESSAGE);
    }//TODO: тут можно третим аргументом в message передать почему именно пароль плохой
}
