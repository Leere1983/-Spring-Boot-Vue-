package com.spboot.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jntoo.db.DB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AiQaService {

    @Value("${ai.enabled:false}")
    private boolean aiEnabled;

    @Value("${ai.api-base:https://api.openai.com/v1}")
    private String aiApiBase;

    @Value("${ai.api-key:}")
    private String aiApiKey;

    @Value("${ai.model:gpt-4o-mini}")
    private String aiModel;

    public Map<String, Object> answer(String question, String scene, String username, String role) {
        Map<String, Object> result = new HashMap<>();
        String cleanQuestion = question == null ? "" : question.trim();
        if (cleanQuestion.isEmpty()) {
            result.put("answer", "请输入问题后再发送。");
            result.put("source", "rule");
            return result;
        }
        if (cleanQuestion.length() > 500) {
            result.put("answer", "问题太长了，请精简到 500 字以内。");
            result.put("source", "rule");
            return result;
        }

        // 检查是否是商品搜索
        if (cleanQuestion.contains("搜索") || cleanQuestion.contains("查找") || cleanQuestion.contains("找") || cleanQuestion.contains("想买")) {
            Map<String, Object> searchResult = searchProducts(cleanQuestion);
            result.put("answer", searchResult.get("answer"));
            result.put("source", "rule");
            result.put("products", searchResult.get("products"));
            return result;
        }

        String ruleAnswer = answerByRule(cleanQuestion);
        if (ruleAnswer != null) {
            result.put("answer", ruleAnswer);
            result.put("source", "rule");
            return result;
        }

        String context = "";
        if (username != null && !username.trim().isEmpty()) {
            List<String> dataTypes = detectDataTypes(cleanQuestion);
            context = buildContext(username, role, dataTypes, cleanQuestion);
        }

        String llmAnswer = answerByLlm(cleanQuestion, scene, context);
        if (llmAnswer != null && !llmAnswer.trim().isEmpty()) {
            result.put("answer", llmAnswer.trim());
            result.put("source", "llm");
            return result;
        }

        result.put("answer", "当前智能问答暂时不可用，请稍后重试，或联系管理员。");
        result.put("source", "rule");
        return result;
    }

    private List<String> detectDataTypes(String question) {
        String q = question.toLowerCase(Locale.ROOT);
        List<String> types = new ArrayList<>();
        
        // 按优先级顺序检测，优先级高的先检测
        
        // 公告相关关键词（最高优先级）
        if (q.contains("公告") || q.contains("通知") || q.contains("系统消息")) {
            types.add("announcement");
            return types; // 公告问题只返回公告数据
        }
        
        // 收藏相关关键词
        if (q.contains("收藏") || q.contains("我的收藏")) {
            types.add("favorite");
            return types; // 收藏问题只返回收藏数据
        }
        
        // 留言相关关键词
        if (q.contains("留言") || q.contains("反馈") || q.contains("投诉") || q.contains("建议") || 
            q.contains("客服") || q.contains("我的留言")) {
            types.add("message");
            return types; // 留言问题只返回留言数据
        }
        
        // 评价相关关键词
        if (q.contains("评价") || q.contains("评论") || q.contains("评分") || q.contains("好评") || 
            q.contains("差评") || q.contains("我的评价")) {
            types.add("review");
            return types; // 评价问题只返回评价数据
        }
        
        // 商品搜索关键词（搜索功能优先级最高）
        if (q.contains("搜索") || q.contains("查找") || q.contains("找") || q.contains("想买")) {
            types.add("product_search");
            return types; // 搜索问题只返回搜索结果
        }
        
        // 商品相关关键词（单独询问商品时）
        if ((q.contains("商品") || q.contains("拍卖") || q.contains("竞拍")) && 
            !q.contains("订单") && !q.contains("购买") && !q.contains("支付")) {
            types.add("product");
            return types; // 纯商品问题只返回商品数据
        }
        
        // 订单相关关键词（包含商品+订单的混合问题）
        if (q.contains("订单") || q.contains("购买") || q.contains("支付") || q.contains("付款") || 
            q.contains("下单") || q.contains("待发货") || q.contains("待收货") || q.contains("已完成") ||
            (q.contains("商品") && (q.contains("买") || q.contains("付")))) {
            types.add("order");
            // 如果同时提到商品，也添加商品数据
            if (q.contains("商品") || q.contains("拍卖")) {
                types.add("product");
            }
            return types;
        }
        
        // 价格相关（可能是商品或订单）
        if (q.contains("价格") || q.contains("多少钱") || q.contains("起拍") || q.contains("出价")) {
            types.add("product");
            return types;
        }
        
        // 如果没有匹配到任何类型，默认返回订单数据
        types.add("order");
        return types;
    }

    private String answerByRule(String question) {
        String q = question.toLowerCase(Locale.ROOT);
        if (q.contains("支付") && (q.contains("失败") || q.contains("没反应") || q.contains("扫码"))) {
            return "支付后没反应通常是回调或网络延迟。请先在订单列表刷新状态；若订单已支付请勿重复支付。";
        }
        if (q.contains("重复下单") || (q.contains("下单") && q.contains("两次"))) {
            return "系统已限制同一商品重复下单。若仍出现异常，请提供订单号与商品编号给管理员排查。";
        }
        if (q.contains("发货")) {
            return "订单支付成功后状态会变为待发货。商家发货后，订单状态会更新为待收货。";
        }
        if (q.contains("收货")) {
            return "买家确认收货后，订单状态会更新为已完成。若未更新，请刷新订单列表后重试。";
        }
        if (q.contains("退款")) {
            return "当前系统暂未提供完整在线退款流程，建议联系管理员线下处理并记录订单号。";
        }
        return null;
    }

    private String answerByLlm(String question, String scene, String context) {
        if (!aiEnabled || aiApiKey == null || aiApiKey.trim().isEmpty()) {
            return null;
        }
        try {
            URL url = new URL(aiApiBase + "/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + aiApiKey);

            JSONObject body = new JSONObject();
            body.put("model", aiModel);
            body.put("temperature", 0.3);
            JSONArray messages = new JSONArray();

            JSONObject system = new JSONObject();
            system.put("role", "system");
            system.put("content",
                    "你是在线拍卖平台客服助手。只回答平台注册、竞拍、下单、支付、发货、收货、评价相关问题。" +
                    "请用简洁中文分点回答，不要编造不存在的功能，不要输出任何密钥、密码或内部配置。" +
                    "若提供了系统订单数据，请优先基于系统数据作答。");
            messages.add(system);

            JSONObject user = new JSONObject();
            user.put("role", "user");
            String content = "场景：" + (scene == null ? "auction" : scene) + "；问题：" + question;
            if (context != null && !context.trim().isEmpty()) {
                content += "；系统数据：" + context;
            }
            user.put("content", content);
            messages.add(user);

            body.put("messages", messages);

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(body.toJSONString());
            }

            int code = conn.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject resp = JSON.parseObject(sb.toString());
            JSONArray choices = resp.getJSONArray("choices");
            if (choices == null || choices.isEmpty()) {
                return null;
            }
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message == null ? null : message.getString("content");
        } catch (Exception e) {
            return null;
        }
    }

    private String buildContext(String username, String role, List<String> dataTypes, String question) {
        StringBuilder context = new StringBuilder();
        
        if (dataTypes == null || dataTypes.isEmpty()) {
            dataTypes = Arrays.asList("order");
        }
        
        for (String dataType : dataTypes) {
            switch (dataType) {
                case "order":
                    context.append(buildOrderContext(username, role));
                    break;
                case "product_search":
                    context.append(buildProductSearchContext(question));
                    break;
                case "product":
                    context.append(buildProductContext(username, role));
                    break;
                case "announcement":
                    context.append(buildAnnouncementContext());
                    break;
                case "favorite":
                    context.append(buildFavoriteContext(username));
                    break;
                case "review":
                    context.append(buildReviewContext(username, role));
                    break;
                case "message":
                    context.append(buildMessageContext(username));
                    break;
            }
            context.append("\n");
        }
        
        return context.toString();
    }

    private String buildOrderContext(String username, String role) {
        try {
            String safeUser = username.replace("'", "''");
            String querySql;
            if ("商家".equals(role)) {
                querySql = "SELECT dingdanbianhao,dingdanzhuangtai,iszf,zhifujine,addtime FROM jingpaidingdan " +
                        "WHERE faburen='" + safeUser + "' ORDER BY id DESC LIMIT 5";
            } else {
                querySql = "SELECT dingdanbianhao,dingdanzhuangtai,iszf,zhifujine,addtime FROM jingpaidingdan " +
                        "WHERE jingpairen='" + safeUser + "' ORDER BY id DESC LIMIT 5";
            }
            List<Map> rows = DB.select(querySql);
            if (rows == null || rows.isEmpty()) {
                return "【订单数据】当前用户暂无订单记录。";
            }
            StringBuilder sb = new StringBuilder("【订单数据】当前用户最近订单：");
            for (int i = 0; i < rows.size(); i++) {
                Map row = rows.get(i);
                sb.append(i + 1).append(")")
                        .append("订单号=").append(defaultStr(row.get("dingdanbianhao"))).append("，")
                        .append("状态=").append(defaultStr(row.get("dingdanzhuangtai"))).append("，")
                        .append("已支付=").append(defaultStr(row.get("iszf"))).append("，")
                        .append("金额=").append(defaultStr(row.get("zhifujine"))).append("，")
                        .append("时间=").append(defaultStr(row.get("addtime"))).append("；");
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String buildProductContext(String username, String role) {
        try {
            String safeUser = username.replace("'", "''");
            String querySql;
            if ("商家".equals(role)) {
                querySql = "SELECT shangpinbianhao,shangpinmingcheng,qipaijia,dangqianjingpaijia,shangpinzhuangtai,qipaishijian,jieshushijian FROM shangpinxinxi " +
                        "WHERE faburen='" + safeUser + "' ORDER BY id DESC LIMIT 5";
            } else {
                // 查询用户参与竞拍的商品（通过竞拍记录表关联）
                querySql = "SELECT DISTINCT s.shangpinbianhao,s.shangpinmingcheng,s.qipaijia,s.dangqianjingpaijia,s.shangpinzhuangtai,s.qipaishijian,s.jieshushijian " +
                        "FROM shangpinxinxi s INNER JOIN jingpaijilu j ON s.id = j.shangpinxinxiid " +
                        "WHERE j.jingpairen='" + safeUser + "' ORDER BY s.id DESC LIMIT 5";
            }
            List<Map> rows = DB.select(querySql);
            if (rows == null || rows.isEmpty()) {
                return "【商品数据】暂无商品信息。";
            }
            StringBuilder sb = new StringBuilder("【商品数据】");
            sb.append("商家".equals(role) ? "您发布的商品：" : "您参与竞拍的商品：");
            for (int i = 0; i < rows.size(); i++) {
                Map row = rows.get(i);
                sb.append(i + 1).append(")")
                        .append("编号=").append(defaultStr(row.get("shangpinbianhao"))).append("，")
                        .append("名称=").append(defaultStr(row.get("shangpinmingcheng"))).append("，")
                        .append("起拍价=").append(defaultStr(row.get("qipaijia"))).append("，")
                        .append("当前价=").append(defaultStr(row.get("dangqianjingpaijia"))).append("，")
                        .append("状态=").append(defaultStr(row.get("shangpinzhuangtai"))).append("；");
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    // 中文停用词列表（代词、助词、介词等）
    private static final String[] STOP_WORDS = {
        // 人称代词
        "我", "你", "他", "她", "它", "我们", "你们", "他们", "她们", "它们",
        // 疑问词
        "什么", "哪", "谁", "怎么", "为什么", "如何", "几", "多少", "哪一个", "哪一些",
        // 动词
        "想", "要", "有", "是", "在", "去", "来", "看", "买", "卖", "找", "搜索", "查找", "想买",
        // 助词
        "的", "了", "呢", "吗", "吧", "啊", "呀", "哦", "呢么", "着呢",
        // 介词
        "在", "从", "到", "向", "对", "为", "给", "跟", "和", "与", "同",
        // 连词
        "和", "与", "及", "而", "但", "却", "或", "还是", "并且", "以及",
        // 指示代词
        "这", "那", "这些", "那些", "这个", "那个", "这样", "那样",
        // 其他
        "一些", "有些", "任何", "所有", "每", "各", "自己", "别人", "大家", "人家"
    };
    
    /**
     * 清理搜索关键词中的停用词
     */
    private String cleanKeywords(String question) {
        String result = question.toLowerCase(Locale.ROOT);
        for (String word : STOP_WORDS) {
            result = result.replace(word, "");
        }
        return result.trim();
    }

    private Map<String, Object> searchProducts(String question) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> productList = new ArrayList<>();
        
        try {
            // 提取搜索关键词（使用统一的停用词清理方法）
            String keywords = cleanKeywords(question);
            
            if (keywords.isEmpty()) {
                result.put("answer", "【商品搜索】未提供搜索关键词，请输入具体商品名称进行搜索。");
                result.put("products", productList);
                return result;
            }
            
            String safeKeywords = keywords.replace("'", "''");
            
            // 搜索商品
            String querySql = "SELECT id, shangpinmingcheng, qipaijia, dangqianjingpaijia, shangpinzhuangtai, shangpintupian " +
                    "FROM shangpinxinxi WHERE shangpinmingcheng LIKE '%" + safeKeywords + "%' ORDER BY id DESC LIMIT 10";
            List<Map> rows = DB.select(querySql);
            
            if (rows == null || rows.isEmpty()) {
                querySql = "SELECT id, shangpinmingcheng, qipaijia, dangqianjingpaijia, shangpinzhuangtai, shangpintupian " +
                        "FROM shangpinxinxi ORDER BY id DESC LIMIT 5";
                rows = DB.select(querySql);
                if (rows == null || rows.isEmpty()) {
                    result.put("answer", "【商品搜索】未找到包含'" + keywords + "'的商品，当前平台暂无商品。");
                    result.put("products", productList);
                    return result;
                }
                result.put("answer", "【商品搜索】未找到包含'" + keywords + "'的商品，为您推荐以下商品：");
            } else {
                result.put("answer", "【商品搜索】找到 " + rows.size() + " 个相关商品：");
            }
            
            // 构建结构化商品列表
            for (Map row : rows) {
                Map<String, Object> product = new HashMap<>();
                product.put("id", defaultStr(row.get("id")));
                product.put("name", defaultStr(row.get("shangpinmingcheng")));
                product.put("startPrice", defaultStr(row.get("qipaijia")));
                product.put("currentPrice", defaultStr(row.get("dangqianjingpaijia")));
                product.put("status", defaultStr(row.get("shangpinzhuangtai")));
                
                String imageUrl = defaultStr(row.get("shangpintupian"));
                if (!imageUrl.isEmpty()) {
                    // 清理Markdown链接格式
                    if (imageUrl.contains("](") && imageUrl.endsWith(")")) {
                        int startIdx = imageUrl.indexOf("](") + 2;
                        int endIdx = imageUrl.lastIndexOf(")");
                        if (startIdx < endIdx) {
                            imageUrl = imageUrl.substring(startIdx, endIdx);
                        }
                    }
                    if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                        imageUrl = "http://localhost:8080" + imageUrl;
                    }
                }
                product.put("imageUrl", imageUrl);
                product.put("detailUrl", "/shangpinxinxi/detail?id=" + defaultStr(row.get("id")));
                productList.add(product);
            }
            
            result.put("products", productList);
        } catch (Exception e) {
            result.put("answer", "【商品搜索】搜索失败，请稍后重试。");
            result.put("products", productList);
        }
        
        return result;
    }

    private String buildProductSearchContext(String question) {
        try {
            // 提取搜索关键词（使用统一的停用词清理方法）
            String keywords = cleanKeywords(question);
            
            if (keywords.isEmpty()) {
                return "【商品搜索】未提供搜索关键词，请输入具体商品名称进行搜索。";
            }
            
            // 安全处理关键词
            String safeKeywords = keywords.replace("'", "''");
            
            // 搜索商品（支持名称模糊匹配）
            String querySql = "SELECT id, shangpinbianhao, shangpinmingcheng, qipaijia, dangqianjingpaijia, shangpinzhuangtai, shangpintupian " +
                    "FROM shangpinxinxi " +
                    "WHERE shangpinmingcheng LIKE '%" + safeKeywords + "%' " +
                    "ORDER BY id DESC LIMIT 10";
            
            List<Map> rows = DB.select(querySql);
            if (rows == null || rows.isEmpty()) {
                // 如果没有找到匹配的商品，返回热门商品推荐
                querySql = "SELECT id, shangpinbianhao, shangpinmingcheng, qipaijia, dangqianjingpaijia, shangpinzhuangtai, shangpintupian " +
                        "FROM shangpinxinxi " +
                        "ORDER BY id DESC LIMIT 5";
                rows = DB.select(querySql);
                if (rows == null || rows.isEmpty()) {
                    return "【商品搜索】未找到包含'" + keywords + "'的商品，当前平台暂无商品。";
                }
                StringBuilder sb = new StringBuilder("【商品搜索】未找到包含'" + keywords + "'的商品，为您推荐以下商品：\n");
                for (int i = 0; i < rows.size(); i++) {
                    Map row = rows.get(i);
                    String productId = defaultStr(row.get("id"));
                    String productName = defaultStr(row.get("shangpinmingcheng"));
                    String detailUrl = "/shangpinxinxi/detail?id=" + productId;
                    
                    sb.append(i + 1).append(") ")
                            .append("[").append(productName).append("](").append(detailUrl).append(")，")
                            .append("起拍价：").append(defaultStr(row.get("qipaijia"))).append("元，")
                            .append("当前价：").append(defaultStr(row.get("dangqianjingpaijia"))).append("元");
                    
                    String imageUrl = defaultStr(row.get("shangpintupian"));
                    if (!imageUrl.isEmpty()) {
                        // 清理Markdown链接格式 [文本](链接)，只保留URL部分
                        if (imageUrl.contains("](") && imageUrl.endsWith(")")) {
                            int startIdx = imageUrl.indexOf("](") + 2;
                            int endIdx = imageUrl.lastIndexOf(")");
                            if (startIdx < endIdx) {
                                imageUrl = imageUrl.substring(startIdx, endIdx);
                            }
                        }
                        // 将相对路径转换为完整URL
                        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                            imageUrl = "http://localhost:8080" + imageUrl;
                        }
                        sb.append("\n").append(imageUrl).append("\n");
                    }
                    sb.append("\n");
                }
                return sb.toString();
            }
            
            StringBuilder sb = new StringBuilder("【商品搜索】找到 " + rows.size() + " 个相关商品：\n");
            for (int i = 0; i < rows.size(); i++) {
                Map row = rows.get(i);
                String productId = defaultStr(row.get("id"));
                String productName = defaultStr(row.get("shangpinmingcheng"));
                String detailUrl = "/shangpinxinxi/detail?id=" + productId;
                
                sb.append(i + 1).append(") ")
                        .append("[").append(productName).append("](").append(detailUrl).append(")，")
                        .append("起拍价：").append(defaultStr(row.get("qipaijia"))).append("元，")
                        .append("当前价：").append(defaultStr(row.get("dangqianjingpaijia"))).append("元，")
                        .append("状态：").append(defaultStr(row.get("shangpinzhuangtai")));
                
                String imageUrl = defaultStr(row.get("shangpintupian"));
                if (!imageUrl.isEmpty()) {
                    // 清理Markdown链接格式 [文本](链接)，只保留URL部分
                    if (imageUrl.contains("](") && imageUrl.endsWith(")")) {
                        int startIdx = imageUrl.indexOf("](") + 2;
                        int endIdx = imageUrl.lastIndexOf(")");
                        if (startIdx < endIdx) {
                            imageUrl = imageUrl.substring(startIdx, endIdx);
                        }
                    }
                    // 将相对路径转换为完整URL
                    if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                        imageUrl = "http://localhost:8080" + imageUrl;
                    }
                    sb.append("\n").append(imageUrl).append("\n");
                }
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "【商品搜索】搜索失败，请稍后重试。";
        }
    }

    private String buildAnnouncementContext() {
        try {
            String querySql = "SELECT biaoti,faburiqi FROM gonggao WHERE issh='是' ORDER BY id DESC LIMIT 5";
            List<Map> rows = DB.select(querySql);
            if (rows == null || rows.isEmpty()) {
                return "【公告数据】暂无公告信息。";
            }
            StringBuilder sb = new StringBuilder("【公告数据】最新公告：");
            for (int i = 0; i < rows.size(); i++) {
                Map row = rows.get(i);
                sb.append(i + 1).append(")")
                        .append("标题=").append(defaultStr(row.get("biaoti"))).append("，")
                        .append("日期=").append(defaultStr(row.get("faburiqi"))).append("；");
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String buildFavoriteContext(String username) {
        try {
            String safeUser = username.replace("'", "''");
            String querySql = "SELECT biaoti,biao,addtime FROM shoucang " +
                    "WHERE username='" + safeUser + "' ORDER BY id DESC LIMIT 5";
            List<Map> rows = DB.select(querySql);
            if (rows == null || rows.isEmpty()) {
                return "【收藏数据】当前用户暂无收藏记录。";
            }
            StringBuilder sb = new StringBuilder("【收藏数据】最近收藏：");
            for (int i = 0; i < rows.size(); i++) {
                Map row = rows.get(i);
                sb.append(i + 1).append(")")
                        .append("标题=").append(defaultStr(row.get("biaoti"))).append("，")
                        .append("类型=").append(defaultStr(row.get("biao"))).append("，")
                        .append("时间=").append(defaultStr(row.get("addtime"))).append("；");
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String buildReviewContext(String username, String role) {
        try {
            String safeUser = username.replace("'", "''");
            String querySql;
            if ("商家".equals(role)) {
                querySql = "SELECT shangpinmingcheng,pingfen,pingjia,addtime FROM dingdanpingjia " +
                        "WHERE faburen='" + safeUser + "' ORDER BY id DESC LIMIT 5";
            } else {
                querySql = "SELECT shangpinmingcheng,pingfen,pingjia,addtime FROM dingdanpingjia " +
                        "WHERE jingpairen='" + safeUser + "' ORDER BY id DESC LIMIT 5";
            }
            List<Map> rows = DB.select(querySql);
            if (rows == null || rows.isEmpty()) {
                return "【评价数据】暂无评价记录。";
            }
            StringBuilder sb = new StringBuilder("【评价数据】");
            sb.append("商家".equals(role) ? "收到的评价：" : "您的评价：");
            for (int i = 0; i < rows.size(); i++) {
                Map row = rows.get(i);
                sb.append(i + 1).append(")")
                        .append("商品=").append(defaultStr(row.get("shangpinmingcheng"))).append("，")
                        .append("评分=").append(defaultStr(row.get("pingfen"))).append("，")
                        .append("内容=").append(defaultStr(row.get("pingjia"))).append("；");
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String buildMessageContext(String username) {
        try {
            String safeUser = username.replace("'", "''");
            String querySql = "SELECT xingming,fankuineirong,fankuizhuangtai,fankuihuifu,addtime FROM liuyanban " +
                    "WHERE fankuiyonghu='" + safeUser + "' ORDER BY id DESC LIMIT 5";
            List<Map> rows = DB.select(querySql);
            if (rows == null || rows.isEmpty()) {
                return "【留言数据】当前用户暂无留言记录。";
            }
            StringBuilder sb = new StringBuilder("【留言数据】最近留言：");
            for (int i = 0; i < rows.size(); i++) {
                Map row = rows.get(i);
                sb.append(i + 1).append(")")
                        .append("姓名=").append(defaultStr(row.get("xingming"))).append("，")
                        .append("内容=").append(defaultStr(row.get("fankuineirong"))).append("，")
                        .append("状态=").append(defaultStr(row.get("fankuizhuangtai"))).append("，")
                        .append("回复=").append(defaultStr(row.get("fankuihuifu"))).append("；");
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String defaultStr(Object value) {
        return value == null ? "-" : value.toString();
    }
}

