package com.example.apidb.shipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shipment")
@Slf4j
public class ShipmentController {

    private ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/shipment")
    public Optional getShipement(@RequestParam Long id) {
        return shipmentService.getShipment(id);
    }

    @GetMapping("/list")
    public Iterable<Shipment> list() {
        return shipmentService.getShipments();
    }

    @PostMapping("/saveAll")
    public void save(@RequestBody List<Shipment> shipments) {
        shipmentService.save(shipments);
    }

    @PostMapping("/save")
    public void save(@RequestBody Shipment shipment) {
        shipmentService.save(shipment);
    }
}
