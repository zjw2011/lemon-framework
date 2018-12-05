package org.lemonframework.extra.template;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.lemonframework.extra.template.engine.velocity.VelocityEngine;

/**
 * 测试Velocity模板.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class VelocityTest {

    @Before
    public void setup() {

    }

    @Test
    public void writeFileByClasspathTest() {
        final String dir = ResourceUtil.getResource("").getPath();
        final TemplateConfig config = new TemplateConfig("", TemplateConfig.ResourceMode.CLASSPATH);
        final Engine engine = new VelocityEngine(config);
        final Template template = engine.getTemplate("template/velocity_test.vm");
        final Map<String, Object> bindingMap = new HashMap<>();
        final String name = "zhangsan";
        bindingMap.put("name", name);
        final String fileName = dir + "template/velocity_test_auto_classpath.vm";
        final File outFile = new File(fileName);
        template.render(bindingMap, outFile);

        final File srcFile = new File(fileName);
        final List<String> strings = FileUtil.readUtf8Lines(srcFile);
        String targetName = "";
        if (strings.size() > 0) {
            final int nameStart = strings.get(0).indexOf(":");
            if (nameStart >= 0) {
                targetName = strings.get(0).substring(nameStart + 1);
            }
        }
        Assertions.assertThat(name).isEqualTo(targetName);
    }

    @Test
    public void writeFileByFileTest() {
        final String dir = ResourceUtil.getResource("").getPath();
        final TemplateConfig config = new TemplateConfig(dir, TemplateConfig.ResourceMode.FILE);
        final Engine engine = new VelocityEngine(config);
        final Template template = engine.getTemplate("template/velocity_test.vm");
        final Map<String, Object> bindingMap = new HashMap<>();
        final String name = "lisi";
        bindingMap.put("name", name);
        final String fileName = dir + "template/velocity_test_auto_file.vm";
        final File outFile = new File(fileName);
        template.render(bindingMap, outFile);

        final File srcFile = new File(fileName);
        String targetName = "";
        final List<String> strings = FileUtil.readUtf8Lines(srcFile);
        if (strings.size() > 0) {
            final int nameStart = strings.get(0).indexOf(":");
            if (nameStart >= 0) {
                targetName = strings.get(0).substring(nameStart + 1);
            }
        }
        Assertions.assertThat(name).isEqualTo(targetName);
    }


}
