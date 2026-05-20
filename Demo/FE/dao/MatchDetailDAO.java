package dao;

import model.MatchDetail;
import java.util.ArrayList;
import java.util.List;

public class MatchDetailDAO {
    /**
     * Lấy danh sách chi tiết các ván đấu của một kỳ thủ trong một vòng.
     * Trả về List<MatchDetail> (stub data).
     */
    public List<MatchDetail> getMatchDetail(int playerId, int roundId) {
        List<MatchDetail> list = new ArrayList<>();
        list.add(new MatchDetail(101, "Fabiano Caruana",  1.0f, +4));
        list.add(new MatchDetail(102, "Ding Liren",       0.5f, +1));
        list.add(new MatchDetail(103, "Anish Giri",       0.0f, -3));
        return list;
    }
}
