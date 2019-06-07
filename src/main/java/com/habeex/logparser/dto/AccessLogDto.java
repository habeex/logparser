package com.habeex.logparser.dto;

import org.springframework.boot.ApplicationArguments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AccessLogDto {
    private Long threshold;
    private Date startDate;
    private Date endDate;
    private String accessLog;
    private ApplicationArguments args;

    public AccessLogDto(ApplicationArguments args) {
        this.args = args;
        if (args.containsOption("startDate")) {
            String date = args.getOptionValues("startDate").get(0);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd.hh:mm:ss");
            try {
                this.startDate = formatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.endDate = startDate;
        }
        if (args.containsOption("duration")) {
            String duration = args.getOptionValues("duration").get(0);
            if (duration.equals("hourly")) {
                this.endDate = new Date(this.startDate.getTime() + TimeUnit.HOURS.toMillis( 1 ));
            } else if (duration.equals("daily")) {
                this.endDate = new Date(this.startDate.getTime() + TimeUnit.DAYS.toMillis( 1 ));
            }
        }
        if (args.containsOption("threshold")) {
            this.threshold = new Long(args.getOptionValues("threshold").get(0));
        }
        if (args.containsOption("accesslog")) {
            this.accessLog = args.getOptionValues("accesslog").get(0);
        }
    }

    public Long getThreshold() {
        return threshold;
    }

    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(String accessLog) {
        this.accessLog = accessLog;
    }

    public ApplicationArguments getArgs() {
        return args;
    }

    public void setArgs(ApplicationArguments args) {
        this.args = args;
    }

    public boolean isPresent() {
        return  (this.args.containsOption("startDate") && this.args.containsOption("duration") && this.args.containsOption("threshold"));
    }
}
