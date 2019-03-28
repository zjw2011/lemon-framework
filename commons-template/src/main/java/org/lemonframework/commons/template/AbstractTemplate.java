package org.lemonframework.commons.template;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.lemonframework.shaded.org.apache.commons.io.FileUtils;

/**
 * 模板抽象类.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
abstract class AbstractTemplate implements Template {

    @Override
    public abstract void render(Map context, Writer writer);

    @Override
    public abstract void render(Map context, OutputStream out);

    @Override
    public void render(Map context, File file) {
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(FileUtils.openOutputStream(file));
            this.render(context, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.close(out);
        }
    }

    @Override
    public String render(Map context) {
        final StringWriter writer = new StringWriter();
        this.render(context, writer);
        return writer.toString();
    }

    protected void flush(Flushable flushable) {
        if (null != flushable) {
            try {
                flushable.flush();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    protected void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
}

