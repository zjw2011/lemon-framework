package org.lemonframework.generator.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;


/**
 * Velocity工具类.
 */
public class VelocityUtil {

    /**
     * 根据模板生成文件
     * @param inputVmFilePath 模板路径
     * @param outputFilePath 输出文件路径
     * @param context
     */
    public static void generate(String inputVmFilePath, String outputFilePath, VelocityContext context) {
        FileOutputStream fos = null;
        //这里我们就可以设置编码了
        PrintStream ps = null;
        try {
            final Properties properties = new Properties();
            properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, getPath(inputVmFilePath));
            Velocity.init(properties);

            final Template template = Velocity.getTemplate(getFile(inputVmFilePath), "utf-8");
            final StringWriter writer = new StringWriter();
            template.merge(context, writer);

            final File outputFile = new File(outputFilePath);
            fos = new FileOutputStream(outputFile);
            ps = new PrintStream(fos, true, "UTF-8");
            ps.print(writer.toString());
            ps.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据文件绝对路径获取目录
     * @param filePath
     * @return
     */
    public static String getPath(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            return filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 根据文件绝对路径获取文件
     * @param filePath
     * @return
     */
    public static String getFile(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            return filePath.substring(filePath.lastIndexOf("/") + 1);
        }
        return StringUtils.EMPTY;
    }

}
