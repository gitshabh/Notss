package com.example.notesharingminiprojectapp;

public class MessageModel {

    private String msgId;
    private String senderId;
    private String message;
    private String msgType;
    String senderName;
    String fileName;

    public MessageModel(String msgId, String senderId, String message, String msgType, String senderName, String fileName) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
        this.msgType = msgType;
        this.senderName = senderName;
        this.fileName = fileName;
    }

    public MessageModel() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
