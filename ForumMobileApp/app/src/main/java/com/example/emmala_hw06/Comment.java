/* Assignment: HW06
   File Name: Emmala_HW06
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package com.example.emmala_hw06;

public class Comment {
    String commentCreator, commentText, id, dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Comment(String commentCreator, String commentText, String id, String dateTime) {
        this.commentCreator = commentCreator;
        this.commentText = commentText;
        this.id = id;
        this.dateTime = dateTime;
    }

    public String getCommentCreator() {
        return commentCreator;
    }

    public void setCommentCreator(String commentCreator) {
        this.commentCreator = commentCreator;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentCreator='" + commentCreator + '\'' +
                ", commentText='" + commentText + '\'' +
                ", id='" + id + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }

}
