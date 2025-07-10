package controller;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cliente;
import model.ClienteDAO;
import model.ItemVenda;
import model.ItemVendaDAO;
import model.Produto;
import model.ProdutoDAO;
import model.Venda;
import model.VendaDAO;
import util.AlertaUtil;

public class VendaViewController {

    @FXML
    private Button btnEfetuarVenda;

    @FXML
    private ComboBox<Cliente> cmbxCliente;

    @FXML
    private TextField txtfdDataCompra;

    @FXML
    private TextField txtfdValor;

    private Produto produtoSelecionado;
    private Stage stage;

    public void setProduto(Produto produto) {
        this.produtoSelecionado = produto;
        carregarDadosProduto();
    }

    @FXML
    public void initialize() {
        carregarClientes();
        txtfdDataCompra.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void carregarDadosProduto() {
        if (produtoSelecionado != null) {
            txtfdValor.setText(String.valueOf(produtoSelecionado.getValor()));
        }
    }

    private void carregarClientes() {
        ClienteDAO dao = new ClienteDAO();
        try {
            List<Cliente> clientes = dao.selecionarClientes();
            ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(clientes);
            cmbxCliente.setItems(listaClientes);
        } catch (SQLException e) {
            AlertaUtil.mostrarErro("Erro", "Erro ao carregar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void OnActionEfetuarVenda(ActionEvent event) {
        Cliente clienteSelecionado = cmbxCliente.getSelectionModel().getSelectedItem();

        if (clienteSelecionado == null) {
            AlertaUtil.mostrarErro("Erro", "Selecione um cliente para a venda!");
            return;
        }

        try {
            // 1. Criar venda
            Venda venda = new Venda();
            venda.setDataCompra(Date.valueOf(LocalDate.parse(txtfdDataCompra.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            venda.setValorTotal((long) produtoSelecionado.getValor());
            venda.setIdCliente(clienteSelecionado.getId());

            VendaDAO vendaDAO = new VendaDAO();
            vendaDAO.inserirVenda(venda); // Supondo que insere e gera o ID automaticamente

            // 2. Criar item venda
            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setIdVenda(venda.getId()); // Você pode precisar ajustar isso caso o ID seja gerado automaticamente e precise ser recuperado
            itemVenda.setIdProduto(produtoSelecionado.getId());
            itemVenda.setPrecoUnitario((long) produtoSelecionado.getValor());
            itemVenda.setQuantidade(1);

            ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
            itemVendaDAO.inserirItemVenda(itemVenda);

            // 3. Atualizar estoque do produto
            produtoSelecionado.setQuantidadeEstoque(produtoSelecionado.getQuantidadeEstoque() - 1);
            ProdutoDAO produtoDAO = new ProdutoDAO();
            produtoDAO.atualizarProduto(produtoSelecionado);

            AlertaUtil.mostrarInformacao("Venda", "Venda efetuada com sucesso!");

            if(stage != null){
               stage.close();
            }
            

        } catch (SQLException e) {
            AlertaUtil.mostrarErro("Erro", "Erro ao efetuar venda: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            AlertaUtil.mostrarErro("Erro", "Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
