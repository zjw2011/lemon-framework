package org.lemonframework.extra.template2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

/**
 * desc.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class AbstractTemplate implements Template {

    @Override
    public void render(Map<String, Object> bindingMap, File file) {
        BufferedOutputStream out = null;

        try {
            out = FileUtil.getOutputStream(file);
            this.render(bindingMap, out);
//            try {
//                out.write("aaaa".getBytes("UTF-8"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } finally {
            IoUtil.close(out);
        }
    }

    @Override
    public String render(Map<String, Object> bindingMap) {
        final StringWriter writer = new StringWriter();
        render(bindingMap, writer);
        return writer.toString();
    }

}
