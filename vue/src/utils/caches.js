import { getTime } from "./date";

export function setCache(name, value, exp = null) {
    var data = {
        data: value,
        exp: exp !== null ? exp + getTime() : null,
    };
    var v = JSON.stringify(data);
    localStorage.setItem(name, v);
}

export function getCache(name) {
    var result = localStorage.getItem(name);
    if (!result) {
        return null;
    }
    var obj = JSON.parse(result);
    if (obj.exp && obj.exp < getTime()) {
        delCache(name);
        return null;
    }
    return obj.data;
}

export function delCache(name) {
    localStorage.removeItem(name);
}
