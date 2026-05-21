package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection – Lớp tiện ích quản lý kết nối CSDL MySQL.
 *
 * Thông tin kết nối:
 *   - Host    : localhost:3306
 *   - Database: cnpm_chess
 *   - User    : root
 *   - Password: 2609
 *
 * Cách dùng trong các DAO:
 *   Connection conn = DBConnection.getConnection();
 *   // ... thực hiện truy vấn ...
 *   DBConnection.close(conn, stmt, rs);
 */
public class DBConnection {

    // ── Thông tin kết nối ─────────────────────────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/cnpm_chess"
                                         + "?useSSL=false"          // Không dùng SSL (local dev)
                                         + "&serverTimezone=Asia/Ho_Chi_Minh" // Múi giờ Việt Nam
                                         + "&allowPublicKeyRetrieval=true"    // Cần cho MySQL 8+
                                         + "&characterEncoding=UTF-8";        // Hỗ trợ tiếng Việt
    private static final String USER     = "root";
    private static final String PASSWORD = "2609";

    // ── Đăng ký JDBC Driver khi class được nạp ───────────────────────────
    static {
        try {
            // Nạp MySQL JDBC Driver vào JVM (bắt buộc với MySQL Connector/J 8.x)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Lỗi này xảy ra nếu file JAR không được thêm vào classpath
            System.err.println("[DBConnection] KHÔNG TÌM THẤY MySQL Driver!");
            System.err.println("  → Kiểm tra lại lib/mysql-connector-j-8.4.0.jar trong classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Tạo và trả về một kết nối mới đến CSDL MySQL.
     * Mỗi lần gọi trả về một Connection độc lập – nhớ đóng sau khi dùng.
     *
     * @return Connection đến cnpm_chess, hoặc null nếu kết nối thất bại
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("[DBConnection] Kết nối CSDL thất bại!");
            System.err.println("  URL     : " + URL);
            System.err.println("  Nguyên nhân: " + e.getMessage());
            e.printStackTrace();
            return null; // Trả về null để DAO xử lý tiếp (trả list rỗng)
        }
    }

    /**
     * Đóng an toàn Connection, Statement và ResultSet.
     * Gọi trong khối finally của DAO để tránh rò rỉ tài nguyên.
     *
     * @param conn Kết nối cần đóng (có thể null)
     * @param stmt Statement cần đóng (có thể null)
     * @param rs   ResultSet cần đóng (có thể null)
     */
    public static void close(java.sql.Connection conn,
                             java.sql.Statement stmt,
                             java.sql.ResultSet rs) {
        // Đóng theo thứ tự: ResultSet → Statement → Connection
        try { if (rs   != null) rs.close();   } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close();  } catch (SQLException ignored) {}
        try { if (conn != null) conn.close();  } catch (SQLException ignored) {}
    }
}
