package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO – Truy vấn bảng người dùng (tài khoản đăng nhập).
 *
 * Thứ tự kiểm tra:
 *   1. Kết nối CSDL → tìm trong bảng "account"
 *   2. Nếu CSDL lỗi hoặc không có bảng → fallback tài khoản demo cứng
 *
 * !! Sau khi demo, hãy xóa phần fallback và sửa đúng tên bảng thật !!
 */
public class UserDAO {

    /**
     * Kiểm tra đăng nhập.
     * Trả về User đầy đủ nếu hợp lệ, null nếu sai.
     *
     * @param user Đối tượng User chứa username và password do người dùng nhập
     * @return User với role được gán, hoặc null nếu không tìm thấy
     */
    public User checkLogin(User user) {

        // ── Bước 1: Thử kết nối CSDL ────────────────────────────────────
        // TODO: Đổi "account" thành tên bảng thực trong CSDL của bạn
        String sql = "SELECT id, username, role FROM account "
                   + "WHERE username = ? AND password = ?";

        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rs   = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                rs = stmt.executeQuery();

                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    return user; // Đăng nhập thành công từ DB
                }
                return null; // DB kết nối được nhưng không tìm thấy tài khoản
            }

        } catch (SQLException e) {
            // Bảng chưa tồn tại hoặc lỗi DB → ghi log và fallback xuống dưới
            System.err.println("[UserDAO] DB lỗi, dùng fallback demo: " + e.getMessage());
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        // ── Bước 2: Fallback – tài khoản demo cứng (dùng khi chưa có DB) ─
        if ("admin".equals(user.getUsername()) && "123".equals(user.getPassword())) {
            user.setId(1);
            user.setRole("ADMIN");
            return user;
        }
        if ("staff".equals(user.getUsername()) && "123".equals(user.getPassword())) {
            user.setId(2);
            user.setRole("STAFF");
            return user;
        }

        return null; // Sai tài khoản/mật khẩu
    }
}
