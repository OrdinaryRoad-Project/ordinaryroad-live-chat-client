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

package tech.ordinaryroad.live.chat.client.codec.bilibili.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author mjz
 * @date 2025/7/19
 */
public class BilibiliRandomUtil {
    private static final Random random = new SecureRandom();
    private static final BigDecimal THIRTY_SIX = new BigDecimal(36);
    private static final int PRECISION = 20; // 足够大的精度

    public static String generateQueueUuid() {
        // 1. 生成0-1之间的随机小数（模拟JavaScript的Math.random()）
        BigDecimal randomDecimal = new BigDecimal(random.nextDouble(), new MathContext(PRECISION));

        // 2. 转换为36进制字符串
        String base36 = convertToBase36(randomDecimal);

        // 3. 截取最后8个字符
        return base36.length() > 8 ? base36.substring(base36.length() - 8) : base36;
    }

    private static String convertToBase36(BigDecimal decimal) {
        StringBuilder result = new StringBuilder();
        BigDecimal current = decimal;

        // 模拟JavaScript的小数转36进制算法
        for (int i = 0; i < 16; i++) { // 限制迭代次数防止无限循环
            current = current.multiply(THIRTY_SIX);
            int digit = current.intValue();
            result.append(Character.forDigit(digit, 36));
            current = current.subtract(new BigDecimal(digit));
            if (current.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }

        return result.substring(2); // 跳过"0."
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generateQueueUuid());
        }
    }
}
