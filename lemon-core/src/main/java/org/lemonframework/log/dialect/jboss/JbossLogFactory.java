package org.lemonframework.log.dialect.jboss;

import org.lemonframework.log.Log;
import org.lemonframework.log.LogFactory;

/**
 * <a href="https://github.com/jboss-logging">Jboss-Logging</a> log.
 * 
 * @author Looly
 * @since 4.1.21
 */
public class JbossLogFactory extends LogFactory {

	/**
	 * 构造
	 */
	public JbossLogFactory() {
		super("JBoss Logging");
		checkLogExist(org.jboss.logging.Logger.class);
	}

	@Override
	public Log createLog(String name) {
		return new JbossLog(name);
	}

	@Override
	public Log createLog(Class<?> clazz) {
		return new JbossLog(clazz);
	}

}
