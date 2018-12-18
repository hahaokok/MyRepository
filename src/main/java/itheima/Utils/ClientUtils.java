package itheima.Utils;

import java.io.InputStream;
import java.net.Socket;
import java.util.*;

public class ClientUtils {

    public static Map<Character,String> map =new HashMap<Character,String>();
    public static String arrCompareList[]={"3","4","5","6","7","8","9","10","J","Q","K","A","2","小王","大王"};
    public static int getIndex(String desStr){
        for(int i=0;i<arrCompareList.length;i++){
            if(desStr.equals(arrCompareList[i])){
                return i;
            }
        }
        return -1;
    }
    public static List<String> quickSorted(List<String>arrList){
        int[] arr =new int[arrList.size()];

        int counter=0;
        for(String str:arrList){
            int index = getIndex(str);
            arr[counter++]=index;
        }
        Arrays.sort(arr);

        List<String> result =new ArrayList<String>();
        for(int i=0;i<arr.length;i++){
            result.add(arrCompareList[arr[i]]);
        }

        return result;
    }
    public static String change(String str){
        List<String> list =new ArrayList<String>();
        map.put('C',"10");
        map.put('G',"大王");
        map.put('g',"小王");

        for(int i=0;i<str.length();i++){
            if(str.charAt(i)!=']' && str.charAt(i)!=',' && str.charAt(i)!='['){
                if(map.containsKey(str.charAt(i))){
                    list.add(map.get(str.charAt(i)));
                }else{
                    if(str.charAt(i)!=' '){
                        list.add(str.charAt(i)+"");
                    }
                }
            }
        }


        List<String> sorted = quickSorted(list);
        String result_str="";
        for(String result:sorted){
            System.out.print(result+" ");
            result_str+=result;
        }
        System.out.println();
        return result_str;
    }
}
