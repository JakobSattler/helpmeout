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
public class Topic {

    private int topicid;
    private int categoryid;
    private String username;
    private String title;
    private LocalDate createDate;

    public Topic() {
    }

    public Topic(int topicid, int categoryid,
            String username, String title, LocalDate createDate) {
        this.topicid = topicid;
        this.categoryid = categoryid;
        this.username = username;
        this.title = title;
        this.createDate = createDate;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }
}
