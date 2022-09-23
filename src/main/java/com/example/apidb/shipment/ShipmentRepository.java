package com.example.apidb.shipment;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

//public interface ShipmentRepository extends CrudRepository<Shipment, Long> {
//}
//two options
//when updating the shipment pull back the location and update that as well
//the other option is to fix the query.  To do that we need to map the query to a dto

public class ShipmentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Shipment shipment) {
        Long contactId = shipment.getContact() != null ? shipment.getContact().getId() : null;
        BigInteger locationId = shipment.getLocation() != null ? BigInteger.valueOf(shipment.getLocation().getId()) : null;
        entityManager.createNativeQuery("INSERT INTO shipments (id, creation_date, delivery_date, contact_id, " +
                        "location_id) VALUES (?,?,?,?, ?)")
                .setParameter(1, findLastEntry())
                .setParameter(2, shipment.getCreationdate())
                .setParameter(3, shipment.getDeliverydate())
                .setParameter(4, contactId)
                .setParameter(5, locationId)
                .executeUpdate();
    }

    @Transactional
    public void saveAll(List<Shipment> shipments) {
        for (Shipment s : shipments) {
            save(s);
        }
    }

    Long findLastEntry() {//limit results number
        Iterable<BigInteger> shipmentIds = entityManager.createNativeQuery("SELECT id FROM SHIPMENTS ORDER BY id DESC")
                .getResultList();
        BigInteger id = shipmentIds.iterator().hasNext() ? shipmentIds.iterator().next() : BigInteger.ZERO;
        id = id.add(BigInteger.ONE);
        return  id.longValue();
    }
    Iterable<Shipment> findAll() {
        return entityManager.createNativeQuery("SELECT s.*\n" +
                "  FROM SHIPMENTS s," +
                " LOCATIONS l\n" +
                "  WHERE s.location_id = l.id", Shipment.class).getResultList();
    }

    public Optional find(Long id) {
        return entityManager.createNativeQuery("SELECT *)\n" +
                "  FROM SHIPMENTS s" +
                "WHERE s.id = (?1)" +
                "LIMIT 1")
                .setParameter(1, id).getResultList().stream().findFirst();
    }

    Iterable<Shipment> findShipmentByLocationId(Long id) {
        return entityManager.createNativeQuery("SELECT *)\n" +
                        "  FROM SHIPMENTS s," +
                        " LOCATIONS l" +
                        "WHERE l.id = (?1)" +
                        "LIMIT 1")
                .setParameter(1, id).getResultList();
    }

    Iterable<Shipment> findShipmentByContactId(Long id) {
        return entityManager.createNativeQuery("SELECT *)\n" +
                        "  FROM SHIPMENTS s," +
                        " LOCATIONS l," +
                        "CONTACTS c" +
                        "WHERE c.id = (?1)" +
                        "LIMIT 1")
                .setParameter(1, id).getResultList();
    }
}