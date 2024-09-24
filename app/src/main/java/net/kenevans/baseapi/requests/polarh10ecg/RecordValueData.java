package net.kenevans.baseapi.requests.polarh10ecg;

import java.util.Date;

public class RecordValueData
{
    private final double time;

    private final double timeReceived;

    private double ecg;

    public RecordValueData(double ecg) {
        this.time = new Date().getTime() / 1000.0;
        this.timeReceived = new Date().getTime() / 1000.0;
        this.ecg = ecg;
    }

    public double getEcg() {
        return ecg;
    }

    public void setEcg(double ecg) {
        this.ecg = ecg;
    }

    public double getTimeReceived() {
        return timeReceived;
    }

    public double getTime() {
        return time;
    }

}

