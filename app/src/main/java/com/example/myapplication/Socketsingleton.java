package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.Chat.CoachUser.CChatroomActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Socketsingleton {

    private static Socketsingleton instance;
    public static Socket socket;
    private static Context ctx;
    private String ip = "3.12.49.32";
    private int port = 3001;
    public static BufferedReader input;

    String mid;
    String read;



        private Socketsingleton(Context context) {
            ctx = context;
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


    public void setMid(String mid) {
        this.mid = mid;
    }
}
