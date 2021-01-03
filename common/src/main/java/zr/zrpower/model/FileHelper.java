package zr.zrpower.model;
/**
 * 文件上传实体
 * @author nfzr
 *
 */

public class FileHelper {

    private String name;

    private String url;

    private String suffix;

    private Long size;

    private String uid;

    private Boolean  up;

    private String fileModifiedName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getUp() {
        return up;
    }

    public void setUp(Boolean up) {
        this.up = up;
    }

    public String getFileModifiedName() {
        return fileModifiedName;
    }

    public void setFileModifiedName(String fileModifiedName) {
        this.fileModifiedName = fileModifiedName;
    }
}
