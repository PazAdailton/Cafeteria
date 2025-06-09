package cafeteria.model;

import java.util.Calendar;
import java.util.List;

public class Pedido {
	
	private long id;
	private Calendar dataHoraPedido;
	private Cliente cliente;
	private List<ItemPedido> itens;
	private boolean entregue;
	private boolean cancelado;
	
	
	public Pedido() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getDataHoraPedido() {
		return dataHoraPedido;
	}

	public void setDataHoraPedido(Calendar dataHoraPedido) {
		this.dataHoraPedido = dataHoraPedido;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

	public boolean isEntregue() {
		return entregue;
	}

	public void setEntregue(boolean entregue) {
		this.entregue = entregue;
	}

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}
	
	
	
}
