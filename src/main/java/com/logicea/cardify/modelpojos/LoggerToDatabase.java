package com.logicea.cardify.modelpojos;

import com.safaricom.microservices.logging.LogsManager;
import com.safaricom.microservices.model.RcaAction;
import com.safaricom.microservices.model.RcaData;
import com.safaricom.microservices.model.RcaLearning;
import com.safaricom.microservices.modelapis.EntryResponse;
import com.safaricom.microservices.modelapis.ModelApiResponse;
import com.safaricom.microservices.repository.RcaActionsRepository;
import com.safaricom.microservices.repository.RcaDataRepository;
import com.safaricom.microservices.repository.RcaLearningRepository;
import com.safaricom.microservices.utils.GlobalVariables;
import com.safaricom.microservices.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author EONYANDO
 */

@Service
public class LoggerToDatabase {

    private final RcaDataRepository rcaDataRepository;
    private final RcaLearningRepository rcaLearningRepository;
    private final RcaActionsRepository rcaActionsRepository;

    @Autowired
    public LoggerToDatabase(RcaDataRepository rcaDataRepository, RcaLearningRepository rcaLearningRepository,RcaActionsRepository rcaActionsRepository) {
        this.rcaDataRepository = rcaDataRepository;
        this.rcaLearningRepository=rcaLearningRepository;
        this.rcaActionsRepository=rcaActionsRepository;
    }

    @Async("threadPoolTaskExecutorAsyncConfig")
    public void saveRcaToDatabase(String requestReferenceId, String sourceApp, String RcaNumber,String IncidentNumber,
                                  String EventSequence,String Status,String Division,String Priority,String ServiceOwner,
                                  String AffectedService,String ResolutionTeam,String RcaAssignee,
                                  String CustomerComplaints,String CustomerImpact,String DirectCause,
                                  String RootCause,String Workaround,String PermanentSolution){
        try{
            List<RcaData> listUserExist = rcaDataRepository.findByIncidentNumber(IncidentNumber);

            RcaData rcaData = new RcaData();
            rcaData.setRcaNumber(RcaNumber);
            rcaData.setIncidentNumber(IncidentNumber);
            rcaData.setEventSequence(EventSequence);
            rcaData.setStatus(Status);
            rcaData.setDivision(Division);
            rcaData.setPriority(Priority);
            rcaData.setServiceOwner(ServiceOwner);
            rcaData.setAffectedService(AffectedService);
            rcaData.setResolutionTeam(ResolutionTeam);
            rcaData.setRcaAssignee(RcaAssignee);
            rcaData.setCustomerComplaints(CustomerComplaints);
            rcaData.setCustomerImpact(CustomerImpact);
            rcaData.setDirectCause(DirectCause);
            rcaData.setRootCause(RootCause);
            rcaData.setWorkaround(Workaround);
            rcaData.setPermanentSolution(PermanentSolution);

            if(listUserExist.isEmpty()){
                rcaDataRepository.save(rcaData);
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA DATA TO DATABASE", "Insert Rca Data Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation successful", String.valueOf(GlobalVariables.RESPONSE_CODE_200), "", "", "", "", "");
            }else{
                LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA DATA TO DATABASE", "Insert Rca Data Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-servicee", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_409), "+Incident : "+IncidentNumber+" already exists", "ERROR RCA DATA TO DATABASE", "", "", "");
            }
        }catch(Exception ex){
            LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA DATA TO DATABASE", "Insert Rca Data Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_500), "Failed to add RCA data to the database", ex.getLocalizedMessage(), "", "", "");
        }
    }


    public ResponseEntity<ModelApiResponse> updateRcaData(String requestRefId, String sourceApp,String incidentNumberHeader,Long startTime,String RcaNumber,String IncidentNumber,
                                  String EventSequence,String Status,String Division,String Priority,String ServiceOwner,
                                  String AffectedService,String ResolutionTeam,String RcaAssignee,
                                  String CustomerComplaints,String CustomerImpact,String DirectCause,
                                  String RootCause,String Workaround,String PermanentSolution) {
        try {
            System.out.println("Incident Number"+IncidentNumber);
            List<RcaData> updateRca_Data = rcaDataRepository.findByIncidentNumber(IncidentNumber);
            if (!updateRca_Data.isEmpty()) {
                RcaData updateRcaData = rcaDataRepository.findOneByIncidentNumber(IncidentNumber);
                updateRcaData.setRcaNumber(RcaNumber);
                updateRcaData.setIncidentNumber(IncidentNumber);
                updateRcaData.setEventSequence(EventSequence);
                updateRcaData.setStatus(Status);
                updateRcaData.setDivision(Division);
                updateRcaData.setPriority(Priority);
                updateRcaData.setServiceOwner(ServiceOwner);
                updateRcaData.setAffectedService(AffectedService);
                updateRcaData.setResolutionTeam(ResolutionTeam);
                updateRcaData.setRcaAssignee(RcaAssignee);
                updateRcaData.setCustomerComplaints(CustomerComplaints);
                updateRcaData.setCustomerImpact(CustomerImpact);
                updateRcaData.setDirectCause(DirectCause);
                updateRcaData.setRootCause(RootCause);
                updateRcaData.setWorkaround(Workaround);
                updateRcaData.setPermanentSolution(PermanentSolution);

                System.out.println("Before save update");
                try {
                    rcaDataRepository.save(updateRcaData);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                System.out.println("After update");
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestRefId, "UPDATE RCA DATA", "Update Rca Data", System.currentTimeMillis() - startTime+"ms", "", incidentNumberHeader, sourceApp, "ms-ossportal-rca-service", "Operation successful", String.valueOf(GlobalVariables.RESPONSE_CODE_200), "", "", "", "", "");

                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_200, requestRefId, "RCA data updated successfully",
                                "RCA data updated successfully", updateRca_Data), HttpStatus.OK);
            } else {
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestRefId, "UPDATE RCA DATA", "Update Rca Data", System.currentTimeMillis() - startTime+"ms", "", incidentNumberHeader, sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_404), "", "", "", "", "");
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_404, requestRefId, "RCA not found",
                                "RCA Data Details not found", updateRca_Data), HttpStatus.NOT_FOUND);

            }
        } catch (Exception ex) {
            LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestRefId, "UPDATE Rca DATA", "Update Rca Data", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_500), "Failed to Update Rca data", ex.getLocalizedMessage(), "", "", "");
            return new ResponseEntity<>(EntryResponse
                    .responseFormatter(GlobalVariables.RESPONSE_CODE_500, requestRefId, "RCA data update failed",
                            "RCA data update failed", ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveRcaLearningToDatabase(String requestReferenceId, String sourceApp, String RcaNumber,String Item,
                                  String RAG,String Details,String Learnings,String Action,String Owner,
                                  String Deadline){
        try{
            List<RcaLearning> listUserExist = rcaLearningRepository.findRcaLearningByRcaNumber(RcaNumber);

            RcaLearning rcaLearning = new RcaLearning();
            rcaLearning.setRcaNumber(RcaNumber);
            rcaLearning.setItem(Item);
            rcaLearning.setRAG(RAG);
            rcaLearning.setDetails(Details);
            rcaLearning.setLearnings(Learnings);
            rcaLearning.setAction(Action);
            rcaLearning.setOwner(Owner);
            rcaLearning.setDeadline(Deadline);


            if(listUserExist.isEmpty()){
                rcaLearningRepository.save(rcaLearning);
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA LEARNING DATA TO DATABASE", "Insert Rca Learning Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation successful", String.valueOf(GlobalVariables.RESPONSE_CODE_200), "", "", "", "", "");
            }else{
                LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA LEARNING DATA TO DATABASE", "Insert Rca Learning Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-servicee", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_409), "+RcaNumber : "+RcaNumber+" already exists", "ERROR RCA DATA TO DATABASE", "", "", "");
            }
        }catch(Exception ex){
            LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA LEARNING DATA TO DATABASE", "Insert Rca Learning Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_500), "Failed to add RCA data to the database", ex.getLocalizedMessage(), "", "", "");
        }
    }


    public ResponseEntity<ModelApiResponse> updateRcaLearningData(String requestRefId, String sourceApp,
                                                                  String incidentNumberHeader,
                                                                  Long startTime,String RcaNumber,String Item,
                                                                  String RAG,String Details,String Learnings,
                                                                  String Action,String Owner,
                                                                  String Deadline) {
        try {

            List<RcaLearning> updateRcaLearning_Data = rcaLearningRepository.findRcaLearningByRcaNumber(RcaNumber);
            if (!updateRcaLearning_Data.isEmpty()) {
                RcaLearning updateRcaLearningData = rcaLearningRepository.findOneRcaLearningByRcaNumber(RcaNumber);
                updateRcaLearningData.setRcaNumber(RcaNumber);
                updateRcaLearningData.setItem(Item);
                updateRcaLearningData.setRAG(RAG);
                updateRcaLearningData.setDetails(Details);
                updateRcaLearningData.setLearnings(Learnings);
                updateRcaLearningData.setAction(Action);
                updateRcaLearningData.setOwner(Owner);
                updateRcaLearningData.setDeadline(Deadline);


                System.out.println("Before save update");
                try {
                    rcaLearningRepository.save(updateRcaLearningData);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                System.out.println("After update");
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestRefId, "UPDATE RCA Learning DATA", "Update Rca Learning Data", System.currentTimeMillis() - startTime+"ms", "", incidentNumberHeader, sourceApp, "ms-ossportal-rca-service", "Operation successful", String.valueOf(GlobalVariables.RESPONSE_CODE_200), "", "", "", "", "");

                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_200, requestRefId, "RCA Learning data updated successfully",
                                "RCA Learning data updated successfully", updateRcaLearningData), HttpStatus.OK);
            } else {
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestRefId, "UPDATE RCA Learning DATA", "Update Rca Learning Data", System.currentTimeMillis() - startTime+"ms", "", incidentNumberHeader, sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_404), "", "", "", "", "");
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_404, requestRefId, "RCA Learning not found",
                                "RCA Learning Data Details not found", updateRcaLearning_Data), HttpStatus.NOT_FOUND);

            }
        } catch (Exception ex) {
            LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestRefId, "UPDATE Rca Learning DATA", "Update Rca Learning Data", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_500), "Failed to Update Rca Learning data", ex.getLocalizedMessage(), "", "", "");
            return new ResponseEntity<>(EntryResponse
                    .responseFormatter(GlobalVariables.RESPONSE_CODE_500, requestRefId, "RCA Learning data update failed",
                            "RCA Learning data update failed", ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void saveRcaActionToDatabase(String requestReferenceId, String sourceApp, String RcaNumber,String Item
                                 ){
        try{
            List<RcaAction> listUserExist = rcaActionsRepository.findRcaActionByRcaNumber(RcaNumber);

            RcaAction rcaAction = new RcaAction();
            rcaAction.setRcaNumber(RcaNumber);
            rcaAction.setItem(Item);


            if(listUserExist.isEmpty()){
                rcaActionsRepository.save(rcaAction);
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA ACTION DATA TO DATABASE", "Insert Rca Action Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation successful", String.valueOf(GlobalVariables.RESPONSE_CODE_200), "", "", "", "", "");
            }else{
                LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA  ACTION DATA TO DATABASE", "Insert Rca Action Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_409), "+RcaNumber : "+RcaNumber+" already exists", "ERROR adding RCA Action TO DATABASE", "", "", "");
            }
        }catch(Exception ex){
            LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestReferenceId, "INSERT RCA ACTION DATA TO DATABASE", "Insert Rca Action Data To DB", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_500), "Failed to add RCA Action data to the database", ex.getLocalizedMessage(), "", "", "");
        }
    }

    public ResponseEntity<ModelApiResponse> updateRcaActionData(String requestRefId, String sourceApp,
                                                                  String incidentNumberHeader,
                                                                  Long startTime,String RcaNumber,String Item
                                                                 ) {
        try {

            List<RcaAction> updateRcaAction_Data = rcaActionsRepository.findRcaActionByRcaNumber(RcaNumber);
            if (!updateRcaAction_Data.isEmpty()) {
                RcaAction updateRcaActionData = rcaActionsRepository.findOneRcaActionByRcaNumber(RcaNumber);
                updateRcaActionData.setRcaNumber(RcaNumber);
                updateRcaActionData.setItem(Item);


                System.out.println("Before save update");
                try {
                    rcaActionsRepository.save(updateRcaActionData);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                System.out.println("After update");
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestRefId, "UPDATE RCA Action DATA", "Update Rca Action Data", System.currentTimeMillis() - startTime+"ms", "", incidentNumberHeader, sourceApp, "ms-ossportal-rca-service", "Operation successful", String.valueOf(GlobalVariables.RESPONSE_CODE_200), "", "", "", "", "");

                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_200, requestRefId, "RCA Action data updated successfully",
                                "RCA Action data updated successfully", updateRcaAction_Data), HttpStatus.OK);
            } else {
                LogsManager.logInfo(Helper.getRequestedDateTime(), "INFO", "ms-ossportal-rca-service", requestRefId, "UPDATE RCA Action DATA", "Update Rca Action Data", System.currentTimeMillis() - startTime+"ms", "", incidentNumberHeader, sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_404), "", "", "", "", "");
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_404, requestRefId, "RCA Action not found",
                                "RCA Action Data Details not found", updateRcaAction_Data), HttpStatus.NOT_FOUND);

            }
        } catch (Exception ex) {
            LogsManager.logError(Helper.getRequestedDateTime(), "ERROR", "ms-ossportal-rca-service", requestRefId, "UPDATE Rca Learning DATA", "Update Rca Learning Data", "", "", "", sourceApp, "ms-ossportal-rca-service", "Operation unsuccessful", String.valueOf(GlobalVariables.RESPONSE_CODE_500), "Failed to Update Rca Learning data", ex.getLocalizedMessage(), "", "", "");
            return new ResponseEntity<>(EntryResponse
                    .responseFormatter(GlobalVariables.RESPONSE_CODE_500, requestRefId, "RCA Learning data update failed",
                            "RCA Learning data update failed", ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

