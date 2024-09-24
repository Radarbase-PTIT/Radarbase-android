package net.kenevans.baseapi.requests;

import net.kenevans.baseapi.requests.polarh10ecg.RecordData;

public class PolarH10ECG
{
    private final int key_schema_id;

    private final int value_schema_id;

    private RecordData[] records;

    public PolarH10ECG(int keySchemaId, int valueSchemaId, RecordData[] records) {
        this.key_schema_id = keySchemaId;
        this.value_schema_id = valueSchemaId;
        this.records = records;
    }

    public int getKeySchemaId() {
        return key_schema_id;
    }

    public int getValueSchemaId() {
        return value_schema_id;
    }

    public RecordData[] getRecords() {
        return records;
    }
}
