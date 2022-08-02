/* Assignment: HW06
   File Name: Emmala_HW06
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package com.example.emmala_hw06;

import java.util.ArrayList;
import java.util.List;

public class Forum {
    String id, forumTitle, name, userId, forumDescription, time;
    List<String> likeUsers;

    public Forum(String id, String forumTitle, String name, String userId, String forumDescription, String time) {
        this.id = id;
        this.forumTitle = forumTitle;
        this.name = name;
        this.userId = userId;
        this.forumDescription = forumDescription;
        this.time = time;
        likeUsers = new ArrayList<>();
    }

    public List<String> getLikeUsers() {
        return likeUsers;
    }

    public void setLikeUsers(List<String> likeUsers) {
        this.likeUsers = likeUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getForumDescription() {
        return forumDescription;
    }

    public void setForumDescription(String forumDescription) {
        this.forumDescription = forumDescription;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "id='" + id + '\'' +
                ", forumTitle='" + forumTitle + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", forumDescription='" + forumDescription + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
