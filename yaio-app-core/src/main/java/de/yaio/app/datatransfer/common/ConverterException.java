package de.yaio.app.datatransfer.common;

public class ConverterException extends Exception {
    protected Object converterSrc = null;
    public ConverterException(final String message, final Object converterSrc, final Exception cause) {
        super(message, cause);
        this.converterSrc = converterSrc;
    }

    public Object getConverterSrc() {
        return converterSrc;
    }
}
