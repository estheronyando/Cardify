package com.logicea.cardify.modelapis;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author EOnyando
 *
 */
public class ModelApiResponse {
	@JsonProperty("header")
    private ApiHeaderResponse apiHeaderResponse;

    @JsonProperty("body")
    private Object responseBodyObject;

    public ModelApiResponse() {
        this.apiHeaderResponse = new ApiHeaderResponse();
    }

    /**
     * Get responseBodyObject
     *
     * @return responseBodyObject defines how the Body will look like
     **/
    public Object getResponseBodyObject() {
        return responseBodyObject;
    }

    public void setResponseBodyObject(Object responseBodyObject) {
        this.responseBodyObject = responseBodyObject;
    }

    /**
     * Get apiHeaderResponse
     *
     * @return apiHeaderResponse
     **/
    public ApiHeaderResponse getApiHeaderResponse() {
        return apiHeaderResponse;
    }

    public void setApiHeaderResponse(ApiHeaderResponse apiHeaderResponse) {
        this.apiHeaderResponse = apiHeaderResponse;
    }
}