package cafeteria.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public class Pedido {
    private Long id;
    private Calendar dataHoraPedido;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private boolean cancelado;
    private boolean entregue;
    
    // Getters e setters

    
    public Pedido() {
        this.cancelado = false;
        this.entregue = false;
    }

    // Getters e Setters existentes
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

    // Novos métodos para status do pedido
    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    // Método utilitário para calcular o total do pedido
    public BigDecimal getTotal() {
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            total = total.add(item.getPrecoUnitario()
                              .multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        return total;
    }

    // Método para verificar se o pedido está pendente
    public boolean isPendente() {
        return !cancelado && !entregue;
    }

    @Override
    public String toString() {
        return "Pedido [id=" + id + ", dataHoraPedido=" + dataHoraPedido.getTime() + 
               ", cliente=" + (cliente != null ? cliente.getNome() : "Sem cliente") + 
               ", total=" + getTotal() + ", status=" + getStatus() + "]";
    }

    // Método para obter o status como String
    public String getStatus() {
        if (cancelado) {
            return "Cancelado";
        } else if (entregue) {
            return "Entregue";
        } else {
            return "Pendente";
        }
    }
}