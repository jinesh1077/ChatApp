package com.jin10.chatnow;

public class Chat {

    private String sender;
    private String receiver;
    private String message;

    public Chat(String _sender,String _receiver,String _message){
        sender=_sender;
        receiver=_receiver;
        message=_message;

    }

    public Chat() {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
