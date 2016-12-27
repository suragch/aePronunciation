package com.aepronunciation.ipa;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class Wrong {

    // TODO This class is not being used for much and could be removed.

    // constructor
    Wrong() {}

    /*
     * @param String contat is in the form of wrongIpa ; substitute , wrongIpa ;
     * substitute , ...
     */
    String getIpaSortedByFrequency(String concat) {

        Map<String, Integer> ipaMap = new HashMap<>();

        // parse string and put it in a frequency map
        boolean matchFound = false;
        String[] wrongList = concat.split(",");
        String[] pair = new String[2];
        for (String item : wrongList) {
            pair = item.split(";");
            // item[0] is correct answer (that was gotten wrong)
            // item[1] is substitute (wrong guess)
            for (Map.Entry<String, Integer> entry : ipaMap.entrySet()) {
                if (entry.getKey().equals(pair[0])) {
                    // increment frequency
                    ipaMap.put(entry.getKey(), entry.getValue() + 1);
                    matchFound = true;
                    break;
                }
            }
            if (matchFound) {
                matchFound = false;
            } else {
                // if no matches then add new ipa to list
                ipaMap.put(pair[0], 1);
            }
        }

        // sort by frequency
        Map<String, Integer> sorted = sortByValues(ipaMap);
        StringBuilder returnString = new StringBuilder();
        boolean firstLoop = true;
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            if (firstLoop) {
                returnString.append(entry.getKey()).append("(").append(entry.getValue()).append(")");
                firstLoop = false;
            } else {
                returnString.append(", ");
                returnString.append(entry.getKey()).append("(").append(entry.getValue()).append(")");
            }
        }

        return returnString.toString();
    }

    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable throw
     * NullPointerException if Map contains null values It also sort values even
     * if they are duplicates
     */
    private static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(
            Map<K, V> map) {
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(
                map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // LinkedHashMap will keep the keys in the order they are inserted
        // which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();

        for (Map.Entry<K, V> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}