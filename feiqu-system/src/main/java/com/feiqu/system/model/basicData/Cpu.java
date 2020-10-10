package com.feiqu.system.model.basicData;

import java.io.Serializable;
import java.util.Date;

public class Cpu implements Serializable {
    private Integer id;

    private String name;

    private String remark;

    /**
     * 基础频率
     *
     * @mbg.generated
     */
    private Double basicPinlv;

    /**
     * 核心数
     *
     * @mbg.generated
     */
    private Integer coreNum;

    /**
     * 线程数
     *
     * @mbg.generated
     */
    private Integer threadNum;

    /**
     * 鲁大师跑的分数
     *
     * @mbg.generated
     */
    private Double ludashiScore;

    /**
     * 3dmark的分数
     *
     * @mbg.generated
     */
    private Double sdmarkScore;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date gmtCreate;

    /**
     * 功率 瓦数
     *
     * @mbg.generated
     */
    private Double power;

    /**
     * 多少纳米
     *
     * @mbg.generated
     */
    private Integer nami;

    /**
     * 睿频
     *
     * @mbg.generated
     */
    private Double ruiPinlv;

    /**
     * 超频
     *
     * @mbg.generated
     */
    private Double superPinlv;

    /**
     * 推荐主板
     *
     * @mbg.generated
     */
    private String mainBoard;

    /**
     * 内存类型
     *
     * @mbg.generated
     */
    private String memoryType;

    /**
     * 接口类型 NTEL LGA1200...
     *
     * @mbg.generated
     */
    private String interfaceType;

    /**
     * 额外信息
     *
     * @mbg.generated
     */
    private String extraInfo;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getBasicPinlv() {
        return basicPinlv;
    }

    public void setBasicPinlv(Double basicPinlv) {
        this.basicPinlv = basicPinlv;
    }

    public Integer getCoreNum() {
        return coreNum;
    }

    public void setCoreNum(Integer coreNum) {
        this.coreNum = coreNum;
    }

    public Integer getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }

    public Double getLudashiScore() {
        return ludashiScore;
    }

    public void setLudashiScore(Double ludashiScore) {
        this.ludashiScore = ludashiScore;
    }

    public Double getSdmarkScore() {
        return sdmarkScore;
    }

    public void setSdmarkScore(Double sdmarkScore) {
        this.sdmarkScore = sdmarkScore;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public Integer getNami() {
        return nami;
    }

    public void setNami(Integer nami) {
        this.nami = nami;
    }

    public Double getRuiPinlv() {
        return ruiPinlv;
    }

    public void setRuiPinlv(Double ruiPinlv) {
        this.ruiPinlv = ruiPinlv;
    }

    public Double getSuperPinlv() {
        return superPinlv;
    }

    public void setSuperPinlv(Double superPinlv) {
        this.superPinlv = superPinlv;
    }

    public String getMainBoard() {
        return mainBoard;
    }

    public void setMainBoard(String mainBoard) {
        this.mainBoard = mainBoard;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", remark=").append(remark);
        sb.append(", basicPinlv=").append(basicPinlv);
        sb.append(", coreNum=").append(coreNum);
        sb.append(", threadNum=").append(threadNum);
        sb.append(", ludashiScore=").append(ludashiScore);
        sb.append(", sdmarkScore=").append(sdmarkScore);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", power=").append(power);
        sb.append(", nami=").append(nami);
        sb.append(", ruiPinlv=").append(ruiPinlv);
        sb.append(", superPinlv=").append(superPinlv);
        sb.append(", mainBoard=").append(mainBoard);
        sb.append(", memoryType=").append(memoryType);
        sb.append(", interfaceType=").append(interfaceType);
        sb.append(", extraInfo=").append(extraInfo);
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
        Cpu other = (Cpu) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getBasicPinlv() == null ? other.getBasicPinlv() == null : this.getBasicPinlv().equals(other.getBasicPinlv()))
            && (this.getCoreNum() == null ? other.getCoreNum() == null : this.getCoreNum().equals(other.getCoreNum()))
            && (this.getThreadNum() == null ? other.getThreadNum() == null : this.getThreadNum().equals(other.getThreadNum()))
            && (this.getLudashiScore() == null ? other.getLudashiScore() == null : this.getLudashiScore().equals(other.getLudashiScore()))
            && (this.getSdmarkScore() == null ? other.getSdmarkScore() == null : this.getSdmarkScore().equals(other.getSdmarkScore()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getPower() == null ? other.getPower() == null : this.getPower().equals(other.getPower()))
            && (this.getNami() == null ? other.getNami() == null : this.getNami().equals(other.getNami()))
            && (this.getRuiPinlv() == null ? other.getRuiPinlv() == null : this.getRuiPinlv().equals(other.getRuiPinlv()))
            && (this.getSuperPinlv() == null ? other.getSuperPinlv() == null : this.getSuperPinlv().equals(other.getSuperPinlv()))
            && (this.getMainBoard() == null ? other.getMainBoard() == null : this.getMainBoard().equals(other.getMainBoard()))
            && (this.getMemoryType() == null ? other.getMemoryType() == null : this.getMemoryType().equals(other.getMemoryType()))
            && (this.getInterfaceType() == null ? other.getInterfaceType() == null : this.getInterfaceType().equals(other.getInterfaceType()))
            && (this.getExtraInfo() == null ? other.getExtraInfo() == null : this.getExtraInfo().equals(other.getExtraInfo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getBasicPinlv() == null) ? 0 : getBasicPinlv().hashCode());
        result = prime * result + ((getCoreNum() == null) ? 0 : getCoreNum().hashCode());
        result = prime * result + ((getThreadNum() == null) ? 0 : getThreadNum().hashCode());
        result = prime * result + ((getLudashiScore() == null) ? 0 : getLudashiScore().hashCode());
        result = prime * result + ((getSdmarkScore() == null) ? 0 : getSdmarkScore().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getPower() == null) ? 0 : getPower().hashCode());
        result = prime * result + ((getNami() == null) ? 0 : getNami().hashCode());
        result = prime * result + ((getRuiPinlv() == null) ? 0 : getRuiPinlv().hashCode());
        result = prime * result + ((getSuperPinlv() == null) ? 0 : getSuperPinlv().hashCode());
        result = prime * result + ((getMainBoard() == null) ? 0 : getMainBoard().hashCode());
        result = prime * result + ((getMemoryType() == null) ? 0 : getMemoryType().hashCode());
        result = prime * result + ((getInterfaceType() == null) ? 0 : getInterfaceType().hashCode());
        result = prime * result + ((getExtraInfo() == null) ? 0 : getExtraInfo().hashCode());
        return result;
    }
}