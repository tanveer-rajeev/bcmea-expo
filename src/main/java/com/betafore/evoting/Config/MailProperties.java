package com.betafore.evoting.Config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties(prefix = "application.mail")
@Validated
public class MailProperties {

    @NotBlank
    private String host;

    @Min(1)
    private int port;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private final Properties properties = new Properties();

    @Data
    public static class Properties {
        private Mail mail = new Mail();

        @Data
        public static class Mail {
            private Smtp smtp = new Smtp();
            private Transport transport = new Transport();
            private boolean debug;

            @Data
            public static class Smtp {
                private boolean auth;
                private Ssl ssl = new Ssl();

                @Data
                public static class Ssl {
                    private boolean enable;
                }
            }

            @Data
            public static class Transport {
                private String protocol;
            }
        }
    }
}
