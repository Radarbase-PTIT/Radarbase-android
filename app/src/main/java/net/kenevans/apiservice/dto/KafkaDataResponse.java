package net.kenevans.apiservice.dto;

public class KafkaDataResponse {
    private String[] offsets;

    private int keySchemaId;

    private int valueSchemaId;

    public String[] getOffsets() {
        return offsets;
    }

    public int getKeySchemaId() {
        return this.keySchemaId;
    }

    public int getValueSchemaId() {
        return this.valueSchemaId;
    }
}
