package cafeteria.tela;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import cafeteria.connection.ConexaoDAO;
import cafeteria.connection.ProdutoDAO;
import cafeteria.model.Categoria;
import cafeteria.model.Produto;

public class CafeteriaAdmInterface {

    private JFrame frame;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JButton btnAdicionar;
    private JButton btnEditar;
//    private JButton btnExcluir;

   

    public CafeteriaAdmInterface() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Sistema Administrativo - Cafeteria");
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel dos botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        btnEditar = new JButton("Editar Cardápio");
        btnEditar.setBackground(new Color(70, 130, 180));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(btnEditar);

        btnAdicionar = new JButton("Adicionar ao Cardápio");
        btnAdicionar.setBackground(new Color(34, 139, 34));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(btnAdicionar);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Tabela de produtos
        String[] colunas = {"ID", "Nome", "Categoria", "Preço", "Quantidade"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaProdutos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        frame.getContentPane().add(mainPanel);

        carregarProdutos();
        definirEventos();
    }

    private void definirEventos() {
        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnEditar.addActionListener(e -> editarProduto());
//        btnExcluir.addActionListener(e -> excluirProduto());
    }

    private void adicionarProduto() {
        try {
            String nome = JOptionPane.showInputDialog("Nome do produto:");
            String precoStr = JOptionPane.showInputDialog("Preço:");
            String quantidadeStr = JOptionPane.showInputDialog("Quantidade:");
            String categoriaStr = JOptionPane.showInputDialog("Categoria (COMIDAS, BEBIDAS, OUTROS):");

            Produto p = new Produto();
            p.setNome(nome);
            p.setPreco(new BigDecimal(precoStr));
            p.setQuanatidadeProduto(Integer.parseInt(quantidadeStr));
            p.setCategoria(Categoria.valueOf(categoriaStr.toUpperCase()));

            new ProdutoDAO().inserir(p);
            carregarProdutos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao adicionar produto: " + ex.getMessage());
        }
    }

    private void editarProduto() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um produto para editar.");
            return;
        }

        Long id = Long.valueOf(modeloTabela.getValueAt(linha, 0).toString());

        try {
            String nome = JOptionPane.showInputDialog("Novo nome:");
            String precoStr = JOptionPane.showInputDialog("Novo preço:");
            String quantidadeStr = JOptionPane.showInputDialog("Nova quantidade:");
            String categoriaStr = JOptionPane.showInputDialog("Nova categoria:");

            Produto p = new Produto();
            p.setId(id);
            p.setNome(nome);
            p.setPreco(new BigDecimal(precoStr));
            p.setQuanatidadeProduto(Integer.parseInt(quantidadeStr));
            p.setCategoria(Categoria.valueOf(categoriaStr.toUpperCase()));

            new ProdutoDAO().atualizar(p);
            carregarProdutos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao editar produto: " + ex.getMessage());
        }
    }

//    private void excluirProduto() {
//        int linha = tabelaProdutos.getSelectedRow();
//        if (linha == -1) {
//            JOptionPane.showMessageDialog(frame, "Selecione um produto para excluir.");
//            return;
//        }
//
//        Long id = Long.valueOf(modeloTabela.getValueAt(linha, 0).toString());
//
//        int opcao = JOptionPane.showConfirmDialog(frame, "Confirma exclusão do produto?");
//        if (opcao == JOptionPane.YES_OPTION) {
//            try {
//                new ProdutoDAO().deletar(id);
//                carregarProdutos();
//            } catch (Exception ex) {
//                // Mostra a mensagem da exceção (ex: "Produto está em uso e não pode ser excluído.")
//                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }


    private void carregarProdutos() {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.buscarTodos();

        modeloTabela.setRowCount(0); // Limpa a tabela antes de carregar

        for (Produto p : produtos) {
            Object[] linha = {
                p.getId(),
                p.getNome(),
                p.getCategoria(),
                p.getPreco(),
                p.getQuanatidadeProduto()
            };
            modeloTabela.addRow(linha);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CafeteriaAdmInterface window = new CafeteriaAdmInterface();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
