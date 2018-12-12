package org.lemonframework.log;

import org.assertj.core.api.Assertions;
import org.junit.Test;

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
        final String loggerName = LogFactory.getCurrentLogFactory().getName();
        Assertions.assertThat(loggerName).isEqualTo("Log4j2");
    }

}
