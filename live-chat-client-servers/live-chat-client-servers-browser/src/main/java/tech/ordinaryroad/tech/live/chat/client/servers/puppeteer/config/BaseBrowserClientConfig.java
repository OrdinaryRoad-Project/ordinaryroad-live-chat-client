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

package tech.ordinaryroad.tech.live.chat.client.servers.puppeteer.config;

import com.ruiyun.jvppeteer.cdp.entities.LaunchOptions;
import com.ruiyun.jvppeteer.common.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.ordinaryroad.live.chat.client.commons.client.config.BaseLiveChatClientConfig;

import java.util.Arrays;

/**
 * @author mjz
 * @date 2025/5/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseBrowserClientConfig extends BaseLiveChatClientConfig {

    @Builder.Default
    private LaunchOptions launchOptions = LaunchOptions.builder()
            .headless(false)
            .defaultViewport(null)
            .product(Product.Chrome)
            .ignoreAllDefaultArgs(true)
            .dumpio(true)
            .args(
                    // com.ruiyun.jvppeteer.common.Constant.DEFAULT_ARGS
                    Arrays.asList(
                            "--allow-pre-commit-input",
                            "--disable-background-networking",
                            "--disable-background-timer-throttling",
                            "--disable-backgrounding-occluded-windows",
                            "--disable-breakpad",
                            "--disable-client-side-phishing-detection",
                            "--disable-component-extensions-with-background-pages",
                            "--disable-default-apps",
                            "--disable-dev-shm-usage",
                            "--disable-extensions",
                            "--disable-hang-monitor",
                            "--disable-infobars",
                            "--disable-ipc-flooding-protection",
                            "--disable-popup-blocking",
                            "--disable-prompt-on-repost",
                            "--disable-renderer-backgrounding",
                            "--disable-search-engine-choice-screen",
                            "--disable-sync",
                            // "--enable-automation",
                            // "--export-tagged-pdf",
                            // "--generate-pdf-document-outline",
                            "--force-color-profile=srgb",
                            "--metrics-recording-only",
                            "--no-first-run",
                            // "--password-store=basic",
                            // "--use-mock-keychain"
                            // 添加参数
                            "--disable-blink-features=AutomationControlled",
                            "--window-size=1920,1080"
                    )
            )
            .build();

}
