import { useLink } from "vue-router";
import router from "./index";
import { unref } from "vue";
import { isArray, isObject } from "@/utils";

export const isFullPathActive = (to) => {
    var route = unref(router.currentRoute);
    var useLink1 = useLink({ to });
    const isExactActive = unref(useLink1.isExactActive);

    if (isExactActive) {
        if (to.query) {
            return isSameRouteLocationParams(to.query, route.query);
        }
    }
    if (isObject(to)) {
        return route.path === to.path;
    }
    return route.path === to;
};

export const isPathActive = (to) => {
    var route = unref(router.currentRoute);
    if (isObject(to)) {
        return route.path === to.path;
    }
    return route.path === to;
};

function isSameRouteLocationParams(a, b) {
    if (Object.keys(a).length !== Object.keys(b).length) return false;
    for (const key in a) {
        if (!isSameRouteLocationParamsValue(a[key], b[key])) return false;
    }
    return true;
}
function isSameRouteLocationParamsValue(a, b) {
    return isArray(a) ? isEquivalentArray(a, b) : isArray(b) ? isEquivalentArray(b, a) : a == b;
}
/**
 * Check if two arrays are the same or if an array with one single entry is the
 * same as another primitive value. Used to check query and parameters
 *
 * @param a - array of values
 * @param b - array of values or a single value
 */
function isEquivalentArray(a, b) {
    return isArray(b) ? a.length === b.length && a.every((value, i) => value == b[i]) : a.length === 1 && a[0] == b;
}
