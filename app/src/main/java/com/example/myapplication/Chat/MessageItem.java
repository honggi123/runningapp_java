package com.example.myapplication.Chat;

public class MessageItem {

        String id;
        String name;
        String message;
        String time;
        String pofileUrl;
        String roomid;

        int type;
        public MessageItem(String id,String name, String message, String time, String pofileUrl,int type) {
           this.id = id;
            this.name = name;
            this.message = message;
            this.time = time;
            this.pofileUrl = pofileUrl;
            this.type = type;

        }

        //firebase DB에 객체로 값을 읽어올 때..
        //파라미터가 비어있는 생성자가 핑요함.
        public MessageItem() {
        }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //Getter & Setter
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPofileUrl() {
            return pofileUrl;
        }

        public void setPofileUrl(String pofileUrl) {
            this.pofileUrl = pofileUrl;
        }

}
