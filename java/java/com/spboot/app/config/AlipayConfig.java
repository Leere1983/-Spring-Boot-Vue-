package com.spboot.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    // 应用ID (从沙箱获取)
    @Value("${alipay.app-id}")
    private String appId;

    // 商户私钥 (你自己生成的)
    @Value("${alipay.merchant-private-key}")
    private String merchantPrivateKey;

    // 支付宝公钥 (从沙箱获取)
    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;

    // 支付宝网关地址
    @Value("${alipay.gateway-url}")
    private String gatewayUrl;

    // 支付成功回调地址
    @Value("${alipay.notify-url}")
    private String notifyUrl;

    // 支付成功后跳转页面
    @Value("${alipay.return-url}")
    private String returnUrl;

    // 支付完成后前端跳转地址
    @Value("${alipay.front-success-url:http://localhost:8080/#/}")
    private String frontSuccessUrl;

    // 签名方式
    private String signType = "RSA2";

    // 字符编码
    private String charset = "UTF-8";

    // 支付类型
    private String format = "json";

    // Getter和Setter方法
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public void setMerchantPrivateKey(String merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getFrontSuccessUrl() {
        return frontSuccessUrl;
    }

    public void setFrontSuccessUrl(String frontSuccessUrl) {
        this.frontSuccessUrl = frontSuccessUrl;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}