package org.lemonframework.generator.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 通用工具类.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class MixUtil {

    public static List<String> getColumns(final String xmlFile) {
        final String xmlText = FileUtil.readUtf8String(xmlFile);
        final String regx = "<sql id=\"Base_Column_List\">(.*?)</sql>";
        final Pattern pattern= Pattern.compile(regx, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        final List<String> data = ReUtil.findAll(pattern, xmlText, 1, new ArrayList<String>());
        if (data.size() == 0) {
            return null;
        }
        final String columns = data.get(0);
        final List<String> newColumns = new ArrayList<>();
        final List<String> columnList = StrUtil.split(columns, ',', true, false);
        for (int i= 0; i < columnList.size(); i++) {
            final String columnName = ReUtil.replaceAll(columnList.get(i), "\\s+\\w*", "");
            newColumns.add(columnName);
        }
        return newColumns;
    }

//    public static void main(String[] args) {
//        final String regx = "<sql ID=\"base_Column_List\">(.*?)</sql>";
//        final Pattern pattern= Pattern.compile(regx, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
//        final String xmlText = "<sql id=\"Base_Column_List\">\r\n  \r\nddd \nid,\nwere,\n\nrtrtr    \n</sql>";
//        final List<String> data = ReUtil.findAll(pattern, xmlText, 1, new ArrayList<String>());
//        if (data.size() == 0) {
//            return;
//        }
//        final String columns = data.get(0);
//        final List<String> columnList = StrUtil.split(columns, ',', true, false);
//        for (int i= 0; i < columnList.size(); i++) {
//            String columnName = ReUtil.replaceAll(columnList.get(i), "\\s+\\w*", "");
//            System.out.println(columnName);
//        }
//
//        final String testData = "\n rrr\n ";
//        System.out.println(StrUtil.trim(testData));
//    }

}
