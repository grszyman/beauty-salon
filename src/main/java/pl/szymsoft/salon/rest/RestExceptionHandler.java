package pl.szymsoft.salon.rest;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.concat;
import static java.util.stream.StreamSupport.stream;
import static javax.validation.ElementKind.PARAMETER;
import static javax.validation.ElementKind.PROPERTY;

@ControllerAdvice
class RestExceptionHandler {

    private static final Set<ElementKind> ACCEPTED_ELEMENTS = EnumSet.of(PARAMETER, PROPERTY);

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handle(MultipartException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException ex) {

        var errors = concat(globalErrorsStream(ex), fieldErrorsStream(ex))
                .toList();

        return ResponseEntity.badRequest()
                .body(errors);
    }

    private static Stream<String> fieldErrorsStream(Errors ex) {
        return ex.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + toMessage(error));
    }

    private static Stream<String> globalErrorsStream(Errors ex) {
        return ex.getGlobalErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull);
    }

    private static String toMessage(MessageSourceResolvable fieldError) {
        return requireNonNullElse(fieldError.getDefaultMessage(), "unknown issue");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(ConstraintViolationException ex) {

        var violations = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolationDetail::new)
                .toList();

        return ResponseEntity.badRequest()
                .body(new Error("Constraint Violation", violations));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handle(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }

    private record Error(String error, Object details) {
    }

    private record ConstraintViolationDetail(String path, String message) {

        ConstraintViolationDetail(ConstraintViolation<?> violation) {
            this(toString(violation.getPropertyPath()), violation.getMessage());
        }

        private static String toString(Iterable<Path.Node> path) {
            return stream(path.spliterator(), false)
                    .filter(node -> ACCEPTED_ELEMENTS.contains(node.getKind()))
                    .map(Path.Node::toString)
                    .collect(joining("."));
        }
    }
}
