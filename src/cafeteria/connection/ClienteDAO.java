package cafeteria.connection;

import java.util.List;

import cafeteria.model.Cliente;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {

	private Connection con = ConexaoDAO.getConnection();


	public List<Cliente> buscarPorNome(String nome) {
	    String sql = "SELECT id, nome FROM cliente WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";
	    List<Cliente> clientes = new ArrayList<>();
	    
	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, "%" + nome + "%"); // Busca parcial
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Cliente cliente = new Cliente();
	                cliente.setId(rs.getLong("id"));
	                cliente.setNome(rs.getString("nome"));
	                clientes.add(cliente);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Erro ao buscar cliente por nome: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return clientes;
	}
	

	public void salvar(Cliente cliente) {
		String sql = "INSERT INTO cliente (nome) VALUES (?)";
		try (PreparedStatement preparador = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
		preparador.setString(1, cliente.getNome());
		preparador.executeUpdate();

		ResultSet rs = preparador.getGeneratedKeys();
		if (rs.next()) {
			cliente.setId(rs.getLong(1));
		}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Cliente> buscarTodos() {
		List<Cliente> lista = new ArrayList<>();
		String sql = "SELECT * FROM cliente";
		try (Statement preparador = con.createStatement()) {
		ResultSet rs = preparador.executeQuery(sql);
		while (rs.next()) {
		Cliente cliente = new Cliente();
		cliente.setId(rs.getLong("id"));
		cliente.setNome(rs.getString("nome"));
		lista.add(cliente);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}
}
