import { session } from "@/utils/utils";

export default [
    {
        path: "pay",
        name: "AdminPay",
        component: () => import("@/views/zhifu/zhifu.vue"),
        meta: { authLogin: true },
    },

    {
        path: "admins",
        name: "AdminadminsList",
        component: () => import("@/views/admins/list.vue"),
        meta: { title: "管理员列表", authLogin: true },
    },

    {
        path: "admins/add",
        name: "AdminadminsAdd",
        component: () => import("@/views/admins/add.vue"),
        meta: { title: "添加管理员", authLogin: true },
    },
    {
        path: "admins/updt",
        name: "AdminadminsUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/admins/updt.vue"),
        meta: { title: "编辑管理员", authLogin: true },
    },
    {
        path: "admins/updtself",
        name: "AdminadminsUpdtSelf",
        props: (route) => ({ id: session("id") }),
        component: () => import("@/views/admins/updtself.vue"),
        meta: { title: "更新个人资料", authLogin: true },
    },
    {
        path: "yonghu",
        name: "AdminyonghuList",
        component: () => import("@/views/yonghu/list.vue"),
        meta: { title: "用户列表", authLogin: true },
    },

    {
        path: "yonghu/add",
        name: "AdminyonghuAdd",
        component: () => import("@/views/yonghu/add.vue"),
        meta: { title: "添加用户", authLogin: true },
    },
    {
        path: "yonghu/updt",
        name: "AdminyonghuUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/yonghu/updt.vue"),
        meta: { title: "编辑用户", authLogin: true },
    },
    {
        path: "yonghu/updtself",
        name: "AdminyonghuUpdtSelf",
        props: (route) => ({ id: session("id") }),
        component: () => import("@/views/yonghu/updtself.vue"),
        meta: { title: "更新个人资料", authLogin: true },
    },
    {
        path: "liuyanban",
        name: "AdminliuyanbanList",
        component: () => import("@/views/liuyanban/list.vue"),
        meta: { title: "留言反馈列表", authLogin: true },
    },

    {
        path: "liuyanban/fankuiyonghu",
        name: "AdminliuyanbanListfankuiyonghu",
        component: () => import("@/views/liuyanban/fankuiyonghu.vue"),
        meta: { title: "留言反馈列表", authLogin: true },
    },

    {
        path: "liuyanban/add",
        name: "AdminliuyanbanAdd",
        component: () => import("@/views/liuyanban/add.vue"),
        meta: { title: "添加留言反馈", authLogin: true },
    },
    {
        path: "liuyanban/updt",
        name: "AdminliuyanbanUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/liuyanban/updt.vue"),
        meta: { title: "编辑留言反馈", authLogin: true },
    },
    {
        path: "lunbotu",
        name: "AdminlunbotuList",
        component: () => import("@/views/lunbotu/list.vue"),
        meta: { title: "轮播图列表", authLogin: true },
    },

    {
        path: "lunbotu/add",
        name: "AdminlunbotuAdd",
        component: () => import("@/views/lunbotu/add.vue"),
        meta: { title: "添加轮播图", authLogin: true },
    },
    {
        path: "lunbotu/updt",
        name: "AdminlunbotuUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/lunbotu/updt.vue"),
        meta: { title: "编辑轮播图", authLogin: true },
    },
    {
        path: "shoucang",
        name: "AdminshoucangList",
        component: () => import("@/views/shoucang/list.vue"),
        meta: { title: "收藏列表", authLogin: true },
    },

    {
        path: "shoucang/username",
        name: "AdminshoucangListusername",
        component: () => import("@/views/shoucang/username.vue"),
        meta: { title: "收藏列表", authLogin: true },
    },

    {
        path: "shangpinxinxi",
        name: "AdminshangpinxinxiList",
        component: () => import("@/views/shangpinxinxi/list.vue"),
        meta: { title: "商品信息列表", authLogin: true },
    },

    {
        path: "shangpinxinxi/jingpairen",
        name: "AdminshangpinxinxiListjingpairen",
        component: () => import("@/views/shangpinxinxi/jingpairen.vue"),
        meta: { title: "商品信息列表", authLogin: true },
    },
    {
        path: "shangpinxinxi/faburen",
        name: "AdminshangpinxinxiListfaburen",
        component: () => import("@/views/shangpinxinxi/faburen.vue"),
        meta: { title: "商品信息列表", authLogin: true },
    },

    {
        path: "shangpinxinxi/add",
        name: "AdminshangpinxinxiAdd",
        component: () => import("@/views/shangpinxinxi/add.vue"),
        meta: { title: "添加商品信息", authLogin: true },
    },
    {
        path: "shangpinxinxi/updt",
        name: "AdminshangpinxinxiUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/shangpinxinxi/updt.vue"),
        meta: { title: "编辑商品信息", authLogin: true },
    },
    {
        path: "shangpinxinxi/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminshangpinxinxiDetail",
        component: () => import("@/views/shangpinxinxi/detail.vue"),
        meta: { title: "商品信息详情", authLogin: true },
    },
    {
        path: "shangpinfenlei",
        name: "AdminshangpinfenleiList",
        component: () => import("@/views/shangpinfenlei/list.vue"),
        meta: { title: "商品分类列表", authLogin: true },
    },

    {
        path: "shangpinfenlei/add",
        name: "AdminshangpinfenleiAdd",
        component: () => import("@/views/shangpinfenlei/add.vue"),
        meta: { title: "添加商品分类", authLogin: true },
    },
    {
        path: "shangpinfenlei/updt",
        name: "AdminshangpinfenleiUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/shangpinfenlei/updt.vue"),
        meta: { title: "编辑商品分类", authLogin: true },
    },
    {
        path: "jingpaijilu",
        name: "AdminjingpaijiluList",
        component: () => import("@/views/jingpaijilu/list.vue"),
        meta: { title: "竞拍记录列表", authLogin: true },
    },

    {
        path: "jingpaijilu/faburen",
        name: "AdminjingpaijiluListfaburen",
        component: () => import("@/views/jingpaijilu/faburen.vue"),
        meta: { title: "竞拍记录列表", authLogin: true },
    },
    {
        path: "jingpaijilu/jingpairen",
        name: "AdminjingpaijiluListjingpairen",
        component: () => import("@/views/jingpaijilu/jingpairen.vue"),
        meta: { title: "竞拍记录列表", authLogin: true },
    },

    {
        path: "jingpaijilu/add",
        name: "AdminjingpaijiluAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/jingpaijilu/add.vue"),
        meta: { title: "添加竞拍记录", authLogin: true },
    },
    {
        path: "jingpaijilu/updt",
        name: "AdminjingpaijiluUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/jingpaijilu/updt.vue"),
        meta: { title: "编辑竞拍记录", authLogin: true },
    },
    {
        path: "jingpaijilu/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminjingpaijiluDetail",
        component: () => import("@/views/jingpaijilu/detail.vue"),
        meta: { title: "竞拍记录详情", authLogin: true },
    },
    {
        path: "jingpaidingdan",
        name: "AdminjingpaidingdanList",
        component: () => import("@/views/jingpaidingdan/list.vue"),
        meta: { title: "竞拍订单列表", authLogin: true },
    },

    {
        path: "jingpaidingdan/jingpairen",
        name: "AdminjingpaidingdanListjingpairen",
        component: () => import("@/views/jingpaidingdan/jingpairen.vue"),
        meta: { title: "竞拍订单列表", authLogin: true },
    },
    {
        path: "jingpaidingdan/faburen",
        name: "AdminjingpaidingdanListfaburen",
        component: () => import("@/views/jingpaidingdan/faburen.vue"),
        meta: { title: "竞拍订单列表", authLogin: true },
    },

    {
        path: "jingpaidingdan/add",
        name: "AdminjingpaidingdanAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/jingpaidingdan/add.vue"),
        meta: { title: "添加竞拍订单", authLogin: true },
    },
    {
        path: "jingpaidingdan/updt",
        name: "AdminjingpaidingdanUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/jingpaidingdan/updt.vue"),
        meta: { title: "编辑竞拍订单", authLogin: true },
    },
    {
        path: "jingpaidingdan/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminjingpaidingdanDetail",
        component: () => import("@/views/jingpaidingdan/detail.vue"),
        meta: { title: "竞拍订单详情", authLogin: true },
    },
    {
        path: "dingdanpingjia",
        name: "AdmindingdanpingjiaList",
        component: () => import("@/views/dingdanpingjia/list.vue"),
        meta: { title: "订单评价列表", authLogin: true },
    },

    {
        path: "dingdanpingjia/faburen",
        name: "AdmindingdanpingjiaListfaburen",
        component: () => import("@/views/dingdanpingjia/faburen.vue"),
        meta: { title: "订单评价列表", authLogin: true },
    },
    {
        path: "dingdanpingjia/jingpairen",
        name: "AdmindingdanpingjiaListjingpairen",
        component: () => import("@/views/dingdanpingjia/jingpairen.vue"),
        meta: { title: "订单评价列表", authLogin: true },
    },

    {
        path: "dingdanpingjia/add",
        name: "AdmindingdanpingjiaAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/dingdanpingjia/add.vue"),
        meta: { title: "添加订单评价", authLogin: true },
    },
    {
        path: "dingdanpingjia/updt",
        name: "AdmindingdanpingjiaUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/dingdanpingjia/updt.vue"),
        meta: { title: "编辑订单评价", authLogin: true },
    },
    {
        path: "dingdanpingjia/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdmindingdanpingjiaDetail",
        component: () => import("@/views/dingdanpingjia/detail.vue"),
        meta: { title: "订单评价详情", authLogin: true },
    },
    {
        path: "dingdanfahuo",
        name: "AdmindingdanfahuoList",
        component: () => import("@/views/dingdanfahuo/list.vue"),
        meta: { title: "订单发货列表", authLogin: true },
    },

    {
        path: "dingdanfahuo/jingpairen",
        name: "AdmindingdanfahuoListjingpairen",
        component: () => import("@/views/dingdanfahuo/jingpairen.vue"),
        meta: { title: "订单发货列表", authLogin: true },
    },
    {
        path: "dingdanfahuo/faburen",
        name: "AdmindingdanfahuoListfaburen",
        component: () => import("@/views/dingdanfahuo/faburen.vue"),
        meta: { title: "订单发货列表", authLogin: true },
    },

    {
        path: "dingdanfahuo/add",
        name: "AdmindingdanfahuoAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/dingdanfahuo/add.vue"),
        meta: { title: "添加订单发货", authLogin: true },
    },
    {
        path: "dingdanfahuo/updt",
        name: "AdmindingdanfahuoUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/dingdanfahuo/updt.vue"),
        meta: { title: "编辑订单发货", authLogin: true },
    },
    {
        path: "dingdanfahuo/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdmindingdanfahuoDetail",
        component: () => import("@/views/dingdanfahuo/detail.vue"),
        meta: { title: "订单发货详情", authLogin: true },
    },
    {
        path: "querenshouhuo",
        name: "AdminquerenshouhuoList",
        component: () => import("@/views/querenshouhuo/list.vue"),
        meta: { title: "确认收货列表", authLogin: true },
    },

    {
        path: "querenshouhuo/jingpairen",
        name: "AdminquerenshouhuoListjingpairen",
        component: () => import("@/views/querenshouhuo/jingpairen.vue"),
        meta: { title: "确认收货列表", authLogin: true },
    },
    {
        path: "querenshouhuo/faburen",
        name: "AdminquerenshouhuoListfaburen",
        component: () => import("@/views/querenshouhuo/faburen.vue"),
        meta: { title: "确认收货列表", authLogin: true },
    },

    {
        path: "querenshouhuo/add",
        name: "AdminquerenshouhuoAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/querenshouhuo/add.vue"),
        meta: { title: "添加确认收货", authLogin: true },
    },
    {
        path: "querenshouhuo/updt",
        name: "AdminquerenshouhuoUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/querenshouhuo/updt.vue"),
        meta: { title: "编辑确认收货", authLogin: true },
    },
    {
        path: "querenshouhuo/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminquerenshouhuoDetail",
        component: () => import("@/views/querenshouhuo/detail.vue"),
        meta: { title: "确认收货详情", authLogin: true },
    },
    {
        path: "shangjia",
        name: "AdminshangjiaList",
        component: () => import("@/views/shangjia/list.vue"),
        meta: { title: "商家列表", authLogin: true },
    },

    {
        path: "shangjia/add",
        name: "AdminshangjiaAdd",
        component: () => import("@/views/shangjia/add.vue"),
        meta: { title: "添加商家", authLogin: true },
    },
    {
        path: "shangjia/updt",
        name: "AdminshangjiaUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/shangjia/updt.vue"),
        meta: { title: "编辑商家", authLogin: true },
    },
    {
        path: "shangjia/updtself",
        name: "AdminshangjiaUpdtSelf",
        props: (route) => ({ id: session("id") }),
        component: () => import("@/views/shangjia/updtself.vue"),
        meta: { title: "更新个人资料", authLogin: true },
    },
    {
        path: "shangjia/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminshangjiaDetail",
        component: () => import("@/views/shangjia/detail.vue"),
        meta: { title: "商家详情", authLogin: true },
    },
    {
        path: "gonggao",
        name: "AdmingonggaoList",
        component: () => import("@/views/gonggao/list.vue"),
        meta: { title: "公告列表", authLogin: true },
    },

    {
        path: "gonggao/faburen",
        name: "AdmingonggaoListfaburen",
        component: () => import("@/views/gonggao/faburen.vue"),
        meta: { title: "公告列表", authLogin: true },
    },

    {
        path: "gonggao/add",
        name: "AdmingonggaoAdd",
        component: () => import("@/views/gonggao/add.vue"),
        meta: { title: "添加公告", authLogin: true },
    },
    {
        path: "gonggao/updt",
        name: "AdmingonggaoUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/gonggao/updt.vue"),
        meta: { title: "编辑公告", authLogin: true },
    },
    {
        path: "gonggao/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdmingonggaoDetail",
        component: () => import("@/views/gonggao/detail.vue"),
        meta: { title: "公告详情", authLogin: true },
    },
    {
        path: "siliao",
        name: "AdminsiliaoList",
        component: () => import("@/views/siliao/list.vue"),
        meta: { title: "私聊列表", authLogin: true },
    },

    {
        path: "siliao/faxinren",
        name: "AdminsiliaoListfaxinren",
        component: () => import("@/views/siliao/faxinren.vue"),
        meta: { title: "私聊列表", authLogin: true },
    },

    {
        path: "siliao/add",
        name: "AdminsiliaoAdd",
        component: () => import("@/views/siliao/add.vue"),
        meta: { title: "添加私聊", authLogin: true },
    },
    {
        path: "siliao/updt",
        name: "AdminsiliaoUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/siliao/updt.vue"),
        meta: { title: "编辑私聊", authLogin: true },
    },
    {
        path: "xiaoxi",
        name: "AdminxiaoxiList",
        component: () => import("@/views/xiaoxi/list.vue"),
        meta: { title: "消息列表", authLogin: true },
    },

    {
        path: "xiaoxi/add",
        name: "AdminxiaoxiAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/xiaoxi/add.vue"),
        meta: { title: "添加消息", authLogin: true },
    },
    {
        path: "xiaoxi/updt",
        name: "AdminxiaoxiUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/xiaoxi/updt.vue"),
        meta: { title: "编辑消息", authLogin: true },
    },


];
