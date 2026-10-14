import http from "@/utils/ajax/http";
import { empty } from "@/utils/extend";
import date from "@/utils/date";
import config from "@/config";
import DB from "@/utils/db";

function getMessage(msg, def) {
    return msg ? msg : def;
}

function validatePassword(password, minLen = 6, isUpper = true, isLower = true, isNum = true) {
    var minLength = 6;
    var hasUpperCase = /[A-Z]/.test(password);
    var hasLowerCase = /[a-z]/.test(password);
    var hasNumbers = /\d/.test(password);
    var hasSpecialChars = /[\W_]/.test(password);

    // 检查长度
    if (minLen && password.length < minLen) {
        return false;
    }
    // 检查是否包含大写字母
    if (isUpper && !hasUpperCase) {
        return false;
    }
    // 检查是否包含小写字母
    if (isLower && !hasLowerCase) {
        return false;
    }
    // 检查是否包含数字
    if (isNum && !hasNumbers) {
        return false;
    }
    // 检查是否包含特殊字符
    // if (!hasSpecialChars) {
    //     return false;
    // }

    // 所有检查通过
    return true;
}

/**
 * 身份证15位编码规则：dddddd yymmdd xx p dddddd：6位地区编码 yymmdd: 出生年(两位年)月日，如：910215 xx:
 * 顺序编码，系统产生，无法确定 p: 性别，奇数为男，偶数为女
 *
 * 身份证18位编码规则：dddddd yyyymmdd xxx y dddddd：6位地区编码 yyyymmdd:
 * 出生年(四位年)月日，如：19910215 xxx：顺序编码，系统产生，无法确定，奇数为男，偶数为女 y: 校验码，该位数值可通过前17位计算获得
 *
 * 前17位号码加权因子为 Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ] 验证位
 * Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ] 如果验证码恰好是10，为了保证身份证是十八位，那么第十八位将用X来代替
 * 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 ) i为身份证号码1...17 位; Y_P为校验码Y所在校验码数组位置
 */
function isIdCard(idCard) {
    // 15位和18位身份证号码的正则表达式
    var regIdCard = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;

    // 如果通过该验证，说明身份证格式正确，但准确性还需计算
    if (regIdCard.test(idCard)) {
        if (idCard.length == 18) {
            var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); // 将前17位加权因子保存在数组里
            var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); // 这是除以11后，可能产生的11位余数、验证码，也保存成数组
            var idCardWiSum = 0; // 用来保存前17位各自乖以加权因子后的总和
            for (var i = 0; i < 17; i++) {
                idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];
            }

            var idCardMod = idCardWiSum % 11; // 计算出校验码所在数组的位置
            var idCardLast = idCard.substring(17); // 得到最后一位身份证号码

            // 如果等于2，则说明校验码是10，身份证号码最后一位应该是X
            if (idCardMod == 2) {
                if (idCardLast == "X" || idCardLast == "x") {
                    //alert("恭喜通过验证啦！");
                    return true;
                } else {
                    //alert("身份证号码错误！");
                    return false;
                }
            } else {
                // 用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
                if (idCardLast == idCardY[idCardMod]) {
                    //alert("恭喜通过验证啦！");
                    return true;
                } else {
                    //alert("身份证号码错误！");
                    return false;
                }
            }
        } else {
            return true;
        }
    } else {
        //alert("身份证格式不正确!");
        return false;
    }
}

export default {
    async checkRemote(rule, value, callback, source, options) {
        if (empty(value)) {
            return callback();
        }
        var query = DB.name(rule.module);
        query.where(rule.col, value);
        if (rule.checktype == "update") {
            query.where("id", "!=", rule.id);
        }
        var count = await query.count();
        if (count == 0) {
            callback();
            return;
        }
        callback(new Error(rule.message ? rule.message : "已重复"));
    },
    checkPassword(rule, value, callback) {
        if (value == rule.mima) {
            callback();
            return;
        }
        callback(new Error("两次密码不一致"));
    },
    checkEnAndNum(rule, value, callback, source, options) {
        if (empty(value) || /^[0-9a-zA-Z_]+$/.test(value)) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "请输入字母或者数字")));
    },
    // 使用身份证规则进行验证身份证的真实性
    checkCard(rule, value, callback, source, options) {
        if (empty(value) || isIdCard(value)) {
            callback();
            return;
        }
        callback(new Error(options.message ? options.message : "请输入一个正确的身份证号"));
    },
    checkPwd(rule, value, callback, source, options) {
        if (empty(value) || validatePassword(value, 8, true, true, true)) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "请输入最少8位和大写、小写字母数字的组合")));
    },
    checkPwd1(rule, value, callback, source, options) {
        if (empty(value) || validatePassword(value, 0, true, true, true)) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "请输入大写、小写字母数字的组合")));
    },
    checkPhone(rule, value, callback, source, options) {
        if (empty(value) || /^[0-9]{11}$/.test(value)) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "请输入一个正确的手机号码")));
    },
    checkTel(rule, value, callback, source, options) {
        if (empty(value) || /^[+]{0,1}(\d){1,4}[ ]{0,1}([-]{0,1}((\d)|[ ]){1,12})+$/.test(value)) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "请输入正确的固定电话号码")));
    },
    checkMin(rule, value, callback, source, options) {
        if ((empty(value) && value !== 0) || value >= rule.value) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "请输入不小于" + rule.value + "值")));
    },
    checkMax(rule, value, callback, source, options) {
        if ((empty(value) && value !== 0) || value <= rule.value) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "请输入不大于" + rule.value + "值")));
    },
    checkMinlength(rule, value, callback, source, options) {
        if (value == "" || value.length >= rule.value) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "字符不小于" + rule.value + "个字符")));
    },
    checkMaxlength(rule, value, callback, source, options) {
        if (value == "" || value.length <= rule.value) {
            callback();
            return;
        }
        callback(new Error(getMessage(options.message, "字符不大于" + rule.value + "个字符")));
    },
    checkNumber(rule, value, callback, source, options) {
        if (empty(value) || /^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value)) {
            callback();
            return;
        }
        callback(new Error(options.message ? options.message : "请输入一个有效数字"));
    },
    getID() {
        return date("mdHi") + Math.floor(Math.random() * 10000);
    },
    date: date,
};
