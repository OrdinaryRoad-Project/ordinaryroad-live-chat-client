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

package tech.ordinaryroad.live.chat.client.commons.util;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.system.JavaInfo;
import lombok.SneakyThrows;

import javax.script.ScriptEngine;
import java.lang.reflect.Method;

/**
 * @author mjz
 * @date 2025/1/24
 */
public class OrJavaScriptUtil {
    @SneakyThrows
    public static ScriptEngine createScriptEngine() {
        JavaInfo javaInfo = new JavaInfo();
        Class<?> nashornScriptEngineFactoryClass;
        if (javaInfo.getVersionFloat() >= 17) {
            return com.oracle.truffle.js.scriptengine.GraalJSScriptEngine.create(org.graalvm.polyglot.Engine.newBuilder()
                            .option("engine.WarnInterpreterOnly", "false")
                            .build(),
                    org.graalvm.polyglot.Context.newBuilder("js")
                            .allowHostAccess(org.graalvm.polyglot.HostAccess.ALL)
                            .allowHostClassLookup(s -> true)
                            .option("js.ecmascript-version", "2015")
            );
        } else if (javaInfo.getVersionFloat() >= 11) {
            nashornScriptEngineFactoryClass = Class.forName("org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory");
        } else {
            nashornScriptEngineFactoryClass = Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory");
        }
        Object factory = nashornScriptEngineFactoryClass.getConstructor().newInstance();
        Method getScriptEngine = nashornScriptEngineFactoryClass.getDeclaredMethod("getScriptEngine", String[].class);
        return (ScriptEngine) getScriptEngine.invoke(factory, (Object) new String[]{"--language=es6"});

    }

    public static void addCryptoJs(ScriptEngine scriptEngine) {
        if (scriptEngine == null) {
            return;
        }
        try {
            scriptEngine.eval(ResourceUtil.readUtf8Str("js/crypto-js.js"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
