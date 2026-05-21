package dao; // File này thuộc gói "dao" (Data Access Object – tầng truy cập dữ liệu)

import java.sql.Connection;       // Đại diện cho một kết nối đến database
import java.sql.DriverManager;    // Lớp quản lý kết nối JDBC

public class DBConnection {

    // Thông tin kết nối database – thay đổi nếu cấu hình khác
    static String url      = "jdbc:mysql://localhost:3306/cnpm_chess"; // Địa chỉ DB: localhost, cổng 3306, tên DB là cnpm_chess
    static String user     = "root";  // Tên đăng nhập MySQL
    static String password = "2609";  // Mật khẩu MySQL

    // Hàm trả về một kết nối đến database
    // throws Exception: nếu kết nối thất bại thì ném lỗi ra ngoài (để UserDAO bắt)
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Nạp MySQL JDBC Driver vào chương trình (bắt buộc)
        Connection conn = DriverManager.getConnection(url, user, password); // Tạo kết nối đến database
        return conn; // Trả về kết nối cho nơi gọi (UserDAO) sử dụng
    }
}
