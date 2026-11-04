
package com.cia2.azure_cicd;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class EquipmentController {

    public static class Equipment {
        private int id;
        private String name;
        private int quantity;
        private int available;

        public Equipment(int id, String name, int quantity) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.available = quantity;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getAvailable() {
            return available;
        }

        public void book() {
            available--;
        }

        public void release() {
            available++;
        }
    }

    private final List<Equipment> equipmentList = new ArrayList<>(
        List.of(
            new Equipment(1, "Laptop", 10),
            new Equipment(2, "Projector", 4),
            new Equipment(3, "Tablet", 8)
        )
    );

    // Redirect homepage to frontend
    @GetMapping("/")
    public ResponseEntity<Void> home() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/index.html"))
                .build();
    }

    // Get all equipment
    @GetMapping("/api/equipment")
    public List<Equipment> getEquipment() {
        return equipmentList;
    }

    // Book equipment
    @PostMapping("/api/equipment/{id}/book")
    public synchronized Map<String, String> bookEquipment(
            @PathVariable int id) {

        for (Equipment item : equipmentList) {
            if (item.getId() == id) {
                if (item.getAvailable() > 0) {
                    item.book();
                    return Map.of(
                        "message",
                        item.getName() + " booked successfully!"
                    );
                }

                return Map.of(
                    "message",
                    "Equipment is not available."
                );
            }
        }

        return Map.of("message", "Equipment not found.");
    }

    // Release equipment
    @PostMapping("/api/equipment/{id}/release")
    public synchronized Map<String, String> releaseEquipment(
            @PathVariable int id) {

        for (Equipment item : equipmentList) {
            if (item.getId() == id) {
                if (item.getAvailable() < item.getQuantity()) {
                    item.release();
                    return Map.of(
                        "message",
                        item.getName() + " released successfully!"
                    );
                }

                return Map.of(
                    "message",
                    "No active booking to release."
                );
            }
        }

        return Map.of("message", "Equipment not found.");
    }

    // Application version
    @GetMapping("/api/version")
    public Map<String, String> getVersion() {
        return Map.of(
            "version", "2.0",
            "deployment", "CI/CD Redeployment Demo"
        );
    }
}
