
package com.cia2.azure_cicd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EquipmentController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
            "application", "Azure CI/CD Equipment Management",
            "message", "Application is running successfully!",
            "version", "1.0"
        );
    }

    @GetMapping("/api/equipment")
    public List<Map<String, Object>> getEquipment() {
        return List.of(
            Map.of(
                "id", 1,
                "name", "Laptop",
                "quantity", 10,
                "available", true
            ),
            Map.of(
                "id", 2,
                "name", "Projector",
                "quantity", 4,
                "available", true
            ),
            Map.of(
                "id", 3,
                "name", "Tablet",
                "quantity", 8,
                "available", false
            )
        );
    }

    @GetMapping("/api/version")
    public Map<String, String> getVersion() {
        return Map.of(
            "version", "2.0",
            "deployment", "CI/CD Redeployment Demo"
        );
    }
}
