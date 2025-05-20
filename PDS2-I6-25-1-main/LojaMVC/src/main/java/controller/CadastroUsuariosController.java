package controller;

import java.sql.SQLException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Usuario;
import model.UsuarioDAO;
import util.AlertaUtil;

public class CadastroUsuariosController {
    Stage stageCadastroUsuarios;
    Usuario usuarioSelecionado;
    private Runnable onUsuarioSalvo; //Este é o callback
    
      @FXML
    private Button btnExcluir;

    @FXML
    private Button btnFechar;

    @FXML
    private Button btnIncluirAlterar;

    @FXML
    private ComboBox<String> cbPerfil;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtNome;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private TextField txtTelefone;

    @FXML
    void btnExcluirClick(ActionEvent event) throws SQLException {
        Optional<ButtonType> resultado = AlertaUtil.mostrarConfirmacao("Atenção",
                "Tem certeza que quer excluir o registro?");
        if(resultado.isPresent()){
            ButtonType botaoPressionado = resultado.get();
            if(botaoPressionado == ButtonType.OK){
                excluir(usuarioSelecionado.getId());
            }
        }
    }

    @FXML
    void btnFecharClick(ActionEvent event) {
        stageCadastroUsuarios.close();
    }

    @FXML
    void btnIncluirAlterarClick(ActionEvent event) throws SQLException {
        if(usuarioSelecionado == null){
            incluir(txtNome.getText(),
            txtTelefone.getText(), txtLogin.getText(),
            txtSenha.getText(), cbPerfil.getValue());
        } else {
            alterar(usuarioSelecionado.getId(), txtNome.getText(),
                    txtTelefone.getText(), txtLogin.getText(),
                    txtSenha.getText(), cbPerfil.getValue());
        }
    }

    
    void setStage(Stage telaCadastroUsuarios){
        this.stageCadastroUsuarios = telaCadastroUsuarios;
    }
    
    void ajustarElementosJanela(Usuario user){
        this.usuarioSelecionado = user;
        if(user == null){ //Incluir
            txtNome.requestFocus();
            btnExcluir.setVisible(false);
            btnIncluirAlterar.setText("Salvar");
            cbPerfil.getItems().addAll("admin", "user");
        } else {
            btnIncluirAlterar.setText("Editar");
            txtNome.setText(user.getNome());
            txtTelefone.setText(user.getFone());
            txtLogin.setText(user.getLogin());
            txtSenha.setText(user.getSenha());
            cbPerfil.getItems().addAll("admin", "user");
            cbPerfil.setValue(user.getPerfil());
        }
    }

    void incluir(String nome, String fone, 
        String login, String senha, String perfil) throws SQLException {
        Usuario usuario = new Usuario(nome, fone, login,
        senha, perfil);
        new UsuarioDAO().salvar(usuario);
        if(onUsuarioSalvo != null){
            onUsuarioSalvo.run();
        }
        AlertaUtil.mostrarInformacao("Informação",
                "Registro inserido com sucesso!");
        stageCadastroUsuarios.close();
    }
    
    void alterar(int id, String nome, String fone, String login,
            String senha, String perfil) throws SQLException{
        Usuario usuarioAlterado = new Usuario(id, nome, fone, login,
        senha, perfil);
        new UsuarioDAO().alterar(usuarioAlterado);
        if(onUsuarioSalvo != null){
            onUsuarioSalvo.run();
        }
         AlertaUtil.mostrarInformacao("Informação",
                "Registro alterado com sucesso!");
        stageCadastroUsuarios.close();
    }
    
    public void setOnUsuarioSalvo(Runnable callback){
        this.onUsuarioSalvo = callback;
    }
    
    public void excluir(int id) throws SQLException{
        new UsuarioDAO().excluir(id);
         if(onUsuarioSalvo != null){
            onUsuarioSalvo.run();
        }
         AlertaUtil.mostrarInformacao("Informação", 
                 "Registro excluído com sucesso!");
         stageCadastroUsuarios.close();
    }
    
}
