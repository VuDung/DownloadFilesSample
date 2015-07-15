package asia.covisoft.downloadsample;

import java.io.Serializable;

/**
 * Created by USER on 7/14/2015.
 */
public class Item implements Serializable{
    private int id;
    private String name;
    private String filePath;
    private String videoUrl;
    private int downloadStatus;
    private int downloadProgress;

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }



    public Item(int id, String name, String filePath, String videoUrl, int downloadStatus, int downloadProgress) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.videoUrl = videoUrl;
        this.downloadStatus = downloadStatus;
        this.downloadProgress = downloadProgress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }
}
