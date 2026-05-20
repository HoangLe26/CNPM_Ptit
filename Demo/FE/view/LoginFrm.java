package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrm – Màn hình đăng nhập (bước 1-11 trong kịch bản).
 * Tài khoản demo: admin/123  hoặc  staff/123
 */
public class LoginFrm extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;

    // Màu sắc chủ đạo theo UI mẫu (teal/navy)
    private static final Color COLOR_PANEL    = new Color(255, 255, 255, 230);
    private static final Color COLOR_FIELD_BG = new Color(220, 232, 240);
    private static final Color COLOR_BTN      = new Color(92, 148, 165);
    private static final Color COLOR_BTN_HOV  = new Color(60, 110, 130);
    private static final Color COLOR_TITLE    = new Color(30,  30,  30);

    public LoginFrm() {
        setTitle("Chess Championship – Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Background panel ──────────────────────────────────────────────
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // ── Header: logo + "CHESS" label (top-left) ───────────────────────
        JPanel headerPanel = buildLogoLabel();
        GridBagConstraints gbcH = new GridBagConstraints();
        gbcH.gridx = 0; gbcH.gridy = 0;
        gbcH.anchor = GridBagConstraints.NORTHWEST;
        gbcH.insets = new Insets(10, 15, 0, 0);
        gbcH.weightx = 1; gbcH.weighty = 0;
        gbcH.fill = GridBagConstraints.HORIZONTAL;
        bg.add(headerPanel, gbcH);

        // ── Center card ───────────────────────────────────────────────────
        JPanel card = buildCard();
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.gridx = 0; gbcC.gridy = 1;
        gbcC.anchor = GridBagConstraints.CENTER;
        gbcC.insets = new Insets(0, 0, 30, 0);
        gbcC.weightx = 1; gbcC.weighty = 1;
        bg.add(card, gbcC);

        setVisible(true);
    }

    // ──────────────────────────────────────────────────────────────────────
    private JPanel buildLogoLabel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel("♔ CHESS");
        lbl.setFont(new Font("Serif", Font.BOLD, 16));
        lbl.setForeground(Color.DARK_GRAY);
        p.add(lbl);
        return p;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_PANEL);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 220), 1, true),
            new EmptyBorder(30, 50, 40, 50)
        ));
        card.setPreferredSize(new Dimension(460, 340));

        // ── Title ──
        JLabel lblTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(COLOR_TITLE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Big logo in card ──
        JLabel lblBigLogo = new JLabel("♔ CHESS", SwingConstants.CENTER);
        lblBigLogo.setFont(new Font("Serif", Font.BOLD, 22));
        lblBigLogo.setForeground(COLOR_TITLE);
        lblBigLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Username ──
        JLabel lblU = new JLabel("Username");
        lblU.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblU.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setBackground(COLOR_FIELD_BG);
        txtUsername.setBorder(new EmptyBorder(6, 10, 6, 10));
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Placeholder
        setPlaceholder(txtUsername, "Enter account name");

        // ── Password ──
        JLabel lblP = new JLabel("Password");
        lblP.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblP.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setBackground(COLOR_FIELD_BG);
        txtPassword.setBorder(new EmptyBorder(6, 10, 6, 10));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Error label ──
        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(Color.RED);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Login button ──
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setBackground(COLOR_BTN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new EmptyBorder(8, 40, 8, 40));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(this);
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(COLOR_BTN_HOV); }
            public void mouseExited(MouseEvent e)  { btnLogin.setBackground(COLOR_BTN); }
        });

        // Allow pressing ENTER to login
        getRootPane().setDefaultButton(btnLogin);

        // ── Assemble ──
        card.add(lblBigLogo);
        card.add(Box.createVerticalStrut(4));
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(16));
        card.add(lblU);
        card.add(Box.createVerticalStrut(4));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(12));
        card.add(lblP);
        card.add(Box.createVerticalStrut(4));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(8));
        card.add(lblError);
        card.add(Box.createVerticalStrut(8));
        card.add(btnLogin);

        return card;
    }

    // ──────────────────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                lblError.setText("Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Bước 3-5: tạo User object
            User user = new User(username, password);

            // Bước 6-11: gọi UserDAO.checkLogin()
            UserDAO userDAO = new UserDAO();
            User loggedIn = userDAO.checkLogin(user);

            if (loggedIn == null) {
                lblError.setText("Sai tên đăng nhập hoặc mật khẩu!");
            } else {
                lblError.setText(" ");
                dispose();
                // Bước 12-14: mở StatisticMenuFrm
                new StatisticMenuFrm(loggedIn);
            }
        }
    }

    // ── Placeholder helper ─────────────────────────────────────────────────
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

    // ── Inner: gradient background panel ──────────────────────────────────
    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            // Gradient xanh nhạt giống ảnh UI
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(200, 220, 235),
                getWidth(), getHeight(), new Color(170, 200, 220)
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
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
