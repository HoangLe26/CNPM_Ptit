package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // Kiểm tra đăng nhập
    // Trả về User nếu đúng, trả về null nếu sai
    public User checkLogin(User user) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT id, username, role FROM account WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                return user; // Đăng nhập thành công
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Sai tài khoản hoặc mật khẩu
    }
}
