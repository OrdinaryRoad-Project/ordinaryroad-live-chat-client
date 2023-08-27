package tech.ordinaryroad.live.chat.client.commons.client.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author mjz
 * @date 2023/8/27
 */
public class CookieUtil {

    public static Map<String, String> parseCookieString(String cookies) {
        Map<String, String> map = new HashMap<>();
        if (StrUtil.isNotBlank(cookies) && !StrUtil.isNullOrUndefined(cookies)) {
            try {
                String[] split = cookies.split("; ");
                for (String s : split) {
                    String[] split1 = s.split("=");
                    map.put(split1[0], split1[1]);
                }
            } catch (Exception e) {
                throw new RuntimeException("cookie解析失败 " + cookies, e);
            }
        }
        return map;
    }

    public static String getCookieByName(Map<String, String> cookieMap, String name, Supplier<String> supplier) {
        String str = MapUtil.getStr(cookieMap, name);
        return str == null ? supplier.get() : str;
    }
}
