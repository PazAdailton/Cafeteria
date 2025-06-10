package cafeteria.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cafeteria.model.Categoria;
import cafeteria.model.Produto;

public class ProdutoDAO {
    private Connection con = ConexaoDAO.getConnection();

    public List<Produto> buscarTodos() {
        String sql = "SELECT id, nome, preco, quantidade, categoria FROM produto";
        List<Produto> produtos = new ArrayList<>();
        
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setQuantidade(rs.getInt("quantidade"));
                
                try {
                    produto.setCategoria(Categoria.valueOf(rs.getString("categoria")));
                } catch (IllegalArgumentException e) {
                    produto.setCategoria(Categoria.OUTROS);
                }
                
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtos;
    }
    
    public void atualizarEstoque(Long produtoId, int quantidade) {
        String sql = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setLong(2, produtoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
        }
    }
}