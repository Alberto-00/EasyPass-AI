package it.unisa.utils;

import java.util.Comparator;

public class SectorsComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        String firstStr = str1.substring(7);
        String secondStr = str2.substring(7);

        int first = Integer.parseInt(firstStr);
        int second = Integer.parseInt(secondStr);

        if (first < second)
            return -1;
        else if (first == second)
            return 0;
        return 1;
    }
}
