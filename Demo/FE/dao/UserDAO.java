package dao;

import model.User;

public class UserDAO {
    /**
     * Kiểm tra đăng nhập.
     * Trả về đối tượng User với role được set nếu hợp lệ, null nếu sai.
     */
    public User checkLogin(User user) {
        // --- Stub data (thay bằng kết nối DB thực) ---
        if ("admin".equals(user.getUsername()) && "123".equals(user.getPassword())) {
            user.setUsername("admin");
            user.setRole("ADMIN");
            return user;
        }
        if ("staff".equals(user.getUsername()) && "123".equals(user.getPassword())) {
            user.setUsername("staff");
            user.setRole("STAFF");
            return user;
        }
        return null;
    }
}
