package src;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataSort {
    public static void sortByLevel(List<Data> dataList) {
        Collections.sort(dataList, new Comparator<Data>() {
            @Override
            public int compare(Data d1, Data d2) {
                return Integer.compare(d2.getLevel(), d1.getLevel());
            }
        });
    }
}
