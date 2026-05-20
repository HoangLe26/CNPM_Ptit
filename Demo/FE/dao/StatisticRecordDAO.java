package dao;

import model.StatisticRecord;
import java.util.ArrayList;
import java.util.List;

public class StatisticRecordDAO {
    /**
     * Lấy danh sách thống kê Elo theo vòng đấu.
     * Trả về List<StatisticRecord> (stub data).
     */
    public List<StatisticRecord> getEloStatisticList(int roundId) {
        List<StatisticRecord> list = new ArrayList<>();
        list.add(new StatisticRecord(1, "P001", "Magnus Carlsen",   "1990", "Norway",  2847, 2851, +4));
        list.add(new StatisticRecord(2, "P002", "Fabiano Caruana",  "1992", "USA",     2820, 2815, -5));
        list.add(new StatisticRecord(3, "P003", "Ding Liren",       "1992", "China",   2811, 2814, +3));
        list.add(new StatisticRecord(4, "P004", "Ian Nepomniachtchi","1990","Russia",  2793, 2790, -3));
        list.add(new StatisticRecord(5, "P005", "Anish Giri",       "1994", "Netherlands", 2780, 2783, +3));
        return list;
    }
}
