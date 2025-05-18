/*
package uz.pdp.simple_l.configuration.exeption;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, Error.class})
    public String handleException(Exception ex, HttpSession session) {

        String errorType = ex.getClass().getSimpleName();
        
        session.setAttribute("errorMessage", errorType);
        session.setAttribute("errorDetails", "eKutubxona tizimida kichik muammo yuzaga keldi. Iltimos, keyinroq qayta urinib ko'ring!");

        return "exception-handler"; // Bu yerda 'exception-handler' HTML sahifasini qaytaradi
    }
}
*/
