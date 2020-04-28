package uk.ac.leeds.comp2913.api.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileUploadFailedException extends RuntimeException {

    public FileUploadFailedException(String message) {
        super(message);
    }
}
