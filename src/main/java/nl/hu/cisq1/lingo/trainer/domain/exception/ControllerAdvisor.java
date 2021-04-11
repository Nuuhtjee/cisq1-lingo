package nl.hu.cisq1.lingo.trainer.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidRoundException.class)
    public ResponseEntity<Object> handleInvalidRoundException(){
        Map<String,Object> body = new LinkedHashMap<>();

        body.put("message","Round is invalid");
        body.put("status code", HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoGameFoundException.class)
    public ResponseEntity<Object> handleNoOngoingGameFoundException() {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "No game found");
        body.put("status code",HttpStatus.NOT_FOUND.toString());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}
