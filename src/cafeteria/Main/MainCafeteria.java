package cafeteria.Main;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cafeteria.tela.CafeteriaInterface;




public class MainCafeteria {

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            // Configuração da janela principal
	            JFrame frame = new JFrame("Sistema de Cafeteria");
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            
	            // Cria o painel de pedidos
	            CafeteriaInterface painelPedido = new CafeteriaInterface();
	            
	            // Adiciona o painel ao frame
	            frame.getContentPane().add(painelPedido);
	            
	            // Configura o tamanho da janela
	            frame.setPreferredSize(new Dimension(800, 600));
	            frame.pack();
	            
	            // Centraliza a janela
	            frame.setLocationRelativeTo(null);
	            
	            // Torna a janela visível
	            frame.setVisible(true);
	        });
	    
	}
}
