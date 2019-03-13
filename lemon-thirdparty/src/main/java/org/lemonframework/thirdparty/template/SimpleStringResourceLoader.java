package org.lemonframework.thirdparty.template;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * {@link ResourceLoader} 的字符串实现形式<br>.
 * 用于直接获取字符串模板
 *
 * @author looly
 */
public class SimpleStringResourceLoader extends ResourceLoader {

    @Override
    public void init(ExtendedProperties configuration) {
    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        return IOUtils.toInputStream(source, Charset.forName("UTF-8"));
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }

}
