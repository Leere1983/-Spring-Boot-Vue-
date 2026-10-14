package com.spboot.app.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.spboot.app.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AlipayService {

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 创建支付宝支付订单
     * @param subject 商品名称
     * @param totalAmount 支付金额
     * @return 支付表单HTML
     */
    public String createPayment(String subject, Double totalAmount, String biao, Integer id) {
        try {
            // 初始化AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(
                    alipayConfig.getGatewayUrl(),
                    alipayConfig.getAppId(),
                    alipayConfig.getMerchantPrivateKey(),
                    alipayConfig.getFormat(),
                    alipayConfig.getCharset(),
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getSignType()
            );

            // 创建请求对象
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

            // 业务订单号: ALI-表名-业务ID-时间戳, 便于回调落库
            String outTradeNo = buildOutTradeNo(biao, id);

            // 设置回调地址
            request.setNotifyUrl(alipayConfig.getNotifyUrl());
            request.setReturnUrl(alipayConfig.getReturnUrl());

            // 设置业务参数
            request.setBizContent("{" +
                    "\"out_trade_no\":\"" + outTradeNo + "\"," +
                    "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                    "\"total_amount\":\"" + totalAmount + "\"," +
                    "\"subject\":\"" + subject + "\"" +
                    "}");

            // 发起支付请求
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

            if (response.isSuccess()) {
                // 返回支付表单
                return response.getBody();
            } else {
                return null;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildOutTradeNo(String biao, Integer id) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ALI-" + biao + "-" + id + "-" + timestamp;
    }

    /**
     * 查询支付状态
     * @param outTradeNo 订单号
     * @return 支付状态
     */
    public boolean queryPayment(String outTradeNo) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    alipayConfig.getGatewayUrl(),
                    alipayConfig.getAppId(),
                    alipayConfig.getMerchantPrivateKey(),
                    alipayConfig.getFormat(),
                    alipayConfig.getCharset(),
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getSignType()
            );

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizContent("{" +
                    "\"out_trade_no\":\"" + outTradeNo + "\"" +
                    "}");

            AlipayTradeQueryResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                String tradeStatus = response.getTradeStatus();
                return "TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus);
            }
            return false;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
    }
}