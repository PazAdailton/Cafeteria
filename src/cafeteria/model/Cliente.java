package cafeteria.model;

public class Cliente extends Pessoa {
	
	
	
	private long id;
	private String email;
	
	public Cliente() {
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}
