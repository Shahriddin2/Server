package uz.pdp.simple_l.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.pdp.simple_l.configuration.Session;

@Controller
public class PageController {

    private final Session session;

    public PageController(Session session) {
        this.session = session;
    }

    @GetMapping("/login/page")
    public String loginPage() {
        return "simple/test";
    }

    @GetMapping("/admin/page-get")
    public String adminPage(Model model) {
        String username = session.getCurrentUsername();
        model.addAttribute("username", username);
        return "admin/page";
    }

    @GetMapping("/user/page-get")
    public String userPage() {
        return "user-page";
    }

    @GetMapping("/password/reset")
    public String passwordResetPage() {
        return "password-reset";
    }

    @GetMapping("/new/password")
    public String newPasswordPage() {
        return "enter-code";
    }

    @GetMapping("/exception")
    public String exceptionPage() {
        return "exception-handler";
    }
}
