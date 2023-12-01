package com.example.quickash;

public class Transaction {

    String name1, lastMessage, lastMsgTime;
    int imageId;

    public Transaction(String name1, int imageId, String lastMessage) {
        this.name1 = name1;
        this.lastMessage = lastMessage;
        this.lastMsgTime = lastMsgTime;
        this.imageId = imageId;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
