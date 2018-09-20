/*** In The Name of Allah ***/
package ghaffarian.nanologger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple, nimble, thread-safe logging utility.
 * This utility uses a simple text file to save log messages.
 * 
 * In case the initialization process fails to setup a text file,
 * the standard-output stream is used as the means for logging.
 * In other words, the 'log' method will be equivalent to the 
 * 'System.out.println' method.
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class Logger {
    
    
    public enum Level {
        ERROR,
        WARNING,
        INFORMATION
    }
    
    
    private static Lock ioLock;
    private static PrintStream logStream;
    private static PrintWriter logWriter;
    private static DateFormat dateFormat;

    /**
     * Initialize the logger utility using the given file path. If a log file cannot be used successfully, the logger
     * will use the standard-output stream instead.
     */
    public static void init(String path) throws IOException {
        // First, a fail safe initialization
        logStream = System.out;
        // Now, the real deal
        try {
            ioLock = new ReentrantLock();
            dateFormat = new SimpleDateFormat("EEE yyyy/MMM/dd HH:mm:ss:SSS");
            if (!path.toLowerCase().endsWith(".log")) {
                path += ".log";
            }
            File logFile = new File(path);
            if (!logFile.createNewFile()) {
                logFile.delete();
                logFile.createNewFile();
            }
            logStream = new PrintStream(new FileOutputStream(logFile), true);
        } finally {
            logWriter = new PrintWriter(logStream, true);
            logWriter.println("======= LOGGER CREATED on " + date() + " =======");
        }
    }

    /**
     * Redirects the standard-error (std-err) stream to the given file path.
     */
    public static void redirectStandardError(String path) throws IOException {
        dateFormat = new SimpleDateFormat("EEE yyyy/MMM/dd HH:mm:ss:SSS");
        if (!path.toLowerCase().endsWith(".err")) {
            path += ".err";
        }
        File errorFile = new File(path);
        if (!errorFile.createNewFile()) {
            errorFile.delete();
            errorFile.createNewFile();
        }
        PrintStream stdErr = new PrintStream(new FileOutputStream(errorFile), true);
        System.setErr(stdErr);
        System.err.println("======= ERROR FILE CREATED on " + date() + " =======");
    }

    /**
     * Returns a string representation of the current system time. The string is formatted as HH:MM:SS:mmm.
     */
    public static String time() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int m = Calendar.getInstance().get(Calendar.MINUTE);
        int s = Calendar.getInstance().get(Calendar.SECOND);
        int ms = Calendar.getInstance().get(Calendar.MILLISECOND);
        return String.format("%02d:%02d:%02d:%03d", h, m, s, ms);
    }

    /**
     * Returns a string representation of the full date and time based on the current system calendar. The string is
     * formatted as EEE yyyy/MMM/dd HH:mm:ss:SSS.
     */
    public static String date() {
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * Logs the given message as a new line in the log-file.
     */
    public static void log(String msg) {
        ioLock.lock();
        try {
            logWriter.println(msg);
            // No need to flush, since the writer is set to auto-flush.
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Logs the string representation of the given object as a new line in the log-file.
     */
    public static void log(Object obj) {
        ioLock.lock();
        try {
            logWriter.println(obj.toString());
            // No need to flush, since the writer is set to auto-flush.
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Logs the given message as a new line at INFO level in the log-file. This includes the full date and time plus the
     * label [INF] before the message.
     */
    public static void info(String msg) {
        ioLock.lock();
        try {
            logWriter.println(date() + " [INF] " + msg);
            // No need to flush, since the writer is set to auto-flush.
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Logs the given message as a new line at WARNING level in the log-file. This includes the full date and time plus
     * the label [WRN] before the message.
     */
    public static void warn(String msg) {
        ioLock.lock();
        try {
            logWriter.println(date() + " [WRN] " + msg);
            // No need to flush, since the writer is set to auto-flush.
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Fully logs the name and message of the given exception as a new line at WARNING level, and also logs the
     * stack-trace beneath it.
     */
    public static void warn(Exception ex) {
        ioLock.lock();
        try {
            warn(ex.toString());
            ex.printStackTrace(logWriter);
            // No need to flush, since the writer is set to auto-flush.
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Logs the given message as a new line at ERROR level in the log-file. This includes the full date and time plus
     * the label [ERR] before the message.
     */
    public static void error(String msg) {
        ioLock.lock();
        try {
            logWriter.println(date() + " [ERR] " + msg);
            // No need to flush, since the writer is set to auto-flush.
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Fully logs the name and message of the given exception as a new line at ERROR level, and also logs the
     * stack-trace beneath it.
     */
    public static void error(Exception ex) {
        ioLock.lock();
        try {
            error(ex.toString());
            ex.printStackTrace(logWriter);
            // No need to flush, since the writer is set to auto-flush.
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Returns the writer used for this Logger.
     */
    public static PrintWriter getWriter() {
        return logWriter;
    }

    /**
     * Returns the stream used for this Logger.
     */
    public static PrintStream getStream() {
        return logStream;
    }

    /**
     * Close the Logger writer.
     */
    public static void close() {
        logWriter.close();
    }
}
