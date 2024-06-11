package com.mylibrary.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

    private static final boolean IS_DEBUG = false;
    private final String logName;
    private StringBuffer sb = new StringBuffer();

    public Logger(String logName) {
        this.logName = logName;
    }

    public String getLogName() {
        return logName;
    }

    public String getLoggerPath() {

        // Local Logging

         Path parentPath = Paths.get("").toAbsolutePath().getParent();
         Path path = Paths.get(parentPath.toString(), "local-logger", "api-logs");
         return path.toString();
    }

    public void startLog() {
        String logPrefix = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Calendar.getInstance().getTime())
                .concat(": ");

        String divider = "======================================================";

        sb.append(logPrefix);
        sb.append(divider);
        if (IS_DEBUG) {
            System.out.println(divider);
        }
        sb.append(System.lineSeparator());
    }

    public void writeLog(String logString) {
        String logPrefix = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Calendar.getInstance().getTime())
                .concat(": ");

        sb.append(logPrefix);
        sb.append(logString);
        if (IS_DEBUG) {
            System.out.println(logString);
        }
        sb.append(System.lineSeparator());
    }

    public void flushLog() {
        try {
            File file = Paths.get(this.getLoggerPath(), logName).toFile();
            if (!file.exists()) {
                file.mkdirs();
            }

            file = Paths.get(file.getAbsolutePath(),
                    new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()).concat("_act.txt"))
                    .toFile();

            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            out.write(sb.toString());
            out.flush();
            out.close();

            sb.setLength(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void writeError(Throwable throwable) {
        writeError(convertStackTraceToString(throwable));
    }

    public void writeError(String errorLog) {
        try {
            File file = Paths.get(this.getLoggerPath(), logName).toFile();
            if (!file.exists()) {
                file.mkdirs();
            }

            file = Paths.get(file.getAbsolutePath(),
                    new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()).concat("_err.txt"))
                    .toFile();

            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            out.write(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()).concat(": "));
            out.write(errorLog);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String convertStackTraceToString(Throwable throwable) {
        String convertedString = null;
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            convertedString = sw.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertedString;
    }

    public void logExceptionStackTrace(Exception e) {
        for (StackTraceElement el : e.getStackTrace()) {
            this.writeLog(el.toString());
        }
    }
}
