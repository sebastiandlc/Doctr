package com.group.poop.doctr;

import java.util.Date;

public class ChatMessage {

    private String content;
    private String author;
    private String authorUID;
    private long time;

    public ChatMessage(String content, String author, String authorUID){
        this.content = content;
        this.author = author;
        this.authorUID = authorUID;
        this.time = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getContent(){return content;}

    public String getAuthor(){return author;}

    public long getTime(){return time;}

    public String getAuthorUID(){return authorUID;}

}
