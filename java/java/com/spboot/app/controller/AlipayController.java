package com.spboot.app.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.spboot.app.config.AlipayConfig;
import com.spboot.app.pojo.Jingpaidingdan;
import com.spboot.app.pojo.Shangjia;
import com.spboot.app.service.AlipayService;
import com.spboot.app.utils.R;
import com.jntoo.db.DB;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"支付宝支付"})
@RestController
@RequestMapping("/api/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 发起支付
     * @param subject 商品名称
     * @param totalAmount 金额
     * @return 支付表单
     */
    @GetMapping("/pay")
    public R<Object> pay(@RequestParam String subject,
                         @RequestParam Double totalAmount,
                         @RequestParam String biao,
                         @RequestParam Integer id) {
        String form = alipayService.createPayment(subject, totalAmount, biao, id);
        if (form != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("form", form);
            return R.success(result);
        } else {
            return R.error("创建支付订单失败");
        }
    }

    /**
     * 查询订单支付状态（前端轮询使用）
     */
    @GetMapping("/orderStatus")
    public R<Object> orderStatus(@RequestParam Integer id) {
        Jingpaidingdan order = DB.name(Jingpaidingdan.class).find(id);
        if (order == null || order.getId() == null) {
            return R.error("订单不存在");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("isPaid", "是".equals(order.getIszf()));
        result.put("status", order.getDingdanzhuangtai());
        result.put("orderNo", order.getDingdanbianhao());
        return R.success(result);
    }

    /**
     * 支付成功后跳转页面
     */
    @GetMapping("/return")
    @Transactional(rollbackFor = Exception.class)
    public String alipayReturn(HttpServletRequest request) {
        try {
            // 转换参数Map
            Map<String, String> params = convertParameterMap(request.getParameterMap());

            // 验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getCharset(),
                    alipayConfig.getSignType()
            );

            if (signVerified) {
                // 签名验证成功
                String outTradeNo = request.getParameter("out_trade_no");
                String tradeNo = request.getParameter("trade_no");
                String totalAmount = request.getParameter("total_amount");

                // 本地调试兜底: 即使异步通知未到达，也在同步回跳里尝试落库(含幂等)
                boolean handled = handlePaidOrder(outTradeNo, totalAmount);
                if (handled) {
                    return "<script>" +
                            "alert('支付成功！订单号：" + outTradeNo + "');" +
                            "window.close();" +
                            "</script>";
                }
                return "<script>alert('支付结果验证通过，但订单更新失败，请联系管理员');window.close();</script>";
            } else {
                return "<script>alert('签名验证失败');window.close();</script>";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "<script>alert('支付验证失败');window.close();</script>";
        }
    }

    /**
     * 支付宝异步通知回调
     */
    @PostMapping("/notify")
    @Transactional(rollbackFor = Exception.class)
    public String alipayNotify(HttpServletRequest request) {
        try {
            // 转换参数Map
            Map<String, String> params = convertParameterMap(request.getParameterMap());

            // 验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getCharset(),
                    alipayConfig.getSignType()
            );

            if (signVerified) {
                String appId = request.getParameter("app_id");
                if (!alipayConfig.getAppId().equals(appId)) {
                    return "fail";
                }

                String outTradeNo = request.getParameter("out_trade_no");
                if (outTradeNo == null || outTradeNo.trim().isEmpty()) {
                    return "fail";
                }

                String tradeStatus = request.getParameter("trade_status");
                String totalAmount = request.getParameter("total_amount");

                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    boolean handled = handlePaidOrder(outTradeNo, totalAmount);
                    if (handled) {
                        return "success";
                    }
                    return "fail";
                } else {
                    return "success";
                }
            }
            return "fail";
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    private boolean handlePaidOrder(String outTradeNo, String totalAmount) {
        // 期望格式: ALI-表名-业务ID-时间戳
        String[] parts = outTradeNo.split("\\-");
        if (parts.length < 4) {
            return false;
        }
        String biao = parts[1];
        String id = parts[2];

        if (!"jingpaidingdan".equals(biao)) {
            return false;
        }

        Jingpaidingdan order = DB.name(Jingpaidingdan.class).find(id);
        if (order == null || order.getId() == null) {
            return false;
        }

        // 幂等: 已支付直接返回成功
        if ("是".equals(order.getIszf())) {
            return true;
        }

        // 金额校验，避免篡改金额
        BigDecimal expected = BigDecimal.valueOf(order.getZhifujine() == null ? 0D : order.getZhifujine())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal paid = new BigDecimal(totalAmount).setScale(2, RoundingMode.HALF_UP);
        if (expected.compareTo(paid) != 0) {
            return false;
        }

        DB.name("jingpaidingdan").where("id", order.getId()).setField("iszf", "是");
        DB.name("jingpaidingdan").where("id", order.getId()).setField("dingdanzhuangtai", "待发货");
        DB.name("shangpinxinxi").where("shangpinbianhao", order.getShangpinbianhao()).setField("shangpinzhuangtai", "已结束");

        Shangjia shangjia = DB.name(Shangjia.class).where("zhanghao", order.getFaburen()).find();
        if (shangjia != null) {
            shangjia.setQianbaoyue((shangjia.getQianbaoyue() == null ? 0D : shangjia.getQianbaoyue()) + (order.getZhifujine() == null ? 0D : order.getZhifujine()));
            DB.name(shangjia).update(shangjia);
        }
        return true;
    }

    /**
     * 转换参数Map类型
     * @param parameterMap 原始参数Map
     * @return 转换后的参数Map
     */
    private Map<String, String> convertParameterMap(Map<String, String[]> parameterMap) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values != null && values.length > 0) {
                params.put(key, values[0]); // 只取第一个值
            }
        }
        return params;
    }
}