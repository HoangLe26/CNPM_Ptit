package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrm extends JFrame implements ActionListener {

    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnLogin = new JButton("Login");
    private JLabel lblError = new JLabel(" ");

    public LoginFrm() {
        setTitle("Chess Championship – Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Tạo panel chính có khả năng vẽ ảnh nền (dùng anonymous class ghi đè
        // paintComponent)
        JPanel main = new JPanel(new GridBagLayout()) { // GridBagLayout giúp căn giữa form
            // Load ảnh nền từ đường dẫn tương đối
            Image bg = new ImageIcon("../../UI/Screenshot 2026-04-30 085925 (1).png").getImage();

            // Swing tự động gọi paintComponent mỗi khi cần vẽ lại panel
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Gọi hàm cha trước (bắt buộc)
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this); // Vẽ ảnh nền, co giãn vừa cửa sổ
            }
        };
        setContentPane(main);

        // GridLayout(6, 1, 5, 8): 6 hàng, 1 cột, khoảng cách ngang 5px, dọc 8px
        JPanel form = new JPanel(new GridLayout(6, 1, 5, 8));
        form.setBackground(new Color(255, 255, 255, 220)); // Màu nền: trắng bán trong suốt (220/255)
        form.setBorder(BorderFactory.createEmptyBorder(200, 40, 30, 40)); // Khoảng cách lề trong: trên 30, trái 40,
                                                                          // dưới
                                                                          // 30, phải 40
        form.setPreferredSize(new Dimension(360, 280)); // Kích thước ưu tiên của form: 360x280px

        lblError.setForeground(Color.RED); // Đặt màu chữ thông báo lỗi là đỏ
        lblError.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa chữ trong nhãn lỗi

        btnLogin.setBackground(new Color(92, 148, 165)); // Màu nền nút Login: xanh teal
        btnLogin.setForeground(Color.WHITE); // Màu chữ nút Login: trắng
        btnLogin.setFocusPainted(false); // Tắt đường viền xanh khi nút được focus
        btnLogin.addActionListener(this); // Đăng ký: khi bấm nút → gọi actionPerformed của class này

        // Thêm các thành phần vào form theo thứ tự từ trên xuống (6 hàng)
        form.add(new JLabel("Username")); // Hàng 1: nhãn "Username"
        form.add(txtUsername); // Hàng 2: ô nhập tên đăng nhập
        form.add(new JLabel("Password")); // Hàng 3: nhãn "Password"
        form.add(txtPassword); // Hàng 4: ô nhập mật khẩu
        form.add(lblError); // Hàng 5: nhãn hiển thị lỗi
        form.add(btnLogin); // Hàng 6: nút Login

        main.add(form); // Thêm form vào panel chính (GridBagLayout tự căn giữa)
        setVisible(true); // Hiển thị cửa sổ ra màn hình
    }

    // Hàm này được gọi tự động khi người dùng bấm nút btnLogin
    public void actionPerformed(ActionEvent e) {
        String username = txtUsername.getText().trim(); // Lấy text từ ô username, xóa khoảng trắng 2 đầu
        String password = new String(txtPassword.getPassword()).trim(); // Lấy mật khẩu (getPassword trả về char[]),
                                                                        // chuyển sang String

        // Kiểm tra nếu người dùng bỏ trống username hoặc password
        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Vui lòng nhập đầy đủ thông tin!"); // Hiện thông báo lỗi
            return; // Dừng hàm, không xử lý tiếp
        }

        User user = new User(username, password); // Tạo đối tượng User chứa thông tin vừa nhập
        User loggedIn = new UserDAO().checkLogin(user); // Gọi UserDAO để kiểm tra trong database

        if (loggedIn == null) {
            // checkLogin trả về null → không tìm thấy tài khoản khớp → sai
            lblError.setText("Sai tên đăng nhập hoặc mật khẩu!");
        } else {
            // checkLogin trả về User → đăng nhập thành công
            JOptionPane.showMessageDialog(
                    this, // Cửa sổ cha (dialog xuất hiện trên LoginFrm)
                    "Đăng nhập thành công!\nXin chào, " + loggedIn.getUsername(), // Nội dung thông báo
                    "Thành công", // Tiêu đề hộp thoại
                    JOptionPane.INFORMATION_MESSAGE // Biểu tượng chữ "i" xanh
            );
            dispose(); // Đóng cửa sổ Login
        }
    }

    // Điểm khởi động chương trình
    public static void main(String[] args) {
        new LoginFrm(); // Tạo đối tượng LoginFrm → constructor chạy → cửa sổ hiện ra
    }
}
