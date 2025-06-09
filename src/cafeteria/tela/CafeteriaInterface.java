package cafeteria.tela;

import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

import cafeteria.connection.ClienteDAO;
import cafeteria.connection.PedidoDAO;
import cafeteria.connection.ProdutoDAO;
import cafeteria.model.*;

public class CafeteriaInterface extends JPanel {
    private JButton btRemover, btAdicionar, btFinalizarPedido;
    private JTable table;
    private JLabel lbNumero, lbTotal, lbProduto, lbPrecoUnitario, lbQuantidade, lbCliente;
    private JTextField tfNumero, tfTotal, tfPrecoUnitario, tfQuantidade, tfCliente;
    private JComboBox<Produto> cbProdutos;
    private DecimalFormat df = new DecimalFormat("#,###.00");
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private List<ItemPedido> itensPedido = new ArrayList<>();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
   
    
    public CafeteriaInterface() {
    	inicializarComponentes();
        definirEventos();
	}
    
    private void inicializarComponentes() {
        setLayout(null);
        setPreferredSize(new Dimension(600, 500));
         
        // Componentes
        lbCliente = new JLabel("Cliente:");
        lbProduto = new JLabel("Produto:");
        lbQuantidade = new JLabel("Quantidade:");
        lbPrecoUnitario = new JLabel("Preço Unitário:");
        lbNumero = new JLabel("Nº Pedido:");
        lbTotal = new JLabel("Total do Pedido:");
        
        tfCliente = new JTextField();
        tfNumero = new JTextField();
        tfNumero.setEditable(false);
        tfQuantidade = new JTextField("1");
        tfPrecoUnitario = new JTextField();
        tfPrecoUnitario.setEditable(false);
        tfTotal = new JTextField();
        tfTotal.setEditable(false);
        tfTotal.setHorizontalAlignment(JTextField.RIGHT);
        
        cbProdutos = new JComboBox<>();
        carregarProdutos();
        cbProdutos.addActionListener(e -> atualizarPrecoUnitario());
        
        btAdicionar = new JButton("Adicionar");
        btRemover = new JButton("Remover");
        btFinalizarPedido = new JButton("Finalizar Pedido");
        
        // Tabela
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Produto", "Quantidade", "Preço Unitário", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        configurarTabela();
        JScrollPane scrollTable = new JScrollPane(table);
        
        // Posicionamento com setBounds()
        lbCliente.setBounds(20, 20, 80, 25);
        tfCliente.setBounds(100, 20, 200, 25);
        lbNumero.setBounds(320, 20, 80, 25);
        tfNumero.setBounds(400, 20, 80, 25);
        
        lbProduto.setBounds(20, 60, 80, 25);
        cbProdutos.setBounds(100, 60, 200, 25);
        lbQuantidade.setBounds(320, 60, 80, 25);
        tfQuantidade.setBounds(400, 60, 80, 25);
        
        lbPrecoUnitario.setBounds(20, 100, 80, 25);
        tfPrecoUnitario.setBounds(100, 100, 80, 25);
        btAdicionar.setBounds(200, 100, 100, 25);
        btRemover.setBounds(310, 100, 100, 25);
        
        scrollTable.setBounds(20, 140, 560, 250);
        
        lbTotal.setBounds(350, 400, 100, 25);
        tfTotal.setBounds(450, 400, 120, 25);
        btFinalizarPedido.setBounds(20, 400, 150, 30);
        
        // Adicionando componentes
        add(lbCliente);
        add(tfCliente);
        add(lbNumero);
        add(tfNumero);
        add(lbProduto);
        add(cbProdutos);
        add(lbQuantidade);
        add(tfQuantidade);
        add(lbPrecoUnitario);
        add(tfPrecoUnitario);
        add(btAdicionar);
        add(btRemover);
        add(scrollTable);
        add(lbTotal);
        add(tfTotal);
        add(btFinalizarPedido);
        
        
    }
    
    private void configurarTabela() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
    }
    
    @SuppressWarnings("serial")
	private void carregarProdutos() {
        List<Produto> produtos = produtoDAO.buscarTodos();
        DefaultComboBoxModel<Produto> model = new DefaultComboBoxModel<>();
        
        for (Produto produto : produtos) {
            model.addElement(produto);
        }
        
        cbProdutos.setModel(model);
        cbProdutos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto) {
                    Produto p = (Produto) value;
                    setText(p.getNome() + " - R$ " + df.format(p.getPreco()));
                }
                return this;
            }
        });
    }
    
    private void atualizarPrecoUnitario() {
        Produto produto = (Produto) cbProdutos.getSelectedItem();
        if (produto != null) {
            tfPrecoUnitario.setText(df.format(produto.getPreco()));
        }
    }
    
    private void definirEventos() {
        btAdicionar.addActionListener(e -> adicionarItem());
        btRemover.addActionListener(e -> removerItem());
        btFinalizarPedido.addActionListener(e -> finalizarPedido());
    }
    
    private void adicionarItem() {
        try {
            if (cbProdutos.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione um produto!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String textoQuantidade = tfQuantidade.getText().trim();
            if (textoQuantidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a quantidade!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int quantidade = Integer.parseInt(textoQuantidade);

            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Produto produto = (Produto) cbProdutos.getSelectedItem();

            if (quantidade > produto.getQuanatidadeProduto()) {
                JOptionPane.showMessageDialog(this, 
                    "Quantidade indisponível em estoque!\nEstoque atual: " + produto.getQuanatidadeProduto(), 
                    "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            BigDecimal precoUnitario = produto.getPreco();
            BigDecimal totalItem = precoUnitario.multiply(new BigDecimal(quantidade));

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.addRow(new Object[]{
                produto.getNome(),
                quantidade,
                df.format(precoUnitario),
                df.format(totalItem)
            });

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(precoUnitario);
            itensPedido.add(item);

            calcularTotal();
            tfQuantidade.setText("1");
            cbProdutos.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade deve ser um número inteiro válido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void removerItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(selectedRow);
        itensPedido.remove(selectedRow);
        calcularTotal();
    }
    
    private void calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : itensPedido) {
            total = total.add(item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())));
        }
        tfTotal.setText(df.format(total));
    }
    
    private void finalizarPedido() {
        if (itensPedido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione itens ao pedido antes de finalizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeCliente = tfCliente.getText().trim();
        if (nomeCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            tfCliente.requestFocus();
            return;
        }

        Cliente cliente = clienteDAO.buscarPorNome(nomeCliente);
        if (cliente == null) {
            cliente = new Cliente();
            cliente.setNome(nomeCliente);
            clienteDAO.salvar(cliente);
        }
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataHoraPedido(Calendar.getInstance());
        pedido.setItens(itensPedido);
        pedidoDAO.cadastrarPedidoEItens(pedido);
        
        String mensagem = "Pedido finalizado com sucesso!\n\n";
        mensagem += "Cliente: " + cliente.getNome() + "\n";
        mensagem += "Total: R$ " + tfTotal.getText() + "\n";
        mensagem += "Itens: " + itensPedido.size();
        tfNumero.setText(String.valueOf(pedido.getId()));
        JOptionPane.showMessageDialog(this, mensagem, "Pedido Finalizado", JOptionPane.INFORMATION_MESSAGE);
        limparFormulario();
    }

    
    private void limparFormulario() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        itensPedido.clear();
        tfCliente.setText("");
        tfTotal.setText("");
        tfNumero.setText("");
        tfQuantidade.setText("1");
        cbProdutos.setSelectedIndex(0);
        cbProdutos.requestFocus();
    }
}