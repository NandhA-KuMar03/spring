package ticketbooking.Ticket.Booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TicketBookingSystemExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(TicketBookingSystemException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e){
        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setMessage("Enter the date in format of yyyy-mm-dd");
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
