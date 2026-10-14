/**
 * 服务器后端地址 格式为：http://{IP地址}:{端口号}
 * @type {string}
 */
let service_url = "http://localhost:8080";


/**
 * 系统配置信息
 */
const config = {
    service_url,
    title: "在线拍卖平台",
    /**
     * 登录地址
     */
    login_url: "/api/user/login",
    /**
     * token获取用户信息地址
     */
    token_login: "/api/user/tokenLogin",
    /**
     * 退出登录地址
     */
    logout_login: "/api/user/logout",
    /**
     * 查询数据
     */
    query_url: "/api/user/query",
    /**
     * 查询数据
     */
    select_url: "/api/user/select",

    uploadUrl: "/api/upload/save",

    /**
     * 修改密码地址
     */
    user_mod_post: "/api/user/editPassword",

    captch_url: "/api/user/captch",
};

export default config;
