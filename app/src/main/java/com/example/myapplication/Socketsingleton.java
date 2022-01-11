package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.Chat.CoachUser.CChatroomActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Socketsingleton {

    private static Socketsingleton instance;
    public static Socket socket;
    private static Context ctx;
    private String ip = "3.12.49.32";
    private int port = 3001;
    public static BufferedReader input;
    OutputStream socketoutputStream;
    PrintWriter sendWriter;


    private Socketsingleton(Context context) {
            ctx = context;
        // 소켓 생성
        new Thread() {
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                    socketoutputStream = socket.getOutputStream();
                    sendWriter = new PrintWriter(socketoutputStream);
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    sendWriter.println("nova11");
                    sendWriter.flush();

                    String[] splited;

                } catch (IOException e) {
                    e.printStackTrace();
                } }}.start();
        }



    public Socketsingleton getinstance(Context context){
            ctx = context;

            if(instance== null){
                instance = new Socketsingleton(context);
            }
            return instance;
        }

    public static void setSocket(Socket socket) throws IOException {
            if(socket == null){
                Log.e("setsocket","null");
            }

        Socketsingleton.socket = socket;
    }



    public Socket getSocket(){
        if(socket == null){
            Log.e("getSocket","null");
        }else{
            Log.e("getSocket","notnull");
        }
        return socket;
        }


    public void sendmsg(String msg) throws IOException {
        socketoutputStream = getSocket().getOutputStream();
        sendWriter = new PrintWriter(socketoutputStream);
        // 메시지 보내기
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sendWriter.println(msg);
                    //  sendWriter.println(n);
                    sendWriter.flush();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        Socketsingleton.input = input;
    }

    public static synchronized Socketsingleton getInstance(Context context) {
            if (instance == null) {
                instance = new Socketsingleton(context);
            }
            return instance;
        }


}
