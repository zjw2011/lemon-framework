package org.lemonframework.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        final String xmlMapperPath = context.getXmlMapperPath();

        for (int i = 0; i < targetTables.size(); i++) {
            final String tableName = targetTables.get(i);
            final String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
            this.replaceVersion(xmlMapperPath + "/" + clsName + "Mapper.xml");
        }
    }

    private void replaceVersion(String filePath) {
        // version = #{ ==> version = 1 + #{
        final String xmlText = FileUtil.readUtf8String(filePath);
        List<String> resultFindAll = this.findAll("<update .*?</update>", xmlText, 0);
        for (int i = 0; i < resultFindAll.size(); i++) {
            final String result = resultFindAll.get(i);
            StrUtil.replaceIgnoreCase(result, this.columnName + " = #{", this.columnName + " = 1 + #{");
        }
    }

    /**
     * 取得内容中匹配的所有结果
     *
     * @param regex 正则
     * @param content 被查找的内容
     * @param group 正则的分组
     * @return 结果集
     */
    public List<String> findAll(String regex, String content, int group) {
        if (null == regex) {
            return null;
        }

        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        return ReUtil.findAll(pattern, content, group, new ArrayList<String>());
    }

//    public static void main(String[] args) {
//        String xmlText = "<Update id='111'>12345</update><update id='111'>23455</update>";
//        List<String> resultFindAll = new VersionHandler().findAll("<update .*?</update>", xmlText, 0);
//        System.out.println(resultFindAll);
//    }
}
