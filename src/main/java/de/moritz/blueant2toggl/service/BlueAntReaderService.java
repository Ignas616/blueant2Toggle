package de.moritz.blueant2toggl.service;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import de.moritz.blueant2toggl.model.BlueAntEntry;

@Service
public class BlueAntReaderService {

    FlatFileItemReader<BlueAntEntry> reader(byte[] fileContent) {
        FlatFileItemReader<BlueAntEntry> reader = new FlatFileItemReader<>();
        reader.setResource(new ByteArrayResource(fileContent));
        reader.setLineMapper(createBlueAntLineMapper());
        reader.setLinesToSkip(12);
        reader.setEncoding("ISO8859-1");
        return reader;
    }

    private LineMapper<BlueAntEntry> createBlueAntLineMapper() {
        DefaultLineMapper<BlueAntEntry> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(createLineTokenizer());
        mapper.setFieldSetMapper(createFieldSetMapper());
        return mapper;
    }

    private FieldSetMapper<BlueAntEntry> createFieldSetMapper() {
        BeanWrapperFieldSetMapper<BlueAntEntry> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BlueAntEntry.class);
        return fieldSetMapper;
    }

    private LineTokenizer createLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(";");
        tokenizer.setNames(new String[] { "date", "duration", "project" });
        tokenizer.setIncludedFields(new int[] { 1, 2, 4 });
        return tokenizer;
    }
}
