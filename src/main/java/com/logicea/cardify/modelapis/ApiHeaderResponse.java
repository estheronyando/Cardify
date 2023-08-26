package com.logicea.cardify.modelapis;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author EONYANDO
 *
 */
public class ApiHeaderResponse {
	@JsonProperty("requestRefId")
    private String requestRefId;

    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("responseMessage")
    private String responseMessage;

    @JsonProperty("customerMessage")
    private String customerMessage;

    @JsonProperty("timestamp")
    private String timestamp;

    ApiHeaderResponse() {
    }

    public void setHeaders(int responseCode, String responseMessage, String customerMessage, String timestamp, String requestRefId) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.customerMessage = customerMessage;
        this.timestamp = timestamp;
        this.requestRefId = requestRefId;
    }

    /**
     * Get responseCode
     *
     * @return responseCode
     **/
    @ApiModelProperty(example = "200")
    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Get responseMessage
     *
     * @return responseMessage
     **/
    @ApiModelProperty(example = "Success")
    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    /**
     * Get responseDescription
     *
     * @return responseDescription
     **/
    @ApiModelProperty(example = "Successful alarm retrieval")
    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    /**
     * Get requestRefId
     *
     * @return requestRefId
     **/
    @ApiModelProperty(example = "936f9080-cb16-48be-93e7-afc0bc41afb7")
    public String getRequestRefId() {
        return requestRefId;
    }

    public void setRequestRefId(String requestRefId) {
        this.requestRefId = requestRefId;
    }

    /**
     * Get timestamp
     *
     * @return timestamp
     **/
    @ApiModelProperty(example = "2022-09-17T02:10:30.217+0000")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
                "requestRefId='" + requestRefId + '\'' +
                ", responseCode=" + responseCode +'\''+
                ", responseMessage='" + responseMessage + '\'' +
                ", customerMessage='" + customerMessage + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}
