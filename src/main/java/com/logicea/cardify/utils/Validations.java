package com.logicea.cardify.utils;

import com.logicea.cardify.modelapis.EntryResponse;
import com.logicea.cardify.modelapis.ModelApiResponse;
import com.logicea.cardify.modelpojos.HeaderErrorMessage;
import com.logicea.cardify.models.EStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.logicea.cardify.utils.Helper;
import com.logicea.cardify.utils.GlobalVariables;

@Service
public class Validations {
    private HeaderErrorMessage headerErrorMessage;
    public ResponseEntity<ModelApiResponse> validateRequestBody(HttpHeaders headers, String requestRefId,
                                                                String transactionType, String name,
                                                                String description, String color,
                                                                String user) {
        // Global Headers
        headerErrorMessage = new HeaderErrorMessage();
        if(name == null ||!Helper.isColorFormatValid(color) || name.isEmpty()) {
            //LogsManager.warn(requestRefId, transactionType, "", "", "", "","", "Request values cannot be empty", String.valueOf(HttpStatus.BAD_REQUEST), GlobalVariables.RESPONSE_REQUEST_VALUES_EMPTY, "Invalid Request Payload", "", "");
            if(!Helper.isColorFormatValid(color)){
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_400, requestRefId, GlobalVariables.INVALID_COLOR_FORMAT,
                                GlobalVariables.INVALID_COLOR_FORMAT, ""),
                        HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(EntryResponse
                    .responseFormatter(GlobalVariables.RESPONSE_CODE_400, requestRefId, GlobalVariables.RESPONSE_REQUEST_VALUES_EMPTY,
                            GlobalVariables.RESPONSE_REQUEST_VALUES_EMPTY, ""),
                    HttpStatus.BAD_REQUEST);
        }

        return null;
    }


    public ResponseEntity<ModelApiResponse> validateUpdateRequestBody(HttpHeaders headers, String requestRefId,
                                                                String transactionType, String name,
                                                                String description, String color,
                                                                String user,String status) {
        // Global Headers
        headerErrorMessage = new HeaderErrorMessage();
        if(status!=null && !status.equalsIgnoreCase(EStatus.ToDo.toString()) && !status.equalsIgnoreCase(EStatus.InProgress.toString())&&!status.equalsIgnoreCase(EStatus.Done.toString())){
            return new ResponseEntity<>(EntryResponse
                    .responseFormatter(GlobalVariables.RESPONSE_CODE_400, requestRefId, GlobalVariables.INVALID_STATUS_FORMAT,
                            GlobalVariables.INVALID_STATUS_FORMAT, ""),
                    HttpStatus.BAD_REQUEST);
        }
        if(name == null ||!Helper.isColorFormatValid(color) || name.isEmpty()) {
            //LogsManager.warn(requestRefId, transactionType, "", "", "", "","", "Request values cannot be empty", String.valueOf(HttpStatus.BAD_REQUEST), GlobalVariables.RESPONSE_REQUEST_VALUES_EMPTY, "Invalid Request Payload", "", "");
            if(!Helper.isColorFormatValid(color)){
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_400, requestRefId, GlobalVariables.INVALID_COLOR_FORMAT,
                                GlobalVariables.INVALID_COLOR_FORMAT, ""),
                        HttpStatus.BAD_REQUEST);
            }

            if(name==null || name.isEmpty()){
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_400, requestRefId, GlobalVariables.RESPONSE_NAME_VALUES_EMPTY,
                                GlobalVariables.RESPONSE_NAME_VALUES_EMPTY, ""),
                        HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(EntryResponse
                    .responseFormatter(GlobalVariables.RESPONSE_CODE_400, requestRefId, GlobalVariables.RESPONSE_REQUEST_VALUES_EMPTY,
                            GlobalVariables.RESPONSE_REQUEST_VALUES_EMPTY, ""),
                    HttpStatus.BAD_REQUEST);
        }

        return null;
    }



}
