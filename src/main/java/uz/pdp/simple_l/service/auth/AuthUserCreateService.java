package uz.pdp.simple_l.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.simple_l.dto.auth.AuthUserCreateDto;
import uz.pdp.simple_l.entity.AuthUser;
import uz.pdp.simple_l.repository.AuthUserRepository;

import java.util.UUID;

@Service
public class AuthUserCreateService implements AuthUserService {
    private final AuthUserRepository authUserRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthUserCreateService(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Long create(AuthUserCreateDto dto) {
        AuthUser authUser = new AuthUser();
        authUser.setEmail(dto.email());
        authUser.setUsername(dto.username());
        authUser.setPassword(passwordEncoder.encode(dto.password()));
        authUser.setFirstName(dto.firstName());
        authUser.setLastName(dto.lastName());
        authUserRepository.save(authUser);
        return authUser.getId();
    }

}
