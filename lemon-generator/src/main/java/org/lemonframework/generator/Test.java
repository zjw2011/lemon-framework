package org.lemonframework.generator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/11/30 10:53 AM
 */
public class Test {
    public static void main(String[] args) throws IOException {
        // 参数为空
        File directory = new File("");
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile);
        final String s = StringEscapeUtils.escapeXml("&sdsds<>");
        System.out.println(s);
    }
}
