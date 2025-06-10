package cafeteria.model;

import java.math.BigDecimal;

public class Produto {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private Integer quantidade;
    private Categoria categoria;


    // Construtor
    public Produto() {}

    // Getters e Setters existentes
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    // Método setquantidade (novo)
    public void setQuantidade(Integer quantidade) {
        if (quantidade != null && quantidade >= 0) {
            this.quantidade = quantidade;
        } else {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
    }

    public Integer geQuantidade() {
        return quantidade;
    }

    // Método setCategoria (novo)
    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", nome=" + nome + ", preco=" + preco + 
               ", quantidade=" + quantidade + ", categoria=" + categoria + "]";
    }
}