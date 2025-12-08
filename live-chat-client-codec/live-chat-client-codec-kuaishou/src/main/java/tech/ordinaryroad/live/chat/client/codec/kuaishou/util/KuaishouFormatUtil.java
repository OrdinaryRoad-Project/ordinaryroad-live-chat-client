/*
 * MIT License
 *
 * Copyright (c) 2023 OrdinaryRoad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tech.ordinaryroad.live.chat.client.codec.kuaishou.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mjz
 * @date 2025/7/21
 */
public class KuaishouFormatUtil {

    private static final Pattern DISPLAY_COUNT_PATTERN = Pattern.compile("^(\\d+\\.?\\d*)([万亿])$");

    /**
     * 解析显示数量
     *
     * @param displayCount xx万、xx亿
     */
    public static Long parseDisplayCount(String displayCount) {
        if (displayCount == null || displayCount.isEmpty()) {
            return 0L;
        }

        Matcher matcher = DISPLAY_COUNT_PATTERN.matcher(displayCount);
        if (!matcher.find()) {
            try {
                return Long.parseLong(displayCount);
            } catch (NumberFormatException e) {
                return 0L;
            }
        }

        double number = Double.parseDouble(matcher.group(1));
        String unit = matcher.group(2);

        switch (unit) {
            case "万":
                return (long) (number * 10_000);
            case "亿":
                return (long) (number * 100_000_000);
            default:
                return (long) number;
        }
    }

    public static void main(String[] args) {
        System.out.println(parseDisplayCount("1.2万"));
        System.out.println(parseDisplayCount("1.2亿"));
        System.out.println(parseDisplayCount("1"));
        System.out.println(parseDisplayCount("1万"));
        System.out.println(parseDisplayCount("9999"));
    }
}
