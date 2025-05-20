package cafeteria.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//certo
import java.sql.Timestamp;

import cafeteria.model.ItemPedido;
import cafeteria.model.Pedido;

public class PedidoDAO {
		
	

   private Connection con = ConexaoDAO.getConnection();
   
 
   public void cadastrarPedidoEItens(Pedido pedido) {
   String sqlItemPedido = "insert into item_pedido (pedido_id, produto_id, quantidade, precoUnitario) values (?,?,?,?)";
   String sqlPedido = "insert into pedido (dataHoraPedido, cliente_id) values (?,?)";
   		
	try {
		PreparedStatement preparadorPedido = con.prepareStatement(sqlPedido,PreparedStatement.RETURN_GENERATED_KEYS);
		PreparedStatement preparadorItemPedido = con.prepareStatement(sqlItemPedido);
		
		Timestamp timesTamp = new Timestamp(pedido.getDataHoraPedido().getTimeInMillis());
		preparadorPedido.setTimestamp(1, timesTamp);
		
		if(pedido.getCliente() != null) {
			preparadorPedido.setLong(2, pedido.getCliente().getId());
		}else {
            preparadorPedido.setNull(2, java.sql.Types.BIGINT);
        }
		preparadorPedido.executeUpdate();
		ResultSet result = preparadorPedido.getGeneratedKeys();
		if(result.next()) {
			long idPedido = result.getLong(1);
			pedido.setId(idPedido);
			for(ItemPedido item: pedido.getItens()) {
				preparadorItemPedido.setLong(1, idPedido);
				preparadorItemPedido.setLong(2, item.getProduto().getId());
				preparadorItemPedido.setInt(3, item.getQuantidade());
				preparadorItemPedido.setBigDecimal(4, item.getPrecoUnitario());
				
				preparadorItemPedido.executeUpdate();
			}
            System.out.println("Pedido e itens inseridos com sucesso!");
		}
	
	} catch (Exception e) {
		e.printStackTrace();
        System.out.println("Erro ao cadastrar pedido com itens: " + e.getMessage());
	}
	    
	}
	
	        
 }
	
