package de.moritz.blueant2toggl.service;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.moritz.blueant2toggl.model.BlueAntEntry;
import de.moritz.blueant2toggl.model.TimeEntry;
import de.moritz.blueant2toggl.model.TimeEntryWrapper;

@Service
public class BlueAntEntryProcessor implements ItemProcessor<BlueAntEntry, TimeEntryWrapper> {

    private static final Logger            log              = LoggerFactory.getLogger(BlueAntEntryProcessor.class);

    private static final float             SECONDS_PER_HOUR = 3600.0F;

    private static final DateTimeFormatter GERMAN_DATE      = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('.').appendValue(MONTH_OF_YEAR, 2).appendLiteral('.').appendValue(YEAR, 4).toFormatter();

    @Value("${toggl.workspaceId}")
    private int                            workspaceId;

    @Value("${toggl.project.riskManagement}")
    private int                            projectRiskId;

    @Value("${toggl.project.vacation}")
    private int                            projectVacationId;

    @Override
    public TimeEntryWrapper process(BlueAntEntry item) throws Exception {
        if (StringUtils.isEmpty(item.getDate())) {
            return null;
        }

        TimeEntry entry = new TimeEntry();
        entry.setDescription(item.getProject());
        entry.setWid(workspaceId);
        entry.setPid(projectRiskId);
        entry.setBillable(true);
        entry.setStart(LocalDate.parse(item.getDate(), GERMAN_DATE).atTime(7, 0));
        entry.setDuration(parseDuration(item.getDuration()));

        TimeEntryWrapper wrapper = new TimeEntryWrapper();
        wrapper.setTime_entry(entry);
        return wrapper;
    }

    public int parseDuration(String duration) {
        try {
            if (duration.contains(".")){
                duration = duration.replace('.', ',');
            }

            return Math.round(NumberFormat.getNumberInstance(Locale.GERMAN).parse(duration).floatValue() * SECONDS_PER_HOUR);
        } catch (ParseException e) {
            log.error("Unparsable duration {}", duration, e);
        }
        return 0;
    }
}
