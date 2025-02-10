package com.ravindercodes.exception.handler;

import com.ravindercodes.exception.custom.*;
import com.ravindercodes.exception.response.ErrorResponse;
import com.ravindercodes.exception.response.ValidationResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericEx(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        ex.getClass().getName(),
                        "Internal Server Error",
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("validation error: {}", ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing
                ));
        return ResponseEntity.status(status).body(
                new ValidationResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        "validation error",
                        errors,
                        request.getDescription(false).replace("uri=", "")
                )
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationEx(AuthenticationException ex, WebRequest request) {
        log.error("Unauthorized error: {}", ex.getMessage());
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(UnAuthorizedEx.class)
    public ResponseEntity<ErrorResponse> unAuthorizedEx(UnAuthorizedEx ex, WebRequest request) {
        log.error("UnAuthorized error: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(UserNotFoundEx.class)
    public ResponseEntity<ErrorResponse> userNotFoundtEx(UserNotFoundEx ex, WebRequest request) {
        log.error("User not found: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(UsernameExitEx.class)
    public ResponseEntity<ErrorResponse> usernameExitEx(UsernameExitEx ex, WebRequest request) {
        log.error("Username already exit: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(EmailExitEx.class)
    public ResponseEntity<ErrorResponse> emailAlreadyEx(EmailExitEx ex, WebRequest request) {
        log.error("Email already exit: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(ResourceNotFoundEx.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundEx(ResourceNotFoundEx ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(JwtGenerationEx.class)
    public ResponseEntity<ErrorResponse> jwtGenerationEx(JwtGenerationEx ex, WebRequest request) {
        log.error("JWT generation failed for user: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(EmailSendFailedEx.class)
    public ResponseEntity<ErrorResponse> EeailSendingEx(EmailSendFailedEx ex, WebRequest request) {
        log.error("Email sending failed: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(EmailVerificationFailedEx.class)
    public ResponseEntity<ErrorResponse> emailVerificationEx(EmailVerificationFailedEx ex, WebRequest request) {
        log.error("Email verification failed: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(UserDisabledEx.class)
    public ResponseEntity<ErrorResponse> userDisabledEx(UserDisabledEx ex, WebRequest request) {
        log.error("User disabled : {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(IpAddressBlockedEx.class)
    public ResponseEntity<ErrorResponse> ipAddressBlockedEx(IpAddressBlockedEx ex, WebRequest request) {
        log.error("IP address block: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(
                        Instant.now().getEpochSecond(),
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getClass().getName(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }
}
