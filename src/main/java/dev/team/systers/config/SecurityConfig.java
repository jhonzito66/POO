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

/**
 * Configuração de segurança da aplicação.
 * Esta classe gerencia a configuração do Spring Security,
 * definindo regras de autenticação, autorização e proteção contra ataques.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura a cadeia de filtros de segurança.
     * Define as regras de acesso, autenticação e proteção para cada endpoint da aplicação.
     * 
     * @param http Configuração do HttpSecurity
     * @return Cadeia de filtros de segurança configurada
     * @throws Exception Se houver erro na configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/registrar", "/registrar_usuario", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/denuncias/resolver/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/perfil/denunciar").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("login")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/feed", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/css/**", "/js/**")
                        .csrfTokenRepository(org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse())
                );

        return http.build();
    }

    /**
     * Configura o serviço de detalhes do usuário.
     * Responsável por carregar os dados do usuário durante a autenticação,
     * incluindo permissões e status da conta.
     * 
     * @param usuarioRepository Repositório para acesso aos dados do usuário
     * @return Serviço de detalhes do usuário configurado
     */
    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Usuario usuario = usuarioRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

            String role = usuario.getAutorizacao().name();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(usuario.getLogin())
                    .password(usuario.getSenha())
                    .roles(role)
                    .accountLocked(usuario.getStatusConta() == Usuario.StatusConta.BANIDO)
                    .disabled(usuario.getStatusConta() == Usuario.StatusConta.SUSPENSO)
                    .build();
        };
    }

    /**
     * Configura o codificador de senha.
     * Utiliza BCrypt para hash seguro das senhas dos usuários.
     * 
     * @return Codificador de senha BCrypt configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}