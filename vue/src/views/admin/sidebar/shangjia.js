export default [
    {
        label: "商品信息管理",
        to: "",
        children: [
            {
                label: "拍卖商品发布",
                to: { path: "/admin/shangpinxinxi/add" },
            },
            {
                label: "我发布的商品",
                to: { path: "/admin/shangpinxinxi/faburen" },
            },
        ],
    },
    {
        label: "竞拍订单管理",
        to: "",
        children: [
            {
                label: "竞拍订单查询",
                to: { path: "/admin/jingpaidingdan/faburen" },
            },
            {
                label: "订单发货查询",
                to: { path: "/admin/dingdanfahuo/faburen" },
            },
            {
                label: "收货记录查询",
                to: { path: "/admin/querenshouhuo/faburen" },
            },
            {
                label: "订单评价查询",
                to: { path: "/admin/dingdanpingjia/faburen" },
            },
        ],
    },
    {
        label: "留言反馈管理",
        to: "",
        children: [
            {
                label: "留言反馈查询",
                to: { path: "/admin/liuyanban/fankuiyonghu" },
            },
        ],
    },



    {
        label: "个人中心",
        to: "",
        children: [
            {
                label: "修改个人资料",
                to: { path: "/admin/shangjia/updtself" },
            },
            {
                label: "修改密码",
                to: { path: "/admin/mod" },
            },

        ],
    },
];
