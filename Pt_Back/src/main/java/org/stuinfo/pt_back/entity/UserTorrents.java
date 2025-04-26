package org.stuinfo.pt_back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("user_torrents")
@ApiModel(value = "UserTorrents对象", description = "")
public class UserTorrents implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("user_id")
    private Integer userId;

    @TableId("torrent_id")
    private Integer torrentId;

    private Boolean isSeeder;

    private Boolean isLeecher;

    private Long uploaded;

    private Long downloaded;

    private LocalDateTime lastUpdated;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(Integer torrentId) {
        this.torrentId = torrentId;
    }

    public Boolean getIsSeeder() {
        return isSeeder;
    }

    public void setIsSeeder(Boolean isSeeder) {
        this.isSeeder = isSeeder;
    }

    public Boolean getIsLeecher() {
        return isLeecher;
    }

    public void setIsLeecher(Boolean isLeecher) {
        this.isLeecher = isLeecher;
    }

    public Long getUploaded() {
        return uploaded;
    }

    public void setUploaded(Long uploaded) {
        this.uploaded = uploaded;
    }

    public Long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Long downloaded) {
        this.downloaded = downloaded;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "UserTorrents{" +
            "userId = " + userId +
            ", torrentId = " + torrentId +
            ", isSeeder = " + isSeeder +
            ", isLeecher = " + isLeecher +
            ", uploaded = " + uploaded +
            ", downloaded = " + downloaded +
            ", lastUpdated = " + lastUpdated +
        "}";
    }
}
