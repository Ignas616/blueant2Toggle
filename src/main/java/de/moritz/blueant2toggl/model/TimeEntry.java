package de.moritz.blueant2toggl.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeEntry {
    private int wid;
    private int pid;
    private boolean billable;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime start;
    private LocalDateTime stop;
    private int duration;
    private boolean duronly;
    private LocalDateTime at;
    private int uid;
    private String created_with = "BlueAnt2Toggl";

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStop() {
        return stop;
    }

    public void setStop(LocalDateTime stop) {
        this.stop = stop;
    }

    public boolean isDuronly() {
        return duronly;
    }

    public void setDuronly(boolean duronly) {
        this.duronly = duronly;
    }

    public LocalDateTime getAt() {
        return at;
    }

    public void setAt(LocalDateTime at) {
        this.at = at;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCreated_with() {
        return created_with;
    }

    public void setCreated_with(String created_with) {
        this.created_with = created_with;
    }

    boolean isNotEmpty() {
        return duration > 0;
    }

    @Override
    public String toString() {
        return "TimeEntry{" +
                "wid=" + wid +
                ", pid=" + pid +
                ", billable=" + billable +
                ", description='" + description + '\'' +
                ", start=" + start +
                ", stop=" + stop +
                ", duration=" + duration +
                ", duronly=" + duronly +
                ", at=" + at +
                ", uid=" + uid +
                '}';
    }
}
