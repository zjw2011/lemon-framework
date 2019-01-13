package org.lemonframework.log;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import sun.text.resources.cldr.af.FormatData_af;

/**
 * desc.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class Log4j2Test {

    @Test
    public void configTest() {
        final Log logger = LogFactory.get(Log4j2Test.class);
        for (int i = 0; i < 1000; i++) {
            logger.info(i + ":Test info");
//            logger.warn(i + ":Test warn");
//            logger.error(i + ":Test error");
            System.out.println(i + ":===========");
        }
        ClassLoader.getSystemResource("aa");
        final String loggerName = LogFactory.getCurrentLogFactory().getName();
        Assertions.assertThat(loggerName).isEqualTo("Log4j2");
    }

    @Test
    public void classPathTest() {
        System.out.println(System.class.getClassLoader());
        System.out.println(Log4j2Test.class.getClassLoader());
        System.out.println(FormatData_af.class.getClassLoader());
        System.out.println(System.getProperty("java.ext.dirs"));

    }

}
