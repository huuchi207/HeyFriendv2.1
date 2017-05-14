package object;



public class Comment {
    private String content;
    private long time;
    private String name;
    private String photoUrl;
    private String uid;
    public Comment() {
    }

    public Comment(String content, long time, String name, String photoUrl,String uid) {
        this.content = content;
        this.time = time;
        this.name = name;
        this.photoUrl = photoUrl;
        this.uid = uid;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
