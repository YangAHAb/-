package com.yiyan.study.utils.userlogutil;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public class UserLog {
    private static final Logger logger = Logger.getLogger(UserLog.class.getName());
    private static final String BASE_LOG_DIR = "user_logs/"; // 日志文件的基础目录

    // 初始化日志文件处理器
    static {
        try {
            String logFilePath = BASE_LOG_DIR + "default.log";
            setupLogger(logFilePath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to setup logger", e);
        }
    }

    /**
     * 将log文件名设置为 part1_part2_..._partN.log
     * @param fileNamePart
     */
    public static void setLogFileName(String... fileNamePart) {
        try {
            for (Handler handler : logger.getHandlers()) {
                logger.removeHandler(handler);
            }
            String logFilePath = BASE_LOG_DIR;
            for (int i = 0; i < fileNamePart.length - 1; i++) {
                logFilePath += fileNamePart[i] + "_";
            }
            logFilePath += fileNamePart[fileNamePart.length - 1] + ".log";

            setupLogger(logFilePath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to set log file path", e);
        }
    }

    // public static void setLogFileName(String userId, String taskId) {
    //     try {
    //         for (Handler handler : logger.getHandlers()) {
    //             logger.removeHandler(handler);
    //         }
    //         String logFilePath = BASE_LOG_DIR + userId + "_" + taskId + ".log";
    //         setupLogger(logFilePath);
    //     } catch (Exception e) {
    //         logger.log(Level.SEVERE, "Failed to set log file path", e);
    //     }
    // }

    // 初始化日志处理器和格式化器
    private static void setupLogger(String logFilePath) throws Exception {
        FileHandler fileHandler = new FileHandler(logFilePath, true);
        fileHandler.setFormatter(new UserLogFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
    }

    // 记录日志信息
    public static void log(Level level, String msg) {
        logger.log(level, msg);
    }

    public static void finest(String msg) {
        logger.finest(msg);
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warning(String msg) {
        logger.warning(msg);
    }

    public static void severe(String msg) {
        logger.severe(msg);
    }
}
