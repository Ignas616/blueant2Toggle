package de.moritz.blueant2toggl.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import de.moritz.blueant2toggl.configuration.LoggingRequestInterceptor;
import de.moritz.blueant2toggl.model.BlueAntEntry;
import de.moritz.blueant2toggl.model.TimeEntry;
import de.moritz.blueant2toggl.model.TimeEntryWrapper;

@Service
public class TogglService {

    private static final Logger   log = LoggerFactory.getLogger(TogglService.class);

    @Value("${toggl.workspaceId}")
    private int                   workspaceId;

    @Value("${toggl.project.vacation}")
    private int                   projectVacationId;
    @Autowired
    private BlueAntReaderService  readerService;

    @Autowired
    private BlueAntEntryProcessor processor;

    public int callToggl(String userName, String password, LocalDate vacationStartDate, LocalDate vacationEndDate, byte[] fileContent) throws Exception {
        List<TimeEntryWrapper> timeEntryList = new ArrayList<>();
        if (fileContent != null) {
            FlatFileItemReader<BlueAntEntry> reader = readerService.reader(fileContent);
            reader.open(new ExecutionContext());

            timeEntryList = convertBlueAntEntries(reader);
        }

        if (vacationStartDate != null && vacationEndDate != null) {
            timeEntryList.addAll(createVacationDays(vacationStartDate, vacationEndDate));
        }
        if (!timeEntryList.isEmpty()) {
            new TogglWriter(restTemplate(userName, password)).write(timeEntryList);
            return timeEntryList.stream().map(timeEntryWrapper -> timeEntryWrapper.getTime_entry().getDuration()).mapToInt(Integer::intValue).sum();
        }
        return 0;
    }

    private List<TimeEntryWrapper> createVacationDays(LocalDate vacationStartDate, LocalDate vacationEndDate) {
        List<TimeEntryWrapper> wrapperList = new ArrayList<>();

        while (!vacationStartDate.isAfter(vacationEndDate)) {
            System.out.println(vacationStartDate);

            if (!vacationStartDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !vacationStartDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                wrapperList.add(createWrapperVacation(vacationStartDate));
            }
            vacationStartDate = vacationStartDate.plusDays(1);
        }
        return wrapperList;

    }

    private TimeEntryWrapper createWrapperVacation(LocalDate date) {
        TimeEntry entry = new TimeEntry();
        entry.setDescription("Vacation");
        entry.setWid(workspaceId);
        entry.setPid(projectVacationId);
        entry.setBillable(false);
        entry.setStart(date.atTime(9, 0));
        entry.setDuration(processor.parseDuration("8.0"));

        TimeEntryWrapper wrapper = new TimeEntryWrapper();
        wrapper.setTime_entry(entry);
        return wrapper;
    }

    private List<TimeEntryWrapper> convertBlueAntEntries(ItemReader<BlueAntEntry> reader) throws Exception {
        List<TimeEntryWrapper> wrapperList = new ArrayList<>();
        BlueAntEntry entry = reader.read();
        while (entry != null) {
            TimeEntryWrapper timeEntryWrapper = processor.process(entry);
            if (timeEntryWrapper != null && timeEntryWrapper.isNotEmpty()) {
                wrapperList.add(timeEntryWrapper);
            }
            try {
                entry = reader.read();
            } catch (FlatFileParseException e) {
                log.warn("Error reading file", e);
                entry = null;
            }
        }
        return wrapperList;
    }

    private RestOperations restTemplate(String userName, String password) {
        return new RestTemplateBuilder().additionalInterceptors(new BasicAuthorizationInterceptor(userName, password), new LoggingRequestInterceptor()).build();
    }
}
