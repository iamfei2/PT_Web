package com.ruoyi.galaxy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 节点管理对象 glx_peer
 * 
 * @author HexPang
 * @date 2021-03-18
 */
public class GlxPeer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userId;

    /** 节点编号 */
    @Excel(name = "节点编号")
    private String peerId;

    /** 哈希值 **/
    @Excel(name = "信息哈希值")
    private String infoHash;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ip;

    /** 端口 */
    @Excel(name = "端口")
    private Integer port;

    /** 上传量 */
    @Excel(name = "上传量")
    private Long uploaded;

    /** 下载量 */
    @Excel(name = "下载量")
    private Long downloaded;

    /** 剩余量 */
    @Excel(name = "剩余量")
    private Long leftSize;

    /** 下载速度 */
    @Excel(name = "下载速度")
    private Long downloadSpeed;

    /** 上传速度 */
    @Excel(name = "上传速度")
    private Long uploadSpeed;

    /** 钥匙 */
    @Excel(name = "钥匙")
    private String key;

    /** 事件 */
    @Excel(name = "事件")
    private String event;

    /** 种子编号 */
    @Excel(name = "种子编号")
    private Long torrentId;

    @Excel(name = "附件编号")
    private Long attachmentId;

    @Transient
    private String torrentTitle;

    /** 状态（0正常 1暂停） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=暂停")
    private String status;

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getTorrentTitle() {
        return torrentTitle;
    }

    public void setTorrentTitle(String torrentTitle) {
        this.torrentTitle = torrentTitle;
    }

    public String getInfoHash() {
        return infoHash;
    }

    public void setInfoHash(String infoHash) {
        this.infoHash = infoHash;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setPeerId(String peerId) 
    {
        this.peerId = peerId;
    }

    public String getPeerId() 
    {
        return peerId;
    }
    public void setIp(String ip) 
    {
        this.ip = ip;
    }

    public String getIp() 
    {
        return ip;
    }
    public void setPort(Integer port) 
    {
        this.port = port;
    }

    public Integer getPort() 
    {
        return port;
    }
    public void setUploaded(Long uploaded) 
    {
        this.uploaded = uploaded;
    }

    public Long getUploaded() 
    {
        return uploaded;
    }
    public void setDownloaded(Long downloaded) 
    {
        this.downloaded = downloaded;
    }

    public Long getDownloaded() 
    {
        return downloaded;
    }
    public void setLeftSize(Long leftSize) 
    {
        this.leftSize = leftSize;
    }

    public Long getLeftSize() 
    {
        return leftSize;
    }
    public void setDownloadSpeed(Long downloadSpeed) 
    {
        this.downloadSpeed = downloadSpeed;
    }

    public Long getDownloadSpeed() 
    {
        return downloadSpeed;
    }
    public void setUploadSpeed(Long uploadSpeed) 
    {
        this.uploadSpeed = uploadSpeed;
    }

    public Long getUploadSpeed() 
    {
        return uploadSpeed;
    }
    public void setKey(String key) 
    {
        this.key = key;
    }

    public String getKey() 
    {
        return key;
    }
    public void setEvent(String event) 
    {
        this.event = event;
    }

    public String getEvent() 
    {
        return event;
    }
    public void setTorrentId(Long torrentId) 
    {
        this.torrentId = torrentId;
    }

    public Long getTorrentId() 
    {
        return torrentId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("peerId", getPeerId())
            .append("ip", getIp())
            .append("port", getPort())
            .append("uploaded", getUploaded())
            .append("downloaded", getDownloaded())
            .append("leftSize", getLeftSize())
            .append("downloadSpeed", getDownloadSpeed())
            .append("uploadSpeed", getUploadSpeed())
            .append("key", getKey())
            .append("event", getEvent())
            .append("torrentId", getTorrentId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
