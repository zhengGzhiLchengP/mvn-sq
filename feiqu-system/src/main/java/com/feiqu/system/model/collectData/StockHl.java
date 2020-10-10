package com.feiqu.system.model.collectData;

import java.io.Serializable;
import java.util.Date;

public class StockHl implements Serializable {
    private Integer id;

    private String yinli;

    private String fundContent;

    private String suit;

    private String summary;

    private String techData;

    private Date gmtCreate;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYinli() {
        return yinli;
    }

    public void setYinli(String yinli) {
        this.yinli = yinli;
    }

    public String getFundContent() {
        return fundContent;
    }

    public void setFundContent(String fundContent) {
        this.fundContent = fundContent;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTechData() {
        return techData;
    }

    public void setTechData(String techData) {
        this.techData = techData;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", yinli=").append(yinli);
        sb.append(", fundContent=").append(fundContent);
        sb.append(", suit=").append(suit);
        sb.append(", summary=").append(summary);
        sb.append(", techData=").append(techData);
        sb.append(", gmtCreate=").append(gmtCreate);
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
        StockHl other = (StockHl) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getYinli() == null ? other.getYinli() == null : this.getYinli().equals(other.getYinli()))
            && (this.getFundContent() == null ? other.getFundContent() == null : this.getFundContent().equals(other.getFundContent()))
            && (this.getSuit() == null ? other.getSuit() == null : this.getSuit().equals(other.getSuit()))
            && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
            && (this.getTechData() == null ? other.getTechData() == null : this.getTechData().equals(other.getTechData()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getYinli() == null) ? 0 : getYinli().hashCode());
        result = prime * result + ((getFundContent() == null) ? 0 : getFundContent().hashCode());
        result = prime * result + ((getSuit() == null) ? 0 : getSuit().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getTechData() == null) ? 0 : getTechData().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        return result;
    }
}