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
 * EloStatisticFrm – Danh sách thống kê Elo (bước 29-38).
 * Nhấn vào 1 hàng → mở MatchDetailFrm.
 */
public class EloStatisticFrm extends JFrame implements ActionListener {

    private final User  currentUser;
    private final Round currentRound;
    private JTable table;
    private List<StatisticRecord> records;

    private static final Color COLOR_PANEL      = new Color(255, 255, 255, 220);
    private static final Color COLOR_HEADER_BG  = new Color(52,  110, 120);
    private static final Color COLOR_HEADER_FG  = Color.WHITE;
    private static final Color COLOR_ROW_SEL    = new Color(200, 230, 240);

    public EloStatisticFrm(User user, Round round) {
        this.currentUser  = user;
        this.currentRound = round;

        setTitle("Chess Championship – Elo Change Statistics");
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
        JLabel lbl2 = new JLabel(
            "Home/ Elo change statistics/ Select Round/ Elo change list",
            SwingConstants.CENTER);
        lbl2.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl2.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lbl1);
        centerPanel.add(lbl2);
        bar.add(centerPanel, BorderLayout.CENTER);

        return bar;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(COLOR_PANEL);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 220), 1, true),
            new EmptyBorder(20, 30, 20, 30)
        ));
        card.setPreferredSize(new Dimension(760, 380));

        // Title row (BACK + title)
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
        // Bước 30-35: gọi StatisticRecordDAO
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
        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(COLOR_ROW_SEL);
        table.setGridColor(new Color(180, 180, 180));
        table.setShowGrid(true);

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(COLOR_HEADER_BG);
        header.setForeground(COLOR_HEADER_FG);
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.CENTER);

        // Center all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int c = 0; c < cols.length; c++) {
            table.getColumnModel().getColumn(c).setCellRenderer(centerRenderer);
        }

        // Hint label on first row name cell if empty
        // Bước 37-38: click hàng → mở MatchDetailFrm
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    if (row >= 0 && row < records.size()) {
                        StatisticRecord sr = records.get(row);
                        dispose();
                        // Bước 39-47
                        new MatchDetailFrm(currentUser, currentRound, sr);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(COLOR_HEADER_BG, 2));
        card.add(scroll, BorderLayout.CENTER);

        // Hint
        JLabel hint = new JLabel("* Click vào một hàng để xem chi tiết trận đấu");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(Color.GRAY);
        card.add(hint, BorderLayout.SOUTH);

        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Back → SelectRoundFrm
        dispose();
        new SelectRoundFrm(currentUser);
    }
}
