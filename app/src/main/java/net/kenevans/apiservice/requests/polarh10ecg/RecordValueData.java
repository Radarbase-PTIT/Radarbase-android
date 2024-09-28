package net.kenevans.apiservice.requests.polarh10ecg;

import java.util.Date;
import java.util.List;

public class RecordValueData
{
    private final double time;

    private final double timeReceived;

    private List<Double> ecg;

    private List<Integer> heartRate;

    private int measurementTimes;

    public RecordValueData(List<Double> ecg, List<Integer> heartRate, int measurementTimes) {
        this.time = new Date().getTime() / 1000.0;
        this.timeReceived = new Date().getTime() / 1000.0;
        this.ecg = ecg;
        this.heartRate = heartRate;
        this.measurementTimes = measurementTimes;
    }

}

