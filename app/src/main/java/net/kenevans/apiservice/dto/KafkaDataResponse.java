package net.kenevans.apiservice.dto;

import net.kenevans.apiservice.dto.kafkaresponse.Offsets;

public class KafkaDataResponse {
    private Offsets[] offsets;

    private int key_schema_id;

    private int value_schema_id;

    public Offsets[] getOffsets() {
        return offsets;
    }

    public int getKeySchemaId() {
        return this.key_schema_id;
    }

    public int getValueSchemaId() {
        return this.value_schema_id;
    }
}
