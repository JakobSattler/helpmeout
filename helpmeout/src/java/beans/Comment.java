/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDate;

/**
 *
 * @author Jakob
 */
public class Comment {
    private int commentid;
    private int topicid;
    private String username;
    private String text;
    private LocalDate editDate;

    public Comment() {
    }

    public Comment(int commentid, int topicid, String username, String text, LocalDate editDate) {
        this.commentid = commentid;
        this.topicid = topicid;
        this.username = username;
        this.text = text;
        this.editDate = editDate;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getEditDate() {
        return editDate;
    }

    public void setEditDate(LocalDate editDate) {
        this.editDate = editDate;
    }
}
