package com.ruoyi.galaxy.vo;

public class AnnounceVO {
    private String peer_id;
    private byte[] info_hash;
    private int port;
    private long downloaded;
    private long uploaded;
    private long left;
    private int numwant;
    private String ip;
    private String event;
    private String token;
    private long corrupt;
    private String key;
    private long compact;
    private long no_peer_id;
    private int supportcrypto;
    private int redundant;
    private String ipv4;
    private String ipv6;
    private String torrent;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTorrent() {
        return torrent;
    }

    public void setTorrent(String torrent) {
        this.torrent = torrent;
    }

    public long getCorrupt() {
        return corrupt;
    }

    public void setCorrupt(Long corrupt) {
        this.corrupt = corrupt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCompact() {
        return compact;
    }

    public void setCompact(Long compact) {
        this.compact = compact;
    }

    public long getNo_peer_id() {
        return no_peer_id;
    }

    public void setNo_peer_id(Long no_peer_id) {
        this.no_peer_id = no_peer_id;
    }

    public int getSupportcrypto() {
        return supportcrypto;
    }

    public void setSupportcrypto(Integer supportcrypto) {
        this.supportcrypto = supportcrypto;
    }

    public int getRedundant() {
        return redundant;
    }

    public void setRedundant(Integer redundant) {
        this.redundant = redundant;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getNumwant() {
        return numwant;
    }

    public void setNumwant(Integer numwant) {
        this.numwant = numwant;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPeer_id() {
        return peer_id;
    }

    public void setPeer_id(String peer_id) {
        this.peer_id = peer_id;
    }

    public byte[] getInfo_hash() {
        return info_hash;
    }

    public void setInfo_hash(byte[] info_hash) {
        this.info_hash = info_hash;
    }

    public int getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Long downloaded) {
        this.downloaded = downloaded;
    }

    public long getUploaded() {
        return uploaded;
    }

    public void setUploaded(Long uploaded) {
        this.uploaded = uploaded;
    }

    public long getLeft() {
        return left;
    }

    public void setLeft(Long left) {
        this.left = left;
    }
}
