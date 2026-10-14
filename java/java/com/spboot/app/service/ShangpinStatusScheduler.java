package com.spboot.app.service;

import cn.hutool.core.date.DateUtil;
import com.jntoo.db.DB;
import com.jntoo.db.utils.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ShangpinStatusScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ShangpinStatusScheduler.class);

    @Scheduled(fixedDelay = 10000)
    public void updateShangpinStatus() {
        try {
            String currentDateStr = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");

            // 更新状态为空的商品为未开始
            boolean success0 = DB.name("shangpinxinxi")
                .where("issh", "是")
                .where("shangpinzhuangtai", "=", "")
                .setField("shangpinzhuangtai", "未开始");

            boolean success1 = DB.name("shangpinxinxi")
                .where("issh", "是")
                .where("qipaishijian", "<=", currentDateStr)
                .where("jieshushijian", ">=", currentDateStr)
                .where("shangpinzhuangtai", "=", "未开始")
                .setField("shangpinzhuangtai", "竞拍中");

            // 查询结束时间已到的商品，区分已成交和已流拍
            List<Map> endedShangpin = DB.name("shangpinxinxi")
                .where("issh", "是")
                .where("jieshushijian", "<", currentDateStr)
                .where("shangpinzhuangtai", "<>", "已结束")
                .where("shangpinzhuangtai", "<>", "已成交")
                .where("shangpinzhuangtai", "<>", "已流拍")
                .select();

            int liupaiCount = 0;
            int chengjiaoCount = 0;
            for (Map sp : endedShangpin) {
                Long idLong = (Long) sp.get("id");
                Integer id = idLong != null ? idLong.intValue() : null;
                Object jingpairenshuObj = sp.get("jingpairenshu");
                long jingpairenshu = jingpairenshuObj != null ? Convert.toLong(jingpairenshuObj) : 0;
                
                if (jingpairenshu == 0) {
                    // 没有竞拍记录，标记为流拍
                    DB.name("shangpinxinxi").where("id", id).setField("shangpinzhuangtai", "已流拍");
                    liupaiCount++;
                } else {
                    // 有竞拍记录，标记为已成交
                    DB.name("shangpinxinxi").where("id", id).setField("shangpinzhuangtai", "已成交");
                    chengjiaoCount++;
                }
            }

            if (success0 || success1 || liupaiCount > 0 || chengjiaoCount > 0) {
                logger.info("商品状态更新完成，流拍: " + liupaiCount + " 件, 成交: " + chengjiaoCount + " 件");
            }

        } catch (Exception e) {
            logger.error("商品状态更新失败", e);
        }
    }
}
