package com.feiqu.system.model.basicData;

import java.io.Serializable;

public class Corporation implements Serializable {
    private Integer id;

    private String corporationName;

    private String valuation;

    private String valuationDate;

    private String country;

    private String industry;

    private String link;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getValuation() {
        return valuation;
    }

    public void setValuation(String valuation) {
        this.valuation = valuation;
    }

    public String getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(String valuationDate) {
        this.valuationDate = valuationDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", corporationName=").append(corporationName);
        sb.append(", valuation=").append(valuation);
        sb.append(", valuationDate=").append(valuationDate);
        sb.append(", country=").append(country);
        sb.append(", industry=").append(industry);
        sb.append(", link=").append(link);
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
        Corporation other = (Corporation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCorporationName() == null ? other.getCorporationName() == null : this.getCorporationName().equals(other.getCorporationName()))
                && (this.getValuation() == null ? other.getValuation() == null : this.getValuation().equals(other.getValuation()))
                && (this.getValuationDate() == null ? other.getValuationDate() == null : this.getValuationDate().equals(other.getValuationDate()))
                && (this.getCountry() == null ? other.getCountry() == null : this.getCountry().equals(other.getCountry()))
                && (this.getIndustry() == null ? other.getIndustry() == null : this.getIndustry().equals(other.getIndustry()))
                && (this.getLink() == null ? other.getLink() == null : this.getLink().equals(other.getLink()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCorporationName() == null) ? 0 : getCorporationName().hashCode());
        result = prime * result + ((getValuation() == null) ? 0 : getValuation().hashCode());
        result = prime * result + ((getValuationDate() == null) ? 0 : getValuationDate().hashCode());
        result = prime * result + ((getCountry() == null) ? 0 : getCountry().hashCode());
        result = prime * result + ((getIndustry() == null) ? 0 : getIndustry().hashCode());
        result = prime * result + ((getLink() == null) ? 0 : getLink().hashCode());
        return result;
    }
}