package com.facewnd.ad.common.model;

import java.util.Date;

/**
 * @作者 wbj
 * @日期 2017年10月12日 上午11:17:57
 * @描述
 */
public class BaseSAVE{

	/**
     * 状态 0 有效 1 无效
     */
    private Short statusCode=0;

    /**
     * 软删除状态 0 正常 1 删除
     */
    private Short deleteFlag=0;

    /**
     * 备注
     */
    private String comments="";

    /**
     * 记录创建用户Id
     */
    private Long createdUserId=0L;

    /**
     * 记录生成时间
     */
    private Date createdDtm=new Date();

    /**
     * 最后变更用户Id
     */
    private Long lastUserId=0L;

    /**
     * 最后变更时间
     */
    private Date lastDtm=new Date();


    /**
     * 数字预留1
     */
    private Long intVal1=0L;
    /**
     * 数字预留2
     */
    private Long intVal2=0L;
    /**
     * 数字预留3
     */
    private Long intVal3=0L;

    /**
     * 字符预留1
     */
    private String strVal1="";

    /**
     * 字符预留2
     */
    private String strVal2="";
    /**
     * 字符预留3
     */
    private String strVal3="";

    /**
     * 获取状态 0 有效 1 无效
     *
     * @return StatusCode - 状态 0 有效 1 无效
     */
    public Short getStatusCode() {
        return statusCode;
    }

    /**
     * 设置状态 0 有效 1 无效
     *
     * @param statusCode 状态 0 有效 1 无效
     */
    public void setStatusCode(Short statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 获取软删除状态 0 正常 1 删除
     *
     * @return DeleteFlag - 软删除状态 0 正常 1 删除
     */
    public Short getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * 设置软删除状态 0 正常 1 删除
     *
     * @param deleteFlag 软删除状态 0 正常 1 删除
     */
    public void setDeleteFlag(Short deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * 获取备注
     *
     * @return Comments - 备注
     */
    public String getComments() {
        return comments;
    }

    /**
     * 设置备注
     *
     * @param comments 备注
     */
    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }

    /**
     * 获取记录创建用户Id
     *
     * @return CreatedUserId - 记录创建用户Id
     */
    public Long getCreatedUserId() {
        return createdUserId;
    }

    /**
     * 设置记录创建用户Id
     *
     * @param createdUserId 记录创建用户Id
     */
    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    /**
     * 获取记录生成时间
     *
     * @return CreatedDtm - 记录生成时间
     */
    public Date getCreatedDtm() {
        return createdDtm;
    }

    /**
     * 设置记录生成时间
     *
     * @param createdDtm 记录生成时间
     */
    public void setCreatedDtm(Date createdDtm) {
        this.createdDtm = createdDtm;
    }

    /**
     * 获取最后变更用户Id
     *
     * @return LastUserId - 最后变更用户Id
     */
    public Long getLastUserId() {
        return lastUserId;
    }

    /**
     * 设置最后变更用户Id
     *
     * @param lastUserId 最后变更用户Id
     */
    public void setLastUserId(Long lastUserId) {
        this.lastUserId = lastUserId;
    }

    /**
     * 获取最后变更时间
     *
     * @return LastDtm - 最后变更时间
     */
    public Date getLastDtm() {
        return lastDtm;
    }

    /**
     * 设置最后变更时间
     *
     * @param lastDtm 最后变更时间
     */
    public void setLastDtm(Date lastDtm) {
        this.lastDtm = lastDtm;
    }

    /**
     * 获取数字预留1
     *
     * @return IntVal1 - 数字预留1
     */
    public Long getIntVal1() {
        return intVal1;
    }

    /**
     * 设置数字预留1
     *
     * @param intVal1 数字预留1
     */
    public void setIntVal1(Long intVal1) {
        this.intVal1 = intVal1;
    }

    /**
     * 获取数字预留2
     *
     * @return IntVal2 - 数字预留2
     */
    public Long getIntVal2() {
        return intVal2;
    }

    /**
     * 设置数字预留2
     *
     * @param intVal2 数字预留2
     */
    public void setIntVal2(Long intVal2) {
        this.intVal2 = intVal2;
    }

    /**
     * 获取数字预留3
     *
     * @return IntVal3 - 数字预留3
     */
    public Long getIntVal3() {
        return intVal3;
    }

    /**
     * 设置数字预留3
     *
     * @param intVal3 数字预留3
     */
    public void setIntVal3(Long intVal3) {
        this.intVal3 = intVal3;
    }

    /**
     * 获取字符预留1
     *
     * @return StrVal1 - 字符预留1
     */
    public String getStrVal1() {
        return strVal1;
    }

    /**
     * 设置字符预留1
     *
     * @param strVal1 字符预留1
     */
    public void setStrVal1(String strVal1) {
        this.strVal1 = strVal1 == null ? null : strVal1.trim();
    }

    /**
     * 获取字符预留2
     *
     * @return StrVal2 - 字符预留2
     */
    public String getStrVal2() {
        return strVal2;
    }

    /**
     * 设置字符预留2
     *
     * @param strVal2 字符预留2
     */
    public void setStrVal2(String strVal2) {
        this.strVal2 = strVal2 == null ? null : strVal2.trim();
    }

    /**
     * 获取字符预留3
     *
     * @return StrVal3 - 字符预留3
     */
    public String getStrVal3() {
        return strVal3;
    }

    /**
     * 设置字符预留3
     *
     * @param strVal3 字符预留3
     */
    public void setStrVal3(String strVal3) {
        this.strVal3 = strVal3 == null ? null : strVal3.trim();
    }
}
