package itheima.client1;

import itheima.DefaultType.CardofType;
import itheima.DefaultType.CompareBigandLittle;
import itheima.Utils.ClientUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread {
    //id
    public  int threadId=-1;

    public List<String> analysisReSandCard =new ArrayList<String>();

    /**
     * flag 为false时，是地主确定过程
     * flag为true时，时打牌过程
     */
    public boolean flag =false;

    public synchronized void changeFlag(){
        flag=true;
    }
     public String playerGetCard="";
    /**
     * 记录本次出牌
     */
    public String conten="";
    public synchronized void ChangeContent(String str){
        if(!str.equals("上一位玩家已放弃本次出牌权力")){
            conten=str;
        }
    }

    boolean writeFlag =false;
    public synchronized void modifyWriteFlag(){
        writeFlag=true;
    }
    public synchronized void addAnalysisReSandCard(String str){
        if(writeFlag){
            analysisReSandCard.add(str);
        }
    }

    public synchronized void ClearContentorNot(){
        if(analysisReSandCard.size()>=4 && !analysisReSandCard.get(analysisReSandCard.size()-3).equals("过")
                && analysisReSandCard.get(analysisReSandCard.size()-2).equals("上一位玩家已放弃本次出牌权力") && analysisReSandCard.get(analysisReSandCard.size()-1).equals("上一位玩家已放弃本次出牌权力")){
            conten="";
        }
    }

    public synchronized void whoIsWin(){
        if(playerGetCard.length()==0){
            System.out.println("恭喜您胜利了!");
        }
    }
    /**
     * 出完牌，减少手持牌
     */
    public synchronized void dealGoalCard(String line){
        CardofType cardofType = new CardofType();
        List<String> strings = cardofType.splitCard(line);
        List<String> arr =new ArrayList<String>();
        for (String string : strings) {
            if(cardofType.getIndex(string)!=-1){
                arr.add(string);
            }
        }
        List<String> strings1 = cardofType.splitCard(playerGetCard);
        List<String> arr1 =new ArrayList<String>();
        for (String s : strings1) {
            if(cardofType.getIndex(s)!=-1){
                arr1.add(s);
            }
        }

        for (String s : arr) {
            arr1.remove(s);
        }
        String lastStr="";
        for (String s : arr1) {
            lastStr+=s;
        }
        playerGetCard=lastStr;
        conten="";
    }

    public class ClientReadThread implements  Runnable{
        Socket socket =null;
        public ClientReadThread(Socket socket){
            this.socket=socket;
        }
        public void run() {
            try{
                if(socket!=null){
                    InputStream is = socket.getInputStream();
                    while(true){
                        byte[] buffer =new byte[1024];
                        int length =is.read(buffer);
                        if(length!=-1){
                            String string = new String(buffer, 0, length);
                            if(string.contains(":")){
                                String[] split = string.split(":");
                                int transferNumber = Integer.parseInt(split[1]);
                                switch (transferNumber){
                                    case  0:
                                        int desc = string.indexOf("d");
                                        String substring = string.substring(0, desc);
                                        playerGetCard = ClientUtils.change(substring);
                                        break;
                                    case 1:
                                        desc = string.indexOf("d");
                                        substring = string.substring(0, desc);
                                        if(substring.equals("yourTurn")){
                                            System.out.println("是否叫地主?选择分数:1,2,3");
                                        }
                                        break;
                                    case 2:
                                        desc = string.indexOf("d");
                                        threadId=Integer.parseInt(string.substring(0,desc));
                                        break;
                                    case 3:
                                        desc = string.indexOf("d");
                                        substring = string.substring(0, desc);
                                        playerGetCard=ClientUtils.change(substring);
                                        System.out.println("请出牌,地主先生");
                                        changeFlag();
                                        break;
                                    case 4:
                                        desc = string.indexOf("d");
                                        substring = string.substring(0, desc);
                                        String[] split1 = substring.split("current-");

                                        //获取玩家出的牌
                                        ChangeContent(split1[0]);
                                        //

                                        int catChNumber=Integer.parseInt(split1[1]);
                                        char[] chars = split1[0].toCharArray();
                                        //写入数据
                                        addAnalysisReSandCard(split1[0]);
                                        ClearContentorNot();

                                        System.out.print("已经出牌:");
                                        for (char aChar : chars) {
                                            System.out.print(aChar);
                                        }
                                        System.out.println();
                                        if(catChNumber==2 && threadId==0){
                                            System.out.println("轮到您出牌:");
                                        }else if(threadId-catChNumber==1){
                                            System.out.println("轮到您出牌:");
                                        }
                                        changeFlag();
                                        break;
                                }
                            }

                        }
                    }

                }

            }catch (Exception e){

            }
        }
    }



    public class ClientWriterThread implements Runnable{

        private Socket socket=null;
        public ClientWriterThread(Socket socket){
            this.socket=socket;
        }
        public CardofType cardofType=null;
        /**
         * 判断出的牌是否在所持牌中
         */
        public boolean defContianWithinYourCard(String card){

            cardofType = new CardofType();
            List<String> strings = cardofType.splitCard(card);
            List<String> resultStr =new ArrayList<String>();
            for (String string : strings) {
                if(cardofType.getIndex(string)!=-1){
                    resultStr.add(string);
                }
            }
            for (String s : resultStr) {
                if(!playerGetCard.contains(s)){
                    return false;
                }
            }

            return  true;
        }

        /**
         * 判断牌的类型
         * @param card
         * @return
         */
        public int defCardContype(String card){
            if(cardofType!=null){
                //单
                if(cardofType.defSingleCard(card)!=-1){
                    return cardofType.defSingleCard(card);
                }
                //对
                if(cardofType.defDoubleCard(card)!=-1){
                    return cardofType.defDoubleCard(card);
                }
                //三张
                if(cardofType.defThreeSameCard(card)!=-1){
                    return cardofType.defThreeSameCard(card);
                }
                //三代一
                if(cardofType.defThreeAddOneCard(card)!=-1){
                    return cardofType.defThreeAddOneCard(card);
                }
                //三代二
                if(cardofType.defThreeAddTwoCard(card)!=-1){
                    return cardofType.defThreeAddTwoCard(card);
                }
                //顺子
                if(cardofType.defSingleZip(card)!=-1){
                    return  cardofType.defSingleZip(card);
                }
                //连对
                if(cardofType.defLinkDouble(card)!=-1){
                    return cardofType.defLinkDouble(card);
                }
                //飞机
                if(cardofType.defAirPlane(card)!=-1){
                    return cardofType.defAirPlane(card);
                }
                //炸弹
                if(cardofType.defBoom(card)!=-1){
                    return cardofType.defBoom(card);
                }
            }
            return  -1;
        }

        /**
         * 判断出的牌与已出牌的大小
         * card 要比较的牌
         * card2 手动输入的牌
         */
        public boolean defWhichisBig(String card,String card2,int number){
            CompareBigandLittle compareBigandLittle = new CompareBigandLittle();
            boolean flag =false;
            switch (number){
                case 1:
                    flag = compareBigandLittle.CardwithCpmpare(card2, card);
                    break;
                case 2:
                    flag = compareBigandLittle.DoubleCardwithCpmpare(card2, card);
                    break;
                case 4:
                    flag= compareBigandLittle.ThreeCardwithCpmpare(card2, card);
                    break;
                case 3:
                    flag=compareBigandLittle.ZipCompare(card2, card);
                    break;
                case 5:
                    flag=compareBigandLittle.ThreeAddOneandCompare(card2,card);
                    break;
                case 6:
                    flag= compareBigandLittle.ThreeAddTwoandCompare(card2, card);
                    break;
                case 7:
                    flag= compareBigandLittle.CompareAriPlane(card2, card);
                    break;
                case 9:
                    compareBigandLittle.LinkerDoubleCompare(card2, card);
                    break;
            }
            return  flag;
        }

        /**
         * 判断炸弹
         * @param card
         * @param card2
         * @return
         */
        public boolean defBoom(String card,String card2){
            CompareBigandLittle compareBigandLittle = new CompareBigandLittle();
            boolean b = compareBigandLittle.BoomCompare(card2, card);
            return b;
        }


        /**
         *
         */


        public void Method(String line){
            dealGoalCard(line);
            if(!playerGetCard.equals("")){
                System.out.println(playerGetCard);
            }
            whoIsWin();
            modifyWriteFlag();
            addAnalysisReSandCard(line);
        }
        public void run() {
            try{
                if(socket!=null){
                    OutputStream outputStream = socket.getOutputStream();
                    while(true){
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        String line = reader.readLine();

                        if(flag==false){
                            if(line.equals("3分")||line.equals("2分")||line.equals("1分")){
                                outputStream.write((line+"id:"+threadId).getBytes());
                            }
                            else{
                                System.out.println("请输入1分，2分，3分");
                            }
                        }else{

                            if(defContianWithinYourCard(line)==true || line.equals("过")){

                                if(line.equals("过")){

                                    outputStream.write(("id:"+threadId).getBytes());

                                }else{
                                    boolean fla = defContianWithinYourCard(line);
                                    if(fla == true){
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
                                        int number = defCardContype(conten);
                                        int number2= defCardContype(line);
                                        if(conten.equals("") && number2!=-1){
                                            outputStream.write((line+"id:"+threadId).getBytes());
                                            Method(line);

                                        } else if(number!=8 && number2!=8 &&number2!=-1){
                                            if(number==number2){
                                                boolean b = defWhichisBig(conten, line, number);
                                                if(b){
                                                    outputStream.write((line+"id:"+threadId).getBytes());
                                                    //手持牌减少
                                                    Method(line);
                                                }else{
                                                    System.out.println("输入的牌不合理，请重新输入");
                                                }
                                            }else{
                                                System.out.println("输入的牌不合理，请重新输入");
                                            }
                                        }else if(number!=8 && number2==8){
                                            outputStream.write((line+"id:"+threadId).getBytes());
                                            //手持牌减少
                                            Method(line);

                                        }else if(number==8 && number2==8){
                                            boolean b = defBoom(conten, line);
                                            if(b){
                                                outputStream.write((line+"id:"+threadId).getBytes());
                                                //手持牌减少
                                                Method(line);

                                            }
                                        }else{
                                            System.out.println("输入的牌不合理，请重新输入");
                                        }
                                    }
                                    else{
                                        System.out.println("输入的牌不合理，请重新输入");
                                    }
                                }
                            }else{
                                System.out.println("输入的牌不合理，请重新输入");
                            }

                        }

                    }
                }

            }catch (Exception e){

            }

        }
    }
}
