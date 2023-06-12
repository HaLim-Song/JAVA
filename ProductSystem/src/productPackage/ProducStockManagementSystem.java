package productPackage;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProducStockManagementSystem extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JTextField tfProductNumber;
    private JTextField tfProductName;
    private JTextField tfProductPrice;
    private JTextField tfProductStock;
    private JTextArea taResult;
    private Connection connection;

    private String url = "jdbc:mysql://localhost:3306/shl";
    private String username = "root";
    private String password = "1234";

    public ProducStockManagementSystem() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("��ǰ ��� ���� �ý���");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel inputPanel = new JPanel();
        panel.add(inputPanel, BorderLayout.NORTH);
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblProductNumber = new JLabel("��ǰ ��ȣ:");
        inputPanel.add(lblProductNumber);

        tfProductNumber = new JTextField();
        inputPanel.add(tfProductNumber);
        tfProductNumber.setColumns(10);

        JLabel lblProductName = new JLabel("��ǰ �̸�:");
        inputPanel.add(lblProductName);

        tfProductName = new JTextField();
        inputPanel.add(tfProductName);
        tfProductName.setColumns(10);

        JLabel lblProductPrice = new JLabel("��ǰ ����:");
        inputPanel.add(lblProductPrice);

        tfProductPrice = new JTextField();
        inputPanel.add(tfProductPrice);
        tfProductPrice.setColumns(10);

        JLabel lblProductStock = new JLabel("��ǰ ���:");
        inputPanel.add(lblProductStock);

        tfProductStock = new JTextField();
        inputPanel.add(tfProductStock);
        tfProductStock.setColumns(10);

        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JButton btnCreate = new JButton("���");
        buttonPanel.add(btnCreate);
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createProduct();
            }
        });

        JButton btnReadAll = new JButton("��ü ��ȸ");
        buttonPanel.add(btnReadAll);
        btnReadAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readAllProduct();
            }
        });

        JButton btnReadStock = new JButton("��� ��ȸ");
        buttonPanel.add(btnReadStock);
        btnReadStock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readProductStock();
            }
        });

        JButton btnUpdate = new JButton("����");
        buttonPanel.add(btnUpdate);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        JButton btnDelete = new JButton("����");
        buttonPanel.add(btnDelete);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        taResult = new JTextArea();
        scrollPane.setViewportView(taResult);
    }

    private void createProduct() {
        String productNumber = tfProductNumber.getText();
        String productName = tfProductName.getText();
        String productPrice = tfProductPrice.getText();
        String productStock = tfProductStock.getText();

        try {
            // �����ͺ��̽� ����
            connection = DriverManager.getConnection(url, username, password);

            // ���� �غ�
            String sql = "INSERT INTO prod (prod_number, prod_name, prod_price, prod_stock) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productNumber);
            statement.setString(2, productName);
            statement.setString(3, productPrice);
            statement.setString(4, productStock);

            // ���� ����
            int rowsInserted = statement.executeUpdate();

            // ��� ���
            if (rowsInserted > 0) {
                taResult.setText("��ǰ�� ��ϵǾ����ϴ�.\n");
            } else {
                taResult.setText("��ǰ ��� ����\n");
            }

            // ���� ����
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("��ǰ ��� �� ���� �߻�\n");
        }
    }

    private void readAllProduct() {
        try {
            // �����ͺ��̽� ����
            connection = DriverManager.getConnection(url, username, password);

            // ���� �غ�
            String sql = "SELECT * FROM prod";
            Statement statement = connection.createStatement();

            // ���� ����
            ResultSet resultSet = statement.executeQuery(sql);

            // ��� ���
            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String productNumber = resultSet.getString("prod_number");
                String productName = resultSet.getString("prod_name");
                String productPrice = resultSet.getString("prod_price");
                String productStock = resultSet.getString("prod_stock");

                sb.append("��ǰ ��ȣ: ").append(productNumber).append(", ");
                sb.append("��ǰ �̸�: ").append(productName).append(", ");
                sb.append("��ǰ ����: ").append(productPrice).append(", ");
                sb.append("��ǰ ���: ").append(productStock).append("\n");
            }

            if (sb.length() > 0) {
                taResult.setText(sb.toString());
            } else {
                taResult.setText("��ϵ� ��ǰ�� �����ϴ�.\n");
            }

            // ���� ����
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("��ü ��ȸ �� ���� �߻�\n");
        }
    }

    private void readProductStock() {
        String productNumber = tfProductNumber.getText();

        try {
            // �����ͺ��̽� ����
            connection = DriverManager.getConnection(url, username, password);

            // ���� �غ�
            String sql = "SELECT prod_stock FROM prod WHERE prod_number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productNumber);

            // ���� ����
            ResultSet resultSet = statement.executeQuery();

            // ��� ���
            if (resultSet.next()) {
                String productStock = resultSet.getString("prod_stock");
                taResult.setText("��ǰ ���: " + productStock + "\n");
            } else {
                taResult.setText("�ش� ��ǰ�� ã�� �� �����ϴ�.\n");
            }

            // ���� ����
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("��� ��ȸ �� ���� �߻�\n");
        }
    }

    private void updateProduct() {
    	String productNumber = tfProductNumber.getText();
        String productName = tfProductName.getText();
        String productPrice = tfProductPrice.getText();
        String productStock = tfProductStock.getText();

        try {
            // �����ͺ��̽� ����
            connection = DriverManager.getConnection(url, username, password);

            String sql = "UPDATE prod SET prod_name = ?, prod_price = ?, prod_stock = ? WHERE prod_number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productName);
            statement.setString(2, productPrice);
            statement.setString(3, productStock);
            statement.setString(4, productNumber);

            // ���� ����
            int rowsUpdated = statement.executeUpdate();

            // ��� ���
            if (rowsUpdated > 0) {
                taResult.setText("��ǰ ������ ������Ʈ�Ǿ����ϴ�.\n");
            } else {
                taResult.setText("��ǰ ������Ʈ ����\n");
            }

            // ���� ����
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("��ǰ ������Ʈ �� ���� �߻�\n");
        }
    }

    private void deleteProduct() {
        String productNumber = tfProductNumber.getText();

        try {
            // �����ͺ��̽� ����
            connection = DriverManager.getConnection(url, username, password);

            // ���� �غ�
            String sql = "DELETE FROM prod WHERE prod_number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productNumber);

            // ���� ����
            int rowsDeleted = statement.executeUpdate();

            // ��� ���
            if (rowsDeleted > 0) {
                taResult.setText("��ǰ�� �����Ǿ����ϴ�.\n");
            } else {
                taResult.setText("��ǰ ���� ����\n");
            }

            // ���� ����
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("��ǰ ���� �� ���� �߻�\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ProducStockManagementSystem frame = new ProducStockManagementSystem();
                frame.setVisible(true);
            }
        });
    }
}