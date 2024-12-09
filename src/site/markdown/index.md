# Systers - Sistema de Comunidade Online

## Visão Geral

Systers é uma plataforma de comunidade online focada em criar um ambiente seguro e colaborativo para interação entre usuários. O sistema permite a criação e participação em grupos de discussão, compartilhamento de conteúdo e um sistema robusto de moderação para manter a qualidade das interações.

## Funcionalidades Principais

### Sistema de Usuários
* Registro e autenticação de usuários
* Diferentes níveis de acesso (USUARIO, ADMINISTRADOR)
* Perfil personalizado para cada usuário
* Sistema de login seguro com Spring Security

### Gestão de Grupos
* Criação e configuração de grupos de discussão
* Sistema de membros com diferentes autorizações:
  * MEMBRO - Participante padrão
  * MODERADOR - Pode moderar conteúdo
  * ADMINISTRADOR - Controle total do grupo
* Compartilhamento de postagens e comentários
* Gerenciamento de status do grupo (ativo/inativo)

### Sistema de Postagens e Comentários
* Criação de postagens em grupos
* Sistema de comentários em postagens
* Moderação de conteúdo
* Histórico de interações

### Sistema de Denúncias
* Denúncia de conteúdo inadequado
* Diferentes categorias de denúncia
* Fluxo de moderação de denúncias
* Status de acompanhamento (PENDENTE, EM_ANALISE, RESOLVIDA, ARQUIVADA)

## Tecnologias Utilizadas

### Backend
* Java 21
* Spring Boot 3.3.5
* Spring Security
* Spring Data JPA
* PostgreSQL

### Frontend
* Thymeleaf
* Bootstrap
* HTML5/CSS3
* JavaScript

### Ferramentas de Build
* Maven
* Git

## Arquitetura do Sistema

O projeto segue uma arquitetura em camadas:

* **Controladores** (`controller`) - Gerenciamento de requisições HTTP
* **Serviços** (`service`) - Lógica de negócios
* **Repositórios** (`repository`) - Acesso a dados
* **Modelos** (`model`) - Entidades do domínio
* **Configurações** (`config`) - Configurações do sistema
* **Exceções** (`exception`) - Tratamento de erros

## Guia de Instalação

### Pré-requisitos
* JDK 21
* Maven 3.6+
* PostgreSQL 15+

### Configuração
1. Clone o repositório
2. Configure o banco de dados no `application.properties`
3. Execute `mvn clean install`
4. Execute `mvn spring-boot:run`

## Documentação Adicional

* [Javadoc](apidocs/index.html) - Documentação detalhada das classes
* [Dependências](dependencies.html) - Lista de dependências do projeto
* [Relatórios](project-reports.html) - Relatórios do projeto 