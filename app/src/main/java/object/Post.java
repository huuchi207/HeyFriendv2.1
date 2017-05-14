package object;


import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable{
    private String urlAvatar;
    private long time;
    private String name;
    private String content;
    private String urlImage;
    private String firebaseUrl;
    private ArrayList<String> likes;
    private long countOfComment;
    public Post() {
    }

    public Post(String urlAvatar, long time, String name, String content,
                String firebaseUrl, ArrayList<String> like, long countOfComment) {
        this.urlAvatar = urlAvatar;
        this.time = time;
        this.name = name;
        this.content = content;
        this.firebaseUrl = firebaseUrl;
        this.likes = like;
        this.countOfComment = countOfComment;
    }
    public Post(String urlAvatar, long time, String name, String content,
                String firebaseUrl) {
        this.urlAvatar = urlAvatar;
        this.time = time;
        this.name = name;
        this.content = content;
        this.firebaseUrl = firebaseUrl;
    }
    public Post(String urlAvatar, long time, String name, String content, String urlImage, String firebaseUrl) {
        this.urlAvatar = urlAvatar;
        this.time = time;
        this.name = name;
        this.content = content;
        this.urlImage = urlImage;
        this.firebaseUrl = firebaseUrl;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String toString(){
        return "name: "+ name+"\n"+ "content: " + content;
    }

    public String getFirebaseUrl() {
        return firebaseUrl;
    }

    public void setFirebaseUrl(String firebaseUrl) {
        this.firebaseUrl = firebaseUrl;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }
    public int getCountOfLike(){
        if (likes!= null)
            return likes.size();
        else return 0;
    }

    public long getCountOfComment() {
        return countOfComment;
    }

    public void setCountOfComment(long countOfComment) {
        this.countOfComment = countOfComment;
    }
}
