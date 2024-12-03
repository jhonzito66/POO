package dev.team.systers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import dev.team.systers.model.Usuario;
import dev.team.systers.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/registrar", "/registrar_usuario", "/css/**", "/js/**").permitAll() // Páginas públicas
                        .anyRequest().authenticated() // Protege outras páginas
                )
                .formLogin(form -> form
                        .loginPage("/login") // Página personalizada  de login
                        .usernameParameter("login")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/perfil/me", true) // Redireciona à página de perfil após o login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Redireciona à tela de login após logout
                        .invalidateHttpSession(true) // Invalidar sessão
                        .deleteCookies("JSESSIONID") // Deletar cookies
                );
                http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/css/**", "/js/**")
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Usuario usuario = usuarioRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(usuario.getLogin())
                    .password(usuario.getSenha())
                    .authorities("ROLE_USER")
                    .accountLocked(usuario.getStatusConta() == Usuario.StatusConta.BANIDO)
                    .disabled(usuario.getStatusConta() == Usuario.StatusConta.SUSPENSO)
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}