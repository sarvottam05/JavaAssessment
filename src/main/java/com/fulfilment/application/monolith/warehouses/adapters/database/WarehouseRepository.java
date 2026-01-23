package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

	@Override
	public List<Warehouse> getAll() {
		return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
	}

	@Override
	public void create(Warehouse warehouse) {
		persist(DbWarehouse.fromWarehouse(warehouse));
	}

	@Override
	public void update(Warehouse warehouse) {
		DbWarehouse dbWarehouse = find("businessUnitCode", warehouse.businessUnitCode).firstResult();
		if (dbWarehouse == null) {
			throw new IllegalStateException(
					"Warehouse not found for business unit code: " + warehouse.businessUnitCode);
		}
		dbWarehouse.capacity = warehouse.capacity;
		dbWarehouse.stock = warehouse.stock;
		dbWarehouse.location = warehouse.location;
		dbWarehouse.archivedAt = warehouse.archivedAt;

	}

	@Override
	public void remove(Warehouse warehouse) {

		delete("businessUnitCode", warehouse.businessUnitCode);
	}

	@Override
	public Warehouse findByBusinessUnitCode(String buCode) {
		DbWarehouse db = find("businessUnitCode", buCode).firstResult();
		return db == null ? null : db.toWarehouse();
	}
}
