package com.studio.batch.ticket.job;

import com.studio.batch.ticket.processor.TicketReader;
import com.studio.batch.ticket.processor.TicketWriter;
import com.studio.core.ticket.entity.TicketEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class InsertNaverOrderTicketJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TicketReader ticketReader;
    private final TicketWriter ticketWriter;

    @Bean
    public Job insertNaverOrderTicketsJob() {
        return new JobBuilder("insertNaverOrderTicketJob", jobRepository)
                .start(insertNaverOrderTicketsStep())
                .build();
    }

    @Bean
    public Step insertNaverOrderTicketsStep() {
        return new StepBuilder("insertNaverOrderTicketStep", jobRepository)
                .<TicketEntity, TicketEntity>chunk(10, transactionManager)
                .reader(ticketReader)
                .writer(ticketWriter)
                .allowStartIfComplete(true)
                .build();
    }
}

