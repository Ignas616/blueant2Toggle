package de.moritz.blueant2toggl.model;

public final class TimeEntryWrapper {

    private TimeEntry time_entry;

    public TimeEntry getTime_entry() {
        return time_entry;
    }

    public void setTime_entry(TimeEntry time_entry) {
        this.time_entry = time_entry;
    }

    public boolean isNotEmpty() {
        return time_entry.isNotEmpty();
    }

    @Override
    public String toString() {
        return "TimeEntryWrapper{" + "time_entry=" + time_entry + '}';
    }
}
