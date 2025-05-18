package uz.pdp.simple_l.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.simple_l.dto.auth.AuthUserCreateDto;
import uz.pdp.simple_l.service.auth.AuthUserCreateService;

@Controller
public class RegisterController {

    private final AuthUserCreateService authUserCreateService;

    public RegisterController(AuthUserCreateService authUserCreateService) {
        this.authUserCreateService = authUserCreateService;
    }


    @GetMapping("/auth/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/auth/create")
    public String create(@ModelAttribute AuthUserCreateDto dto,
                         RedirectAttributes redirectAttributes) {
        if (dto.firstName() == null || dto.firstName().isEmpty() ||
                dto.lastName() == null || dto.lastName().isEmpty() ||
                dto.email() == null || dto.email().isEmpty() ||
                dto.username() == null || dto.username().isEmpty() ||
                dto.password() == null || dto.password().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Iltimos, barcha maydonlarni to'ldiring!");
            return "redirect:/auth/register";
        }

        authUserCreateService.create(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Siz muvaffaqqiyatli ro`yxatdan o`tdingiz😀😀");
        return "redirect:/user/page-get";
    }
}
