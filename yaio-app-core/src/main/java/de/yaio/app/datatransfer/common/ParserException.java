package de.yaio.app.datatransfer.common;

public class ParserException extends Exception {
    protected Object parserSrc = null;
    public ParserException(final String message, final Object parserSrc, final Exception cause) {
        super(message, cause);
        this.parserSrc = parserSrc;
    }

    public Object getParserSrc() {
        return parserSrc;
    }
}
