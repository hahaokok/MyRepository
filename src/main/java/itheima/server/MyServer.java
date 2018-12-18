package itheima.server;


import itheima.Utils.StringUtils;
import itheima.thread.ServerThread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MyServer {

    public static List<Socket> sockList =new ArrayList<Socket>();
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(8888);
        while(true){
            Socket accept = serverSocket.accept();
            if(accept!=null){
                if(sockList.contains(accept)){
                    continue;
                }else{
                    sockList.add(accept);
                }
            }
            if(sockList.size()==3){
                break;
            }
        }
        //生成结果集
        StringUtils.sortThreeCardandPersonCard();
        Random r =new Random();
        int ranClient = r.nextInt(3);
        Map<Integer,Socket> sockMap =new HashMap<Integer,Socket>();
        for(int i=0;i<sockList.size();i++){
            sockMap.put(i,sockList.get(i));
        }
        ServerThread serverThread = new ServerThread();
        for(int i=0;i<sockList.size();i++){
            new Thread(serverThread.new WriterThread(sockList.get(i), ranClient, i)).start();
        }
        Thread.sleep(1000);
        Thread thread = new Thread(serverThread.new ReadThread(sockMap,ranClient));
        thread.start();
    }
}
