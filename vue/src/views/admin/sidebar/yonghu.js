export default [
    {
        label: "我的竞拍记录",
        to: "",
        children: [
            {
                label: "竞拍记录查询",
                to: { path: "/admin/jingpaijilu/jingpairen" },
            },
        ],
    },
    {
        label: "竞拍订单管理",
        to: "",
        children: [
            {
                label: "竞拍订单查询",
                to: { path: "/admin/jingpaidingdan/jingpairen" },
            },
            {
                label: "订单发货查询",
                to: { path: "/admin/dingdanfahuo/jingpairen" },
            },
            {
                label: "收货记录查询",
                to: { path: "/admin/querenshouhuo/jingpairen" },
            },
            {
                label: "订单评价查询",
                to: { path: "/admin/dingdanpingjia/jingpairen" },
            },
        ],
    },
    {
        label: "留言反馈管理",
        to: "",
        children: [
            {
                label: "我的反馈查询",
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
                to: { path: "/admin/yonghu/updtself" },
            },
            {
                label: "修改密码",
                to: { path: "/admin/mod" },
            },
            {
                label: "我的收藏",
                to: { path: "/admin/shoucang/username" },
            },
        ],
    },
];
