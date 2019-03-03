package org.lemonframework.generator;

import java.io.IOException;
import java.nio.file.Files;

import org.lemonframework.dao.entity.Example;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/11/30 10:53 AM
 */
public class Test {

    //-Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector
    public static void main(String[] args) throws IOException {

//        final Map<String, Object> bindingMap = new HashMap<>();
//        bindingMap.put("name", "aa");
//        Engine engine = TemplateUtil.createEngine(new TemplateConfig("", TemplateConfig.ResourceMode.CLASSPATH));
//        Template template = engine.getTemplate("template/aa.vm");
//        File outputFile = new File("test.txt");
//        template.render(bindingMap, outputFile);
//        System.out.println();

//        final Charset charset = Charset.forName("UTF-8");
//        System.out.println(charset);
//        final Charset charset2 = Charset.forName("utf-8");
//        System.out.println(charset2);
//        final Charset charset3 = Charset.forName("utf8");
//        System.out.println(charset3);
//        final Charset charset4 = Charset.forName("UTF8");
//        System.out.println(charset4);

//        FileUtils.readFileToString();

        Example example = new Example();
        example.createCriteria()
                .start("id")
                .andGreaterThan(100L)
                .andLessThan(151L)
                .end();
        example.or().start("id").andLessThan( 41L).end();
        System.out.println(example.getOredCriter);

    }
}
