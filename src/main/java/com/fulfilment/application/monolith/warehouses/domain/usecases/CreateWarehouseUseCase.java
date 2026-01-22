package com.fulfilment.application.monolith.warehouses.domain.usecases;

import java.time.Instant;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore,
		  LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
		// Business Unit Code must be unique
		if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
			throw new IllegalArgumentException("Warehouse with business unit code already exists");
		}
		// Location must be valid
		Location location = locationResolver.resolveByIdentifier(warehouse.location);
		if (location == null) {
			throw new IllegalArgumentException("Invalid warehouse location");
		}
		// Capacity must not exceed location limit
		if (warehouse.capacity > location.maxCapacity) {
			throw new IllegalArgumentException("Warehouse capacity exceeds location maximum");
		}
		// Stock must fit into capacity
		if (warehouse.stock > warehouse.capacity) {
			throw new IllegalArgumentException("Warehouse stock exceeds warehouse capacity");
		}

		// if all went well, create the warehouse
		warehouseStore.create(warehouse);
  }
}
