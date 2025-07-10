package principal;

import controller.LoginController;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import model.Cliente;
import model.ClienteDAO;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Loja MVC");
        URL url = new File("src/main/java/view/Login.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Stage telaLogin = new Stage();
        LoginController lc = loader.getController();
        lc.setStage(telaLogin);
        telaLogin.setOnShown(event -> {
            lc.abrirJanela();
        });
        Scene scene = new Scene (root);
        scene.getStylesheets().add(getClass().getResource(""
                + "/css/login.css").toExternalForm());
        telaLogin.setScene(scene);
        telaLogin.show();
    }

    public static void main(String[] args) throws SQLException {
        
       /* ClienteDAO clienteDAO = new ClienteDAO();
        
        //Cria um novo cliente
        Cliente novoCliente = new Cliente();
        
        novoCliente.setNome("Gabriel");
        novoCliente.setTelefone("123456789012");
        novoCliente.setEndereco("Rua Filipinas, 123");
        novoCliente.setDataNascimento(Date.valueOf("2000-02-14"));
        
        //Insere o cliente no banco de dados
        clienteDAO.inserirCliente(novoCliente);
        
        //Lista os clientes cadastrados no banco de dados
        clienteDAO.listarClientes();
        */
        launch();
    }

}