package org.stuinfo.pt_back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author lt
 * @since 2025-04-26
 */
@ApiModel(value = "Torrents对象", description = "")
public class Torrents implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "torrent_id", type = IdType.AUTO)
    private Integer torrentId;

    private String infoHash;

    private String title;

    private String description;

    private Long size;

    private Integer categoryId;

    private Integer uploaderId;

    private LocalDateTime createdAt;

    private Integer downloads;

    private Integer seeders;

    private Integer leechers;

    private Boolean isDeleted;

    public Integer getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(Integer torrentId) {
        this.torrentId = torrentId;
    }

    public String getInfoHash() {
        return infoHash;
    }

    public void setInfoHash(String infoHash) {
        this.infoHash = infoHash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Integer uploaderId) {
        this.uploaderId = uploaderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public Integer getSeeders() {
        return seeders;
    }

    public void setSeeders(Integer seeders) {
        this.seeders = seeders;
    }

    public Integer getLeechers() {
        return leechers;
    }

    public void setLeechers(Integer leechers) {
        this.leechers = leechers;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Torrents{" +
            "torrentId = " + torrentId +
            ", infoHash = " + infoHash +
            ", title = " + title +
            ", description = " + description +
            ", size = " + size +
            ", categoryId = " + categoryId +
            ", uploaderId = " + uploaderId +
            ", createdAt = " + createdAt +
            ", downloads = " + downloads +
            ", seeders = " + seeders +
            ", leechers = " + leechers +
            ", isDeleted = " + isDeleted +
        "}";
    }
}
