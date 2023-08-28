package com.logicea.cardify.modelpojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author EONYANDO
 *
 */
public class BodyErrorMessage {
	@JsonProperty("MissingParameter")
    private boolean missingParameter;

    @JsonProperty("InvalidParameter")
    private String invalidParameter;

    public boolean isMissingParameter() {
        return missingParameter;
    }

    public void setMissingParameter(boolean missingParameter) {
        this.missingParameter = missingParameter;
    }

    public String getInvalidParameter() {
        return invalidParameter;
    }

    public void setInvalidParameter(String invalidParameter) {
        this.invalidParameter = invalidParameter;
    }

    @Override
    public String toString() {
        return String.format("{" +
                        "\"MissingParameterErrors\": \"%s\" " +
                        "\"InvalidParameterErrors\": \"%s\" " +
                        "}",
                missingParameter, invalidParameter
        );
    }
}
