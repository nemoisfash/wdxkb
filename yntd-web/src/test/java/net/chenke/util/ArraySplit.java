package net.chenke.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArraySplit {  
    
    /** 
     * 2014-8-4 上午10:39:28 
     * @param args 
     *  
     */  
	public static void main(String[] args) {
//        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13,14 };
       int[] array = { 1, 2, 3, 4, 5, 6, 7};
//        String[] abc = { "a", "b"," c","d "};
        String[] abc = { "a", "b", "c"};
        List<Integer> list = new ArrayList<Integer>();
        List<String> str = new ArrayList<String>();
        for (Integer i : array) {
            list.add(i);
        }
        for (String i : abc) {
            str.add(i);
        }
        Collections.shuffle(list);
        Collections.shuffle(str);
        /*System.out.println(list.size());
        System.out.println(str.size());*/
       // int a= str.size()-1;
        int arraySumCount = list.size() / str.size();     //每组数量
        	for(int m=0;m<list.size();m++){
        		int a=m %str.size();
        		for (int i = 0; i < str.size(); i++) {
        			if (a==i) {
        				System.out.println(str.get(i)+":"+ m+":"  +"    " );
					}
        			//System.out.print(str.get(i)+":"+ (a*(m*2)+i)+":"  +"    " );
        			
        		}
        }
/*        	for(int m=0;m<=arraySumCount;m++){
        		for (int i = 0; i < str.size(); i++) {
        			if(((a*(m*2)+i))==(list.size())){
        				break;
        			}
        			System.out.print(str.get(i)+":"+ (a*(m*2)+i)+":"  +"    " );
        			
        		}
        		System.out.println();
        	}
*/        
       /* for (int i = 0; i < list.size(); i++) {
            if ((i + 1) % arraySumCount == 0) {
                for (int j = startIndex; j <= i; j++) {
                    sBuilder.append(list.get(j)).append(",");
                }
                sBuilder.append("/");
                startIndex = i + 1;
            }
        }
        String strArrayString[] = sBuilder.toString().split("/");
        for (String string : strArrayString) {
            System.out.println(string.substring(0, string.length() - 1));
        }*/
    }
  
    /** 
     * splitAry方法<br> 
     * 2014-8-4 上午10:45:36 
     * @param ary 要分割的数组 
     * @param subSize 分割的块大小 
     * @return 
     * 
     */  
    private static Object[] splitAry(int[] ary, int subSize) {  
          int count = ary.length % subSize == 0 ? ary.length / subSize: ary.length / subSize + 1;  
  
          List<List<Integer>> subAryList = new ArrayList<List<Integer>>();  
  
          for (int i = 0; i < count; i++) {  
           int index = i * subSize;  
           List<Integer> list = new ArrayList<Integer>();  
           int j = 0;  
               while (j < subSize && index < ary.length) {  
                    list.add(ary[index++]);  
                    j++;  
               }  
           subAryList.add(list);  
          }  
            
          Object[] subAry = new Object[subAryList.size()];  
            
          for(int i = 0; i < subAryList.size(); i++){  
               List<Integer> subList = subAryList.get(i);  
               int[] subAryItem = new int[subList.size()];  
               for(int j = 0; j < subList.size(); j++){  
                   subAryItem[j] = subList.get(j).intValue();  
               }  
               subAry[i] = subAryItem;  
          }  
            
          return subAry;  
         }  
}  


