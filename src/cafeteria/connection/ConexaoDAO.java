package cafeteria.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConexaoDAO {
	
	 public static String driver = "com.mysql.cj.jdbc.Driver";

	 final static String dbName = "cafeteria";
	 final static String url = "" + dbName;
	 final static String login = "";
	 final static String senha = "";

	
	public ConexaoDAO(){
		
	}
	
	public static Connection getConnection() {
		Connection connection = null;
	try {
		Class.forName(driver);
		connection = DriverManager.getConnection(url, login, senha);
		JOptionPane.showMessageDialog(null, "Success Connetion");
	} catch (ClassNotFoundException erro) {
		JOptionPane.showMessageDialog(null, "Driver não encontrado" + 
		erro.toString());
	} catch (SQLException e) {
		JOptionPane.showMessageDialog(null, "Problema na conexão coma  fonte de dados" +
	    e.toString());
	}
		return connection;
 }
}
