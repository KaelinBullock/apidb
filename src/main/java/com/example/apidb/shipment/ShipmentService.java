package com.example.apidb.shipment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;


    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Iterable<Shipment> getShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipment(Long id) {
        return (Shipment) shipmentRepository.find(id).orElse(null);
    }

    public Iterable<Shipment> getShipmentsByLocationId(Long id) {
        return shipmentRepository.findShipmentsByLocationId(id);
    }

    public Iterable<Shipment> getShipmentsByContactId(Long id) {
        return shipmentRepository.findShipmentsByContactId(id);
    }
    public void save(Shipment shipment) {
        shipmentRepository.saveShipment(shipment);
    }

    public void save(List<Shipment> shipments) {
        shipmentRepository.saveAll(shipments);
    }
}
