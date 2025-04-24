package com.studio.batch.product.job;

import com.studio.batch.product.processor.NaverProductProcessor;
import com.studio.batch.product.processor.NaverProductReader;
import com.studio.batch.product.processor.NaverProductWriter;
import com.studio.core.global.naver.dto.NaverChannelProductsDto;
import com.studio.core.product.entity.ProductEntity;
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
public class UpdateNaverProductJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final NaverProductReader naverProductReader;
    private final NaverProductProcessor naverProductProcessor;
    private final NaverProductWriter naverProductWriter;

    @Bean
    public Job updateNaverProductsJob() {
        return new JobBuilder("updateNaverProductJob", jobRepository)
                .start(getNaverProductListStep())
                .build();
    }

    @Bean
    public Step getNaverProductListStep() {
        return new StepBuilder("getNaverProductListStep", jobRepository)
                .<NaverChannelProductsDto, ProductEntity>chunk(10, transactionManager)
                .reader(naverProductReader)
                .processor(naverProductProcessor)
                .writer(naverProductWriter)
                .allowStartIfComplete(true)
                .build();
    }
}

