package uz.pdp.simple_l.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.simple_l.dto.auth.PasswordResetDto;
import uz.pdp.simple_l.entity.AuthUser;
import uz.pdp.simple_l.repository.AuthUserRepository;
import uz.pdp.simple_l.service.email.EmailService;

import java.util.Optional;

@Controller
public class PasswordResetController {

    private final AuthUserRepository authUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(AuthUserRepository authUserRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/send/message")
    public String passwordReset(@RequestParam String email,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (email == null || email.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kechirasiz, email manzilingizni kiriting!");
            return "redirect:/password/reset";
        }

        Optional<AuthUser> currentUser = authUserRepository.findByEmail(email);
        if (currentUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Foydalanuvchi tizimda ro‘yxatdan o‘tmagan. Iltimos, qayta urinib ko‘ring!");
            return "redirect:/password/reset";
        }

        // Kod yuborish va emailni sessionda saqlash
        emailService.sendVerificationEmail(email);
        session.setAttribute("email", email);
        redirectAttributes.addFlashAttribute("successMessage", "Email manzilingizga kod jo‘natildi.");
        return "redirect:/new/password";
    }

    @PostMapping("/password/changed")
    public String passwordChanged(@ModelAttribute PasswordResetDto dto,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {
        if (dto.code() == null || dto.code().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email manzilingizga yuborilgan kodni kiriting!");
            return "redirect:/new/password";
        }
        if (dto.newPassword() == null || dto.newPassword().isEmpty() ||
                dto.confirmPassword() == null || dto.confirmPassword().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Yangi parolingizni kiriting!");
            return "redirect:/new/password";
        }

        if (!dto.newPassword().equals(dto.confirmPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Parollar mos kelmadi!");
            return "redirect:/new/password";
        }

        // Sessiondan emailni olish
        String email = (String) session.getAttribute("email");
        if (email == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Session muddati tugagan, qayta urinib ko‘ring!");
            return "redirect:/password/reset";
        }

        Optional<AuthUser> userOptional = authUserRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Foydalanuvchi topilmadi!");
            return "redirect:/password/reset";
        }

        AuthUser authUser = userOptional.get();

        // Kod tekshirish
        boolean isValidCode = emailService.isCodeValid(dto.code());

        if (!isValidCode) {
            redirectAttributes.addFlashAttribute("errorMessage", "Yuborilgan kod noto‘g‘ri!");
            return "redirect:/password/reset";
        }

        // Yangi parolni saqlash
        authUser.setPassword(passwordEncoder.encode(dto.newPassword()));
        authUserRepository.save(authUser);

        // Sessionni tozalash
        session.removeAttribute("email");
        redirectAttributes.addFlashAttribute("successMessage", "Parolingiz muvaffaqiyatli o‘zgartirildi!");
        return "redirect:/auth/login";
    }

}
