package uz.pdp.simple_l.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import uz.pdp.simple_l.configuration.CustomUserDetails;
import uz.pdp.simple_l.entity.AuthUser;
import uz.pdp.simple_l.repository.AuthUserRepository;

import java.util.Optional;

@Controller
public class AuthLogoutController {

    private final AuthUserRepository authUserRepository;

    public AuthLogoutController(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @GetMapping("/auth/logout-page")
    public String getLogOut(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails != null) {
            model.addAttribute("username", customUserDetails.getUsername());
        }
        return "logout";
    }

    @GetMapping("/auth/logout")
    public String logout(){
        return "login";
    }

    @PostMapping("/auth/logout-all")
    public String logoutAll(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails != null) {
            String email = customUserDetails.getEmail();
            Optional<AuthUser> byEmail = authUserRepository.findByEmail(email);
            if (byEmail.isPresent()) {
                AuthUser authUser = byEmail.get();
                authUser.setActive(false);
                authUserRepository.save(authUser);
            }
        }

        return "redirect:/auth/login";
    }
}
