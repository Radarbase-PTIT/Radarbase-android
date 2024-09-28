package net.kenevans.apiservice.requests.polarh10ecg;


import java.util.HashMap;

public class RecordKeyData
{
    private HashMap<String, String> projectId;

    private String sourceId;

    private String userId;

    public RecordKeyData(String project, String sourceId, String userId) {
        this.projectId = new HashMap<>();
        this.projectId.put("string", project);
        this.sourceId = sourceId;
        this.userId = userId;
    }

    public HashMap<String, String> getProjectId() {
        return projectId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getUserId() {
        return userId;
    }
}
