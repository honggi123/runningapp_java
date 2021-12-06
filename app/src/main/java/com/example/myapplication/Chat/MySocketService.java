package com.example.myapplication.Chat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.myapplication.Socketsingleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MySocketService extends Service {

    private String ip = "3.12.49.32";
    int port = 3001;
    public static Socket socket2;

    public BufferedReader input;
    OutputStream socketoutputStream;
    PrintWriter sendWriter;
    String mid;
    String read ="";
    IntentFilter filter = new IntentFilter();
    public final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MySocketService getService() {
            return MySocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 액티비티에서 bindService() 를 실행하면 호출됨
        // 리턴한 IBinder 객체는 서비스와 클라이언트 사이의 인터페이스 정의한다
        return mBinder; // 서비스 객체를 리턴
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public void onCreate() {
        super.onCreate(); // 서버 소켓을 실행하는 코드
    }

    public BroadcastReceiver mBroadTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("send")) {
                // 브로드캐스트 수신받았을 때 실행할 내용
                String read = intent.getStringExtra("send");
                Log.e("mysocketservice",read);
                sendmsg(read);
            }
        }
    };

    public void sendmsg(String msg){

        // 메시지 보내기
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sendWriter.println(msg);
                    sendWriter.flush();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void serverconn(){
    //소켓 연결
            new Thread() {
                public void run() {
                    try {
                        InetAddress serverAddr = InetAddress.getByName(ip);
                        socket2 = new Socket(serverAddr, port);
                        socketoutputStream = socket2.getOutputStream();
                        sendWriter = new PrintWriter(socketoutputStream);
                        input = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
                        sendWriter.println(mid);
                        sendWriter.flush();
                        Log.e("socketadd",socket2.getLocalSocketAddress()+"");

                        new Thread(new Runnable() {
                            @Override
                            public void run(){
                                String[] splited;
                                // 데이터읽기
                                while(true){
                                    try {
                                        read = input.readLine();
                                        if(read != null){
                                            Log.e("socketservice",read);
                                            Intent intent = new Intent("action");
                                            intent.setPackage("com.example.myapplication");
                                            intent.putExtra("read",read);
                                            getApplicationContext().sendBroadcast(intent);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } }}.start();
        IntentFilter intentFilter = new IntentFilter("send");
        registerReceiver(mBroadTestReceiver, intentFilter);
    }

    public String getRead() {
        return read;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class ServerThread extends Thread {

    }



}
