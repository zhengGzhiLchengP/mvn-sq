package com.feiqu.system.model.basicData;

import java.io.Serializable;
import java.util.Date;

public class ShellScript implements Serializable {
    private Integer id;

    /**
     * 备注
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * 类型 1:直接执行bash 2：生成文件 执行文件
     *
     * @mbg.generated
     */
    private String type;

    /**
     * 目录
     *
     * @mbg.generated
     */
    private String dictionary;

    private Date gmtCreate;

    /**
     * 操作人
     *
     * @mbg.generated
     */
    private String operator;

    /**
     * shell文件名称
     *
     * @mbg.generated
     */
    private String shellFileName;

    /**
     * shell脚本简称
     *
     * @mbg.generated
     */
    private String shellName;

    /**
     * 脚本详细内容
     *
     * @mbg.generated
     */
    private String bash;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getShellFileName() {
        return shellFileName;
    }

    public void setShellFileName(String shellFileName) {
        this.shellFileName = shellFileName;
    }

    public String getShellName() {
        return shellName;
    }

    public void setShellName(String shellName) {
        this.shellName = shellName;
    }

    public String getBash() {
        return bash;
    }

    public void setBash(String bash) {
        this.bash = bash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", remark=").append(remark);
        sb.append(", type=").append(type);
        sb.append(", dictionary=").append(dictionary);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", operator=").append(operator);
        sb.append(", shellFileName=").append(shellFileName);
        sb.append(", shellName=").append(shellName);
        sb.append(", bash=").append(bash);
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
        ShellScript other = (ShellScript) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getDictionary() == null ? other.getDictionary() == null : this.getDictionary().equals(other.getDictionary()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getOperator() == null ? other.getOperator() == null : this.getOperator().equals(other.getOperator()))
            && (this.getShellFileName() == null ? other.getShellFileName() == null : this.getShellFileName().equals(other.getShellFileName()))
            && (this.getShellName() == null ? other.getShellName() == null : this.getShellName().equals(other.getShellName()))
            && (this.getBash() == null ? other.getBash() == null : this.getBash().equals(other.getBash()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getDictionary() == null) ? 0 : getDictionary().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getOperator() == null) ? 0 : getOperator().hashCode());
        result = prime * result + ((getShellFileName() == null) ? 0 : getShellFileName().hashCode());
        result = prime * result + ((getShellName() == null) ? 0 : getShellName().hashCode());
        result = prime * result + ((getBash() == null) ? 0 : getBash().hashCode());
        return result;
    }
}