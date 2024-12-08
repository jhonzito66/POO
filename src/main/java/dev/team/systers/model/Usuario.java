package dev.team.systers.model;

import java.util.List;
import java.util.TimeZone;

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
 * Representa um usuário do sistema.
 */
@Entity
@Table(name = "usuario")
public class Usuario {
    /**
     * Identificador único do usuário.
     * Obrigatório (não há usuário sem ID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", nullable = false)
    private Long id;

    /**
     * Login do usuário.
     * Funciona como sua tag @ no restante do sistema.
     * Obrigatório.
     */
    @Column(name = "usuario_login", nullable = false)
    private String login;

    /**
     * Senha do usuário.
     * Obrigatório.
     */
    @Column(name = "usuario_senha", nullable = false)
    private String senha;

    /**
     * Nome do usuário
     * Obrigatório.
     */
    @Column(name = "usuario_nome", nullable = false)
    private String nome;

    /**
     * Email do usuário.
     * Opcional.
     */
    @Column(name = "usuario_email")
    private String email;

    /**
     * Telefone do usuário.
     * Apenas um.
     * Opcional.
     */
    @Column(name = "usuario_telefone")
    private String telefone;

    /**
     * Fuso horário do usuário.
     * Deve ser definido pelo próprio usuário (preferencialmente na tela de cadastro).
     * Caso contrário, o horário no sistema aparecerá -- para o usuário -- como o padrão universal.
     * Opcional.
     */
    @Column(name = "usuario_fuso_horario")
    private TimeZone fusoHorario;

    /**
     * Nível de acesso do usuário ao sistema.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_autorizacao", nullable = false)
    private Autorizacao autorizacao;

    /**
     * StatusConta da conta do usuário (padrão ou moderador).
     * Qualquer usuário é criado inicialmente com acesso padrão.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_status_conta", nullable = false)
    private StatusConta statusConta;

    /**
     *  Variável que declara se o usuário é mentor ou não. Se for, ele pode criar mentorias.
     *  Caso contrário, essa opção não estará ativa e somente poderá participar de mentorias.
     */
    @Column(name = "usuario_tipo_mentor")
    private Boolean tipoMentor;

    /**
     * Associação com a tabela de membros.
     */
    @JsonManagedReference(value = "usuario-membro")
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Membro> membros;

    /**
     * Associação com a tabela de denúncia para a relação de autor.
     */
    @JsonManagedReference(value = "usuario-denuncia-autor")
    @OneToMany(mappedBy = "usuarioAutor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Denuncia> denunciasCriadas;

    /**
     * Associação com a tabela de denúncia para a relação de reportado.
     */
    @JsonManagedReference(value = "usuario-denuncia-reportado")
    @OneToMany(mappedBy = "usuarioReportado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Denuncia> denunciasRecebidas;

    /**
     * Associação com a tabela de notificação (envios).
     */
    @OneToMany(mappedBy = "usuarioNotificacaoRemetente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notificacao> notificacoesEnviadas;

    /**
     * Associação com a tabela de notificação (recebimentos).
     */
    @OneToMany(mappedBy = "usuarioNotificacaoDestinatario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notificacao> notificacoesRecebidas;

    /**
     * Associação com a tabela de perfil.
     */
    @OneToOne(mappedBy = "usuarioPerfil", cascade = CascadeType.ALL)
    private Perfil perfilUsuario;

    /**
     * Avaliações associada ao usuário.
     */
    @OneToMany(mappedBy = "participanteAvaliador")
    private List<Avaliacao> avaliacoesUsuario;


    /**
     * Construtor sem parâmetros.
     */
    public Usuario() {}

    /**
     * Construtor completo.
     * @param id
     * @param login
     * @param senha
     * @param nome
     * @param email
     * @param telefone
     * @param fusoHorario
     * @param autorizacao
     * @param statusConta
     * @param membros
     * @param denunciasCriadas
     * @param denunciasRecebidas
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
     * Enum para representar o nível de autorização do usuário.
     * Pode ser <i>PADRAO</i> ou <i>ADMINISTRADOR</i>.
     */
    public enum Autorizacao {
        PADRAO,
        ADMINISTRADOR
    }

    /**
     * Enum para representar os status de acesso do usuário.
     * Pode sr <i>NORMAL</i>, <i>SUSPENSO</i> ou <i>BANIDO</i>.
     */
    public enum StatusConta {
        NORMAL,
        SUSPENSO,
        BANIDO
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TimeZone getFusoHorario() {
        return fusoHorario;
    }

    public void setFusoHorario(TimeZone fusoHorario) {
        this.fusoHorario = fusoHorario;
    }

    public Autorizacao getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Autorizacao autorizacao) {
        this.autorizacao = autorizacao;
    }

    public StatusConta getStatusConta() {
        return statusConta;
    }

    public void setStatusConta(StatusConta statusConta) {
        this.statusConta = statusConta;
    }

    public Boolean getTipoMentor() {
        return tipoMentor;
    }

    public void setTipoMentor(Boolean tipoMentor) {
        this.tipoMentor = tipoMentor;
    }

    public List<Membro> getMembros() {
        return membros;
    }

    public void setMembros(List<Membro> membros) {
        this.membros = membros;
    }

    public List<Denuncia> getDenunciasCriadas() {
        return denunciasCriadas;
    }

    public void setDenunciasCriadas(List<Denuncia> denunciasCriadas) {
        this.denunciasCriadas = denunciasCriadas;
    }

    public List<Denuncia> getDenunciasRecebidas() {
        return denunciasRecebidas;
    }

    public void setDenunciasRecebidas(List<Denuncia> denunciasRecebidas) {
        this.denunciasRecebidas = denunciasRecebidas;
    }

    public List<Notificacao> getNotificacoesRecebidas() {
        return notificacoesRecebidas;
    }

    public void setNotificacoesRecebidas(List<Notificacao> notificacoesRecebidas) {
        this.notificacoesRecebidas = notificacoesRecebidas;
    }

    public List<Notificacao> getNotificacoesEnviadas() {
        return notificacoesEnviadas;
    }

    public void setNotificacoesEnviadas(List<Notificacao> notificacoesEnviadas) {
        this.notificacoesEnviadas = notificacoesEnviadas;
    }

    public Perfil getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(Perfil perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
        perfilUsuario.setUsuarioPerfil(this);
    }
}
