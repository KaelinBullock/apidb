package com.example.apidb.shipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shipment")
@Slf4j
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping()
    public Shipment getShipment(@RequestParam Long id) {
        return shipmentService.getShipment(id);
    }

    @GetMapping("/getShipmentByLocationId")
    public Iterable<Shipment> getShipmentsByLocationId(@RequestParam Long id) {
        return shipmentService.getShipmentsByLocationId(id);
    }

    @GetMapping("/getShipmentsByContactId")
    public Iterable<Shipment> getShipmentsByContactId(@RequestParam Long id) {
        return shipmentService.getShipmentsByContactId(id);
    }

    @GetMapping("/list")
    public Iterable<Shipment> list() {
        return shipmentService.getShipments();
    }


    //TODO do not allow users to submit a id.  Create a separate function for update
    @PostMapping("/save")
    public void save(@RequestBody Shipment shipment) {
        shipmentService.save(shipment);
    }

    @PostMapping("/saveAll")
    public void save(@RequestBody List<Shipment> shipments) {
        shipmentService.save(shipments);
    }
}
