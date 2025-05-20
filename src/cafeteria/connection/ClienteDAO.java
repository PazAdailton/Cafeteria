package cafeteria.connection;

import java.util.List;
import cafeteria.model.Cliente;
import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {

	private Connection con = ConexaoDAO.getConnection();

	public Cliente buscarPorNome(String nome) {
		String sql = "SELECT * FROM cliente WHERE nome = ?";
		try (PreparedStatement preparador = con.prepareStatement(sql)) {
		preparador.setString(1, nome);
		ResultSet rs = preparador.executeQuery();

		if (rs.next()) {
		Cliente cliente = new Cliente();
		cliente.setId(rs.getLong("id"));
		cliente.setNome(rs.getString("nome"));
		return cliente;
		}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
