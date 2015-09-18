
package tv.onsign.rc.util;

import android.util.Log;

public class Logging {
    private final String mLogTag;
    private final String mClassName;

    public Logging(Class<?> clazz) {
        mLogTag = "net.signagewidgets";
        mClassName = clazz.getSimpleName();
    }

    public void captureStack() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = 3; i < trace.length; i++) {
            error("--", trace[i].toString());
        }
    }

    public void captureException(Exception e) {
        error("Caught exception", e.toString());
        captureStack();
    }

    public void verbose(Object... messageComponents) {
        String msg = buildMessage(messageComponents);
        // do not queue this log level
        Log.v(mLogTag, msg);
    }

    public void debug(Object... messageComponents) {
        String msg = buildMessage(messageComponents);
        // do not queue this log level
        Log.d(mLogTag, msg);
    }

    public void info(Object... messageComponents) {
        String msg = buildMessage(messageComponents);
        Log.i(mLogTag, msg);
    }

    public void warning(Object... messageComponents) {
        String msg = buildMessage(messageComponents);
        Log.w(mLogTag, msg);
    }

    public void error(Object... messageComponents) {
        String msg = buildMessage(messageComponents);
        Log.e(mLogTag, msg);
    }

    private String buildMessage(Object... components) {
        String tmpMessage = "";

        for (int i = 0; i < components.length; i++) {
            if (i != 0) tmpMessage += " ";
            tmpMessage += (components[i] == null) ? "null" : components[i].toString();
        }

        return "[" + mClassName +"] " + tmpMessage;
    }
}
