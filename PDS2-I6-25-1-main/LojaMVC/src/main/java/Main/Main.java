package Main;

import java.sql.Date;
import model.Cliente;
import model.ClienteDAO;

public class Main {
    
    public static void main(String[] args){
        ClienteDAO clienteDAO = new ClienteDAO();
        
        //Criar novo cliente
        Cliente novoCliente = new Cliente();
        novoCliente.setNome("Gabriel");
        novoCliente.setTelefone("123456789012");
        novoCliente.setEndereco("Rua Filipina, 123");
        novoCliente.setDataNascimento(Date.valueOf("2000-02-14"));
        
        //Insirir no banco
        clienteDAO.inserirCliente(novoCliente);
        
        //Listar todos os clientes
        clienteDAO.listarClientes();
    }
    
}
