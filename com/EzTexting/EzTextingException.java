package com.EzTexting;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;

/**
 * EzTexting application errors.
 */
public class EzTextingException extends Exception {
    private final int responseCode;

    public EzTextingException(int responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
