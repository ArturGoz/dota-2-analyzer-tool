package artur.goz.authservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class RemoteResponse {
    private boolean succeeded;
    private String statusMessage;
    private List<?> results;

    public static RemoteResponse create(boolean succeeded, String statusMessage, List<?> additionalElements) {
        RemoteResponse response = new RemoteResponse();
        response.setSucceeded(succeeded);
        response.setStatusMessage(statusMessage);
        response.setResults(additionalElements);
        return response;
    }
}
