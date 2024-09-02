package com.yiyan.study.utils.userlogutil;

import java.util.logging.Logger;
import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserLog {
    private static final Logger logger = Logger.getLogger(UserLog.class.getName());
    // private static final String BASE_LOG_DIR = "transfer\\user_logs\\"; //
    // 日志文件的基础目录
    private static final Path baseLogDir = Paths.get("transfer", "user_logs");

    // 初始化日志文件处理器
    static {
        try {
            File logDirectory = baseLogDir.toFile();
            if (!logDirectory.exists()) {
                if (!logDirectory.mkdirs()) {
                    throw new Exception("Failed to create log directory: " + baseLogDir);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to setup logger", e);
        }
    }

    /**
     * 将log文件名设置为 part1_part2_..._partN.log
     * 
     * @param fileNamePart
     */
    public static void setLogFileName(String... fileNamePart) {
        try {
            // String logFilePath = BASE_LOG_DIR;
            String fileName = "";
            for (int i = 0; i < fileNamePart.length - 1; i++) {
                fileName += fileNamePart[i] + "_";
            }
            fileName += fileNamePart[fileNamePart.length - 1] + ".log";

            Path logFilePath = baseLogDir.resolve(fileName);
            setupLogger(logFilePath.toString());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to set log file path", e);
        }
    }

    // 初始化日志处理器和格式化器
    private static void setupLogger(String logFilePath) throws Exception {
        closeHandles();
        FileHandler fileHandler = new FileHandler(logFilePath, true);
        fileHandler.setFormatter(new UserLogFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
    }

    private static void closeHandles() {
        for (Handler handler : logger.getHandlers()) {
            handler.close();
            logger.removeHandler(handler);
        }
    }

    public static void endLog() {
        closeHandles();
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
