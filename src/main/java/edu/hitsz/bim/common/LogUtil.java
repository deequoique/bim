package edu.hitsz.bim.common;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * log object
 */
public class LogUtil {


    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    private LogUtil(){}

    /**
     * Print the log
     */
    public static void error(Exception ex, String msg, Object ... args){
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        if (args.length > 0){
            logger.error(msg + " cause: \n{}\n", ArrayUtils.add(args, writer.toString()));
        } else {
            logger.error(msg + " cause: \n{}\n", writer.toString());
        }
    }

    /**
     * Print the log
     */
    public static void error(String msg, Object ... args){
        StringWriter writer = new StringWriter();
        if (args.length > 0){
            StringBuffer buffer = new StringBuffer();
            buffer.append(msg).append(" ");
            for (Object arg : args) {
                buffer.append(arg.toString()).append(" ");
            }
            logger.error(buffer.toString(), writer.toString());
        } else {
            logger.error(msg, writer.toString());
        }
    }

    /**
     * Print the log
     */
    public static void info(String msg, Object ... args){
        StringWriter writer = new StringWriter();
        if (args.length > 0){
            StringBuffer buffer = new StringBuffer();
            buffer.append(msg).append(" ");
            for (Object arg : args) {
                buffer.append(arg.toString()).append(" ");
            }
            logger.info(buffer.toString(), writer.toString());
        } else {
            logger.info(msg, writer.toString());
        }
    }
}
