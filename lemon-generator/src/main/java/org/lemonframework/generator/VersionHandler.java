package org.lemonframework.generator;

import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

/**
 * desc.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class VersionHandler implements GeneratorHandler {

    /**
     * 列名.
     */
    private String columnName = "version";

    /**
     * 表名.
     */
    private List<String> tables;

    public VersionHandler() {
    }

    public VersionHandler(String columnName) {
        this(columnName, null);
    }

    public VersionHandler(String columnName, List<String> tables) {
        this.columnName = columnName;
        this.tables = tables;
    }

    @Override
    public void run(GeneratorContext context) {
        //比较传统的方式 查找替换
        final List<String> targetTables = this.tables == null ? context.getDbTables() : this.tables;
        final String mapperPackage = context.getMapperPackage();
        final String mapperPath = mapperPackage.replaceAll("\\.", "/");
        final String xmlMapperPath = context.getXmlMapperPath() + "/" + mapperPath;

        for (int i = 0; i < targetTables.size(); i++) {
            final String tableName = targetTables.get(i);
            final String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
            this.replaceUpdateVersion(xmlMapperPath + "/" + clsName + "Mapper.xml");
        }
    }

    private void replaceUpdateVersion(String filePath) {
        this.replaceVersion(filePath, "update");
    }

    private void replaceVersion(String filePath, String eleName) {
        // version = #{ ==> version = 1 + #{
        final String xmlText = FileUtil.readUtf8String(filePath);
        final StringBuilder sb = new StringBuilder();
        final String startEle = "<" + eleName;
        final String endEle = "</" + eleName + ">";
        final int endUpdateLen = endEle.length();
        final int xmlTextLen = xmlText.length();

        int lastEnd = 0;
        while (lastEnd > -1) {
            final int nextStart = StrUtil.indexOfIgnoreCase(xmlText, startEle, lastEnd);

            if (nextStart < 0) {
                sb.append(StrUtil.sub(xmlText, lastEnd, xmlTextLen));
                break;
            }

            // lastEnd, nextStart ->不变
            sb.append(StrUtil.sub(xmlText, lastEnd, nextStart));

            final int nextEnd = StrUtil.indexOfIgnoreCase(xmlText, endEle, nextStart) + endUpdateLen;

            final String updateXml = StrUtil.sub(xmlText, nextStart, nextEnd);
            //可以考虑正则替换
            final String newUpdateXml = ReUtil.replaceAll(updateXml, this.columnName + "\\s*=\\s*#\\{", this.columnName + " = 1 + #{");
            //final String newUpdateXml = StrUtil.replaceIgnoreCase(updateXml, this.columnName + " = #{", this.columnName + " = 1 + #{");
            sb.append(newUpdateXml);

            lastEnd = nextEnd;
        }

        FileUtil.writeUtf8String(sb.toString(), filePath);
    }

//    public static void main(String[] args) {
//        String updateXml = "version = #{aa}";
//        String columnName = "version";
//        System.out.println(ReUtil.findAll(columnName + "\\s*=\\s*#\\{", updateXml, 0));
//        final String s = ReUtil.replaceAll(updateXml, columnName + "\\s*=\\s*#\\{", columnName + " = 1 + #{");
//        System.out.println(s);
//    }
}
