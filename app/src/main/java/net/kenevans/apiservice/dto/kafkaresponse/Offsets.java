package net.kenevans.apiservice.dto.kafkaresponse;

public class Offsets
{
    private int partition;

    private int offset;

    private int errorCode;

    private String error;

    public Offsets(int partition, int offset, int errorCode, String error) {
        this.partition = partition;
        this.offset = offset;
        this.errorCode = errorCode;
        this.error = error;
    }

    public int getPartition() {
        return partition;
    }

    public int getOffset() {
        return offset;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getError() {
        return error;
    }
}
