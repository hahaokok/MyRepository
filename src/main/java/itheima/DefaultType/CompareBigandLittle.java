package itheima.DefaultType;

import java.util.*;

public class CompareBigandLittle {


    private static CardofType cardofType =null;

    static {
        cardofType =new CardofType();
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
    public String[] arr ={"3","4","5","6","7","8","9","10","J","Q","K","A","2","小王","大王"};
    public int getIndex(String card){
        for(int i=0;i<arr.length;i++){
            if(card.equals(arr[i])){
                return i;
            }
        }
        return -1;
    }
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
    /**
     * card:要输入的数值
     * card2:要比较的值
     * @param card
     * @param card2
     * @return 输入的值大，则true  否则 false
     *
     * 适应范围：
     *    单张比大小  1
     *    对子比大小  2
     *    三代比大小  4
     *    炸弹比大小  8
     *    card 输入的
     *    card2 要比较的
     */
    public boolean CardwithCpmpare(String card,String card2){
        int  number= cardofType.defSingleCard(card);
        int  number2= cardofType.defSingleCard(card2);
        if(number==number2 && number2==1){
           return  getIndex(card)>getIndex(card2)?true:false;
        }
        return false;
    }
    public boolean DoubleCardwithCpmpare(String card,String card2){
        int  number= cardofType.defDoubleCard(card);
        int  number2= cardofType.defDoubleCard(card2);
        if(number==number2 && number2==2){
            List<String> strings = splitCard(card);
            List<String> strings1 = splitCard(card2);
            List<Integer> arr =new ArrayList<Integer>();
            List<Integer> arr1 = new ArrayList<Integer>();

            for (String s: strings) {
                if(getIndex(s)!=-1){
                    arr.add(getIndex(s));
                }
            }

            for (String s : strings1) {
                if(getIndex(s)!=-1){
                    arr1.add(getIndex(s));
                }
            }

            return  arr.get(0)>arr1.get(0)?true:false;
        }
        return false;
    }
    public boolean ThreeCardwithCpmpare(String card,String card2){
        int  number= cardofType.defThreeSameCard(card);
        int  number2= cardofType.defThreeSameCard(card2);
        if(number==number2 && number2==4){
            List<String> strings = splitCard(card);
            List<String> strings1 = splitCard(card2);
            List<Integer> arr =new ArrayList<Integer>();
            List<Integer> arr1 = new ArrayList<Integer>();

            for (String s: strings) {
                if(getIndex(s)!=-1){
                    arr.add(getIndex(s));
                }
            }

            for (String s : strings1) {
                if(getIndex(s)!=-1){
                    arr1.add(getIndex(s));
                }
            }
            return  arr.get(0)>arr1.get(0)?true:false;
        }
        return false;
    }
    public boolean BoomCompare(String card,String card2){
        int  number= cardofType.defBoom(card);
        int  number2= cardofType.defBoom(card2);

        if(card.contains("大王") && card.contains("小王")){
            return  true;
        }
        if(card2.contains("大王") && card2.contains("小王")){
            return  false;
        }
        if(number==number2 && number2 == 8){
            List<String> strings = splitCard(card);
            List<String> strings1 = splitCard(card2);
            List<Integer> arr =new ArrayList<Integer>();
            List<Integer> arr1 = new ArrayList<Integer>();

            for (String s: strings) {
                if(getIndex(s)!=-1){
                    arr.add(getIndex(s));
                }
            }

            for (String s : strings1) {
                if(getIndex(s)!=-1){
                    arr1.add(getIndex(s));
                }
            }
            return  arr.get(0)>arr.get(0)?true:false;
        }

        if(number==8 && number2!=8){
            return true;
        }
        return  false;
    }
    /**
     * 顺子比大小
     *
     * card:要输入的
     * card2:要比较的
     */

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
    public boolean ZipCompare(String card,String card2){
        int number = cardofType.defSingleZip(card);
        int number2 = cardofType.defSingleZip(card2);
        if(number==number2 && number2==3){
            List<String> strings = splitCard(card);
            List<String> strings1 = splitCard(card2);
            List<Integer>arr =new ArrayList<Integer>();
            List<Integer>arr1 =new ArrayList<Integer>();
            for (String string : strings) {
                if(getIndex(string)!=-1){
                    arr.add(getIndex(string));
                }
            }
            for (String s : strings1) {
                if(getIndex(s)!=-1){
                    arr1.add(getIndex(s));
                }

            }

            if(arr.size()==arr1.size()){
                Integer[] objects=ObjectArrtoIntegerArr(arr);
                Integer[] objects1=ObjectArrtoIntegerArr(arr1);
                Arrays.sort(objects);
                Arrays.sort(objects1);
                return  objects[0]<objects1[0]?false:true;
            }
        }
        return false;
    }
    /**
     * 三代一比较大小
     * 三代二适用
     * card 要输入的值
     * card2 要比较的值
     */
    public boolean ThreeAddOneandCompare(String card,String card2){
        int number = cardofType.defThreeAddOneCard(card);
        int number2 = cardofType.defThreeAddOneCard(card2);
        if(number==number2 && number2==5){
            List<String> strings = splitCard(card);
            List<String> strings1 = splitCard(card2);
            Map<String,Integer>map =new HashMap<String, Integer>();
            Map<String,Integer>map1 =new HashMap<String, Integer>();

            for (String string : strings) {
                if(getIndex(string)!=-1){
                    if(map.containsKey(string)){
                        Integer integer = map.get(string);
                        integer+=1;
                        map.put(string,integer);
                    }else{
                        map.put(string,1);
                    }
                }
            }
            for (String string : strings1) {

                if(getIndex(string)!=-1){
                    if(map1.containsKey(string)){
                        Integer integer = map1.get(string);
                        integer+=1;
                        map1.put(string,integer);
                    }else{
                        map1.put(string,1);
                    }
                }
            }
            int number_card=-1;
            Set<String> strings2 = map.keySet();
            for (String s : strings2) {
                if(getIndex(s)!=-1){
                    if(map.get(s)==3){
                        number_card=getIndex(s);
                        break;
                    }
                }
            }

            int number_card2=-1;
            Set<String> strings3 = map1.keySet();
            for (String s : strings3) {
                if(getIndex(s)!=-1){
                    if(map1.get(s)==3){
                        number_card2=getIndex(s);
                        break;
                    }
                }
            }
            return number_card<number_card2?false:true;
        }
        return false;
    }

    public boolean ThreeAddTwoandCompare(String card,String card2){
        int number = cardofType.defThreeAddTwoCard(card);
        int number2 = cardofType.defThreeAddTwoCard(card2);
        if(number==number2 && number2==6){
            List<String> strings = splitCard(card);
            List<String> strings1 = splitCard(card2);
            Map<String,Integer>map =new HashMap<String, Integer>();
            Map<String,Integer>map1 =new HashMap<String, Integer>();

            for (String string : strings) {
                if(getIndex(string)!=-1){
                    if(map.containsKey(string)){
                        Integer integer = map.get(string);
                        integer+=1;
                        map.put(string,integer);
                    }else{
                        map.put(string,1);
                    }
                }
            }
            for (String string : strings1) {

                if(getIndex(string)!=-1){
                    if(map1.containsKey(string)){
                        Integer integer = map1.get(string);
                        integer+=1;
                        map1.put(string,integer);
                    }else{
                        map1.put(string,1);
                    }
                }
            }
            int number_card=-1;
            Set<String> strings2 = map.keySet();
            for (String s : strings2) {
                if(map.get(s)==3){
                    number_card=getIndex(s);
                    break;
                }
            }

            int number_card2=-1;
            Set<String> strings3 = map1.keySet();
            for (String s : strings3) {
                if(map1.get(s)==3){
                    number_card2=getIndex(s);
                    break;
                }
            }
            return number_card<number_card2?false:true;
        }
        return false;
    }
    /**
     * 飞机比较大小
     *
     * card 要输入的值
     * card2 要比较的值
     */
    public boolean CompareAriPlane(String card,String card2){
        int number = cardofType.defAirPlane(card);
        int number2= cardofType.defAirPlane(card2);

        if(number==number2 && number2==7){
            List<String> strings = splitCard(card);
            List<String> strings1 = splitCard(card2);


            Map<String,Integer> map =new HashMap<String, Integer>();
            Map<String,Integer> map1 =new HashMap<String, Integer>();
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

            for (String string : strings1) {
                if(getIndex(string)!=-1){
                    if(!map1.containsKey(string)){
                        map1.put(string,1);
                    }else{
                        Integer integer = map1.get(string);
                        integer+=1;
                        map1.put(string,integer);
                    }
                }
            }
            if(map1.size()==map.size()){
                int MinMap =100;
                Set<String> strings2 = map.keySet();
                for (String s : strings2) {
                    if(map.get(s)==3 && MinMap>getIndex(s)){
                        MinMap=getIndex(s);
                    }
                }


                int MinMap2 =100;
                Set<String> strings3 = map1.keySet();
                for (String s : strings3) {
                    if(map1.get(s)==3 && MinMap2>getIndex(s)){
                        MinMap2=getIndex(s);
                    }
                }
                return MinMap>MinMap2?true:false;
            }
        }
        return false;
    }
    /**
     * 连对的比较
     */

    public boolean LinkerDoubleCompare(String card,String card2){

        int number= cardofType.defLinkDouble(card);
        int number2= cardofType.defLinkDouble(card2);

        if(number== number2 && number2==9){
            List<String> strings = splitCard(card);
            List<String> strings1 = splitCard(card2);

            Set<Integer> arr =new HashSet<Integer>();
            Set<Integer> arr1 =new HashSet<Integer>();
            for (String string : strings) {
                if(getIndex(string)!=-1){
                    arr.add(getIndex(string));
                }
            }

            for (String s : strings1) {
                if(getIndex(s)!=-1){
                    arr1.add(getIndex(s));
                }
            }
            if(arr.size()==arr1.size()){
                int minNumber = getMinNumber(arr);
                int minNumber1 = getMinNumber(arr1);
                return minNumber>minNumber1?true:false;
            }
        }
        return  false;
    }
    public int getMinNumber(Set<Integer> set){
        int min =100;
        Iterator<Integer> iterator = set.iterator();
        while(iterator.hasNext()){
            Integer next = iterator.next();
            if(next<min){
                min=next;
            }
        }
        return min;

    }

}
