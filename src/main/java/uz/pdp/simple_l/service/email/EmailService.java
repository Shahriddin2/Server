package uz.pdp.simple_l.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Map<String, LocalDateTime> codeExpiryMap = new HashMap<>();

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendVerificationEmail(String toEmail) {
        String code = UUID.randomUUID().toString().substring(0, 6);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(2);
        codeExpiryMap.put(code, expiryTime);

        String htmlMsg = """
            <div style="font-family: Arial, sans-serif; text-align: center; padding: 20px;">
                <h2 style="color: #1A285A;">🔐 Tasdiqlash Kodingiz</h2>
                <p style="font-size: 16px;">Hurmatli foydalanuvchi,</p>
                <p style="font-size: 18px; font-weight: bold; color: #D9534F;">%s</p>
                <p style="color: #555;">⏳ Ushbu kod 2 daqiqa ichida eskiradi.</p>
                <p>Agar siz bu so‘rovni yubormagan bo‘lsangiz, bu xabarni e'tiborsiz qoldiring.</p>
                <hr style="border: 0; border-top: 1px solid #ddd;">
                <p style="font-size: 14px; color: #777;">Hurmat bilan,<br><strong>[Sobirov Shahriddin]</strong></p>
            </div>
        """.formatted(code);

        try {
            sendHtmlEmail(toEmail, htmlMsg);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }

        return code;
    }

    private void sendHtmlEmail(String toEmail, String htmlMsg) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("🔐 Tasdiqlash Kodingiz");
        helper.setText(htmlMsg, true);

        mailSender.send(message);
    }

    public boolean isCodeValid(String code) {
        if (codeExpiryMap.containsKey(code)) {
            LocalDateTime expiryTime = codeExpiryMap.get(code);
            if (LocalDateTime.now().isBefore(expiryTime)) {
                return true;
            } else {
                codeExpiryMap.remove(code);
            }
        }
        return false;
    }
}
