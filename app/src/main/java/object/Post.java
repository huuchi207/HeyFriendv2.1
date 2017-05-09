package object;



public class Post {
    private String urlAvatar;
    private long time;
    private String name;
    private String content;
    private String urlImage;

    public Post() {
    }

    public Post(String urlAvatar, long time, String name, String content) {
        this.urlAvatar = urlAvatar;
        this.time = time;
        this.name = name;
        this.content = content;
    }
    public Post(String urlAvatar, long time, String name, String content, String urlImage) {
        this.urlAvatar = urlAvatar;
        this.time = time;
        this.name = name;
        this.content = content;
        this.urlImage = urlImage;

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

}
