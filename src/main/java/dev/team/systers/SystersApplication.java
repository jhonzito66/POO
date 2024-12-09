package dev.team.systers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Systers.
 * Responsável por inicializar a aplicação Spring Boot e configurar seus componentes.
 * Esta aplicação fornece uma plataforma para mentorias e networking entre mulheres
 * na área de tecnologia, facilitando a troca de conhecimentos e experiências.
 */
@SpringBootApplication
public class SystersApplication {
    
    /**
     * Método principal que inicia a aplicação Spring Boot.
     * Configura o contexto da aplicação e inicializa todos os componentes necessários.
     * 
     * @param args Argumentos de linha de comando passados para a aplicação
     */
    public static void main(String[] args) {
        SpringApplication.run(SystersApplication.class, args);
    }
}
