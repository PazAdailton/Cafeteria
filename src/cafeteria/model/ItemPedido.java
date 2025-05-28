package cafeteria.model;

import java.math.BigDecimal;

public class ItemPedido {
    private Long id;               // ID do item_pedido (pode ser null para novos itens)
    private Pedido pedido;         // Referência ao pedido (opcional, dependendo da necessidade)
    private Produto produto;       // Produto associado
    private Integer quantidade;    // Usando Integer para permitir null
    private BigDecimal precoUnitario; // Preço no momento da venda
    private Boolean cancelado;     // Status do item (opcional)

    public ItemPedido() {
        this.quantidade = 1;       // Valor padrão
        this.cancelado = false;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        if (quantidade != null && quantidade > 0) {
            this.quantidade = quantidade;
        }
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        if (precoUnitario != null && precoUnitario.compareTo(BigDecimal.ZERO) >= 0) {
            this.precoUnitario = precoUnitario;
        }
    }

    public Boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    // Método utilitário para calcular o total do item
    public BigDecimal getTotalItem() {
        if (precoUnitario == null || quantidade == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }

    @Override
    public String toString() {
        return produto.getNome() + " x" + quantidade + " (R$ " + precoUnitario + ")";
    }
}