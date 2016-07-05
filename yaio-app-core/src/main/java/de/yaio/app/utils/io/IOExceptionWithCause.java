package de.yaio.app.utils.io;

public class IOExceptionWithCause extends Exception {
    protected Object src = null;
    public IOExceptionWithCause(final String message, final Object src, final Throwable cause) {
        super(message, cause);
        this.src = src;
    }

    public Object getSrc() {
        return src;
    }
}
