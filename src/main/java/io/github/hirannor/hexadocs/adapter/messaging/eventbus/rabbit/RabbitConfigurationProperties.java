package io.github.hirannor.hexadocs.adapter.messaging.eventbus.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "messaging.rabbit")
public class RabbitConfigurationProperties {
    private String exchange;
    private String queue;
    private Retry retry;

    public RabbitConfigurationProperties() {
        this.retry = new Retry();
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(final String queue) {
        this.queue = queue;
    }

    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    public static class Retry {
        private int maxAttempts;

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }
    }
}