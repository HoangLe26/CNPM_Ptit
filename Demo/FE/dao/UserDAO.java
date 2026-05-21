package dao; // File này thuộc gói "dao"

import model.User; // Dùng class User để nhận vào và trả về thông tin tài khoản

import java.sql.Connection;        // Đại diện kết nối database
import java.sql.PreparedStatement; // Câu lệnh SQL có tham số (tránh SQL Injection)
import java.sql.ResultSet;         // Kết quả trả về sau khi thực thi câu lệnh SELECT

public class UserDAO {

    // Hàm kiểm tra đăng nhập
    // Nhận vào: đối tượng User chứa username và password người dùng vừa nhập
    // Trả về: User (nếu đúng) hoặc null (nếu sai)
    public User checkLogin(User user) {
        try {
            Connection conn = DBConnection.getConnection(); // Lấy kết nối đến database

            // Câu lệnh SQL tìm tài khoản khớp username VÀ password
            // Dấu ? là tham số, sẽ được gán giá trị ở dưới (tránh SQL Injection)
            String sql = "SELECT id, username, role FROM account WHERE username = ? AND password = ?";

            PreparedStatement stmt = conn.prepareStatement(sql); // Chuẩn bị câu lệnh SQL
            stmt.setString(1, user.getUsername()); // Gán tham số ? thứ 1 = username người dùng nhập
            stmt.setString(2, user.getPassword()); // Gán tham số ? thứ 2 = password người dùng nhập

            ResultSet rs = stmt.executeQuery(); // Thực thi câu lệnh SELECT, lấy kết quả về rs

            if (rs.next()) { // Nếu có ít nhất 1 hàng kết quả → tìm thấy tài khoản khớp
                user.setId(rs.getInt("id"));           // Đọc cột "id" từ kết quả, gán vào user
                user.setUsername(rs.getString("username")); // Đọc cột "username", gán vào user
                user.setRole(rs.getString("role"));    // Đọc cột "role" (ADMIN/STAFF), gán vào user
                return user; // Trả về user đã có đầy đủ thông tin → đăng nhập thành công
            }

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console nếu có vấn đề kết nối hoặc truy vấn
        }

        return null; // Không tìm thấy tài khoản khớp → trả về null → đăng nhập thất bại
    }
}
