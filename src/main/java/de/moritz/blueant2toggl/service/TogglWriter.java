package de.moritz.blueant2toggl.service;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.web.client.RestOperations;

import de.moritz.blueant2toggl.model.TimeEntryWrapper;

public final class TogglWriter implements ItemWriter<TimeEntryWrapper> {

    private final RestOperations restTemplate;

    TogglWriter(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void write(List<? extends TimeEntryWrapper> items) throws Exception {
        for (TimeEntryWrapper wrapper : items) {
            restTemplate.postForEntity("https://www.toggl.com/api/v8/time_entries", wrapper, Object.class);
        }
    }
}
