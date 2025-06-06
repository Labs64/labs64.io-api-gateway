package io.labs64.apigateway.controller;

import io.labs64.audit.api.AuditEventPublisherApi;
import io.labs64.audit.model.AuditEvent;
import io.labs64.apigateway.service.MessagePublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditEventPublisherController implements AuditEventPublisherApi {

    private final MessagePublisherService messagePublisherService;

    public AuditEventPublisherController(MessagePublisherService messagePublisherService) {
        this.messagePublisherService = messagePublisherService;
    }

    @Override
    public ResponseEntity<String> publishEvent(AuditEvent event) {
        boolean res = messagePublisherService.publishMessage(event.toString());
        if (res) {
            return ResponseEntity.ok("Message sent successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }
}
