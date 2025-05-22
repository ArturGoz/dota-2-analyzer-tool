package artur.goz.userservice.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class RemoteResponse {
    private boolean succeeded;
    private String statusMessage;
    private List<?> results;

    public static RemoteResponse create(boolean succeeded, String statusMessage, List<?> additionalElements) {
        RemoteResponse response = new RemoteResponse();
        response.setSucceeded(succeeded);
        response.setStatusMessage(statusMessage);
        response.setResults(additionalElements);
        logMessage(statusMessage, succeeded);
        return response;
    }

    private static void logMessage(String message, Boolean isSuccess) {
        if(!isSuccess) log.error(message);
        else log.info(message);
    }

}
