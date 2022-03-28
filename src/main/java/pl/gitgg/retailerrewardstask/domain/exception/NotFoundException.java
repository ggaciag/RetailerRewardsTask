package pl.gitgg.retailerrewardstask.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String object, String id) {
        super(String.format("Could not find %s with id %s", object, id));
    }
}
