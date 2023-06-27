package com.example.apidb.shipment;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class ShipmentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveShipment(Shipment shipment) {
        Long contactId = shipment.getContact() != null ? shipment.getContact().getId() : null;

        entityManager.createNativeQuery("INSERT INTO shipments (id, name, creation_date, delivery_date, " +
                        "contact_id) VALUES (?,?,?,?,?)")
                .setParameter(1, findLastEntry())
                .setParameter(2, shipment.getName())
                .setParameter(3, shipment.getCreationDate())
                .setParameter(4, shipment.getDeliveryDate())
                .setParameter(5, contactId)
                .executeUpdate();
    }

    @Transactional
    public void saveAll(List<Shipment> shipments) {
        for (Shipment s : shipments) {
            saveShipment(s);
        }
    }

    Long findLastEntry() {
        Iterable<BigInteger> shipmentIds = entityManager.createNativeQuery("SELECT id FROM SHIPMENTS ORDER BY id DESC")
                .getResultList();
        BigInteger id = shipmentIds.iterator().hasNext() ? shipmentIds.iterator().next() : BigInteger.ZERO;
        id = id.add(BigInteger.ONE);
        return  id.longValue();
    }
    Iterable<Shipment> findAll() {
        return entityManager.createNativeQuery("SELECT *" +
                        "  FROM SHIPMENTS", Shipment.class).getResultList();
    }

    public Optional find(Long id) {
        return entityManager.createNativeQuery("SELECT * \n" +
                "FROM SHIPMENTS s " +
                "WHERE s.id = (?1)" +
                "LIMIT 1", Shipment.class)
                .setParameter(1, id).getResultList().stream().findFirst();
    }

    Iterable<Shipment> findShipmentsByLocationId(Long id) {
        return entityManager.createNativeQuery("SELECT *\n" +
                        "FROM Shipments s\n" +
                        "JOIN Contacts c ON c.id = s.contact_id\n" +
                        "JOIN Companies com ON com.id = c.company_id\n" +
                        "JOIN Locations l ON l.id = com.location_id\n" +
                        "WHERE l.id = (?1)", Shipment.class)
                .setParameter(1, id).getResultList();
    }

    Iterable<Shipment> findShipmentsByContactId(Long id) {
        return entityManager.createNativeQuery("SELECT * \n" +
                        "FROM SHIPMENTS s " +
                        "WHERE s.contact_id = (?1)", Shipment.class)
                .setParameter(1, id).getResultList();
    }
}