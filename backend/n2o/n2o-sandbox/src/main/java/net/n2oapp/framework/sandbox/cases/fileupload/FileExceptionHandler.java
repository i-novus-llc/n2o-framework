package net.n2oapp.framework.sandbox.cases.fileupload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<FileErrorModel> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exception) {
        FileErrorModel errorModel = new FileErrorModel(exception.getMessage());
        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }
}

