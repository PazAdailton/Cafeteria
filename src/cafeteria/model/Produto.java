package cafeteria.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Produto{
	
	private long id;
	private String nome;
	private BigDecimal preco;
	private Categoria categoria;
	private int quanatidadeProduto;
	
	public Produto() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public int getQuanatidadeProduto() {
		return quanatidadeProduto;
	}

	public void setQuanatidadeProduto(int quanatidadeProduto) {
		this.quanatidadeProduto = quanatidadeProduto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return id == other.id;
	}

	 @Override
	    public String toString() {
	        return getNome() + " - R$ " + getPreco(); // Exibe nome e preço formatado
	    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
}
