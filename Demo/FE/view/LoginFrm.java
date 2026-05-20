package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;

/**
 * LoginFrm – Màn hình đăng nhập sử dụng ảnh thực từ folder UI.
 * Tài khoản demo: admin/123  hoặc  staff/123
 */
public class LoginFrm extends JFrame implements ActionListener {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnLogin;
    private JLabel         lblError;

    // ── Đường dẫn ảnh tương đối so với thư mục chạy ──────────────────────
    private static final String PATH_BG   = "../../UI/Screenshot 2026-04-30 085925 (1).png";
    private static final String PATH_LOGO = "../../UI/logo-removebg-preview.png";

    // ── Màu sắc theo UI mẫu ───────────────────────────────────────────────
    private static final Color COLOR_PANEL    = new Color(255, 255, 255, 220);
    private static final Color COLOR_FIELD_BG = new Color(220, 232, 240);
    private static final Color COLOR_BTN      = new Color(92, 148, 165);
    private static final Color COLOR_BTN_HOV  = new Color(60, 110, 130);

    // ── Hình nền (load 1 lần, dùng chung) ────────────────────────────────
    static Image bgImage   = null;
    static Image logoImage = null;

    static {
        bgImage   = loadImage(PATH_BG);
        logoImage = loadImage(PATH_LOGO);
    }

    public LoginFrm() {
        setTitle("Chess Championship – Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel bg = new BackgroundPanel(bgImage);
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // ── Header top-left ───────────────────────────────────────────────
        JPanel headerPanel = buildLogoPanel(50, 50);
        GridBagConstraints gbcH = new GridBagConstraints();
        gbcH.gridx = 0; gbcH.gridy = 0;
        gbcH.anchor = GridBagConstraints.NORTHWEST;
        gbcH.insets = new Insets(8, 10, 0, 0);
        gbcH.weightx = 1; gbcH.weighty = 0;
        gbcH.fill = GridBagConstraints.HORIZONTAL;
        bg.add(headerPanel, gbcH);

        // ── Center card ───────────────────────────────────────────────────
        JPanel card = buildCard();
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.gridx = 0; gbcC.gridy = 1;
        gbcC.anchor = GridBagConstraints.CENTER;
        gbcC.insets = new Insets(0, 0, 40, 0);
        gbcC.weightx = 1; gbcC.weighty = 1;
        bg.add(card, gbcC);

        setVisible(true);
    }

    // ──────────────────────────────────────────────────────────────────────
    /** Panel logo nhỏ ở góc trên trái */
    static JPanel buildLogoPanel(int w, int h) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setOpaque(false);
        if (logoImage != null) {
            Image scaled = logoImage.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            p.add(new JLabel(new ImageIcon(scaled)));
        } else {
            JLabel lbl = new JLabel("♔ CHESS");
            lbl.setFont(new Font("Serif", Font.BOLD, 16));
            lbl.setForeground(Color.DARK_GRAY);
            p.add(lbl);
        }
        return p;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_PANEL);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 220), 1, true),
            new EmptyBorder(25, 50, 35, 50)
        ));
        card.setPreferredSize(new Dimension(460, 360));

        // ── Logo lớn ở giữa card ──
        JPanel logoCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoCenter.setOpaque(false);
        if (logoImage != null) {
            Image scaled = logoImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoCenter.add(new JLabel(new ImageIcon(scaled)));
        }
        logoCenter.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Tiêu đề ──
        JLabel lblTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(new Color(30, 30, 30));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Username ──
        JLabel lblU = new JLabel("Username");
        lblU.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblU.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setBackground(COLOR_FIELD_BG);
        txtUsername.setBorder(new EmptyBorder(7, 12, 7, 12));
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        setPlaceholder(txtUsername, "Enter account name");

        // ── Password ──
        JLabel lblP = new JLabel("Password");
        lblP.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblP.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setBackground(COLOR_FIELD_BG);
        txtPassword.setBorder(new EmptyBorder(7, 12, 7, 12));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Error ──
        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(Color.RED);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Login button ──
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setBackground(COLOR_BTN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new EmptyBorder(9, 50, 9, 50));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(this);
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(COLOR_BTN_HOV); }
            public void mouseExited(MouseEvent e)  { btnLogin.setBackground(COLOR_BTN); }
        });
        getRootPane().setDefaultButton(btnLogin);

        // ── Assemble ──
        card.add(logoCenter);
        card.add(Box.createVerticalStrut(2));
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(14));
        card.add(lblU);
        card.add(Box.createVerticalStrut(4));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(12));
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
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || username.equals("Enter account name") || password.isEmpty()) {
            lblError.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        User user = new User(username, password);
        UserDAO userDAO = new UserDAO();
        User loggedIn = userDAO.checkLogin(user);

        if (loggedIn == null) {
            lblError.setText("Sai tên đăng nhập hoặc mật khẩu!");
        } else {
            lblError.setText(" ");
            dispose();
            new StatisticMenuFrm(loggedIn);
        }
    }

    // ── Placeholder helper ────────────────────────────────────────────────
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    // ── Helper: load ảnh từ đường dẫn tương đối ─────────────────────────
    static Image loadImage(String relativePath) {
        try {
            File f = new File(relativePath);
            if (f.exists()) return new ImageIcon(f.getAbsolutePath()).getImage();
        } catch (Exception ignored) {}
        return null;
    }

    // ── Inner: Panel vẽ ảnh nền ──────────────────────────────────────────
    static class BackgroundPanel extends JPanel {
        private final Image bg;
        BackgroundPanel(Image bg) { this.bg = bg; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bg != null) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback gradient
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(200, 220, 235),
                    getWidth(), getHeight(), new Color(170, 200, 220)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new LoginFrm();
        });
    }
}
