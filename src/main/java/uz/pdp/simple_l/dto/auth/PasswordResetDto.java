package uz.pdp.simple_l.dto.auth;

public record PasswordResetDto(
        String code,
        String newPassword,
        String confirmPassword
) {
}
