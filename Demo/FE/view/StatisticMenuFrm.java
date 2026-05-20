package view;

import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * StatisticMenuFrm – Menu thống kê sau khi đăng nhập (bước 12-16).
 * Có 2 nút: View Standings (chưa implement) và Elo Change Statistics.
 */
public class StatisticMenuFrm extends JFrame implements ActionListener {

    private final User currentUser;
    private JButton btnElo;
    private JButton btnStandings;

    private static final Color COLOR_PANEL   = new Color(255, 255, 255, 220);
    private static final Color COLOR_BTN1    = new Color(52,  90, 120);
    private static final Color COLOR_BTN2    = new Color(42, 128, 130);
    private static final Color COLOR_BTN_HOV = new Color(30,  60,  90);

    public StatisticMenuFrm(User user) {
        this.currentUser = user;

        setTitle("Chess Championship – Static Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        LoginFrm.BackgroundPanel bg = new LoginFrm.BackgroundPanel();
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        // ── Top bar ───────────────────────────────────────────────────────
        bg.add(buildTopBar(), BorderLayout.NORTH);

        // ── Center card ───────────────────────────────────────────────────
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(buildCard());
        bg.add(wrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(10, 15, 5, 15));

        JLabel lblLogo = new JLabel("♔ CHESS");
        lblLogo.setFont(new Font("Serif", Font.BOLD, 16));
        lblLogo.setForeground(Color.DARK_GRAY);
        bar.add(lblLogo, BorderLayout.WEST);

        // Center: logo + HOME
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel lblCenter = new JLabel("♔ CHESS", SwingConstants.CENTER);
        lblCenter.setFont(new Font("Serif", Font.BOLD, 20));
        lblCenter.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblHome = new JLabel("HOME", SwingConstants.CENTER);
        lblHome.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblCenter);
        centerPanel.add(lblHome);
        bar.add(centerPanel, BorderLayout.CENTER);

        // Right: username + icon
        JLabel lblUser = new JLabel(currentUser.getUsername() + "  \uD83D\uDC64");
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblUser.setForeground(Color.DARK_GRAY);
        bar.add(lblUser, BorderLayout.EAST);

        return bar;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_PANEL);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 220), 1, true),
            new EmptyBorder(30, 50, 40, 50)
        ));
        card.setPreferredSize(new Dimension(600, 340));

        JLabel lblTitle = new JLabel("STATIC MENU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Select an option to continue", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Buttons row ──
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        btnPanel.setOpaque(false);

        btnStandings = buildMenuButton("🏆  VIEW\nSTANDINGS", COLOR_BTN1);
        btnElo       = buildMenuButton("📈  ELO CHANGE\nSTATISTICS", COLOR_BTN2);

        btnStandings.addActionListener(this);
        btnElo.addActionListener(this);

        btnPanel.add(btnStandings);
        btnPanel.add(btnElo);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(6));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(20));
        card.add(btnPanel);

        return card;
    }

    /** Tạo nút lớn kiểu menu như trong UI mẫu */
    private JButton buildMenuButton(String text, Color bg) {
        // Multi-line text dùng HTML
        String html = "<html><center>" + text.replace("\n", "<br>") + "</center></html>";
        JButton btn = new JButton(html);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 160));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new LineBorder(bg.darker(), 2, true));

        Color hover = bg.darker();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnElo) {
            // Bước 17-25: mở SelectRoundFrm
            dispose();
            new SelectRoundFrm(currentUser);
        } else if (e.getSource() == btnStandings) {
            JOptionPane.showMessageDialog(this,
                "Chức năng \"View Standings\" chưa được triển khai.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
