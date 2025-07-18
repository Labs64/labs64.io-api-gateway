package io.labs64.apigateway.controller.audit.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.labs64.audit.v1.api.AuditEventApi;
import io.labs64.audit.v1.model.AuditEvent;
import io.labs64.apigateway.service.MessagePublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class AuditEventController implements AuditEventApi {

    private static final Logger logger = LoggerFactory.getLogger(AuditEventController.class);

    private final MessagePublisherService messagePublisherService;
    private final ObjectMapper objectMapper;

    public AuditEventController(MessagePublisherService messagePublisherService, ObjectMapper objectMapper) {
        this.messagePublisherService = messagePublisherService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<String> publishEvent(AuditEvent event) {
        String eventJson;
        try {
            eventJson = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert AuditEvent to JSON! Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to convert AuditEvent to JSON. Error: " + e.getMessage());
        }

        boolean res = messagePublisherService.publishMessage(eventJson);
        if (res) {
            return ResponseEntity.ok("Message sent successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }

}
