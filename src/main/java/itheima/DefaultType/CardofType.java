package itheima.DefaultType;

import java.util.*;

public class CardofType {

    public String[] arr ={"3","4","5","6","7","8","9","10","J","Q","K","A","2","小王","大王"};
    public int defSingleCard(String card){
        if(card.length()<=2 && card.length()>=1){
            for (String s : arr) {
                if(card.equals(s)){
                    return 1;
                }
            }
        }
        return -1;
    }
    /**
     * 获取单张卡片所对应的下标索引
     */
    public int getIndex(String card){
      for(int i=0;i<arr.length;i++){
          if(card.equals(arr[i])){
              return i;
          }
      }
      return -1;
    }
    /*
    * 为处理是否是顺子做准备的
    * */
    public int dealArr(Integer[] objects){
        Arrays.sort(objects);
        for(int i=0;i<objects.length-1;i++){
            if((objects[i+1]-objects[i])!=1){
                return -1;
            }
        }
        return 3;
    }
    public int defDoubleCard(String card){

        if(card.length()!=4 && card.length()!=2){
            return -1;
        }
        if(card.length()==4){
            String preString = card.substring(0,2);
            String secondString=card.substring(2,4);
            if(!preString.equals("大王")&&!preString.equals("小王")&& !secondString.equals("大王") && !secondString.equals("小王")){
                if(getIndex(preString)!=-1 && getIndex(secondString)!=-1){
                    return getIndex(preString)==getIndex(secondString)?2:-1;
                }
            }
        }

        if(card.length()==2){
            String preString = card.charAt(0)+"";
            String secondString=card.charAt(1)+"";
            if(preString.equals(secondString)){
                return 2;
            }
        }
        return -1;
    }


    public Integer[] ObjectArrtoIntegerArr( List<Integer> arr){

        Object[] obj = arr.toArray();
        int length =obj.length;
        Integer[] objects =new Integer[length];
        int counter=0;
        while(counter!=length){

            objects[counter]=(Integer)obj[counter];
            counter++;
        }
        return objects;
    }
    public int defSingleZip(String card){
        if(card.contains("2") || card.contains("大王") || card.contains("小王")){
            return -1;
        }
        if( card.length()>=5){
            List<String> strings = splitCard(card);
            List<Integer> arr =new ArrayList<Integer>();
            for (String string : strings) {
                if(getIndex(string)!=-1){
                    arr.add(getIndex(string));
                }
            }
            if(arr.size()>=5){
                Integer[] objects = ObjectArrtoIntegerArr(arr);
                return dealArr(objects);
            }
        }
        return -1;
    }
    public int defThreeSameCard(String card){
        if(card.length()!=3 && card.length()!=6){
            return -1;
        }
        List<Integer> array =new ArrayList<Integer>();
       if(!card.contains("10")){
           String first = card.charAt(0) + "";
           String second = card.charAt(1) + "";
           String third = card.charAt(2)+"";
           if(first.equals(second) && second.equals(third)){
               return 4;
           }
           return -1;
       }

       if(card.contains("10")){
           String preString = card.substring(0, 2);
           String secondString = card.substring(2, 4);
           String thirdString = card.substring(4, 6);
           if(getIndex(preString)==7 && getIndex(secondString)==7 && getIndex(thirdString)==7)
               return 4;
           return -1;
       }

       return -1;
    }
    /**
     * 元素分离
     */
    public List<String> splitCard(String card){

        List<String> arr =new ArrayList<String>();
        for(int i=0;i<card.length();i++){
            arr.add(card.charAt(i)+"");
        }

        for(int i=0;i<card.length()-1;i++){
            String substring = card.substring(i, i + 2);
            arr.add(substring);
        }
        return arr;
    }
    public int defThreeAddOneCard(String card){
        if(!card.contains("大王") && !card.contains("小王")){

            if(card.length()>=4 && card.length()<=8){
                Set<Integer> setArray =new HashSet<Integer>();
                Map<String,Integer> map =new HashMap<String,Integer>();

                List<String> strings = splitCard(card);
                for (String s : strings) {
                    if(getIndex(s)!=-1){
                        if(map.containsKey(s)){
                            Integer integer = map.get(s);
                            integer+=1;
                            map.put(s,integer);
                        }else{
                           map.put(s,1);
                        }
                    }
                }

                Set<String> strings1 = map.keySet();
                int counter=0;
                for (String s : strings1) {
                    if(map.get(s)==1 || map.get(s)==3){
                        counter++;
                    }
                }
                if(counter!=2){
                    return  -1;
                }
                for (String str : strings) {
                    if(getIndex(str)!=-1){
                        setArray.add(getIndex(str));
                    }
                }
                if(setArray.size()==2){
                    return 5;
                }
            }
        }

        return -1;
    }
    public int defThreeAddTwoCard(String card){

        if(card.length()>=5 && card.length()<=8){
            Set<Integer> arrSet =new HashSet<Integer>();
            List<String> strings = splitCard(card);

            Map<String,Integer> map =new HashMap<String,Integer>();
            for (String s : strings) {
                if(getIndex(s)!=-1){
                    if(map.containsKey(s)){
                        Integer integer = map.get(s);
                        integer+=1;
                        map.put(s,integer);
                    }else{
                        map.put(s,1);
                    }
                }
            }

            Set<String> strings1 = map.keySet();
            int counter_two=0;
            int counter_three=0;
            for (String s : strings1) {
                if( map.get(s)==3){
                    counter_three++;
                }
                if(map.get(s)==2){
                    counter_two++;
                }
            }
            if(counter_two!=1|| counter_three!=1){
                return  -1;
            }
            for (String string : strings) {

                if(getIndex(string)!=-1){
                    arrSet.add(getIndex(string));
                }
            }
            if(arrSet.size()==2){
                return 6;
            }
        }
        return -1;
    }
    public int defAirPlane(String card){

        if(card.length()>=8 && card.length()<=18){

            Map<String,Integer >map =new HashMap<String,Integer>();
            Set<Integer> setArray =new HashSet<Integer>();
            List<String> strings = splitCard(card);
            for (String string : strings) {
               if(getIndex(string)!=-1){
                   if(!map.containsKey(string)){
                       map.put(string,1);
                   }else{
                       Integer integer = map.get(string);
                       integer+=1;
                       map.put(string,integer);
                   }
               }
            }
            for (String string : strings) {
                if(getIndex(string)!=-1){
                    setArray.add(getIndex(string));
                }
            }
            List<Integer> arr =new ArrayList<Integer>();
            if(setArray.size()==4 || setArray.size()==6){
                Set<String> strings1 = map.keySet();
                for (String s : strings1) {
                    if(map.get(s)==3){
                        arr.add(getIndex(s));
                    }
                }
            }
            Integer[] objects = ObjectArrtoIntegerArr(arr);
            Arrays.sort(objects);
            for(int i=0;i<objects.length-1;i++){
                if((objects[i+1]-objects[i]!=1)){
                      return -1;
                }
            }

            if(setArray.size()==4){
                Set<String> strings1 = map.keySet();
                int counter_one=0;
                int counter_two=0;
                for (String s : strings1) {
                    if(map.get(s)==1){
                        counter_one++;
                    }
                    if(map.get(s)==2){
                        counter_two++;
                    }
                }
                if((counter_one==2 && counter_two==0 ) || (counter_one==0 && counter_two==2)){
                    return 7;
                }
            }

            if(setArray.size()==6){
                Set<String> strings1 = map.keySet();
                int counter_one=0;
                int counter_two=0;
                for (String s : strings1) {
                    if(map.get(s)==1){
                        counter_one++;
                    }
                    if(map.get(s)==2){
                        counter_two++;
                    }
                }
                if((counter_one==3 && counter_two==0 ) || (counter_one==0 && counter_two==3)){
                    return 7;
                }
            }
        }
        return -1;
    }
    public int defBoom(String card){

        if(card.length()==8 || card.length()==4 ){
            if(card.contains("大王") && card.contains("小王")){
                return 8;
            }
            Set<Integer> arrSet =new HashSet<Integer>();
            List<String> strings = splitCard(card);
            for (String string : strings) {
                if(getIndex(string)!=-1){
                    arrSet.add(getIndex(string));
                }
            }
            if(arrSet.size()==1){
                return 8;
            }
        }
        return -1;
    }
    public int defLinkDouble(String card){
        if(card.contains("大王") || card.contains("小王") || card.contains("2")){
            return -1;
        }
        if(card.length()<6 || card.length()>26){
            return -1;
        }
        List<String> strings = splitCard(card);
        Map<String,Integer >map =new HashMap<String,Integer>();
        for (String string : strings) {
            if(getIndex(string)!=-1){

                if(!map.containsKey(string)){
                    map.put(string,1);
                }else{
                    Integer integer = map.get(string);
                    integer+=1;
                    map.put(string,integer);
                }

            }
        }
        Set<String> strings1 = map.keySet();
        for (String s : strings1) {
            if(map.get(s)!=2){
                return -1;
            }
        }
        List<Integer> arrIndex =new ArrayList<Integer>();
        for(String s : strings1){
            arrIndex.add(getIndex(s));
        }
        Integer[] objects = ObjectArrtoIntegerArr(arrIndex);
        Arrays.sort(objects);
        for(int i=0;i<objects.length-1;i++){
            if( (objects[i+1]-objects[i])!=1){
                return -1;
            }
        }
        return 9;
    }
    /**
     * cardType：返回的数据类型
     * 1：单
     * 2：对
     * 3：顺子
     * 4：三带
     * 5：三带一
     * 6：三代一对
     * 7：飞机
     * 8：炸弹
     * 9：连对
     */

}
