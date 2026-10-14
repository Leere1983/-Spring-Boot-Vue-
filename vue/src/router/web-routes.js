import { session } from "@/utils/utils";
export default [
    {
        path: "pay",
        name: "WebPay",
        component: () => import("@/views/zhifu/zhifu.vue"),
        meta: { authLogin: true },
    },
    {
        path: "yonghu/add",
        name: "IndexyonghuAdd",
        component: () => import("@/views/yonghu/add-web.vue"),
        meta: { title: "用户添加" },
    },
    {
        path: "liuyanban/add",
        name: "IndexliuyanbanAdd",
        component: () => import("@/views/liuyanban/add-web.vue"),
        meta: { title: "留言反馈添加", authLogin: true, msg: true },
    },
    {
        path: "shoucang/add",
        name: "IndexshoucangAdd",
        component: () => import("@/views/shoucang/add-web.vue"),
        meta: { title: "收藏添加", authLogin: true, msg: true },
    },
    {
        path: "shangpinxinxi",
        name: "IndexshangpinxinxiList",
        component: () => import("@/views/shangpinxinxi/index.vue"),
        meta: { title: "商品信息列表" },
    },
    {
        path: "shangpinxinxi/detail",
        name: "IndexshangpinxinxiDetail",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/shangpinxinxi/detail-web.vue"),
        meta: { title: "商品信息详情" },
    },
    {
        path: "jingpaijilu/add",
        name: "IndexjingpaijiluAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/jingpaijilu/add-web.vue"),
        meta: { title: "竞拍记录添加", authLogin: true, msg: true },
    },
    {
        path: "jingpaidingdan/add",
        name: "IndexjingpaidingdanAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/jingpaidingdan/add-web.vue"),
        meta: { title: "竞拍订单添加", authLogin: true, msg: true },
    },
    {
        path: "shangjia/add",
        name: "IndexshangjiaAdd",
        component: () => import("@/views/shangjia/add-web.vue"),
        meta: { title: "商家添加" },
    },
    {
        path: "gonggao",
        name: "IndexgonggaoList",
        component: () => import("@/views/gonggao/index.vue"),
        meta: { title: "公告列表" },
    },
    {
        path: "gonggao/detail",
        name: "IndexgonggaoDetail",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/gonggao/detail-web.vue"),
        meta: { title: "公告详情" },
    },
    {
        path: "siliao/add",
        name: "IndexsiliaoAdd",
        component: () => import("@/views/siliao/add-web.vue"),
        meta: { title: "私聊添加", authLogin: true, msg: true },
    },
];
