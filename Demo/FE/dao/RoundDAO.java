package dao;

import model.Round;
import java.util.ArrayList;
import java.util.List;

public class RoundDAO {
    /**
     * Lấy danh sách các vòng đấu.
     * Trả về List<Round> (stub data).
     */
    public List<Round> getRoundList() {
        List<Round> list = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            list.add(new Round(i, i, 1));
        }
        return list;
    }
}
