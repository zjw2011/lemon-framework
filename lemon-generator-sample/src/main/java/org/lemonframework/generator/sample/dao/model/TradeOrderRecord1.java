package org.lemonframework.generator.sample.dao.model;

import java.io.Serializable;
import java.util.Date;

public class TradeOrderRecord1 implements Serializable {
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
     * 系统订单ID.
     *
     * @mbg.generated
     */
    private String tradeNo;

    /**
     * 商户订单ID.
     *
     * @mbg.generated
     */
    private String outTradeNo;

    /**
     * 渠道订单ID.
     *
     * @mbg.generated
     */
    private String chanTradeNo;

    /**
     * 合作伙伴ID或者代理商ID.
     *
     * @mbg.generated
     */
    private String partnerId;

    /**
     * 商户appid.
     *
     * @mbg.generated
     */
    private String appId;

    /**
     * 商户号.
     *
     * @mbg.generated
     */
    private String mchId;

    /**
     * 渠道ID.
     *
     * @mbg.generated
     */
    private Integer chanId;

    /**
     * 渠道appid.
     *
     * @mbg.generated
     */
    private String chanAppId;

    /**
     * 渠道子appid.
     *
     * @mbg.generated
     */
    private String chanSubappId;

    /**
     * 渠道商户号.
     *
     * @mbg.generated
     */
    private String chanMchId;

    /**
     * 渠道子商户号.
     *
     * @mbg.generated
     */
    private String chanSubmchId;

    /**
     * 交易金额.
     *
     * @mbg.generated
     */
    private Integer totalAmount;

    /**
     * 支付模式.
     *
     * @mbg.generated
     */
    private String payMode;

    /**
     * 创建IP.
     *
     * @mbg.generated
     */
    private String createIp;

    /**
     * OPENID.
     *
     * @mbg.generated
     */
    private String openId;

    /**
     * 订单名称.
     *
     * @mbg.generated
     */
    private String title;

    /**
     * 订单描述.
     *
     * @mbg.generated
     */
    private String detail;

    /**
     * 商户异步通知URL.
     *
     * @mbg.generated
     */
    private String notifyUrl;

    /**
     * 商户同步通知URL.
     *
     * @mbg.generated
     */
    private String callbakUrl;

    /**
     * 渠道异步通知URL.
     *
     * @mbg.generated
     */
    private String chanNotifyUrl;

    /**
     * 附加数据.
     *
     * @mbg.generated
     */
    private String attach;

    /**
     * 服务编码.
     *
     * @mbg.generated
     */
    private String serviceCode;

    /**
     * 银行类型.
     *
     * @mbg.generated
     */
    private String bankType;

    /**
     * 货币类型.
     *
     * @mbg.generated
     */
    private String amountType;

    /**
     * 设备信息.
     *
     * @mbg.generated
     */
    private String deviceInfo;

    /**
     * 是否订阅公众号.
     *
     * @mbg.generated
     */
    private String isSubscribe;

    /**
     * 是否订阅子公众号.
     *
     * @mbg.generated
     */
    private String isSubscribeSub;

    /**
     * 订单创建时间.
     *
     * @mbg.generated
     */
    private Date orderCreateTime;

    /**
     * 支付完成时间.
     *
     * @mbg.generated
     */
    private String payEndTime;

    /**
     * 交易状态: 1 未支付 2 支付成功 3 已关闭 4 转入退款 5 已撤销(刷卡支付) 6 用户支付中 7 支付失败(其他原因，如银行返回失败).
     *
     * @mbg.generated
     */
    private Byte tradeStatus;

    /**
     * 数据状态 0:未公开 1:正常 2:暂停 99:删除.
     *
     * @mbg.generated
     */
    private Byte dataStatus;

    /**
     * 行版本号.
     *
     * @mbg.generated
     */
    private Integer version;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getChanTradeNo() {
        return chanTradeNo;
    }

    public void setChanTradeNo(String chanTradeNo) {
        this.chanTradeNo = chanTradeNo;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Integer getChanId() {
        return chanId;
    }

    public void setChanId(Integer chanId) {
        this.chanId = chanId;
    }

    public String getChanAppId() {
        return chanAppId;
    }

    public void setChanAppId(String chanAppId) {
        this.chanAppId = chanAppId;
    }

    public String getChanSubappId() {
        return chanSubappId;
    }

    public void setChanSubappId(String chanSubappId) {
        this.chanSubappId = chanSubappId;
    }

    public String getChanMchId() {
        return chanMchId;
    }

    public void setChanMchId(String chanMchId) {
        this.chanMchId = chanMchId;
    }

    public String getChanSubmchId() {
        return chanSubmchId;
    }

    public void setChanSubmchId(String chanSubmchId) {
        this.chanSubmchId = chanSubmchId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCallbakUrl() {
        return callbakUrl;
    }

    public void setCallbakUrl(String callbakUrl) {
        this.callbakUrl = callbakUrl;
    }

    public String getChanNotifyUrl() {
        return chanNotifyUrl;
    }

    public void setChanNotifyUrl(String chanNotifyUrl) {
        this.chanNotifyUrl = chanNotifyUrl;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getIsSubscribeSub() {
        return isSubscribeSub;
    }

    public void setIsSubscribeSub(String isSubscribeSub) {
        this.isSubscribeSub = isSubscribeSub;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(String payEndTime) {
        this.payEndTime = payEndTime;
    }

    public Byte getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Byte tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Byte getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Byte dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
}