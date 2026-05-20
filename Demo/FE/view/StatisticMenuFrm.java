package view;

import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * StatisticMenuFrm – Menu thống kê sau khi đăng nhập.
 * Dùng ảnh top123.png và elo.png từ folder UI làm nút menu.
 */
public class StatisticMenuFrm extends JFrame implements ActionListener {

    private final User currentUser;
    private JButton btnStandings;
    private JButton btnElo;

    private static final String PATH_TOP  = "../../UI/top123.png";
    private static final String PATH_ELO  = "../../UI/elo.png";
    private static final String PATH_HUMAN= "../../UI/human-removebg-preview.png";

    private static final Color COLOR_PANEL = new Color(255, 255, 255, 215);

    public StatisticMenuFrm(User user) {
        this.currentUser = user;

        setTitle("Chess Championship – Static Menu");
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

        // Logo nhỏ trái
        bar.add(LoginFrm.buildLogoPanel(48, 48), BorderLayout.WEST);

        // Logo lớn + HOME ở giữa
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        if (LoginFrm.logoImage != null) {
            Image scaled = LoginFrm.logoImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel imgLbl = new JLabel(new ImageIcon(scaled));
            imgLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            center.add(imgLbl);
        }
        JLabel lblHome = new JLabel("HOME", SwingConstants.CENTER);
        lblHome.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(lblHome);
        bar.add(center, BorderLayout.CENTER);

        // Username + human icon ở phải
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        right.setOpaque(false);
        JLabel lblUser = new JLabel(currentUser.getUsername());
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblUser.setForeground(Color.DARK_GRAY);
        right.add(lblUser);

        Image humanImg = LoginFrm.loadImage(PATH_HUMAN);
        if (humanImg != null) {
            Image scaled = humanImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            right.add(new JLabel(new ImageIcon(scaled)));
        }
        bar.add(right, BorderLayout.EAST);

        return bar;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(COLOR_PANEL);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 220), 1, true),
            new EmptyBorder(28, 50, 35, 50)
        ));
        card.setPreferredSize(new Dimension(620, 340));

        // Title
        JLabel lblTitle = new JLabel("STATIC MENU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel lblSub = new JLabel("Select an option to continue", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(lblSub);
        card.add(titlePanel, BorderLayout.NORTH);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        btnPanel.setOpaque(false);

        // Nút Standings – dùng ảnh top123.png
        Image topImg = LoginFrm.loadImage(PATH_TOP);
        btnStandings = buildImageButton(topImg, "VIEW STANDINGS", 160, 160);
        btnStandings.addActionListener(this);

        // Nút ELO – dùng ảnh elo.png
        Image eloImg = LoginFrm.loadImage(PATH_ELO);
        btnElo = buildImageButton(eloImg, "ELO CHANGE STATISTICS", 160, 160);
        btnElo.addActionListener(this);

        btnPanel.add(btnStandings);
        btnPanel.add(btnElo);
        card.add(btnPanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Tạo nút hiển thị ảnh. Nếu ảnh null thì hiện text fallback.
     */
    private JButton buildImageButton(Image img, String fallbackText, int w, int h) {
        JButton btn;
        if (img != null) {
            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            btn = new JButton(new ImageIcon(scaled));
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
        } else {
            btn = new JButton("<html><center>" + fallbackText + "</center></html>");
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
            btn.setBackground(new Color(52, 90, 120));
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(w, h));
        }
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setOpaque(true); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setOpaque(false); btn.repaint(); }
        });

        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnElo) {
            dispose();
            new SelectRoundFrm(currentUser);
        } else if (e.getSource() == btnStandings) {
            JOptionPane.showMessageDialog(this,
                "Chức năng \"View Standings\" chưa được triển khai.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
