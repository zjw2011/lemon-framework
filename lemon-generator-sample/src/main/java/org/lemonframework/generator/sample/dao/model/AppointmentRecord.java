package org.lemonframework.generator.sample.dao.model;

import java.io.Serializable;

public class AppointmentRecord implements Serializable {
    /**
     * 序列号.
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * id.
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * code.
     *
     * @mbg.generated
     */
    private String weahanCode;

    /**
     * 所属组织机构.
     *
     * @mbg.generated
     */
    private Long organizationRootId;

    /**
     * 客户端类型: 1 微信公众号 2 支付宝服务窗.
     *
     * @mbg.generated
     */
    private Integer clientId;

    /**
     * 自己使用APPID.
     *
     * @mbg.generated
     */
    private String appId;

    /**
     * 微信或者支付宝的OPENID.
     *
     * @mbg.generated
     */
    private String openid;

    /**
     * 就诊人ID.
     *
     * @mbg.generated
     */
    private Long patientId;

    /**
     * 病人姓名.
     *
     * @mbg.generated
     */
    private String patientName;

    /**
     * 预约金额（单位是分）.
     *
     * @mbg.generated
     */
    private Integer appointmentAmount;

    /**
     * 预约日期.
     *
     * @mbg.generated
     */
    private String appointmentDate;

    /**
     * 预约时间.
     *
     * @mbg.generated
     */
    private String appointmentTime;

    /**
     * 预约号码.
     *
     * @mbg.generated
     */
    private Integer appointmentNo;

    /**
     * 预约超时时间，单位是分钟.
     *
     * @mbg.generated
     */
    private Integer appointmentTimeout;

    /**
     * 预约状态: 1 待支付 2 预约成功(已支付) 3 已取号 4 已完成 5 已失效 6 已排队.
     *
     * @mbg.generated
     */
    private Byte appointmentStatus;

    /**
     * 科室ID.
     *
     * @mbg.generated
     */
    private Long officeId;

    /**
     * 科室名称.
     *
     * @mbg.generated
     */
    private String officeName;

    /**
     * 医生ID.
     *
     * @mbg.generated
     */
    private Long doctorId;

    /**
     * 医生姓名.
     *
     * @mbg.generated
     */
    private String doctorName;

    /**
     * 医生职称.
     *
     * @mbg.generated
     */
    private String doctorTitle;

    /**
     * 数据状态 0:未公开 1:正常 2:暂停 99:删除.
     *
     * @mbg.generated
     */
    private Byte dataStatus;

    /**
     * 创建人.
     *
     * @mbg.generated
     */
    private String createby;

    /**
     * 构建时间.
     *
     * @mbg.generated
     */
    private String buildtime;

    /**
     * HIS的预约挂号的结果.
     *
     * @mbg.generated
     */
    private String hisResult;

    /**
     * 取号时间.
     *
     * @mbg.generated
     */
    private String offertime;

    /**
     * 完成时间.
     *
     * @mbg.generated
     */
    private String finishtime;

    /**
     * 创建时间.
     *
     * @mbg.generated
     */
    private Long createtime;

    /**
     * 更新时间.
     *
     * @mbg.generated
     */
    private Long updatetime;

    /**
     * 行版本号.
     *
     * @mbg.generated
     */
    private Long version;

    /**
     * 医生预约流水ID.
     *
     * @mbg.generated
     */
    private Long doctorAppointmentRecordId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeahanCode() {
        return weahanCode;
    }

    public void setWeahanCode(String weahanCode) {
        this.weahanCode = weahanCode;
    }

    public Long getOrganizationRootId() {
        return organizationRootId;
    }

    public void setOrganizationRootId(Long organizationRootId) {
        this.organizationRootId = organizationRootId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getAppointmentAmount() {
        return appointmentAmount;
    }

    public void setAppointmentAmount(Integer appointmentAmount) {
        this.appointmentAmount = appointmentAmount;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Integer getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(Integer appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public Integer getAppointmentTimeout() {
        return appointmentTimeout;
    }

    public void setAppointmentTimeout(Integer appointmentTimeout) {
        this.appointmentTimeout = appointmentTimeout;
    }

    public Byte getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(Byte appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(String doctorTitle) {
        this.doctorTitle = doctorTitle;
    }

    public Byte getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Byte dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getBuildtime() {
        return buildtime;
    }

    public void setBuildtime(String buildtime) {
        this.buildtime = buildtime;
    }

    public String getHisResult() {
        return hisResult;
    }

    public void setHisResult(String hisResult) {
        this.hisResult = hisResult;
    }

    public String getOffertime() {
        return offertime;
    }

    public void setOffertime(String offertime) {
        this.offertime = offertime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getDoctorAppointmentRecordId() {
        return doctorAppointmentRecordId;
    }

    public void setDoctorAppointmentRecordId(Long doctorAppointmentRecordId) {
        this.doctorAppointmentRecordId = doctorAppointmentRecordId;
    }
}