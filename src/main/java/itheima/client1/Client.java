package itheima.client1;

import java.net.Socket;

public class Client {

    public static Socket  socket;
    public Client(){

        try {
            socket= new Socket("127.0.0.1",8888);
            ClientThread clientThread = new ClientThread();
            Thread thread1 = new Thread(clientThread.new ClientWriterThread(socket));
            Thread thread = new Thread(clientThread.new ClientReadThread(socket));
            thread.start();
            thread1.start();

        }catch (Exception e){

        }finally {


        }

    }

    public static void main(String[] args) {
        new Client();
    }
}
