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
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();

        try {
            PreparedStatement preparador = con.prepareStatement(sql);
            ResultSet resultado = preparador.executeQuery();

            while (resultado.next()) {
                Produto produto = new Produto();
                produto.setId(resultado.getLong("id"));
                produto.setNome(resultado.getString("nome"));
                produto.setPreco(resultado.getBigDecimal("preco"));
                produto.setQuanatidadeProduto(resultado.getInt("quantidade"));

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

    public void inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, preco, quantidade, categoria) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparador = con.prepareStatement(sql);
            preparador.setString(1, produto.getNome());
            preparador.setBigDecimal(2, produto.getPreco());
            preparador.setInt(3, produto.getQuanatidadeProduto());
            preparador.setString(4, produto.getCategoria().name());

            preparador.execute();
            //Captura exceção e armazena em "e" depois imprime no console
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Método atualizar atribuido ao botão de editar na CafeteriaAdmInterface
    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, preco = ?, quantidade = ?, categoria = ? WHERE id = ?";

        try {
            PreparedStatement preparador = con.prepareStatement(sql);
            preparador.setString(1, produto.getNome());
            preparador.setBigDecimal(2, produto.getPreco());
            preparador.setInt(3, produto.getQuanatidadeProduto());
            preparador.setString(4, produto.getCategoria().name());
            preparador.setLong(5, produto.getId());

            preparador.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar(long id) throws Exception {
        String verificarSQL = "SELECT COUNT(*) FROM itens_pedido WHERE produto_id = ?";
        String deletarSQL = "DELETE FROM produto WHERE id = ?";
        
        try (PreparedStatement psVerificar = con.prepareStatement(verificarSQL);
             PreparedStatement psDeletar = con.prepareStatement(deletarSQL)) {
            
            // Verifica se produto está em uso
            psVerificar.setLong(1, id);
            try (ResultSet rs = psVerificar.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    throw new Exception("Produto está em uso e não pode ser excluído.");
                }
            }
            
            // Produto não está em uso, pode deletar
            psDeletar.setLong(1, id);
            psDeletar.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // relança exceção para informar quem chamou
        }
    }

}
