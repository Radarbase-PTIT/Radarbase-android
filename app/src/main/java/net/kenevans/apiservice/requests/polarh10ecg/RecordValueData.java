package net.kenevans.apiservice.requests.polarh10ecg;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RecordValueData
{
    private final double time;

    private final double timeReceived;

    private List<Double> ecg;

    private List<Integer> heartRate;

    private int measurementTimes;

    public RecordValueData(List<Double> ecg, List<Integer> heartRate, int measurementTimes) {
        this.time = convertUTCToVietNam();
        this.timeReceived = convertUTCToVietNam();
        this.ecg = ecg;
        this.heartRate = heartRate;
        this.measurementTimes = measurementTimes;
    }

    private double convertUTCToVietNam() {
        // Create a Calendar instance initialized with the current date and time in the system's timezone
        Calendar calendar = Calendar.getInstance();

        // Adjust the timezone to UTC+7
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));

        // Print the time in UTC+7 timezone
        Date d = calendar.getTime();

        return d.getTime() / 1000.0;
    }

}

