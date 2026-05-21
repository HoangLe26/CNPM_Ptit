package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // Thông tin kết nối database
    static String url      = "jdbc:mysql://localhost:3306/cnpm_chess";
    static String user     = "root";
    static String password = "2609";

    // Hàm lấy kết nối đến database
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
}
