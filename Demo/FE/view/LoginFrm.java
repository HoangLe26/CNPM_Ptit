package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;

/**
 * 
 * Tài khoản demo: admin/123 hoặc staff/123
 *
 * Kế thừa JFrame → đây là 1 cửa sổ ứng dụng độc lập (cửa sổ chính)
 * Implements ActionListener → tự xử lý sự kiện nút bấm trong cùng class này
 */
public class LoginFrm extends JFrame implements ActionListener {

    // ── Khai báo các thành phần giao diện (UI components) ─────────────────
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;

    private static final String PATH_BG = "../../UI/Screenshot 2026-04-30 085925 (1).png";

    private static final String PATH_LOGO = "../../UI/logo-removebg-preview.png";
    private static final Color COLOR_PANEL = new Color(255, 255, 255, 220);
    private static final Color COLOR_FIELD_BG = new Color(220, 232, 240);
    private static final Color COLOR_BTN = new Color(92, 148, 165);
    private static final Color COLOR_BTN_HOV = new Color(60, 110, 130);

    // ── Hình nền và logo (load 1 lần, dùng chung cho toàn bộ các màn hình) ─
    /**
     * Biến static → chỉ load ảnh nền 1 lần duy nhất, các màn hình khác tái sử dụng
     */
    static Image bgImage = null;

    /** Biến static → chỉ load logo 1 lần duy nhất, các màn hình khác tái sử dụng */
    static Image logoImage = null;

    // Khối static chạy 1 lần khi class được nạp vào bộ nhớ
    static {
        bgImage = loadImage(PATH_BG); // Nạp ảnh nền từ đường dẫn
        logoImage = loadImage(PATH_LOGO); // Nạp ảnh logo từ đường dẫn
    }

    /**
     * Constructor – Khởi tạo và hiển thị cửa sổ đăng nhập.
     */
    public LoginFrm() {
        setTitle("Chess Championship – Login"); // Tiêu đề hiển thị trên thanh tiêu đề cửa sổ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Bấm X → thoát hẳn ứng dụng
        setSize(900, 600); // Kích thước cửa sổ: rộng 900px, cao 600px
        setLocationRelativeTo(null); // Căn giữa màn hình máy tính
        setResizable(false); // Không cho phép kéo thay đổi kích thước

        // Tạo panel nền với ảnh thực; nếu không có ảnh → vẽ gradient
        BackgroundPanel bg = new BackgroundPanel(bgImage);
        bg.setLayout(new GridBagLayout()); // Dùng GridBagLayout để canh giữa card login
        setContentPane(bg); // Đặt panel nền làm nội dung chính của cửa sổ

        // ── Header (logo nhỏ ở góc trên trái) ────────────────────────────
        JPanel headerPanel = buildLogoPanel(50, 50); // Tạo panel logo kích thước 50x50px
        GridBagConstraints gbcH = new GridBagConstraints();
        gbcH.gridx = 0;
        gbcH.gridy = 0; // Vị trí ô lưới: cột 0, hàng 0
        gbcH.anchor = GridBagConstraints.NORTHWEST; // Ghim vào góc trên trái
        gbcH.insets = new Insets(8, 10, 0, 0); // Lề: trên 8px, trái 10px
        gbcH.weightx = 1;
        gbcH.weighty = 0; // Chiều ngang co giãn, chiều dọc cố định
        gbcH.fill = GridBagConstraints.HORIZONTAL; // Trải rộng theo chiều ngang
        bg.add(headerPanel, gbcH);

        // ── Card đăng nhập ở giữa màn hình ───────────────────────────────
        JPanel card = buildCard(); // Tạo card chứa form đăng nhập
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.gridx = 0;
        gbcC.gridy = 1; // Vị trí ô lưới: cột 0, hàng 1 (dưới header)
        gbcC.anchor = GridBagConstraints.CENTER; // Căn giữa
        gbcC.insets = new Insets(0, 0, 40, 0); // Lề dưới 40px để card không dính đáy
        gbcC.weightx = 1;
        gbcC.weighty = 1; // Co giãn cả hai chiều → đảm bảo căn giữa
        bg.add(card, gbcC);

        setVisible(true); // Hiển thị cửa sổ ra màn hình
    }

    // ──────────────────────────────────────────────────────────────────────
    /**
     * Tạo panel chứa logo nhỏ ở góc trên trái.
     * Được dùng chung ở tất cả các màn hình (static để các class khác gọi được).
     *
     * @param w Chiều rộng logo (pixel)
     * @param h Chiều cao logo (pixel)
     * @return JPanel chứa logo hoặc text fallback nếu không có ảnh
     */
    static JPanel buildLogoPanel(int w, int h) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4)); // Căn trái, khoảng cách 8px ngang, 4px dọc
        p.setOpaque(false); // Trong suốt để thấy ảnh nền bên dưới

        if (logoImage != null) {
            // Có ảnh → co giãn ảnh về kích thước w×h rồi đặt vào JLabel
            Image scaled = logoImage.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            p.add(new JLabel(new ImageIcon(scaled)));
        } else {
            // Không tìm thấy file ảnh → hiển thị text thay thế
            JLabel lbl = new JLabel("♔ CHESS");
            lbl.setFont(new Font("Serif", Font.BOLD, 16));
            lbl.setForeground(Color.DARK_GRAY);
            p.add(lbl);
        }
        return p;
    }

    /**
     * Tạo "card" – hộp chứa toàn bộ form đăng nhập (logo lớn, tiêu đề,
     * ô nhập username, ô nhập password, thông báo lỗi, nút Login).
     *
     * @return JPanel card đã được lắp ráp đầy đủ
     */
    private JPanel buildCard() {
        // Card dùng BoxLayout chiều dọc (Y_AXIS) → các thành phần xếp chồng từ trên
        // xuống
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_PANEL); // Nền trắng bán trong suốt
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 210, 220), 1, true), // Đường viền 1px màu xanh-xám, bo góc
                new EmptyBorder(25, 50, 35, 50) // Padding bên trong: trên 25, trái/phải 50, dưới 35
        ));
        card.setPreferredSize(new Dimension(460, 360)); // Kích thước card: 460×360px

        // ── Logo lớn ở giữa đầu card ──────────────────────────────────────
        JPanel logoCenter = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Căn giữa
        logoCenter.setOpaque(false); // Trong suốt
        if (logoImage != null) {
            // Scale logo về 80×80px để hiển thị to hơn logo ở góc
            Image scaled = logoImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoCenter.add(new JLabel(new ImageIcon(scaled)));
        }
        logoCenter.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa theo trục X của BoxLayout

        // ── Tiêu đề "LOGIN" ──────────────────────────────────────────────
        JLabel lblTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32)); // Font đậm, cỡ 32
        lblTitle.setForeground(new Color(30, 30, 30)); // Màu gần đen
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa

        // ── Nhãn "Username" ───────────────────────────────────────────────
        JLabel lblU = new JLabel("Username");
        lblU.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblU.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái

        // ── Ô nhập username ───────────────────────────────────────────────
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setBackground(COLOR_FIELD_BG); // Nền xanh-xám nhạt
        txtUsername.setBorder(new EmptyBorder(7, 12, 7, 12)); // Padding bên trong ô
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38)); // Chiều cao cố định 38px, ngang tự do
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        setPlaceholder(txtUsername, "Enter account name"); // Gán văn bản gợi ý (placeholder)

        // ── Nhãn "Password" ───────────────────────────────────────────────
        JLabel lblP = new JLabel("Password");
        lblP.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblP.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Ô nhập password (ký tự bị ẩn) ────────────────────────────────
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setBackground(COLOR_FIELD_BG);
        txtPassword.setBorder(new EmptyBorder(7, 12, 7, 12));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Nhãn hiển thị lỗi ────────────────────────────────────────────
        lblError = new JLabel(" ", SwingConstants.CENTER); // Mặc định là khoảng trắng (ẩn thông báo lỗi)
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(Color.RED); // Màu đỏ để nổi bật thông báo lỗi
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Nút Login ─────────────────────────────────────────────────────
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setBackground(COLOR_BTN); // Màu nền teal
        btnLogin.setForeground(Color.WHITE); // Chữ trắng
        btnLogin.setFocusPainted(false); // Tắt đường viền focus khi tab/click
        btnLogin.setBorder(new EmptyBorder(9, 50, 9, 50)); // Padding lớn để nút to ra
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Con trỏ chuột hình bàn tay
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(this); // Đăng ký xử lý sự kiện click

        // Hiệu ứng hover: đổi màu nút khi rê/rời chuột
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(COLOR_BTN_HOV);
            } // Tối hơn khi hover

            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(COLOR_BTN);
            } // Về màu gốc khi rời
        });

        // Phím Enter từ bất kỳ đâu trong cửa sổ cũng kích hoạt nút Login
        getRootPane().setDefaultButton(btnLogin);

        // ── Lắp ráp các thành phần vào card theo thứ tự từ trên xuống ────
        card.add(logoCenter);
        card.add(Box.createVerticalStrut(2)); // Khoảng trống 2px
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(14)); // Khoảng trống 14px
        card.add(lblU);
        card.add(Box.createVerticalStrut(4)); // Khoảng trống 4px
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(12)); // Khoảng trống 12px
        card.add(lblP);
        card.add(Box.createVerticalStrut(4));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(6));
        card.add(lblError);
        card.add(Box.createVerticalStrut(8));
        card.add(btnLogin);

        return card;
    }

    // ──────────────────────────────────────────────────────────────────────
    /**
     * Xử lý sự kiện khi người dùng bấm nút Login (hoặc nhấn Enter).
     * Quy trình: Lấy dữ liệu → Validate → Gọi DAO → Chuyển màn hình.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Lấy dữ liệu từ các ô nhập, cắt khoảng trắng thừa
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Kiểm tra dữ liệu rỗng hoặc vẫn đang là placeholder
        if (username.isEmpty() || username.equals("Enter account name") || password.isEmpty()) {
            lblError.setText("Vui lòng nhập đầy đủ thông tin!"); // Hiển thị lỗi
            return; // Dừng, không tiếp tục xử lý
        }

        // Tạo đối tượng User chứa thông tin đăng nhập để truyền cho DAO
        User user = new User(username, password);
        UserDAO userDAO = new UserDAO();

        // Gọi DAO kiểm tra đăng nhập trong CSDL
        User loggedIn = userDAO.checkLogin(user);

        if (loggedIn == null) {
            // Sai tài khoản/mật khẩu → hiển thị lỗi, giữ nguyên màn hình
            lblError.setText("Sai tên đăng nhập hoặc mật khẩu!");
        } else {
            // Đăng nhập thành công → hiển thị thông báo với tên user
            JOptionPane.showMessageDialog(
                this,
                "Đăng nhập thành công!\nXin chào, " + loggedIn.getUsername(),
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE
            );
            dispose(); // Đóng cửa sổ Login
        }
    }

    // ── Hàm hỗ trợ: Placeholder ───────────────────────────────────────────
    /**
     * Gán văn bản gợi ý (placeholder) cho ô nhập liệu.
     * Khi người dùng click vào → xóa placeholder và chuyển màu chữ sang đen.
     * Khi người dùng rời khỏi mà không nhập gì → hiện lại placeholder màu xám.
     *
     * @param field       Ô nhập cần gán placeholder
     * @param placeholder Nội dung gợi ý hiển thị khi ô trống
     */
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY); // Màu xám ban đầu cho placeholder
        field.setText(placeholder); // Hiển thị nội dung placeholder

        field.addFocusListener(new FocusAdapter() {
            // Khi ô được focus (click vào)
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText(""); // Xóa placeholder
                    field.setForeground(Color.BLACK); // Chuyển màu chữ sang đen
                }
            }

            // Khi ô mất focus (click ra chỗ khác)
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY); // Màu xám trở lại
                    field.setText(placeholder); // Hiện lại placeholder
                }
            }
        });
    }

    // ── Hàm tiện ích: Load ảnh từ đường dẫn tương đối ───────────────────
    /**
     * Nạp ảnh từ đường dẫn file tương đối.
     * Trả về null nếu file không tồn tại hoặc có lỗi (an toàn, không ném
     * exception).
     * Được dùng chung (static) để các class khác cũng có thể gọi.
     *
     * @param relativePath Đường dẫn tương đối đến file ảnh
     * @return Image object hoặc null nếu không tìm thấy
     */
    static Image loadImage(String relativePath) {
        try {
            File f = new File(relativePath);
            if (f.exists())
                return new ImageIcon(f.getAbsolutePath()).getImage();
        } catch (Exception ignored) {
        } // Bỏ qua mọi lỗi, trả về null
        return null;
    }

    // ── Inner class: Panel vẽ ảnh nền cho toàn bộ cửa sổ ────────────────
    /**
     * BackgroundPanel – Một JPanel tùy chỉnh có khả năng vẽ ảnh nền.
     * Được dùng làm contentPane (panel gốc) của tất cả các cửa sổ trong app.
     * Khai báo static → có thể tạo đối tượng mà không cần instance của LoginFrm.
     */
    static class BackgroundPanel extends JPanel {
        private final Image bg; // Ảnh nền cần vẽ

        BackgroundPanel(Image bg) {
            this.bg = bg;
        }

        /**
         * Override phương thức vẽ của JPanel.
         * Được Swing tự động gọi mỗi khi cần vẽ lại cửa sổ.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Phải gọi super trước để tránh lỗi vẽ
            if (bg != null) {
                // Vẽ ảnh nền, tự động co giãn vừa khít kích thước panel
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Không có ảnh → vẽ gradient xanh-xám nhạt làm fallback
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(
                        0, 0, new Color(200, 220, 235), // Góc trên trái: xanh nhạt
                        getWidth(), getHeight(), new Color(170, 200, 220) // Góc dưới phải: xanh đậm hơn
                ));
                g2.fillRect(0, 0, getWidth(), getHeight()); // Tô màu toàn bộ diện tích
            }
        }
    }

    /**
     * Entry point – Điểm khởi động của toàn bộ ứng dụng.
     * invokeLater đảm bảo GUI được tạo trên Event Dispatch Thread (EDT) của Swing.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            } // Nếu không đặt được L&F hệ thống → dùng mặc định
            new LoginFrm(); // Tạo và hiển thị màn hình đăng nhập
        });
    }
}
