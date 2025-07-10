package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cliente;
import model.ClienteDAO;
import util.AlertaUtil;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClienteViewController {

    @FXML
    private Button btnSalvar;

    @FXML
    private TableView<Cliente> TableViewClientes;

    @FXML
    private TableColumn<Cliente, Integer> colunaID;

    @FXML
    private TableColumn<Cliente, String> colunaNome;

    @FXML
    private TableColumn<Cliente, String> colunaTelefone;

    @FXML
    private TableColumn<Cliente, String> colunaEndereco;

    @FXML
    private TableColumn<Cliente, Date> colunaNascimento;

    @FXML
    private TextField txtDataNascimento;

    @FXML
    private TextField txtEndereco;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtTelefone;

    private Stage stage;
    private Cliente clienteAtual;

    /**
     * Salva o cliente no banco e atualiza a tabela da própria tela.
     */
    @FXML
    void btnSalvarOnActionPerformed(ActionEvent event) {
        String nome = txtNome.getText();
        String telefone = txtTelefone.getText();
        String endereco = txtEndereco.getText();
        
        try {
            
            if(nome.isEmpty() || telefone.isEmpty() || endereco.isEmpty()){
            AlertaUtil.mostrarErro("Erro", "Campos obrigatórios não preenchidos");
            
            return;
            }
            
            if (clienteAtual == null) {
                clienteAtual = new Cliente();
            }

            clienteAtual.setNome(txtNome.getText());
            clienteAtual.setTelefone(txtTelefone.getText());
            clienteAtual.setEndereco(txtEndereco.getText());

            if (!txtDataNascimento.getText().isEmpty()) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate data = LocalDate.parse(txtDataNascimento.getText(), dtf);
                clienteAtual.setDataNascimento(Date.valueOf(data));
            } else {
                clienteAtual.setDataNascimento(null);
            }

            ClienteDAO dao = new ClienteDAO();
            if (clienteAtual.getId() == 0) {
                dao.inserirCliente(clienteAtual);
            } else {
                dao.atualizarCliente(clienteAtual);
            }

            AlertaUtil.mostrarInformacao("Sucesso", "Cliente salvo com sucesso!");

            carregarClientesTabela(); // ✅ Atualiza a tabela da própria tela
            limparCampos();

        } catch (Exception e) {
            AlertaUtil.mostrarErro("Erro", "Erro ao salvar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Preenche os campos para editar um cliente (se vier da listagem).
     */
    public void ajustarElementosJanela(Cliente cliente) {
        this.clienteAtual = cliente;
        if (cliente != null) {
            txtNome.setText(cliente.getNome());
            txtTelefone.setText(cliente.getTelefone());
            txtEndereco.setText(cliente.getEndereco());

            if (cliente.getDataNascimento() != null) {
                txtDataNascimento.setText(
                        cliente.getDataNascimento().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
            } else {
                txtDataNascimento.setText("");
            }
        }
        
        carregarClientesTabela();   
    }

    public void setStage(Stage telaCadastroCliente) {
        this.stage = telaCadastroCliente;
    }

    /**
     * Carrega os clientes do banco e preenche a tabela.
     */
    public void carregarClientesTabela() {
        try {
            ClienteDAO dao = new ClienteDAO();
            ObservableList<Cliente> lista = FXCollections.observableArrayList(dao.selecionarClientes());

            colunaID.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
            colunaNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
            colunaTelefone.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefone()));
            colunaEndereco.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEndereco()));
            colunaNascimento.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDataNascimento()));

            TableViewClientes.setItems(lista);

        } catch (SQLException e) {
            AlertaUtil.mostrarErro("Erro", "Erro ao carregar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Limpa os campos após salvar.
     */
    private void limparCampos() {
        txtNome.clear();
        txtTelefone.clear();
        txtEndereco.clear();
        txtDataNascimento.clear();
        clienteAtual = null;
    }
}
