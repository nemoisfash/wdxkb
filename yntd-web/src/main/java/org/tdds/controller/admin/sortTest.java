package org.tdds.controller.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class sortTest {
	    public static void main(String[] args) {
	        Map<String, Double> map = new HashMap<String, Double>();
	        map.put("1", 100D);
	        map.put("2", 50D);
	        map.put("3", 75D);
	        map.put("4", 32D);
	        map.put("5", 235D);
	        map.put("6", 42D);
	        map.put("7", 12D);
	        map.put("8", 33D);
	        map.put("9", 221D);
	 
	        List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
	        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
	        	@Override
	            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
	                return -o1.getValue().compareTo(o2.getValue());
	            }
	        });
	 
	        for (Map.Entry<String, Double> mapping : list) {
	            System.out.println(mapping.getKey() + ":" + mapping.getValue());
	        }
	    }
	}
