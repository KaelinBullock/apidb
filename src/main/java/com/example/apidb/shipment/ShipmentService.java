package com.example.apidb.shipment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;


    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Iterable<Shipment> getShipments() {
        return shipmentRepository.findAll();
//        Shipment shipment = new Shipment();
//        shipment.setLocation(new Location());
//        return Collections.singleton(shipment);
    }

    public Optional getShipment(Long id) {
        return shipmentRepository.find(id);
//        Shipment shipment = new Shipment();
//        shipment.setLocation(new Location());
//        return Collections.singleton(shipment);
    }

//    public Optional<Shipment> getShipment(Long id) {
//        return shipmentRepository.findById(id);
//    }

    public void save(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    public void save(List<Shipment> shipments) {
        shipmentRepository.saveAll(shipments);
    }
}
