package src;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataSort {
    public static void sortByLevel(List<CharaData> dataList) {
        Collections.sort(dataList, new Comparator<CharaData>() {
            @Override
            public int compare(CharaData d1, CharaData d2) {
                return Integer.compare(d2.getCharacterLevel(), d1.getCharacterLevel());
            }
        });
    }
}
