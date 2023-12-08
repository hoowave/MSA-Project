package org.msa.item.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.msa.item.dto.ResponseDTO;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> Exception(HttpServletRequest request, Exception e) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();
        responseDTOBuilder.code("500").message(e.getMessage());
        return ResponseEntity.ok(responseDTOBuilder.build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) throws Exception {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append((fieldError.getRejectedValue()));
            builder.append("]");
        }
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();
        responseDTOBuilder.code("500").message(builder.toString());
        return ResponseEntity.ok(responseDTOBuilder.build());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> ApiException(HttpServletRequest request, ApiException e) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();
        responseDTOBuilder.code("501").message(e.getMessage());
        return ResponseEntity.ok(responseDTOBuilder.build());
    }
}
