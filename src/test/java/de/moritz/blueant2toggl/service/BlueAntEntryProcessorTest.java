package de.moritz.blueant2toggl.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Month;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import de.moritz.blueant2toggl.model.BlueAntEntryBuilder;
import de.moritz.blueant2toggl.model.TimeEntryWrapper;

@RunWith(MockitoJUnitRunner.class)
public class BlueAntEntryProcessorTest {

    @InjectMocks
    private BlueAntEntryProcessor processor;

    @Test
    public void shouldYieldNullForEmptyEntry() throws Exception {
        TimeEntryWrapper wrapper = processor.process(BlueAntEntryBuilder.aBlueAntEntry().build());

        assertThat(wrapper).isNull();
    }

    @Test
    public void shouldYieldValidWrapperWithGermanNumber() throws Exception {
        TimeEntryWrapper wrapper = processor
                .process(BlueAntEntryBuilder.aBlueAntEntry().withDate("01.01.1980").withDuration("2,50").withProject("TestProject").build());

        assertThat(wrapper).isNotNull();
        assertThat(wrapper.isNotEmpty()).isTrue();
        assertThat(wrapper.getTime_entry().getDescription()).isEqualTo("TestProject");
        assertThat(wrapper.getTime_entry().getDuration()).isEqualTo(9000);
        assertThat(wrapper.getTime_entry().getStart().getMonth()).isEqualTo(Month.JANUARY);
    }

    @Test
    public void shouldYieldValidWrapperWithEnglishNumber() throws Exception {
        TimeEntryWrapper wrapper = processor
                .process(BlueAntEntryBuilder.aBlueAntEntry().withDate("16.08.1980").withDuration("2.50").withProject("TestProject").build());

        assertThat(wrapper).isNotNull();
        assertThat(wrapper.isNotEmpty()).isTrue();
        assertThat(wrapper.getTime_entry().getDescription()).isEqualTo("TestProject");
        assertThat(wrapper.getTime_entry().getDuration()).isEqualTo(9000);
        assertThat(wrapper.getTime_entry().getStart().getMonth()).isEqualTo(Month.AUGUST);
    }

    @Test
    public void shouldYieldInvalidWrapperWithInvalidDuration() throws Exception {
        TimeEntryWrapper wrapper = processor
                .process(BlueAntEntryBuilder.aBlueAntEntry().withDate("16.08.1980").withDuration("asdf").withProject("TestProject").build());

        assertThat(wrapper).isNotNull();
        assertThat(wrapper.isNotEmpty()).isFalse();
        assertThat(wrapper.getTime_entry().getDescription()).isEqualTo("TestProject");
        assertThat(wrapper.getTime_entry().getDuration()).isEqualTo(0);
        assertThat(wrapper.getTime_entry().getStart().getMonth()).isEqualTo(Month.AUGUST);
    }
}
