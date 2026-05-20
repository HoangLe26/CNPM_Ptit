package view;

import dao.RoundDAO;
import model.Round;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * SelectRoundFrm – Chọn vòng đấu, dùng ảnh nền và logo thực từ folder UI.
 */
public class SelectRoundFrm extends JFrame implements ActionListener {

    private final User currentUser;

    private static final Color COLOR_PANEL   = new Color(255, 255, 255, 215);
    private static final Color COLOR_BTN = new Color(52, 110, 120);

    public SelectRoundFrm(User user) {
        this.currentUser = user;

        setTitle("Chess Championship – Select Round");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        LoginFrm.BackgroundPanel bg = new LoginFrm.BackgroundPanel(LoginFrm.bgImage);
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        bg.add(buildTopBar(), BorderLayout.NORTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(buildCard());
        bg.add(wrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(8, 10, 4, 14));

        bar.add(LoginFrm.buildLogoPanel(48, 48), BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        if (LoginFrm.logoImage != null) {
            Image scaled = LoginFrm.logoImage.getScaledInstance(55, 55, Image.SCALE_SMOOTH);
            JLabel imgLbl = new JLabel(new ImageIcon(scaled));
            imgLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            center.add(imgLbl);
        }
        JLabel breadcrumb = new JLabel("Home/ Elo change statistics/ Select Round", SwingConstants.CENTER);
        breadcrumb.setFont(new Font("SansSerif", Font.BOLD, 11));
        breadcrumb.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(breadcrumb);
        bar.add(center, BorderLayout.CENTER);

        return bar;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout(0, 14));
        card.setBackground(COLOR_PANEL);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 220), 1, true),
            new EmptyBorder(22, 36, 28, 36)
        ));
        card.setPreferredSize(new Dimension(750, 360));

        JLabel lblTitle = new JLabel("Select Round", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        card.add(lblTitle, BorderLayout.NORTH);

        // Lấy danh sách vòng
        RoundDAO roundDAO = new RoundDAO();
        List<Round> rounds = roundDAO.getRoundList();

        int cols = 5;
        int rows = (int) Math.ceil((double) rounds.size() / cols);
        JPanel grid = new JPanel(new GridLayout(rows, cols, 12, 12));
        grid.setOpaque(false);

        for (Round r : rounds) {
            JButton btn = new JButton("Round " + r.getRoundNum());
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setBackground(COLOR_BTN);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addActionListener(ev -> {
                dispose();
                new EloStatisticFrm(currentUser, r);
            });

            grid.add(btn);
        }

        // Padding cells
        int rem = rows * cols - rounds.size();
        for (int i = 0; i < rem; i++) {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            grid.add(empty);
        }

        card.add(grid, BorderLayout.CENTER);

        // Back button
        JButton btnBack = buildBackButton();
        btnBack.addActionListener(this);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.setOpaque(false);
        south.add(btnBack);
        card.add(south, BorderLayout.SOUTH);

        return card;
    }

    static JButton buildBackButton() {
        JButton btn = new JButton("◀ BACK");
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(new Color(52, 110, 120));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        new StatisticMenuFrm(currentUser);
    }
}
