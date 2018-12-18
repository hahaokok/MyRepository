package itheima.Utils;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringUtils {

    public static int ready_number=0;
    public static List<List<Character>>resultList=new ArrayList<List<Character>>();
    private static String dest_str="AAAA22223333444455556666777788889999CCCC" + "JJJJQQQQKKKKGg";
    public static String deleChar(int index){
        String destr_first= dest_str.substring(0,index);
        String destr_third=dest_str.substring(index+1,dest_str.length());
        return destr_first+destr_third;
    }

   public static void sortThreeCardandPersonCard(){

        List<List<Character>>  arrList =new ArrayList<List<Character>>();
        arrList.add(lastThreeCard());
        for(int i=0;i<3;i++){
            arrList.add(perPersonCard());
        }
       resultList= arrList;
   }

    public static List<Character> lastThreeCard(){

        List<Character>arr =new ArrayList<Character>();
        int counter=0;
        Random ra =new Random();
        while(counter<3){
            int length = dest_str.length();
            int number = ra.nextInt(length - 1);
            char ch = dest_str.charAt(number);
            arr.add(ch);
            dest_str = deleChar(number);
            counter++;
        }
        return arr;
    }
    public synchronized static List<Character> perPersonCard(){

        List<Character>arr =new ArrayList<Character>();
            int counter=0;
            Random ra =new Random();
            while(counter<17){
                if(ready_number==2){
                    arr.clear();
                    for(int i=0;i<dest_str.length();i++){
                        arr.add(dest_str.charAt(i));
                    }
                    break;
                }else{
                    int length = dest_str.length();
                    int number = ra.nextInt(length - 1);
                    char ch = dest_str.charAt(number);
                    arr.add(ch);
                    dest_str = deleChar(number);
                    counter++;
                }
            }
        ready_number++;
        return arr;
    }

    public static int WhoCatch(String core){

        if(core.equals("3分")){
            return 3;
        }else if(core.equals("2分")){
            return 2;
        }else if(core.equals("1分")){
            return 1;
        }else{
            return 0;
        }
    }
}
