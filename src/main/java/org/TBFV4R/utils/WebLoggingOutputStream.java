package org.TBFV4R.utils;

import org.apache.logging.log4j.Logger;
import org.TBFV4R.api.WebSocketConfig;

import java.io.IOException;
import java.io.OutputStream;

public class WebLoggingOutputStream extends OutputStream {

    private final Logger logger;
    private final boolean isError;
    private final WebSocketConfig wsConfig;
    private final StringBuilder buffer = new StringBuilder();

    public WebLoggingOutputStream(Logger logger, boolean isError, WebSocketConfig wsConfig) {
        this.logger = logger;
        this.isError = isError;
        this.wsConfig = wsConfig;
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        if (c == '\n') {
            flush();
        } else {
            buffer.append(c);
        }
    }

    @Override
    public void flush() {
        if (buffer.length() == 0) return;

        String message = buffer.toString();
        buffer.setLength(0);

        if (isError) {
            logger.error(message);
        } else {
            logger.info(message);
        }

        if (wsConfig != null) {
            wsConfig.getHandler().broadcast(message);
        }
    }
}
