package cafeteria.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cafeteria.model.Categoria;
import cafeteria.model.Produto;

public class ProdutoDAO {
		
	   private Connection con = ConexaoDAO.getConnection();
		
	   public List<Produto> buscarTodos() {
	   String sql = "select * from produto";
	   List<Produto> produtos = new ArrayList<Produto>();
	   try {
		
		PreparedStatement preparador = con.prepareStatement(sql);
		ResultSet resultado = preparador.executeQuery();
		while(resultado.next()) {
		Produto produto = new Produto();
		produto.setId(resultado.getLong("id"));
		produto.setNome(resultado.getString("nome"));
		produto.setPreco(resultado.getBigDecimal("preco"));
		produto.setQuanatidadeProduto(resultado.getInt("quantidadeProduto"));
		
		String categoriaSTR = resultado.getString("categoria");
		Categoria categoria = Categoria.valueOf(categoriaSTR);
		produto.setCategoria(categoria);
		produtos.add(produto);
		}
	   	} catch (Exception e) {
		e.printStackTrace();
	   	}	
	   	return produtos;
		}
}
