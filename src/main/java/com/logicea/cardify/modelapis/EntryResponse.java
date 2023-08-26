package com.logicea.cardify.modelapis;
import java.sql.Timestamp;

/**
 * @author EONYANDO
 *
 */
public class
EntryResponse extends ModelApiResponse {
	  /**
     * Format the response Object according to the standard. That is {@code} ModelApiResponse
     *
     * @param responseCode   Contains the response code
     * @param referenceId    Contains the reference id
     * @param message        Holds the message of the header
     * @param responseObject Holds the response Object if available.
     * @return responseFormatter of type EntryResponse
     */

    public static ModelApiResponse responseFormatter(
            int responseCode, String referenceId, String message, String description, Object responseObject){
        EntryResponse entryResponse = new EntryResponse();
        entryResponse.getApiHeaderResponse().setResponseCode(responseCode);
        entryResponse.getApiHeaderResponse().setRequestRefId(referenceId);
        entryResponse.getApiHeaderResponse().setResponseMessage(message);
        entryResponse.getApiHeaderResponse().setCustomerMessage(description);
        entryResponse.getApiHeaderResponse().setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        if(responseObject != null){
            entryResponse.setResponseBodyObject(responseObject);
        }
        return entryResponse;
    }

}
