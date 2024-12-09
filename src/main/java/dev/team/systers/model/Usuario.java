package dev.team.systers.model;

import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Representa um usuário no sistema Systers.
 * Esta classe contém todas as informações básicas de um usuário,
 * incluindo suas credenciais de acesso e informações pessoais.
 */
@Entity
@Table(name = "usuario")
public class Usuario {
    /**
     * Identificador único do usuário.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", nullable = false)
    private Long id;

    /**
     * Login do usuário.
     * Funciona como identificador único visível no sistema (tag @).
     * Não pode ser nulo e deve ser único no sistema.
     */
    @Column(name = "usuario_login", nullable = false)
    private String login;

    /**
     * Senha do usuário.
     * Armazenada de forma segura no sistema.
     * Campo obrigatório.
     */
    @Column(name = "usuario_senha", nullable = false)
    private String senha;

    /**
     * Nome completo do usuário.
     * Campo obrigatório para identificação.
     */
    @Column(name = "usuario_nome", nullable = false)
    private String nome;

    /**
     * Email do usuário.
     * Utilizado para comunicações do sistema.
     * Campo opcional.
     */
    @Column(name = "usuario_email")
    private String email;

    /**
     * Número de telefone do usuário.
     * Formato livre para compatibilidade internacional.
     * Campo opcional.
     */
    @Column(name = "usuario_telefone")
    private String telefone;

    /**
     * Fuso horário do usuário.
     * Utilizado para exibir horários corretos nas interações do sistema.
     * Recomendado definir durante o cadastro para melhor experiência.
     */
    @Column(name = "usuario_fuso_horario")
    private TimeZone fusoHorario;

    /**
     * Nível de acesso do usuário no sistema.
     * Define as permissões e capacidades do usuário.
     * @see Autorizacao
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_autorizacao", nullable = false)
    private Autorizacao autorizacao;

    /**
     * Status atual da conta do usuário.
     * Controla se o usuário pode ou não acessar o sistema.
     * @see StatusConta
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_status_conta", nullable = false)
    private StatusConta statusConta;

    /**
     * Indica se o usuário é mentor.
     * Mentores podem criar e gerenciar mentorias no sistema.
     */
    @Column(name = "usuario_tipo_mentor")
    private Boolean tipoMentor;

    /**
     * Lista de grupos dos quais o usuário é membro.
     * Gerencia a participação do usuário em diferentes grupos do sistema.
     */
    @JsonManagedReference(value = "usuario-membro")
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Membro> membros;

    /**
     * Denúncias criadas pelo usuário.
     * Rastreia as denúncias que este usuário fez sobre outros usuários ou conteúdos.
     */
    @JsonIgnoreProperties({"usuarioAutor", "usuarioReportado"})
    @OneToMany(mappedBy = "usuarioAutor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Denuncia> denunciasCriadas;

    /**
     * Denúncias recebidas pelo usuário.
     * Rastreia as denúncias feitas contra este usuário.
     */
    @JsonIgnoreProperties({"usuarioAutor", "usuarioReportado"})
    @OneToMany(mappedBy = "usuarioReportado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Denuncia> denunciasRecebidas;

    /**
     * Notificações enviadas pelo usuário.
     * Histórico de notificações que este usuário enviou para outros.
     */
    @OneToMany(mappedBy = "usuarioNotificacaoRemetente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notificacao> notificacoesEnviadas;

    /**
     * Notificações recebidas pelo usuário.
     * Histórico de notificações recebidas de outros usuários.
     */
    @OneToMany(mappedBy = "usuarioNotificacaoDestinatario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notificacao> notificacoesRecebidas;

    /**
     * Perfil do usuário.
     * Contém informações adicionais e personalizadas sobre o usuário.
     */
    @OneToOne(mappedBy = "usuarioPerfil", cascade = CascadeType.ALL)
    private Perfil perfilUsuario;

    /**
     * Avaliações feitas pelo usuário.
     * Histórico de avaliações realizadas em mentorias ou outros contextos.
     */
    @OneToMany(mappedBy = "participanteAvaliador")
    private List<Avaliacao> avaliacoesUsuario;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Usuario() {}

    /**
     * Construtor completo para criação de um usuário com todos os atributos principais.
     * @param id Identificador único
     * @param login Nome de usuário único
     * @param senha Senha do usuário
     * @param nome Nome completo
     * @param email Endereço de email
     * @param telefone Número de telefone
     * @param fusoHorario Fuso horário do usuário
     * @param autorizacao Nível de autorização
     * @param statusConta Status da conta
     * @param membros Lista de grupos dos quais é membro
     * @param denunciasCriadas Lista de denúncias feitas
     * @param denunciasRecebidas Lista de denúncias recebidas
     */
    public Usuario(Long id, String login, String senha, String nome, String email, String telefone, TimeZone fusoHorario, Autorizacao autorizacao, StatusConta statusConta, List<Membro> membros, List<Denuncia> denunciasCriadas, List<Denuncia> denunciasRecebidas) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.fusoHorario = fusoHorario;
        this.autorizacao = autorizacao;
        this.statusConta = statusConta;
        this.membros = membros;
        this.denunciasCriadas = denunciasCriadas;
        this.denunciasRecebidas = denunciasRecebidas;
    }

    /**
     * Níveis de autorização disponíveis no sistema.
     */
    public enum Autorizacao {
        /** Usuário com acesso padrão ao sistema */
        PADRAO,
        /** Usuário com privilégios administrativos */
        ADMINISTRADOR
    }

    /**
     * Status possíveis para uma conta de usuário.
     */
    public enum StatusConta {
        /** Conta com acesso normal ao sistema */
        NORMAL,
        /** Conta temporariamente suspensa */
        SUSPENSO,
        /** Conta permanentemente banida */
        BANIDO
    }

    /**
     * Retorna o ID único do usuário.
     * @return O ID do usuário
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID do usuário.
     * @param id O novo ID a ser definido
     */
    public void setId(Long id) {
        this.id = id;
    }

    // Getters e Setters
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public TimeZone getFusoHorario() { return fusoHorario; }
    public void setFusoHorario(TimeZone fusoHorario) { this.fusoHorario = fusoHorario; }
    public Autorizacao getAutorizacao() { return autorizacao; }
    public void setAutorizacao(Autorizacao autorizacao) { this.autorizacao = autorizacao; }
    public StatusConta getStatusConta() { return statusConta; }
    public void setStatusConta(StatusConta statusConta) { this.statusConta = statusConta; }
    public Boolean getTipoMentor() { return tipoMentor; }
    public void setTipoMentor(Boolean tipoMentor) { this.tipoMentor = tipoMentor; }
    public List<Membro> getMembros() { return membros; }
    public void setMembros(List<Membro> membros) { this.membros = membros; }
    public List<Denuncia> getDenunciasCriadas() { return denunciasCriadas; }
    public void setDenunciasCriadas(List<Denuncia> denunciasCriadas) { this.denunciasCriadas = denunciasCriadas; }
    public List<Denuncia> getDenunciasRecebidas() { return denunciasRecebidas; }
    public void setDenunciasRecebidas(List<Denuncia> denunciasRecebidas) { this.denunciasRecebidas = denunciasRecebidas; }
    public List<Notificacao> getNotificacoesRecebidas() { return notificacoesRecebidas; }
    public void setNotificacoesRecebidas(List<Notificacao> notificacoesRecebidas) { this.notificacoesRecebidas = notificacoesRecebidas; }
    public List<Notificacao> getNotificacoesEnviadas() { return notificacoesEnviadas; }
    public void setNotificacoesEnviadas(List<Notificacao> notificacoesEnviadas) { this.notificacoesEnviadas = notificacoesEnviadas; }
    public Perfil getPerfilUsuario() { return perfilUsuario; }
    public void setPerfilUsuario(Perfil perfilUsuario) { this.perfilUsuario = perfilUsuario; }
    public List<Avaliacao> getAvaliacoesUsuario() { return avaliacoesUsuario; }
    public void setAvaliacoesUsuario(List<Avaliacao> avaliacoesUsuario) { this.avaliacoesUsuario = avaliacoesUsuario; }
}
