package uz.pdp.simple_l.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uz.pdp.simple_l.entity.AuthUser;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.repository.AuthUserRepository;
import uz.pdp.simple_l.repository.BookRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final BookRepository bookRepository;
    private final AuthUserRepository authUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        HttpSession session = request.getSession();

        if (!customUserDetails.isEnabled()) {
            response.sendRedirect("/auth/register");
        }
        if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            long booksCount = bookRepository.count();
            long authUsersCount = authUserRepository.countActiveUsers();

            session.setAttribute("booksCount", booksCount);
            session.setAttribute("authUsersCount", authUsersCount);

            response.sendRedirect("/admin/page-get");

        } else if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {

            List<Book> books = bookRepository.findAllBooks();
            request.getSession().setAttribute("books", books);
            response.sendRedirect("/user/page-get");
        }
    }
}

