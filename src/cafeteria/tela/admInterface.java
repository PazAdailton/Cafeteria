package cafeteria.tela;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.*;
import java.util.*;
import cafeteria.connection.PedidoDAO;
import cafeteria.model.*;

public class admInterface extends JFrame {
    private JTable tabelaPedidos;
    private JLabel lbTotalFaturado;
    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private PedidoDAO pedidoDAO = new PedidoDAO();
    
    public admInterface() {
        super("Painel Administrativo");
        configurarJanela();
        inicializarComponentes(); // Inicializa primeiro
        carregarPedidos();        // Depois carrega os dados
        atualizarTotalFaturado();
        setVisible(true);
    }
    
    private void configurarJanela() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JLabel titulo = new JLabel("PAINEL ADMINISTRATIVO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);
    }
    
    private void inicializarComponentes() {
        // 1. Primeiro cria o modelo
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Nº Pedido", "Cliente", "Data/Hora", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // 2. Depois cria a tabela com o modelo
        tabelaPedidos = new JTable(model);
        
        // 3. Configura a tabela
        configurarTabela();
        
        // 4. Adiciona a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabelaPedidos);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Painel do total faturado
        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lbTotalFaturado = new JLabel("Total Faturado: R$ 0,00");
        lbTotalFaturado.setFont(new Font("Arial", Font.BOLD, 16));
        painelTotal.add(lbTotalFaturado);
        
        // Layout principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.NORTH);
        painelPrincipal.add(painelTotal, BorderLayout.SOUTH);
        
        add(painelPrincipal, BorderLayout.CENTER);
    }
    
    private void configurarTabela() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Configura as colunas (agora com 5 colunas incluindo status)
        tabelaPedidos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabelaPedidos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tabelaPedidos.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tabelaPedidos.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        tabelaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaPedidos.getTableHeader().setReorderingAllowed(false);
    }
    
    private void carregarPedidos() {
        DefaultTableModel model = (DefaultTableModel) tabelaPedidos.getModel();
        model.setRowCount(0); // Limpa a tabela
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        ArrayList<Pedido> pedidos = (ArrayList<Pedido>) pedidoDAO.listarTodosPedidos();
        
        for (Pedido pedido : pedidos) {
            String status = pedido.isCancelado() ? "Cancelado" : 
                          pedido.isEntregue() ? "Entregue" : "Pendente";
            
            model.addRow(new Object[]{
                pedido.getId(),
                pedido.getCliente() != null ? pedido.getCliente().getNome() : "Sem cliente",
                sdf.format(pedido.getDataHoraPedido().getTime()),
                df.format(pedido.getTotal()),
                status
            });
        }
    }
    
    private void atualizarTotalFaturado() {
        BigDecimal total = pedidoDAO.calcularTotalFaturado();
        lbTotalFaturado.setText("Total Faturado: R$ " + df.format(total));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new admInterface());
    }
}