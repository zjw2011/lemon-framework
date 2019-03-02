package org.lemonframework.generator;

import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 多表连接.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class JoinTableHandler implements GeneratorHandler {

    private final static String Combined_Column_List = "Combined_Column_List";

    /**
     * 列名.
     * b.aa,c.cc
     */
    private List<String> columns;

    /**
     * 表名.
     *  user b, person c
     */
    private List<String> tableNames;

    /**
     * 源表.
     */
    private String source;

    /**
     * 源表的别名.
     */
    private String aliasSource = "a";


    public JoinTableHandler(List<String> columns, List<String> tableNames, String source) {
        this.columns = columns;
        this.tableNames = tableNames;
        this.source = source;
    }

    @Override
    public void run(GeneratorContext context) {
        //比较传统的方式 查找替换
        final String mapperPath = context.getMapperPackage().replaceAll("\\.", "/");
        final String xmlMapperPath = context.getXmlMapperPath() + "/" + mapperPath;

        final String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(this.source));

        final String sourceFile = xmlMapperPath + "/" + clsName + "Mapper.xml";

        replaceSelectByExample(sourceFile);
    }

    private void replaceSelectByExample(String filePath) {
        final String xmlText = FileUtil.readUtf8String(filePath);
        final StringBuilder sb = new StringBuilder();
        final String startEle = "<select id=\"selectByExample\"";
        final String endEle = "</select>";
        final int endUpdateLen = endEle.length();

        final List<String> baseColumnList = findBaseColumnList(xmlText);
        final String newColumnList = buildNewColumnList(baseColumnList);


        final int nextStart = StrUtil.indexOfIgnoreCase(xmlText, startEle, 0);

        if (nextStart < 0) {
            return;
        }

        // nextStart ->不变
        sb.append(StrUtil.sub(xmlText, 0, nextStart));
        sb.append(newColumnList);

        final int nextEnd = StrUtil.indexOfIgnoreCase(xmlText, endEle, nextStart) + endUpdateLen;
        final String updateXml = StrUtil.sub(xmlText, nextStart, nextEnd);

        //可以考虑正则替换
        //Base_Column_List -> Combined_Column_List
        final String newUpdateXml = StrUtil.replace(updateXml, "Base_Column_List", Combined_Column_List);

        final String newUpdateXml2 = StrUtil.replace(newUpdateXml, "from " + this.source, "from " + this.source + " " + this.aliasSource);

        this.tableNames.forEach((t) -> {
            sb.append("," + t + "");
        });

        sb.append(newUpdateXml2);

        FileUtil.writeUtf8String(sb.toString(), filePath);
    }

    private String buildNewColumnList(List<String> baseColumnList) {
        final StringBuilder sb = new StringBuilder();

        sb.append("<sql id=\"" + Combined_Column_List + "\">");
        baseColumnList.forEach((col) -> {
            sb.append(this.aliasSource + "." + col + ",");
        });
        this.columns.forEach((col) -> {
            sb.append(col + ",");
        });
        sb.deleteCharAt(sb.length());
        sb.append("</sql>");
        return sb.toString();
    }

    private List<String> findBaseColumnList(String xmlText) {
        final String startEle = "<sql id=\"Base_Column_List\"";
        final String endEle = "</sql>";

        final int s = StrUtil.indexOfIgnoreCase(xmlText, startEle, 0);
        final int e = StrUtil.indexOfIgnoreCase(xmlText, endEle, s);

        final String cols = StrUtil.trimToEmpty(StrUtil.sub(xmlText, s, e));

        return StrUtil.split(cols, ',', true, false);
    }
}
