package uz.pdp.simple_l.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import uz.pdp.simple_l.entity.AuthUser;
import uz.pdp.simple_l.repository.AuthUserRepository;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthUserRepository authUserRepository;
    private final CustomAuthenticationSuccessHandler successHandler;

    private static final String[] PUBLIC_PAGES = {
            "/auth/login",
            "/auth/logout",
            "/auth/register",
            "/auth/create",
            "/file/**",
            "/password/**",
            "/send/**",
            "/new/**",
            "/exception",
            "/api/support",
            "/login/page",
            "/css/**",
            "/js/**"
    };

    private static final String[] ADMIN_PAGES = {
            "/admin/**",
            "/auth/**",
            "/category/**",
            "/dashboard/**",
            "/book/**",
            "/control-page",
            "/current/user/delete/{id}",
            "/current/user/block/{id}",
            "/current/user/activate/{id}"

    };

    private static final String[] USERS_PAGES = {
            "/user/**",
            "/images/**",
            "/saved/book/{id}",
            "/saved/books",
            "/saved/book/delete/{id}"
    };

    public SecurityConfiguration(AuthUserRepository authUserRepository, CustomAuthenticationSuccessHandler successHandler) {
        this.authUserRepository = authUserRepository;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(PUBLIC_PAGES).permitAll()

                        .requestMatchers("/auth/logout-page", "/book/search", "/book/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(USERS_PAGES).hasRole("USER")

                        .requestMatchers(ADMIN_PAGES).hasRole("ADMIN")


                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login/page")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login")
                        .permitAll()
                );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService customUserDetails() {
        return username -> {
            Optional<AuthUser> byUsername = authUserRepository.findByUsername(username);
            if (byUsername.isEmpty()) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            AuthUser authUser = byUsername.get();

            return new CustomUserDetails(
                    authUser.getId(),
                    authUser.getUsername(),
                    authUser.getPassword(),
                    authUser.getEmail(),
                    authUser.isActive(),
                    authUser.getRole()
            );
        };
    }
}
