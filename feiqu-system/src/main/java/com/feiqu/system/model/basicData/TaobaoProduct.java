package com.feiqu.system.model.basicData;

import java.io.Serializable;
import java.util.Date;

public class TaobaoProduct implements Serializable {
    private Integer id;

    private String linkUrl;

    /**
     * 商品描述
     *
     * @mbg.generated
     */
    private String productDesc;

    private Date createTime;

    /**
     * 淘口令
     *
     * @mbg.generated
     */
    private String taoKouLin;

    /**
     * 二维码图片
     *
     * @mbg.generated
     */
    private String qrcode;

    /**
     * 产品图片
     *
     * @mbg.generated
     */
    private String productImg;

    /**
     * 类型
     *
     * @mbg.generated
     */
    private String type;

    /**
     * 提供商
     *
     * @mbg.generated
     */
    private String provider;

    /**
     * 标签
     *
     * @mbg.generated
     */
    private String label;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTaoKouLin() {
        return taoKouLin;
    }

    public void setTaoKouLin(String taoKouLin) {
        this.taoKouLin = taoKouLin;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", linkUrl=").append(linkUrl);
        sb.append(", productDesc=").append(productDesc);
        sb.append(", createTime=").append(createTime);
        sb.append(", taoKouLin=").append(taoKouLin);
        sb.append(", qrcode=").append(qrcode);
        sb.append(", productImg=").append(productImg);
        sb.append(", type=").append(type);
        sb.append(", provider=").append(provider);
        sb.append(", label=").append(label);
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
        TaobaoProduct other = (TaobaoProduct) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getLinkUrl() == null ? other.getLinkUrl() == null : this.getLinkUrl().equals(other.getLinkUrl()))
                && (this.getProductDesc() == null ? other.getProductDesc() == null : this.getProductDesc().equals(other.getProductDesc()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getTaoKouLin() == null ? other.getTaoKouLin() == null : this.getTaoKouLin().equals(other.getTaoKouLin()))
                && (this.getQrcode() == null ? other.getQrcode() == null : this.getQrcode().equals(other.getQrcode()))
                && (this.getProductImg() == null ? other.getProductImg() == null : this.getProductImg().equals(other.getProductImg()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getProvider() == null ? other.getProvider() == null : this.getProvider().equals(other.getProvider()))
                && (this.getLabel() == null ? other.getLabel() == null : this.getLabel().equals(other.getLabel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLinkUrl() == null) ? 0 : getLinkUrl().hashCode());
        result = prime * result + ((getProductDesc() == null) ? 0 : getProductDesc().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getTaoKouLin() == null) ? 0 : getTaoKouLin().hashCode());
        result = prime * result + ((getQrcode() == null) ? 0 : getQrcode().hashCode());
        result = prime * result + ((getProductImg() == null) ? 0 : getProductImg().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getProvider() == null) ? 0 : getProvider().hashCode());
        result = prime * result + ((getLabel() == null) ? 0 : getLabel().hashCode());
        return result;
    }
}