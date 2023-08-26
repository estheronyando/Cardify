package com.logicea.cardify.modelpojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;


/**
 * @author TOKOTH
 *
 */
public class HeaderError implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("header")
    private String header;

    @JsonProperty("error")
    private String error;

    public HeaderError() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return String.format("{" +
                "\"header\": \"%s\"" +
                "\"error\": \"%s\"" +
                "}", header, error);
    }
}
