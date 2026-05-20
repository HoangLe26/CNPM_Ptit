package view;

import dao.MatchDetailDAO;
import model.MatchDetail;
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
 * MatchDetailFrm – Chi tiết các ván đấu của một kỳ thủ (bước 39-47).
 */
public class MatchDetailFrm extends JFrame implements ActionListener {

    private final User            currentUser;
    private final Round           currentRound;
    private final StatisticRecord statRecord;

    private static final Color COLOR_PANEL     = new Color(255, 255, 255, 220);
    private static final Color COLOR_HEADER_BG = new Color(52,  110, 120);
    private static final Color COLOR_HEADER_FG = Color.WHITE;
    private static final Color COLOR_ROW_SEL   = new Color(200, 230, 240);

    public MatchDetailFrm(User user, Round round, StatisticRecord sr) {
        this.currentUser  = user;
        this.currentRound = round;
        this.statRecord   = sr;

        setTitle("Chess Championship – Match Details");
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
            "Home/ Elo change statistics/ Select Round/ Elo change list/ Details of the matches",
            SwingConstants.CENTER);
        lbl2.setFont(new Font("SansSerif", Font.BOLD, 10));
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

        // Title row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleRow.setOpaque(false);

        JButton btnBack = SelectRoundFrm.buildBackButton();
        btnBack.addActionListener(this);
        titleRow.add(btnBack);

        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setOpaque(false);
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Match details of player:");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel lblName  = new JLabel(statRecord.getName(), SwingConstants.CENTER);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleTextPanel.add(lblTitle);
        titleTextPanel.add(lblName);
        titleRow.add(titleTextPanel);

        card.add(titleRow, BorderLayout.NORTH);

        // Table
        // Bước 41-46: gọi MatchDetailDAO
        MatchDetailDAO dao = new MatchDetailDAO();
        List<MatchDetail> details = dao.getMatchDetail(statRecord.getPlayerId(), currentRound.getId());

        String[] cols = {"MATCH CODE", "OPPONENT'S NAME", "RESULT", "+/- ELO"};
        Object[][] data = new Object[details.size()][4];
        for (int i = 0; i < details.size(); i++) {
            MatchDetail md = details.get(i);
            data[i][0] = md.getMatchId();
            data[i][1] = md.getOpponentName();
            // Kết quả: 1.0 = Thắng, 0.5 = Hòa, 0.0 = Thua
            String resultStr;
            if (md.getResult() == 1.0f)      resultStr = "1 (Win)";
            else if (md.getResult() == 0.5f) resultStr = "½ (Draw)";
            else                              resultStr = "0 (Loss)";
            data[i][2] = resultStr;
            data[i][3] = (md.getEloChange() >= 0 ? "+" : "") + md.getEloChange();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(COLOR_ROW_SEL);
        table.setGridColor(new Color(180, 180, 180));
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(COLOR_HEADER_BG);
        header.setForeground(COLOR_HEADER_FG);
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int c = 0; c < cols.length; c++) {
            table.getColumnModel().getColumn(c).setCellRenderer(centerRenderer);
        }
        // Wider opponent column
        table.getColumnModel().getColumn(1).setPreferredWidth(220);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(COLOR_HEADER_BG, 2));
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Back → EloStatisticFrm
        dispose();
        new EloStatisticFrm(currentUser, currentRound);
    }
}
