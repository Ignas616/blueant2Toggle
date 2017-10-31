package de.moritz.blueant2toggl.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import de.moritz.blueant2toggl.configuration.LoggingRequestInterceptor;
import de.moritz.blueant2toggl.model.BlueAntEntry;
import de.moritz.blueant2toggl.model.TimeEntryWrapper;

@Service
public class TogglService {

    private static final Logger log = LoggerFactory.getLogger(TogglService.class);

    @Autowired
    private BlueAntReaderService readerService;

    @Autowired
    private BlueAntEntryProcessor processor;

    public int callToggl(String userName, String password, byte[] fileContent) throws Exception {
        FlatFileItemReader<BlueAntEntry> reader = readerService.reader(fileContent);
        reader.open(new ExecutionContext());

        List<TimeEntryWrapper> timeEntryList = convertBlueAntEntries(reader);
        new TogglWriter(restTemplate(userName, password)).write(timeEntryList);
        return timeEntryList.stream().map(timeEntryWrapper -> timeEntryWrapper.getTime_entry().getDuration()).mapToInt(Integer::intValue).sum();
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
                log.warn("End of file is reached");
                entry = null;
            }
        }
        return wrapperList;
    }

    private RestOperations restTemplate(String userName, String password) {
        return new RestTemplateBuilder().additionalInterceptors(new BasicAuthorizationInterceptor(userName, password), new LoggingRequestInterceptor()).build();
    }
}
