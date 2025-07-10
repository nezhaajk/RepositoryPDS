package model;

import dal.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    
    public void inserirCliente(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO clientes(nome, telefone, endereco, data_nascimento) VALUES (?,?,?,?)";
        
        try(Connection conn =  ConexaoBD.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){
           
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEndereco());
            stmt.setDate(4, cliente.getDataNascimento());
            
            stmt.executeUpdate();
            
            System.out.println("Cliente inserido com sucesso!");
        }catch(SQLException e){
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        }
        }
    
     public void atualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, telefone = ?, endereco = ?, data_nascimento = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEndereco());
            stmt.setDate(4, cliente.getDataNascimento());
            stmt.setInt(5, cliente.getId());

            stmt.executeUpdate();

            System.out.println("Cliente atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
            throw e;
        }
    }

    public List<Cliente> selecionarClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEndereco(rs.getString("endereco"));
                cliente.setDataNascimento(rs.getDate("data_nascimento"));

                lista.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao selecionar clientes: " + e.getMessage());
            throw e;
        }

        return lista;
    }
}