package org.lemonframework.thirdparty.template;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * TemplateBuilderTest.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class TemplateBuilderTest {

    @Test
    public void test1() {
        final Template template = TemplateBuilder.newBuilder()
                .template("bbbbbbbbb, ${aa}")
                .build(ResourceLoader.STRING);
        Map<String, Object> context = new HashMap();
        context.put("aa", "123");
        Assertions.assertThat(template.render(context)).isEqualTo("bbbbbbbbb, 123");
    }

    @Test
    public void test2() {
        final Template template = TemplateBuilder.newBuilder()
                .template("aa.txt")
                .build(ResourceLoader.CLASSPATH);
        Map<String, Object> context = new HashMap();
        context.put("bb", "456");
        Assertions.assertThat(template.render(context)).isEqualTo("中456");
    }
}
