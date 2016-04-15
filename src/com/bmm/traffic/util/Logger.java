/* 
 * Logger
 * Log输出工具
 * @version 0.01
 * @author young (young.ma@solidinfo.com)
 */
package com.bmm.traffic.util;



public class Logger {

	private org.apache.log4j.Logger logger;

	/**
	 * 构�?方法，初始化Log4j的日志对�?
	 */
	private Logger(org.apache.log4j.Logger log4jLogger) {
		logger = log4jLogger;
	}

	/**
	 * 获取构�?器，根据类初始化Logger对象
	 * 
	 * @param Class Class对象
	 * @return Logger对象
	 */
	public static Logger getLogger(Class<?> classObject) {
		return new Logger(org.apache.log4j.Logger.getLogger(classObject));
	}

	/**
	 * 获取构�?器，根据类名初始化Logger对象
	 * 
	 * @param String 类名字符�?
	 * @return Logger对象
	 */
	public static Logger getLogger(String loggerName) {
		return new Logger(org.apache.log4j.Logger.getLogger(loggerName));
	}

	public void debug(Object object) {
		logger.debug(object);
	}

	public void debug(Object object, Throwable e) {
		logger.debug(object, e);
	}

	public void info(Object object) {
		logger.info(object);
	}

	public void info(Object object, Throwable e) {
		logger.info(object, e);
	}

	public void warn(Object object) {
		logger.warn(object);
	}

	public void warn(Object object, Throwable e) {
		logger.warn(object, e);
	}

	public void error(Object object) {
		logger.error(object);
	}

	public void error(Object object, Throwable e) {
		logger.error(object, e);
	}

	public void fatal(Object object) {
		logger.fatal(object);
	}

	public String getName() {
		return logger.getName();
	}

	public org.apache.log4j.Logger getLog4jLogger() {
		return logger;
	}

	public boolean equals(Logger newLogger) {
		return logger.equals(newLogger.getLog4jLogger());
	}
}