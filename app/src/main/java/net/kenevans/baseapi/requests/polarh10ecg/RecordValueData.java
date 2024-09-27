package net.kenevans.baseapi.requests.polarh10ecg;

import java.util.Date;

public class RecordValueData
{
    private final double time;

    private final double timeReceived;

    private double ecg;

    private int heartRate;

    private int measurementTimes;

    private int measurementCurrentState;

    public RecordValueData(double ecg, int heartRate, int measurementTimes, int measurementCurrentState) {
        this.time = new Date().getTime() / 1000.0;
        this.timeReceived = new Date().getTime() / 1000.0;
        this.ecg = ecg;
        this.heartRate = heartRate;
        this.measurementTimes = measurementTimes;
        this.measurementCurrentState = measurementCurrentState;
    }

    public double getEcg() {
        return ecg;
    }

    public int getHeartRate() { return heartRate; }

    public double getTime() {
        return time;
    }

}

