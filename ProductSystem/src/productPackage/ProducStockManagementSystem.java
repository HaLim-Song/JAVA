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
        setTitle("물품 재고 관리 시스템");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel inputPanel = new JPanel();
        panel.add(inputPanel, BorderLayout.NORTH);
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblProductNumber = new JLabel("물품 번호:");
        inputPanel.add(lblProductNumber);

        tfProductNumber = new JTextField();
        inputPanel.add(tfProductNumber);
        tfProductNumber.setColumns(10);

        JLabel lblProductName = new JLabel("물품 이름:");
        inputPanel.add(lblProductName);

        tfProductName = new JTextField();
        inputPanel.add(tfProductName);
        tfProductName.setColumns(10);

        JLabel lblProductPrice = new JLabel("물품 가격:");
        inputPanel.add(lblProductPrice);

        tfProductPrice = new JTextField();
        inputPanel.add(tfProductPrice);
        tfProductPrice.setColumns(10);

        JLabel lblProductStock = new JLabel("물품 재고:");
        inputPanel.add(lblProductStock);

        tfProductStock = new JTextField();
        inputPanel.add(tfProductStock);
        tfProductStock.setColumns(10);

        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JButton btnCreate = new JButton("등록");
        buttonPanel.add(btnCreate);
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createProduct();
            }
        });

        JButton btnReadAll = new JButton("전체 조회");
        buttonPanel.add(btnReadAll);
        btnReadAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readAllProduct();
            }
        });

        JButton btnReadStock = new JButton("재고 조회");
        buttonPanel.add(btnReadStock);
        btnReadStock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readProductStock();
            }
        });

        JButton btnUpdate = new JButton("수정");
        buttonPanel.add(btnUpdate);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        JButton btnDelete = new JButton("삭제");
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
            // 데이터베이스 연결
            connection = DriverManager.getConnection(url, username, password);

            // 쿼리 준비
            String sql = "INSERT INTO prod (prod_number, prod_name, prod_price, prod_stock) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productNumber);
            statement.setString(2, productName);
            statement.setString(3, productPrice);
            statement.setString(4, productStock);

            // 쿼리 실행
            int rowsInserted = statement.executeUpdate();

            // 결과 출력
            if (rowsInserted > 0) {
                taResult.setText("물품이 등록되었습니다.\n");
            } else {
                taResult.setText("물품 등록 실패\n");
            }

            // 연결 해제
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("물품 등록 중 오류 발생\n");
        }
    }

    private void readAllProduct() {
        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection(url, username, password);

            // 쿼리 준비
            String sql = "SELECT * FROM prod";
            Statement statement = connection.createStatement();

            // 쿼리 실행
            ResultSet resultSet = statement.executeQuery(sql);

            // 결과 출력
            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String productNumber = resultSet.getString("prod_number");
                String productName = resultSet.getString("prod_name");
                String productPrice = resultSet.getString("prod_price");
                String productStock = resultSet.getString("prod_stock");

                sb.append("물품 번호: ").append(productNumber).append(", ");
                sb.append("물품 이름: ").append(productName).append(", ");
                sb.append("물품 가격: ").append(productPrice).append(", ");
                sb.append("물품 재고: ").append(productStock).append("\n");
            }

            if (sb.length() > 0) {
                taResult.setText(sb.toString());
            } else {
                taResult.setText("등록된 물품이 없습니다.\n");
            }

            // 연결 해제
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("전체 조회 중 오류 발생\n");
        }
    }

    private void readProductStock() {
        String productNumber = tfProductNumber.getText();

        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection(url, username, password);

            // 쿼리 준비
            String sql = "SELECT prod_stock FROM prod WHERE prod_number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productNumber);

            // 쿼리 실행
            ResultSet resultSet = statement.executeQuery();

            // 결과 출력
            if (resultSet.next()) {
                String productStock = resultSet.getString("prod_stock");
                taResult.setText("물품 재고: " + productStock + "\n");
            } else {
                taResult.setText("해당 물품을 찾을 수 없습니다.\n");
            }

            // 연결 해제
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("재고 조회 중 오류 발생\n");
        }
    }

    private void updateProduct() {
    	String productNumber = tfProductNumber.getText();
        String productName = tfProductName.getText();
        String productPrice = tfProductPrice.getText();
        String productStock = tfProductStock.getText();

        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection(url, username, password);

            String sql = "UPDATE prod SET prod_name = ?, prod_price = ?, prod_stock = ? WHERE prod_number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productName);
            statement.setString(2, productPrice);
            statement.setString(3, productStock);
            statement.setString(4, productNumber);

            // 쿼리 실행
            int rowsUpdated = statement.executeUpdate();

            // 결과 출력
            if (rowsUpdated > 0) {
                taResult.setText("물품 정보가 업데이트되었습니다.\n");
            } else {
                taResult.setText("물품 업데이트 실패\n");
            }

            // 연결 해제
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("물품 업데이트 중 오류 발생\n");
        }
    }

    private void deleteProduct() {
        String productNumber = tfProductNumber.getText();

        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection(url, username, password);

            // 쿼리 준비
            String sql = "DELETE FROM prod WHERE prod_number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productNumber);

            // 쿼리 실행
            int rowsDeleted = statement.executeUpdate();

            // 결과 출력
            if (rowsDeleted > 0) {
                taResult.setText("물품이 삭제되었습니다.\n");
            } else {
                taResult.setText("물품 삭제 실패\n");
            }

            // 연결 해제
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            taResult.setText("물품 삭제 중 오류 발생\n");
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