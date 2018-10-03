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
 * This utility uses a simple text file to save log entries.
 * 
 * In case the initialization process fails to setup a text file,
 * the standard-output stream is used as the means for logging.
 * In other words, the 'log' method will be equivalent to the 
 * 'System.out.println' method.
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class Logger {
    
    /**
     * Enumeration of available log-levels.
     * Log-levels are used to tag log entries with a label.
     * There is an ordering among the levels which is used for filtering:
     * { RAW = 0, ERROR = 1, WARNING = 2, INFORMATION = 3, DEBUG = 4 }
     */
    public enum Level {
        
        RAW         (0, null),
        ERROR       (1, "ERR"),
        WARNING     (2, "WRN"),
        INFORMATION (3, "INF"),
        DEBUG       (4, "DBG");
        
        private Level(int order, String label) {
            ORDER = order;
            LABEL = label;
        }
        public final int ORDER;
        public final String LABEL;
    }
    
    
    private static Lock ioLock;
    private static Level activeLevel;
    private static PrintStream logStream;
    private static PrintWriter logWriter;
    private static DateFormat dateFormat;
    

    /**
     * Initialize the logger utility using the given file path. 
     * If a log file cannot be used successfully, the logger
     * will use the standard-output stream instead.
     */
    public static void init(String path) throws IOException {
        // First, a fail safe initialization
        logStream = System.out;
        // Set the active log-level
        activeLevel = Level.INFORMATION;
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
            logWriter.println("======= LOG CREATED on " + date() + " =======");
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
     * Set the active log-level to the given level.
     * When the log-level is set, only log-operations less-than or 
     * equal to the active level will be stored in the log file; 
     * otherwise, they're discarded.
     * 
     * This method affects all logging operations afterwards,
     * and does not affect any logging performed before-hand.
     */
    public static void setActiveLevel(Level lvl) {
        activeLevel = lvl;
    }

    /**
     * Returns a string representation of the current system time. 
     * The string is formatted as HH:MM:SS:mmm.
     */
    public static String time() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int m = Calendar.getInstance().get(Calendar.MINUTE);
        int s = Calendar.getInstance().get(Calendar.SECOND);
        int ms = Calendar.getInstance().get(Calendar.MILLISECOND);
        return String.format("%02d:%02d:%02d:%03d", h, m, s, ms);
    }

    /**
     * Returns a string representation of the full date and time based on the current system calendar. 
     * The string is formatted as EEE yyyy/MMM/dd HH:mm:ss:SSS.
     */
    public static String date() {
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * Logs the given message as a new line in the log-file.
     * This message will be logged at the RAW level
     * (i.e. without any time-tag and label).
     */
    public static void log(String msg) {
        log(msg, Level.RAW);
    }

    /**
     * Logs the given message as a new line in the log-file.
     */
    public static void log(String msg, Level lvl) {
        if (lvl.ORDER <= activeLevel.ORDER) {
            ioLock.lock();
            try {
                if (lvl.ORDER > Level.RAW.ORDER) {
                    logWriter.printf("%s [%s] %s\n", date(), lvl.LABEL, msg);
                    // No need to flush, since the writer is set to auto-flush.
                } else {
                    logWriter.println(msg);
                }
            } finally {
                ioLock.unlock();
            }
        }
    }

    /**
     * Fully logs the name and message of the given exception as a new line 
     * at the given level, and also logs the stack-trace beneath it.
     */
    public static void log(Exception ex, Level lvl) {
        if (lvl.ORDER <= activeLevel.ORDER) {
            ioLock.lock();
            try {
                if (lvl.ORDER > Level.RAW.ORDER) {
                    logWriter.printf("%s [%s] %s\n", date(), lvl.LABEL, ex.toString());
                    // No need to flush, since the writer is set to auto-flush.
                } else {
                    logWriter.println(ex.toString());
                }
                ex.printStackTrace(logWriter);
            } finally {
                ioLock.unlock();
            }
        }
    }

    /**
     * Logs the string representation of the given object as a new line in the log-file.
     */
    public static void log(Object obj, Level lvl) {
        log(obj.toString(), lvl);
    }

    /**
     * Logs the given message as a new line at DEBUG level in the log-file. 
     */
    public static void debug(String msg) {
        log(msg, Level.DEBUG);
    }

    /**
     * Logs the given message as a new line at INFO level in the log-file. 
     */
    public static void info(String msg) {
        log(msg, Level.INFORMATION);
    }

    /**
     * Logs the given message as a new line at WARNING level in the log-file. 
     */
    public static void warn(String msg) {
        log(msg, Level.WARNING);
    }

    /**
     * Fully logs the name and message of the given exception as a new line at WARNING level, 
     * and also logs the stack-trace beneath it.
     */
    public static void warn(Exception ex) {
        log(ex, Level.WARNING);
    }

    /**
     * Logs the given message as a new line at ERROR level in the log-file. 
     */
    public static void error(String msg) {
        log(msg, Level.ERROR);
    }

    /**
     * Fully logs the name and message of the given exception as a new line at ERROR level, 
     * and also logs the stack-trace beneath it.
     */
    public static void error(Exception ex) {
        log(ex, Level.ERROR);
    }

    /**
     * Close the Logger writer.
     */
    public static void close() {
        logWriter.close();
    }
}
