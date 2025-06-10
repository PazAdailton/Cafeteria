package cafeteria.connection;


import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import cafeteria.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//certo
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import cafeteria.model.ItemPedido;
import cafeteria.model.Pedido;


public class PedidoDAO {
    private Connection con = ConexaoDAO.getConnection();

    // --- Métodos de Consulta ---
    public List<Pedido> listarTodosPedidos() {
        String sql = "SELECT p.codigo, p.dataHoraPedido, p.cliente_id, " +
                    "c.nome as cliente_nome, p.cancelado, p.entregue " +
                    "FROM pedido p LEFT JOIN cliente c ON p.cliente_id = c.id " +
                    "ORDER BY p.dataHoraPedido DESC";
        
        List<Pedido> pedidos = new ArrayList<>();
        
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getLong("codigo"));
                
                Calendar data = Calendar.getInstance();
                data.setTime(rs.getTimestamp("dataHoraPedido"));
                pedido.setDataHoraPedido(data);
                
                if (rs.getObject("cliente_id") != null) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getLong("cliente_id"));
                    cliente.setNome(rs.getString("cliente_nome"));
                    pedido.setCliente(cliente);  // Use setCliente em vez de setPessoa
                }
                
                pedido.setCancelado(rs.getBoolean("cancelado"));
                pedido.setEntregue(rs.getBoolean("entregue"));
                pedido.setItens(listarItensPedido(pedido.getId()));
                
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    private List<ItemPedido> listarItensPedido(long pedidoId) {
        String sql = "SELECT ip.id, ip.produto_id, ip.quantidade, ip.precoUnitario, " +
                    "pr.nome as produto_nome, pr.preco as produto_preco, " +
                    "pr.categoria, pr.quantidade as produto_estoque " +
                    "FROM item_pedido ip JOIN produto pr ON ip.produto_id = pr.id " +
                    "WHERE ip.pedido_id = ?";
        
        List<ItemPedido> itens = new ArrayList<>();
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, pedidoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setId(rs.getLong("id"));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setPrecoUnitario(rs.getBigDecimal("precoUnitario"));
                    
                    Produto produto = new Produto();
                    produto.setId(rs.getLong("produto_id"));
                    produto.setNome(rs.getString("produto_nome"));
                    produto.setPreco(rs.getBigDecimal("produto_preco"));
                    produto.setCategoria(Categoria.valueOf(rs.getString("categoria")));
                    produto.setQuantidade(rs.getInt("produto_estoque"));
                    
                    item.setProduto(produto);
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar itens do pedido: " + e.getMessage());
        }
        return itens;
    }

    public void cadastrarPedidoEItens(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (dataHoraPedido, cliente_id, cancelado, entregue) VALUES (?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, precoUnitario) VALUES (?, ?, ?, ?)";
        
        try {
            con.setAutoCommit(false);
            
            // Cadastra pedido principal
            try (PreparedStatement stmtPedido = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setTimestamp(1, new Timestamp(pedido.getDataHoraPedido().getTimeInMillis()));
                
                if (pedido.getCliente() != null) {
                    stmtPedido.setLong(2, pedido.getCliente().getId());
                } else {
                    stmtPedido.setNull(2, Types.BIGINT);
                }
                
                stmtPedido.setBoolean(3, pedido.isCancelado());
                stmtPedido.setBoolean(4, pedido.isEntregue());
                stmtPedido.executeUpdate();
                
                try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getLong(1));
                    }
                }
            }
            
            // Cadastra itens
            try (PreparedStatement stmtItem = con.prepareStatement(sqlItem)) {
                for (ItemPedido item : pedido.getItens()) {
                    stmtItem.setLong(1, pedido.getId());
                    stmtItem.setLong(2, item.getProduto().getId());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setBigDecimal(4, item.getPrecoUnitario());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }
            
            con.commit();
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {}
            throw new RuntimeException("Erro ao cadastrar pedido", e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {}
        }
    }
    
    public Pedido buscarPedidoPorId(long idPedido) {
        String sqlPedido = "SELECT p.codigo, p.dataHoraPedido, p.cliente_id, " +
                          "c.nome as cliente_nome, p.cancelado, p.entregue " +
                          "FROM pedido p LEFT JOIN cliente c ON p.cliente_id = c.id " +
                          "WHERE p.codigo = ?";
        String sqlItens = "SELECT ip.id, ip.produto_id, ip.quantidade, ip.precoUnitario, " +
                         "p.nome as produto_nome FROM item_pedido ip " +
                         "JOIN produto p ON ip.produto_id = p.id WHERE ip.pedido_id = ?";
        
        Pedido pedido = null;
        
        try {
            // Busca os dados básicos do pedido
            try (PreparedStatement stmtPedido = con.prepareStatement(sqlPedido)) {
                stmtPedido.setLong(1, idPedido);
                
                try (ResultSet rsPedido = stmtPedido.executeQuery()) {
                    if (rsPedido.next()) {
                        pedido = new Pedido();
                        pedido.setId(rsPedido.getLong("codigo"));
                        
                        Calendar data = Calendar.getInstance();
                        data.setTime(rsPedido.getTimestamp("dataHoraPedido"));
                        pedido.setDataHoraPedido(data);
                        
                        pedido.setCancelado(rsPedido.getBoolean("cancelado"));
                        pedido.setEntregue(rsPedido.getBoolean("entregue"));
                        
                        if (rsPedido.getObject("cliente_id") != null) {
                            Pessoa pessoa = new Pessoa();
                            pessoa.setId(rsPedido.getLong("cliente_id"));
                            pessoa.setNome(rsPedido.getString("cliente_nome"));
                            pedido.setPessoa(pessoa);
                        }
                    }
                }
            }
            
            // Busca os itens do pedido
            if (pedido != null) {
                try (PreparedStatement stmtItens = con.prepareStatement(sqlItens)) {
                    stmtItens.setLong(1, idPedido);
                    
                    try (ResultSet rsItens = stmtItens.executeQuery()) {
                        List<ItemPedido> itens = new ArrayList<>();
                        
                        while (rsItens.next()) {
                            ItemPedido item = new ItemPedido();
                            item.setId(rsItens.getLong("id"));
                            item.setQuantidade(rsItens.getInt("quantidade"));
                            item.setPrecoUnitario(rsItens.getBigDecimal("precoUnitario"));
                            
                            Produto produto = new Produto();
                            produto.setId(rsItens.getLong("produto_id"));
                            produto.setNome(rsItens.getString("produto_nome"));
                            item.setProduto(produto);
                            
                            itens.add(item);
                        }
                        pedido.setItens(itens);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pedido por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return pedido;
    }

    // --- Métodos de Atualização ---
    public boolean marcarComoEntregue(long idPedido) {
        String sql = "UPDATE pedido SET entregue = true, cancelado = false WHERE codigo = ? AND entregue = false";
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, idPedido);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pedido #" + idPedido + " marcado como entregue com sucesso!");
                return true;
            } else {
                System.out.println("Pedido não encontrado ou já estava entregue/cancelado");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao marcar pedido como entregue: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean cancelarPedido(long idPedido) {
        String sql = "UPDATE pedido SET cancelado = true, entregue = false WHERE codigo = ? AND cancelado = false AND entregue = false";
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, idPedido);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pedido #" + idPedido + " cancelado com sucesso!");
                return true;
            } else {
                System.out.println("Pedido não encontrado ou já estava cancelado/entregue");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cancelar pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public BigDecimal calcularTotalFaturado() {
    	 String sql = "SELECT COALESCE(SUM(ip.quantidade * ip.precoUnitario), 0) as total " +
                 "FROM item_pedido ip " +
                 "WHERE EXISTS (SELECT 1 FROM pedido p WHERE p.codigo = ip.pedido_id AND p.entregue = true)";
        
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular total faturado: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    


		
	
	
	    
	}
	
  
   
}
	        
 
	

