package org.lemonframework.thirdparty.template;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * 模板.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface Template {

    void render(Map context, Writer writer);
    void render(Map context, OutputStream out);
    void render(Map context, File file);
    String render(Map context);

}
