package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.ItemVendaDAO;
import model.Produto;
import model.ProdutoDAO;
import util.AlertaUtil;

public class ItemVendaController {

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnFechar;

    @FXML
    private TableColumn<Produto, Integer> colunaId;

    @FXML
    private TableColumn<Produto, String> colunaDescricao;

    @FXML
    private TableColumn<Produto, Long> colunaValor;

    @FXML
    private TableColumn<Produto, Integer> colunaEstoque;

    @FXML
    private TableView<Produto> tableViewItemVenda;

    private Stage stage;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarProdutos();
        configurarDuploClique();
    }

    private void configurarColunas() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));
    }

    private void carregarProdutos() {
        ProdutoDAO dao = new ProdutoDAO();
        try {
            List<Produto> produtos = dao.listarTodosProdutos();
            ObservableList<Produto> lista = FXCollections.observableArrayList(produtos);
            tableViewItemVenda.setItems(lista);
        } catch (SQLException e) {
            AlertaUtil.mostrarErro("Erro", "Erro ao carregar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarDuploClique() {
        tableViewItemVenda.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Produto selecionado = tableViewItemVenda.getSelectionModel().getSelectedItem();
                if (selecionado != null) {
                    abrirTelaVenda(selecionado);
                }
            }
        });
    }

    private void abrirTelaVenda(Produto produto) {
        try {
            URL url = new File("src/main/java/view/VendaView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            VendaViewController vendaController = loader.getController();
            vendaController.setProduto(produto);  // Passa o produto pra tela de venda

            Stage stageVenda = new Stage();
            stageVenda.setTitle("Efetuar Venda");
            stageVenda.setScene(new Scene(root));
            stageVenda.show();

        } catch (IOException e) {
            AlertaUtil.mostrarErro("Erro", "Erro ao abrir tela de venda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnExcluirOnAction(ActionEvent event) {
    Produto produtoSelecionado = tableViewItemVenda.getSelectionModel().getSelectedItem();

    if (produtoSelecionado == null) {
        AlertaUtil.mostrarErro("Erro", "Selecione um produto para excluir");
        return;
    }

    try {
        ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
        boolean temVendas = itemVendaDAO.produtoTemVendas(produtoSelecionado.getId());

        if (temVendas) {
            AlertaUtil.mostrarErro("Erro", "Não é possível excluir o produto, pois ele já está vinculado a uma venda.");
            return;
        }

        ProdutoDAO dao = new ProdutoDAO();
        dao.excluirProduto(produtoSelecionado.getId());

        AlertaUtil.mostrarInformacao("Sucesso", "Produto excluído com sucesso!");

        tableViewItemVenda.getItems().remove(produtoSelecionado);

    } catch (SQLException e) {
        AlertaUtil.mostrarErro("Erro", "Erro ao excluir produto: " + e.getMessage());
        e.printStackTrace();
    }
}

    @FXML
    void OnActionFecharTela(ActionEvent event) {
        if (stage != null) {
            stage.close();
        }
    }

    public void setStage(Stage telaItemVenda) {
        this.stage = telaItemVenda;
    }

    void ajustarElementosJanela() {
        // Método vazio, mas deixei aqui caso queira configurar elementos da tela futuramente.
    }
}
