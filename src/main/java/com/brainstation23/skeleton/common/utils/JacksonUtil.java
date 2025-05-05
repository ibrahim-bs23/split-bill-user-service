package com.brainstation23.skeleton.common.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JacksonUtil {

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);

        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        module.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        module.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        module.addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);
        module.addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);

        objectMapper.registerModule(module);
        return objectMapper;
    }

    public static <T> T jsonToInstance(String data, Class<T> clazz) {
        try {
            return objectMapper().readValue(data, clazz);
        } catch (Exception ex) {
            log.warn("JacksonUtil.jsonToInstance: {}", ex.getMessage());
        }
        return null;
    }

    public static String objectToJson(Object object) {
        try {
            return objectMapper().writeValueAsString(object);
        } catch (Exception ex) {
            log.warn("JacksonUtil.objectToJson: {}", ex.getMessage());
        }
        return null;
    }

    public static Map<String, Object> convert(Object object) {
        try {
            return objectMapper().convertValue(object, new TypeReference<>() {
            });
        } catch (Exception ex) {
            log.warn("JacksonUtil.convert: {}", ex.getMessage());
        }
        return null;
    }

    class DateTimeJsonSerializer<T> extends JsonSerializer<T> {
        private static final ZoneId ASIA_TIMEZONE = ZoneId.of("Asia/Dhaka");
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DateTimeUtils.TRANSACTION_DATE_FORMAT);

        @Override
        public void serialize(T dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (dateTime != null) {
                String formattedDate = null;
                if (dateTime instanceof ZonedDateTime date) {
                    formattedDate = FORMATTER.format(date.withZoneSameInstant(ASIA_TIMEZONE).toLocalDateTime());
                } else if (dateTime instanceof LocalDate date) {
                    formattedDate = FORMATTER.format(date.atStartOfDay(ASIA_TIMEZONE));
                } else if (dateTime instanceof LocalDateTime date) {
                    formattedDate = FORMATTER.format(date.atZone(ZoneOffset.UTC).withZoneSameInstant(ASIA_TIMEZONE).toLocalDateTime());
                } else if (dateTime instanceof OffsetDateTime date) {
                    formattedDate = FORMATTER.format(date.atZoneSameInstant(ASIA_TIMEZONE).toLocalDateTime());
                } else if (dateTime instanceof Instant date) {
                    formattedDate = FORMATTER.format(date.atZone(ASIA_TIMEZONE).toLocalDateTime());
                } else if (dateTime instanceof Date date) {
                    formattedDate = FORMATTER.format(date.toInstant().atZone(ASIA_TIMEZONE).toLocalDateTime());
                }
                jsonGenerator.writeString(formattedDate);
            }
        }
    }

}
