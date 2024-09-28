package net.kenevans.apiservice.requests.polarh10ecg;

public class RecordData
{
    private final RecordKeyData key;
    private final RecordValueData value;

    public RecordData(RecordKeyData recordKeyData, RecordValueData recordValueData) {
        this.key = recordKeyData;
        this.value = recordValueData;
    }

    public RecordValueData getRecordValueData() {
        return value;
    }

    public RecordKeyData getRecordKeyData() {
        return key;
    }
}