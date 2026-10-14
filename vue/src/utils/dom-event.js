function trim(str) {
    return str.replace(/^\s+|\s+$/gm, "");
}

var domEvent = {
    on(el, event, fn) {
        var events = event.split(",");
        for (var i = 0; i < events.length; i++) {
            var e = trim(events[i]);
            if (el.addEventListener) {
                el.addEventListener(e, fn, false);
            } else {
                el.attachEvent("on" + e, fn);
            }
        }
    },
    off(el, event, fn) {
        var events = event.split(",");
        for (var i = 0; i < events.length; i++) {
            var e = trim(events[i]);
            if (el.removeEventListener) {
                el.removeEventListener(e, fn, false);
            } else {
                el.detachEvent && el.detachEvent("on" + e, fn);
            }
        }
    },
    once(el, event, fn) {
        var listener = function () {
            if (fn) {
                fn.apply(this, arguments);
            }
            domEvent.off(el, event, listener);
        };
        domEvent.on(el, event, listener);
    },
};

export default domEvent;
