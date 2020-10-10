package com.feiqu.system.model.basicData;

import java.io.Serializable;

public class WsLog implements Serializable {
    private Long id;

    private Double bytespertcpreceive;

    private Integer decodeerrorcount;

    private Long duration;

    private Integer durationtype;

    private String formatedDuration;

    private Long handledBytes;

    private Float handledCostsPerPacket;

    private Integer handledPacketCosts;

    private Integer handledPackets;

    private String ip;

    private Double packetsPerTcpReceive;

    private Long receivedBytes;

    private Integer receivedPackets;

    private Integer receivedTcps;

    private Integer requestCount;

    private Long sentBytes;

    private Integer sentPackets;

    private String start;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBytespertcpreceive() {
        return bytespertcpreceive;
    }

    public void setBytespertcpreceive(Double bytespertcpreceive) {
        this.bytespertcpreceive = bytespertcpreceive;
    }

    public Integer getDecodeerrorcount() {
        return decodeerrorcount;
    }

    public void setDecodeerrorcount(Integer decodeerrorcount) {
        this.decodeerrorcount = decodeerrorcount;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getDurationtype() {
        return durationtype;
    }

    public void setDurationtype(Integer durationtype) {
        this.durationtype = durationtype;
    }

    public String getFormatedDuration() {
        return formatedDuration;
    }

    public void setFormatedDuration(String formatedDuration) {
        this.formatedDuration = formatedDuration;
    }

    public Long getHandledBytes() {
        return handledBytes;
    }

    public void setHandledBytes(Long handledBytes) {
        this.handledBytes = handledBytes;
    }

    public Float getHandledCostsPerPacket() {
        return handledCostsPerPacket;
    }

    public void setHandledCostsPerPacket(Float handledCostsPerPacket) {
        this.handledCostsPerPacket = handledCostsPerPacket;
    }

    public Integer getHandledPacketCosts() {
        return handledPacketCosts;
    }

    public void setHandledPacketCosts(Integer handledPacketCosts) {
        this.handledPacketCosts = handledPacketCosts;
    }

    public Integer getHandledPackets() {
        return handledPackets;
    }

    public void setHandledPackets(Integer handledPackets) {
        this.handledPackets = handledPackets;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getPacketsPerTcpReceive() {
        return packetsPerTcpReceive;
    }

    public void setPacketsPerTcpReceive(Double packetsPerTcpReceive) {
        this.packetsPerTcpReceive = packetsPerTcpReceive;
    }

    public Long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Integer getReceivedPackets() {
        return receivedPackets;
    }

    public void setReceivedPackets(Integer receivedPackets) {
        this.receivedPackets = receivedPackets;
    }

    public Integer getReceivedTcps() {
        return receivedTcps;
    }

    public void setReceivedTcps(Integer receivedTcps) {
        this.receivedTcps = receivedTcps;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(Long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public Integer getSentPackets() {
        return sentPackets;
    }

    public void setSentPackets(Integer sentPackets) {
        this.sentPackets = sentPackets;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", bytespertcpreceive=").append(bytespertcpreceive);
        sb.append(", decodeerrorcount=").append(decodeerrorcount);
        sb.append(", duration=").append(duration);
        sb.append(", durationtype=").append(durationtype);
        sb.append(", formatedDuration=").append(formatedDuration);
        sb.append(", handledBytes=").append(handledBytes);
        sb.append(", handledCostsPerPacket=").append(handledCostsPerPacket);
        sb.append(", handledPacketCosts=").append(handledPacketCosts);
        sb.append(", handledPackets=").append(handledPackets);
        sb.append(", ip=").append(ip);
        sb.append(", packetsPerTcpReceive=").append(packetsPerTcpReceive);
        sb.append(", receivedBytes=").append(receivedBytes);
        sb.append(", receivedPackets=").append(receivedPackets);
        sb.append(", receivedTcps=").append(receivedTcps);
        sb.append(", requestCount=").append(requestCount);
        sb.append(", sentBytes=").append(sentBytes);
        sb.append(", sentPackets=").append(sentPackets);
        sb.append(", start=").append(start);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WsLog other = (WsLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBytespertcpreceive() == null ? other.getBytespertcpreceive() == null : this.getBytespertcpreceive().equals(other.getBytespertcpreceive()))
                && (this.getDecodeerrorcount() == null ? other.getDecodeerrorcount() == null : this.getDecodeerrorcount().equals(other.getDecodeerrorcount()))
                && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
                && (this.getDurationtype() == null ? other.getDurationtype() == null : this.getDurationtype().equals(other.getDurationtype()))
                && (this.getFormatedDuration() == null ? other.getFormatedDuration() == null : this.getFormatedDuration().equals(other.getFormatedDuration()))
                && (this.getHandledBytes() == null ? other.getHandledBytes() == null : this.getHandledBytes().equals(other.getHandledBytes()))
                && (this.getHandledCostsPerPacket() == null ? other.getHandledCostsPerPacket() == null : this.getHandledCostsPerPacket().equals(other.getHandledCostsPerPacket()))
                && (this.getHandledPacketCosts() == null ? other.getHandledPacketCosts() == null : this.getHandledPacketCosts().equals(other.getHandledPacketCosts()))
                && (this.getHandledPackets() == null ? other.getHandledPackets() == null : this.getHandledPackets().equals(other.getHandledPackets()))
                && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
                && (this.getPacketsPerTcpReceive() == null ? other.getPacketsPerTcpReceive() == null : this.getPacketsPerTcpReceive().equals(other.getPacketsPerTcpReceive()))
                && (this.getReceivedBytes() == null ? other.getReceivedBytes() == null : this.getReceivedBytes().equals(other.getReceivedBytes()))
                && (this.getReceivedPackets() == null ? other.getReceivedPackets() == null : this.getReceivedPackets().equals(other.getReceivedPackets()))
                && (this.getReceivedTcps() == null ? other.getReceivedTcps() == null : this.getReceivedTcps().equals(other.getReceivedTcps()))
                && (this.getRequestCount() == null ? other.getRequestCount() == null : this.getRequestCount().equals(other.getRequestCount()))
                && (this.getSentBytes() == null ? other.getSentBytes() == null : this.getSentBytes().equals(other.getSentBytes()))
                && (this.getSentPackets() == null ? other.getSentPackets() == null : this.getSentPackets().equals(other.getSentPackets()))
                && (this.getStart() == null ? other.getStart() == null : this.getStart().equals(other.getStart()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBytespertcpreceive() == null) ? 0 : getBytespertcpreceive().hashCode());
        result = prime * result + ((getDecodeerrorcount() == null) ? 0 : getDecodeerrorcount().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + ((getDurationtype() == null) ? 0 : getDurationtype().hashCode());
        result = prime * result + ((getFormatedDuration() == null) ? 0 : getFormatedDuration().hashCode());
        result = prime * result + ((getHandledBytes() == null) ? 0 : getHandledBytes().hashCode());
        result = prime * result + ((getHandledCostsPerPacket() == null) ? 0 : getHandledCostsPerPacket().hashCode());
        result = prime * result + ((getHandledPacketCosts() == null) ? 0 : getHandledPacketCosts().hashCode());
        result = prime * result + ((getHandledPackets() == null) ? 0 : getHandledPackets().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getPacketsPerTcpReceive() == null) ? 0 : getPacketsPerTcpReceive().hashCode());
        result = prime * result + ((getReceivedBytes() == null) ? 0 : getReceivedBytes().hashCode());
        result = prime * result + ((getReceivedPackets() == null) ? 0 : getReceivedPackets().hashCode());
        result = prime * result + ((getReceivedTcps() == null) ? 0 : getReceivedTcps().hashCode());
        result = prime * result + ((getRequestCount() == null) ? 0 : getRequestCount().hashCode());
        result = prime * result + ((getSentBytes() == null) ? 0 : getSentBytes().hashCode());
        result = prime * result + ((getSentPackets() == null) ? 0 : getSentPackets().hashCode());
        result = prime * result + ((getStart() == null) ? 0 : getStart().hashCode());
        return result;
    }
}