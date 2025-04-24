package com.studio.batch.order.job;

import com.studio.batch.order.processor.NaverProductOrderProcessor;
import com.studio.batch.order.processor.NaverProductOrderReader;
import com.studio.batch.order.processor.NaverProductOrderWriter;
import com.studio.core.global.naver.dto.NaverDataDto;
import com.studio.core.order.entity.OrderEntity;
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
public class UpsertNaverProductOrderJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final NaverProductOrderReader naverProductOrderReader;
    private final NaverProductOrderProcessor naverProductOrderProcessor;
    private final NaverProductOrderWriter naverProductOrderWriter;

    @Bean
    public Job insertOrUpdateNaverProductOrderJob() {
        return new JobBuilder("insertNaverProductPurchaseOrderListJob", jobRepository)
                .start(getNaverPurchaseOrderListStep())
                .build();
    }

    @Bean
    public Step getNaverPurchaseOrderListStep() {
        return new StepBuilder("getNaverPurchaseOrderListStep", jobRepository)
                .<NaverDataDto, OrderEntity>chunk(10, transactionManager)
                .reader(naverProductOrderReader)
                .processor(naverProductOrderProcessor)
                .writer(naverProductOrderWriter)
                .allowStartIfComplete(true)
                .build();
    }
}

