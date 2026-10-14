export default [
    {
        label: "账号管理",
        to: "",
        children: [
            {
                label: "管理员账号管理",
                to: { path: "/admin/admins" },
            },
            {
                label: "管理员账号添加",
                to: { path: "/admin/admins/add" },
            },
            {
                label: "密码修改",
                to: { path: "/admin/mod" },
            },
        ],
    },
    {
        label: "用户信息管理",
        to: "",
        children: [
            {
                label: "用户查询",
                to: { path: "/admin/yonghu" },
            },
        ],
    },
    {
        label: "商家管理",
        to: "",
        children: [
            {
                label: "商家添加",
                to: { path: "/admin/shangjia/add" },
            },
            {
                label: "商家查询",
                to: { path: "/admin/shangjia" },
            },
        ],
    },
    {
        label: "商品信息管理",
        to: "",
        children: [
            {
                label: "商品分类添加",
                to: { path: "/admin/shangpinfenlei/add" },
            },
            {
                label: "商品分类查询",
                to: { path: "/admin/shangpinfenlei" },
            },

            {
                label: "商品信息查询",
                to: { path: "/admin/shangpinxinxi" },
            },
        ],
    },
    {
        label: "竞拍订单管理",
        to: "",
        children: [
            {
                label: "竞拍订单查询",
                to: { path: "/admin/jingpaidingdan/" },
            },
            {
                label: "订单发货查询",
                to: { path: "/admin/dingdanfahuo/" },
            },
            {
                label: "收货记录查询",
                to: { path: "/admin/querenshouhuo/" },
            },
            {
                label: "订单评价查询",
                to: { path: "/admin/dingdanpingjia/" },
            },
        ],
    },
    {
        label: "留言反馈管理",
        to: "",
        children: [
            {
                label: "留言反馈查询",
                to: { path: "/admin/liuyanban" },
            },
        ],
    },
    {
        label: "公告信息管理",
        to: "",
        children: [
            {
                label: "公告添加",
                to: { path: "/admin/gonggao/add" },
            },
            {
                label: "公告查询",
                to: { path: "/admin/gonggao" },
            },
        ],
    },

    {
        label: "系统管理",
        to: "",
        children: [
            {
                label: "轮播图添加",
                to: { path: "/admin/lunbotu/add" },
            },
            {
                label: "轮播图查询",
                to: { path: "/admin/lunbotu" },
            },
        ],
    },
];
