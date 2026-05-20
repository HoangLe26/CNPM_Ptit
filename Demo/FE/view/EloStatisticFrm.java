package view;

import dao.StatisticRecordDAO;
import model.Round;
import model.StatisticRecord;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * EloStatisticFrm – Bảng thống kê Elo. Dùng ảnh nền và logo thực.
 */
public class EloStatisticFrm extends JFrame implements ActionListener {

    private final User   currentUser;
    private final Round  currentRound;
    private List<StatisticRecord> records;

    private static final Color COLOR_PANEL     = new Color(255, 255, 255, 215);
    private static final Color COLOR_HEADER_BG = new Color(52,  110, 120);
    private static final Color COLOR_HEADER_FG = Color.WHITE;

    public EloStatisticFrm(User user, Round round) {
        this.currentUser  = user;
        this.currentRound = round;

        setTitle("Chess Championship – Elo Change Statistics");
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
        JLabel bc = new JLabel("Home/ Elo change statistics/ Select Round/ Elo change list", SwingConstants.CENTER);
        bc.setFont(new Font("SansSerif", Font.BOLD, 11));
        bc.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(bc);
        bar.add(center, BorderLayout.CENTER);

        return bar;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(COLOR_PANEL);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 220), 1, true),
            new EmptyBorder(18, 28, 18, 28)
        ));
        card.setPreferredSize(new Dimension(760, 390));

        // Title row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleRow.setOpaque(false);
        JButton btnBack = SelectRoundFrm.buildBackButton();
        btnBack.addActionListener(this);
        titleRow.add(btnBack);
        JLabel lblTitle = new JLabel("Elo change statistics");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleRow.add(lblTitle);
        card.add(titleRow, BorderLayout.NORTH);

        // Table
        StatisticRecordDAO dao = new StatisticRecordDAO();
        records = dao.getEloStatisticList(currentRound.getId());

        String[] cols = {"CODE", "NAME", "YOB", "NATIONALITY", "OLD ELO", "NEW ELO", "+/- ELO"};
        Object[][] data = new Object[records.size()][7];
        for (int i = 0; i < records.size(); i++) {
            StatisticRecord sr = records.get(i);
            data[i][0] = sr.getCode();
            data[i][1] = sr.getName();
            data[i][2] = sr.getYob();
            data[i][3] = sr.getNationality();
            data[i][4] = sr.getOldElo();
            data[i][5] = sr.getNewElo();
            data[i][6] = (sr.getEloChange() >= 0 ? "+" : "") + sr.getEloChange();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Tắt highlight khi chọn hàng – màu cố định trắng
        table.setSelectionBackground(Color.WHITE);
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);
        table.setGridColor(new Color(180, 180, 180));
        table.setShowGrid(true);

        // Header: custom renderer ép cứng màu teal, không bị system L&F ghi đè
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(COLOR_HEADER_BG);
                setForeground(COLOR_HEADER_FG);
                setFont(new Font("SansSerif", Font.BOLD, 13));
                setOpaque(true);
            }
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean sel, boolean foc, int row, int col) {
                setText(value == null ? "" : value.toString());
                return this;
            }
        });
        table.getTableHeader().setReorderingAllowed(false);

        // Cell renderer: căn giữa, màu cố định
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, false, false, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                setForeground(Color.BLACK);
                return this;
            }
        };
        for (int c = 0; c < cols.length; c++) {
            table.getColumnModel().getColumn(c).setCellRenderer(cellRenderer);
        }

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < records.size()) {
                    dispose();
                    new MatchDetailFrm(currentUser, currentRound, records.get(row));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(COLOR_HEADER_BG, 2));
        card.add(scroll, BorderLayout.CENTER);

        JLabel hint = new JLabel("* Click vào một hàng để xem chi tiết trận đấu");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(Color.GRAY);
        card.add(hint, BorderLayout.SOUTH);

        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        new SelectRoundFrm(currentUser);
    }
}
