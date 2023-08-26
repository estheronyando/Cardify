package com.logicea.cardify.modelpojos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author TOKOTH
 *
 */
public class HeaderErrorMessage{

	@JsonProperty("MissingHeaders")
    private boolean missingHeaders;

    @JsonProperty("InvalidHeaders")
    private List<HeaderError> invalidHeaderErrors;

    public HeaderErrorMessage() {
        invalidHeaderErrors = new ArrayList<>();
    }

    public boolean isMissingHeaders() {
        return missingHeaders;
    }

    public void setMissingHeaders(boolean missingHeaders) {
        this.missingHeaders = missingHeaders;
    }

    public List<HeaderError> getInvalidHeaderErrors() {
        return invalidHeaderErrors;
    }

    public void setInvalidHeaderErrors(List<HeaderError> invalidHeaderErrors) {
        this.invalidHeaderErrors = invalidHeaderErrors;
    }

    @Override
    public String toString() {
        return String.format("{" +
                "\"MissingHeaderErrors\": \"%s\" " +
                "\"InvalidHeaderErrors\": \"%s\" " +
                "}", missingHeaders, invalidHeaderErrors);
    }
}
