package itheima.thread;

import itheima.Utils.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class ServerThread {

   public static int sendSocketId=-1;
   //标记是否修改完客户端的id值
   public static boolean flag =false;

    public static int currentId=-1;
    public static List<String> cardRecord=new ArrayList<String>();
    /**
     * actionNumebr 表示写线程要进行的动作
     * 0代表叫地主权力轮询
     * 1代表地主已经确定 和牌
     * 2代表管牌处理
     */
    public static int actionNumber=-1;
    public synchronized void modifyActionNumber(int number){
        actionNumber=number;
    }
    public synchronized void modify(int nextId){
        if(nextId==2){
            sendSocketId=0;
            flag=true;
        }else{
            sendSocketId=nextId+1;
            flag =true;
        }
        actionNumber=0;
        try{
            Thread.sleep(1000L);
            notify();
        }catch (Exception e){

        }
    }
    public synchronized void sendSingnal(){
        try{
            notify();
        }catch (Exception e){

        }
    }
    public synchronized void modifyFlag(){
        try {
            wait();
        }catch (Exception e){

        }
    }
    /**
     * 写一种特定的信号让其重新发牌
     */
    public class ReadThread implements Runnable{
        private Map<Integer,Socket> hashMap =new HashMap<Integer, Socket>();
        private int sockConNumber=-1;
        public ReadThread(Map<Integer,Socket> hashMap,int sockConNumber){
            this.hashMap=hashMap;
            this.sockConNumber=sockConNumber;
        }

        /**
         * 因为是公用代码而抽取出来的
         */
        public void changeSockIndexDeal(){
            flag=true;
            int  changeSockIndex=currentId+1;
            if(changeSockIndex>2){
                sockConNumber=0;
            }else{
                sockConNumber=changeSockIndex;
            }
            sendSingnal();
        }

        public void run() {

            try{
                if(hashMap!=null){

                    while(true){

                        Set<Integer> integers = hashMap.keySet();
                        InputStream is=null;
                        for (Integer integer : integers) {
                            if(integer==sockConNumber){
                                is=hashMap.get(sockConNumber).getInputStream();
                                break;
                            }
                        }
                        byte[] buffer =new byte[1024];
                        int length =is.read(buffer);
                        if(length!=-1){
                            String string = new String(buffer, 0, length);
                            System.out.println("接收到的消息为"+string);
                            String[] split = string.split("id:");
                            //叫份以及通知合牌的操作
                            if(actionNumber!=2){
                                int number = StringUtils.WhoCatch(split[0]);
                                int nextId = Integer.parseInt(split[1]);
                                if(number==3){
                                    actionNumber=1;
                                    sockConNumber=nextId;
                                    flag=true;
                                    sendSingnal();
                                }else{

                                    actionNumber=0;
                                    if(nextId==2){
                                        sockConNumber=0;
                                    }else{
                                        sockConNumber=nextId+1;
                                    }
                                    modify(nextId);
                                }
                            }else{//出牌的过程了
                               // cardRecord=split[0];
                                cardRecord.clear();
                                if(split[0].equals("") && split.length==2){
                                    currentId=Integer.parseInt(split[1]);
                                    for(int i=0;i<3;i++){
                                        cardRecord.add("上一位玩家已放弃本次出牌权力");
                                    }
                                    changeSockIndexDeal();

                                }else{
                                    currentId=Integer.parseInt(split[1]);
                                    for(int i=0;i<3;i++){
                                        cardRecord.add(split[0]);
                                    }
                                    changeSockIndexDeal();
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){

            }

        }
    }

    public class WriterThread implements Runnable{

        public  String threeCard;
        public  String PersonCard;

        private int defNumber=-1;
        private int threadTempId=-1;
        private int counter=0;
        private Socket socket=null;

        public WriterThread(Socket socket,int sendId,int defNumber){
            this.socket=socket;
            threadTempId=sendId;
            this.defNumber=defNumber;
            sendSocketId=sendId;
        }

        public void run() {
            try{
                if(socket!=null){
                    OutputStream outputStream = socket.getOutputStream();
                    while(true){
                        switch (counter){
                            case 0:

                                List<List<Character>> resultList = StringUtils.resultList;
                                if(resultList.size()>0){
                                    outputStream.write(((resultList.get(0)).toString()+"des:"+counter).getBytes());
                                    threeCard=resultList.get(0).toString();
                                    Thread.sleep(1000);
                                    List<Character> perPersonCard = resultList.get(defNumber+1);
                                    outputStream.write((perPersonCard.toString()+"des:"+counter).getBytes());
                                    PersonCard=perPersonCard.toString();
                                }
                                counter++;
                                break;
                            case 1:
                                if(threadTempId==defNumber){
                                    outputStream.write(("yourTurn"+"des:1").getBytes());
                                    Thread.sleep(50);
                                }
                                Thread.sleep(50);
                                counter++;
                                break;
                            case 2:
                                outputStream.write((String.valueOf(defNumber)+"des:"+counter).getBytes());
                                Thread.sleep(50);
                                counter++;
                                break;
                            default:
                                modifyFlag();
                                if(flag && actionNumber==0)
                                {
                                    if(defNumber==sendSocketId){
                                        outputStream.write(("yourTurn"+"des:1").getBytes());
                                        Thread.sleep(1000);
                                        flag=false;
                                        counter++;
                                    }else{
                                        sendSingnal();
                                    }
                                }else if(flag && actionNumber==1){
                                    if(defNumber==sendSocketId){
                                        char[] chars = threeCard.toCharArray();
                                        char[] chars1 = PersonCard.toCharArray();
                                        List<Character> li =new ArrayList<Character>();
                                        for (char aChar : chars) {
                                            li.add(aChar);
                                        }
                                        for(char aChar:chars1){
                                            li.add(aChar);
                                        }
                                        outputStream.write((li.toString()+"des:3").getBytes());
                                        Thread.sleep(1000);
                                        //对牌状态进行管理
                                        modifyActionNumber(2);
                                        //切换成2代表出牌动作了
                                        flag=false;
                                    }else{
                                        sendSingnal();
                                    }
                                }else if(flag && actionNumber==2){

                                   if(!(cardRecord.get(defNumber).equals(""))){
                                       outputStream.write((cardRecord.get(defNumber)+"current-"+currentId+"des:4").getBytes());
                                       Thread.sleep(50);
                                       sendSingnal();
                                       cardRecord.set(defNumber,"");
                                   }
                                }
                                break;
                        }
                    }
                }


            }catch (Exception e){

            }

        }
    }



}
