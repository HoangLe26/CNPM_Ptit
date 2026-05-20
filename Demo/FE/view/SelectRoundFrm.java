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
 * SelectRoundFrm – Chọn vòng đấu (bước 17-27).
 * Hiển thị lưới các nút Round theo UI mẫu.
 */
public class SelectRoundFrm extends JFrame implements ActionListener {

    private final User currentUser;
    private Round selectedRound;

    private static final Color COLOR_PANEL    = new Color(255, 255, 255, 220);
    private static final Color COLOR_BTN      = new Color(52,  110, 120);
    private static final Color COLOR_BTN_SEL  = new Color(30,   70,  85);
    private static final Color COLOR_BTN_HOV  = new Color(35,   85, 100);

    public SelectRoundFrm(User user) {
        this.currentUser = user;

        setTitle("Chess Championship – Select Round");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        LoginFrm.BackgroundPanel bg = new LoginFrm.BackgroundPanel();
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
        bar.setBorder(new EmptyBorder(10, 15, 5, 15));

        JLabel lblLogo = new JLabel("♔ CHESS");
        lblLogo.setFont(new Font("Serif", Font.BOLD, 16));
        lblLogo.setForeground(Color.DARK_GRAY);
        bar.add(lblLogo, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel lbl1 = new JLabel("♔ CHESS", SwingConstants.CENTER);
        lbl1.setFont(new Font("Serif", Font.BOLD, 20));
        lbl1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lbl2 = new JLabel("Home/ Elo change statistics/ Select Round", SwingConstants.CENTER);
        lbl2.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl2.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lbl1);
        centerPanel.add(lbl2);
        bar.add(centerPanel, BorderLayout.CENTER);

        return bar;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(COLOR_PANEL);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 220), 1, true),
            new EmptyBorder(25, 40, 30, 40)
        ));
        card.setPreferredSize(new Dimension(750, 360));

        // Title
        JLabel lblTitle = new JLabel("Select Round", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        card.add(lblTitle, BorderLayout.NORTH);

        // Round grid
        // Bước 19-24: lấy danh sách từ RoundDAO
        RoundDAO roundDAO = new RoundDAO();
        List<Round> rounds = roundDAO.getRoundList();

        int cols = 5;
        int rows = (int) Math.ceil((double) rounds.size() / cols);
        JPanel grid = new JPanel(new GridLayout(rows, cols, 12, 12));
        grid.setOpaque(false);

        ButtonGroup bg = new ButtonGroup();

        for (Round r : rounds) {
            JToggleButton btn = new JToggleButton("Round " + r.getRoundNum());
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            btn.setBackground(COLOR_BTN);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(new LineBorder(COLOR_BTN.darker(), 2, true));
            btn.setPreferredSize(new Dimension(120, 50));

            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (!btn.isSelected()) btn.setBackground(COLOR_BTN_HOV);
                }
                public void mouseExited(MouseEvent e) {
                    if (!btn.isSelected()) btn.setBackground(COLOR_BTN);
                }
            });

            btn.addActionListener(e -> {
                selectedRound = r;
                btn.setBackground(COLOR_BTN_SEL);
                // Bước 27-28: actionPerformed -> mở EloStatisticFrm
                dispose();
                new EloStatisticFrm(currentUser, selectedRound);
            });

            bg.add(btn);
            grid.add(btn);
        }

        // Pad remaining cells if grid not full
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
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.setOpaque(false);
        southPanel.add(btnBack);
        card.add(southPanel, BorderLayout.SOUTH);

        return card;
    }

    static JButton buildBackButton() {
        JButton btn = new JButton("◀ BACK");
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(new Color(52, 110, 120));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Back → StatisticMenuFrm
        dispose();
        new StatisticMenuFrm(currentUser);
    }
}
