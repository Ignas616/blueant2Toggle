package de.moritz.blueant2toggl.model;

public final class BlueAntEntryBuilder {

    private String date;
    private String project;
    private String duration;

    private BlueAntEntryBuilder() {
    }

    public static BlueAntEntryBuilder aBlueAntEntry() {
        return new BlueAntEntryBuilder();
    }

    public BlueAntEntryBuilder withDate(String date) {
        this.date = date;
        return this;
    }

    public BlueAntEntryBuilder withProject(String project) {
        this.project = project;
        return this;
    }

    public BlueAntEntryBuilder withDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public BlueAntEntry build() {
        BlueAntEntry blueAntEntry = new BlueAntEntry();
        blueAntEntry.setDate(date);
        blueAntEntry.setProject(project);
        blueAntEntry.setDuration(duration);
        return blueAntEntry;
    }
}
