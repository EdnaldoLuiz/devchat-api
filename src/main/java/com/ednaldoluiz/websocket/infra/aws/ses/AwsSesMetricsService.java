package com.ednaldoluiz.websocket.infra.aws.ses;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AwsSesMetricsService {

    CloudWatchClient cloudWatchClient;

    public void publishEmailMetric(String metricName, double value) {
        PutMetricDataRequest request = PutMetricDataRequest.builder()
                .namespace("SES/Metrics")
                .metricData(
                        MetricDatum.builder()
                                .metricName(metricName)
                                .value(value)
                                .unit(StandardUnit.COUNT)
                                .build()
                )
                .build();

        cloudWatchClient.putMetricData(request);
        log.info("Métrica enviada para CloudWatch: {} -> {}", metricName, value);
    }
}