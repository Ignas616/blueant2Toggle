package de.moritz.blueant2toggl.model;

public final class BlueAntEntry {

    private String date;
    private String project;
    private String duration;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "BlueAntEntry{" +
                "date='" + date + '\'' +
                ", project='" + project + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
