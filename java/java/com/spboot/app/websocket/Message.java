package com.spboot.app.websocket;

public class Message<T> {
    private String cmd;
    private T data;

    public Message(String cmd, T data) {
        this.cmd = cmd;
        this.data = data;
    }

    public static Message ok(){
        return new Message("ok",null);
    }


    public Message() {
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
