package com.fulfilment.application.monolith.warehouses.domain.usecases;

import java.time.LocalDateTime;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

	private final WarehouseStore warehouseStore;

	public ReplaceWarehouseUseCase(WarehouseStore warehouseStore) {
		this.warehouseStore = warehouseStore;
	}

	@Override
	public void replace(Warehouse newWarehouse) {

		if (newWarehouse == null) {
			throw new IllegalArgumentException("New warehouse must not be null");
		}
		if (newWarehouse.businessUnitCode == null) {
			throw new IllegalArgumentException("Business Unit code must be provided");
		}
		// Find existing warehouse
		Warehouse existing = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);
		if (existing == null) {
			throw new IllegalArgumentException("Warehouse to replace does not exist");
		}

		if (existing.archivedAt != null) {
			throw new IllegalStateException("Cannot replace an archived warehouse");
		}
		// Validate capacity
		if (newWarehouse.capacity < existing.stock) {
			throw new IllegalArgumentException("New warehouse capacity cannot hold existing stock");
		}

		// Archive old warehouse
		existing.archivedAt = LocalDateTime.now();
		warehouseStore.update(existing);

		// Prepare replacement warehouse
		newWarehouse.createdAt = LocalDateTime.now();
		newWarehouse.archivedAt = null;

		warehouseStore.update(newWarehouse);
	}
}
