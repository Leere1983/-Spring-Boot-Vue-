package com.spboot.app.utils;

import com.jntoo.db.DB;
import com.jntoo.db.utils.Convert;
import com.jntoo.db.utils.InfoUtil;
import com.jntoo.db.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Info extends InfoUtil {

    public static String getAllChild(String table, String pid, Object value) {
        List templists = DB.name(table).select();
        return StringUtil.join(",", getAllChild(table, pid, value, templists));
    }

    public static List getAllChild(String table, String pid, Object value, List templists) {
        List $ret = null;
        List $result = new ArrayList();
        String parentid = String.valueOf(value);
        $result.add(parentid);
        Iterator var8 = templists.iterator();

        while (var8.hasNext()) {
            HashMap child = (HashMap) var8.next();
            if (Convert.toStr(child.get(pid)).equals(parentid)) {
                $ret = getAllChild(table, pid, child.get("id"), templists);
                if ($ret.size() > 0) {
                    $result.addAll($ret);
                }
            }
        }

        return $result;
    }
}
