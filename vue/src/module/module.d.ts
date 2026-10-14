
declare interface EModuleForm<T>{
    form:T;
}

declare interface EModuleReadForm<T,R>{
    form:T;
    readMap:R;
}



// "管理员" 模块结构。
declare interface EAdmins {
    // 帐号 。
    username?:string;
    // 密码 。
    pwd?:string;
        
}

declare type EAdminsForm = EModuleForm<EAdmins>;

// "用户" 模块结构。
declare interface EYonghu {
    // 用户名 。
    yonghuming?:string;
    // 密码 。
    mima?:string;
    // 姓名 。
    xingming?:string;
    // 性别 。
    // 可选项:男,女。
    xingbie?:string;
    // 钱包余额 。
    qianbaoyue?:number;
    // 手机 。
    shouji?:string;
    // 邮箱 。
    youxiang?:string;
    // 身份证 。
    shenfenzheng?:string;
    // 头像 。
    touxiang?:string;
            // 用户提现的数量。
    yonghutixianCount?:number;

}

declare type EYonghuForm = EModuleForm<EYonghu>;

// "留言反馈" 模块结构。
declare interface ELiuyanban {
    // 姓名 。
    xingming?:string;
    // 联系电话 。
    lianxidianhua?:string;
    // 反馈内容 。
    fankuineirong?:string;
    // 反馈用户 。
    fankuiyonghu?:string;
    // 反馈状态 。
    // 可选项:待回复,已回复。
    fankuizhuangtai?:"待回复"|"已回复";
    // 反馈时间 。
    addtime?:string;
    // 反馈回复 。
    fankuihuifu?:string;
    // 回复时间 。
    huifushijian?:string;
        
}

declare type ELiuyanbanForm = EModuleForm<ELiuyanban>;

// "轮播图" 模块结构。
declare interface ELunbotu {
    // 标题 。
    title?:string;
    // 图片 。
    image?:string;
    // 连接地址 。
    url?:string;
        
}

declare type ELunbotuForm = EModuleForm<ELunbotu>;

// "收藏" 模块结构。
declare interface EShoucang {
    // 用户 。
    username?:string;
    // 关联表id 。
    xwid?:number;
    // 关联表 。
    biao?:string;
    // 标题 。
    biaoti?:string;
    // 收藏时间 。
    addtime?:string;
        
}

declare type EShoucangForm = EModuleForm<EShoucang>;

// "商品信息" 模块结构。
declare interface EShangpinxinxi {
    // 商品编号 。
    shangpinbianhao?:string;
    // 分类 。
    fenlei?:number;
    // 商品名称 。
    shangpinmingcheng?:string;
    // 商品图片 。
    shangpintupian?:string;
    // 起拍价 。
    qipaijia?:number;
    // 加价幅度 。
    jiajiafudu?:number;
    // 当前竞拍价 。
    dangqianjingpaijia?:number;
    // 起拍时间 。
    qipaishijian?:string;
    // 结束时间 。
    jieshushijian?:string;
    // 竞拍人数 。
    jingpairenshu?:number;
    // 商品详情 。
    shangpinxiangqing?:string;
    // 商品状态 。
    // 可选项:竞拍中,已结束,已成交,已流拍。
    shangpinzhuangtai?:"竞拍中"|"已结束"|"已成交"|"已流拍";
    // 竞拍人 。
    jingpairen?:string;
    // 发布人 。
    faburen?:string;
    issh?:string;
        // 竞拍记录的数量。
    jingpaijiluCount?:number;
    // 竞拍订单的数量。
    jingpaidingdanCount?:number;

}

declare type EShangpinxinxiForm = EModuleForm<EShangpinxinxi>;

// "商品分类" 模块结构。
declare interface EShangpinfenlei {
    // 分类名称 。
    fenleimingcheng?:string;
        
}

declare type EShangpinfenleiForm = EModuleForm<EShangpinfenlei>;

// "竞拍记录" 模块结构。
declare interface EJingpaijilu {
    // 商品信息id 。
    shangpinxinxiid?:number;
    // 商品编号 。
    shangpinbianhao?:string;
    // 分类 。
    fenlei?:number;
    // 商品名称 。
    shangpinmingcheng?:string;
    // 商品图片 。
    shangpintupian?:string;
    // 起拍价 。
    qipaijia?:number;
    // 发布人 。
    faburen?:string;
    // 加价幅度 。
    jiajiafudu?:number;
    // 竞拍价 。
    jingpaijia?:number;
    // 竞拍人 。
    jingpairen?:string;
    // 添加时间 。
    addtime?:string;
        
}

declare type EJingpaijiluForm = EModuleReadForm<EJingpaijilu,EShangpinxinxi>;

// "竞拍订单" 模块结构。
declare interface EJingpaidingdan {
    // 商品信息id 。
    shangpinxinxiid?:number;
    // 商品编号 。
    shangpinbianhao?:string;
    // 商品名称 。
    shangpinmingcheng?:string;
    // 商品图片 。
    shangpintupian?:string;
    // 当前竞拍价 。
    dangqianjingpaijia?:number;
    // 竞拍人 。
    jingpairen?:string;
    // 发布人 。
    faburen?:string;
    // 分类 。
    fenlei?:number;
    // 订单编号 。
    dingdanbianhao?:string;
    // 订单状态 。
    // 可选项:待支付,已支付,已发货,已签收,已评价。
    dingdanzhuangtai?:"待支付"|"已支付"|"已发货"|"已签收"|"已评价";
    // 支付金额 。
    zhifujine?:number;
    // 手续费 。
    shouxufei?:number;
    // 联系人 。
    lianxiren?:string;
    // 联系电话 。
    lianxidianhua?:string;
    // 联系地址 。
    lianxidizhi?:string;
    // 添加时间 。
    addtime?:string;
    issh?:string;
    iszf?:string;
    // 订单评价的数量。
    dingdanpingjiaCount?:number;
    // 订单发货的数量。
    dingdanfahuoCount?:number;
    // 确认收货的数量。
    querenshouhuoCount?:number;

}

declare type EJingpaidingdanForm = EModuleReadForm<EJingpaidingdan,EShangpinxinxi>;

// "订单评价" 模块结构。
declare interface EDingdanpingjia {
    // 竞拍订单id 。
    jingpaidingdanid?:number;
    // 商品编号 。
    shangpinbianhao?:string;
    // 商品名称 。
    shangpinmingcheng?:string;
    // 分类 。
    fenlei?:number;
    // 发布人 。
    faburen?:string;
    // 订单编号 。
    dingdanbianhao?:string;
    // 竞拍人 。
    jingpairen?:string;
    // 评分 。
    pingfen?:number;
    // 评价 。
    pingjia?:string;
    // 评价时间 。
    addtime?:string;
        
}

declare type EDingdanpingjiaForm = EModuleReadForm<EDingdanpingjia,EJingpaidingdan>;

// "订单发货" 模块结构。
declare interface EDingdanfahuo {
    // 竞拍订单id 。
    jingpaidingdanid?:number;
    // 商品编号 。
    shangpinbianhao?:string;
    // 商品名称 。
    shangpinmingcheng?:string;
    // 商品图片 。
    shangpintupian?:string;
    // 竞拍人 。
    jingpairen?:string;
    // 发布人 。
    faburen?:string;
    // 分类 。
    fenlei?:number;
    // 订单编号 。
    dingdanbianhao?:string;
    // 物流公司 。
    wuliugongsi?:string;
    // 物流单号 。
    wuliudanhao?:string;
    // 发货备注 。
    fahuobeizhu?:string;
    // 发货时间 。
    addtime?:string;
        
}

declare type EDingdanfahuoForm = EModuleReadForm<EDingdanfahuo,EJingpaidingdan>;

// "确认收货" 模块结构。
declare interface EQuerenshouhuo {
    // 竞拍订单id 。
    jingpaidingdanid?:number;
    // 商品编号 。
    shangpinbianhao?:string;
    // 商品名称 。
    shangpinmingcheng?:string;
    // 商品图片 。
    shangpintupian?:string;
    // 竞拍人 。
    jingpairen?:string;
    // 发布人 。
    faburen?:string;
    // 分类 。
    fenlei?:number;
    // 订单编号 。
    dingdanbianhao?:string;
    // 支付金额 。
    zhifujine?:number;
    // 收货备注 。
    shouhuobeizhu?:string;
    // 收货时间 。
    addtime?:string;
        
}

declare type EQuerenshouhuoForm = EModuleReadForm<EQuerenshouhuo,EJingpaidingdan>;

// "商家" 模块结构。
declare interface EShangjia {
    // 账号 。
    zhanghao?:string;
    // 密码 。
    mima?:string;
    // 店铺名称 。
    dianpumingcheng?:string;
    // 钱包余额 。
    qianbaoyue?:number;
    // 负责人 。
    fuzeren?:string;
    // 身份证 。
    shenfenzheng?:string;
    // 电话 。
    dianhua?:string;
    // 邮箱 。
    youxiang?:string;
    // 地址 。
    dizhi?:string;
    // 简介 。
    jianjie?:string;
    // 资质证书 。
    zizhizhengshu?:string;
    issh?:string;
        // 商家提现的数量。
    shangjiatixianCount?:number;

}

declare type EShangjiaForm = EModuleForm<EShangjia>;

// "公告" 模块结构。
declare interface EGonggao {
    // 标题 。
    biaoti?:string;
    // 内容 。
    neirong?:string;
    // 发布日期 。
    faburiqi?:string;
    // 发布人 。
    faburen?:string;
        
}

declare type EGonggaoForm = EModuleForm<EGonggao>;

// "私聊" 模块结构。
declare interface ESiliao {
    // 编号 。
    bianhao?:string;
    // 收信人 。
    shouxinren?:string;
    // 发信人 。
    faxinren?:string;
    // 消息最后时间 。
    addtime?:string;
            // 消息的数量。
    xiaoxiCount?:number;

}

declare type ESiliaoForm = EModuleForm<ESiliao>;

// "消息" 模块结构。
declare interface EXiaoxi {
    // 私聊id 。
    siliaoid?:number;
    // 编号 。
    bianhao?:string;
    // 内容 。
    neirong?:string;
    // 发送人 。
    fasongren?:string;
    // 发送时间 。
    fasongshijian?:string;
    // 是否查看 。
    shifouzhakan?:string;
        
}

declare type EXiaoxiForm = EModuleReadForm<EXiaoxi,ESiliao>;

// "用户提现" 模块结构。
declare interface EYonghutixian {
    // 用户id 。
    yonghuid?:number;
    // 用户名 。
    yonghuming?:string;
    // 姓名 。
    xingming?:string;
    // 提现金额 。
    tixianjine?:number;
    // 提现日期 。
    tixianriqi?:string;
    // 提现人 。
    tixianren?:string;
        
}

declare type EYonghutixianForm = EModuleReadForm<EYonghutixian,EYonghu>;

// "商家提现" 模块结构。
declare interface EShangjiatixian {
    // 商家id 。
    shangjiaid?:number;
    // 账号 。
    zhanghao?:string;
    // 店铺名称 。
    dianpumingcheng?:string;
    // 负责人 。
    fuzeren?:string;
    // 提现金额 。
    tixianjine?:number;
    // 提现日期 。
    tixianriqi?:string;
    // 提现商家 。
    tixianshangjia?:string;
        
}

declare type EShangjiatixianForm = EModuleReadForm<EShangjiatixian,EShangjia>;

// "在线充值" 模块结构。
declare interface EZaixianchongzhi {
    // 充值编号 。
    chongzhibianhao?:string;
    // 充值金额 。
    chongzhijine?:number;
    // 充值日期 。
    chongzhiriqi?:string;
    // 充值人 。
    chongzhiren?:string;
        iszf?:string;

}

declare type EZaixianchongzhiForm = EModuleForm<EZaixianchongzhi>;


// ajax 返回数据的结构
declare interface EResponseData<T>{
    // 为0 表示成功，其他表示错误码
    code:number;
    // 错误信息
    msg:string;
    // 数据
    data:T;
}
