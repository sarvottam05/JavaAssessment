package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

	@Inject
	private WarehouseRepository warehouseRepository;
	@Inject
	private CreateWarehouseOperation createWarehouse;
	@Inject
	private ArchiveWarehouseOperation archiveWarehouse;
	@Inject
	private ReplaceWarehouseOperation replaceWarehouse;

	@Override
	public List<Warehouse> listAllWarehousesUnits() {
		return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
	}

	@Override
	public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse domain = toDomain(data);
		createWarehouse.create(domain);
		return toWarehouseResponse(domain);
	}

	@Override
	public Warehouse getAWarehouseUnitByID(String id) {
		var warehouse = warehouseRepository.findByBusinessUnitCode(id);
		if (warehouse == null) {
			throw new NotFoundException("WAREHOUSE_NOT_FOUND: " + id);

		}
		return toWarehouseResponse(warehouse);

	}

	@Override
	public void archiveAWarehouseUnitByID(String id) {
		var warehouse = warehouseRepository.findByBusinessUnitCode(id);
		if (warehouse == null) {
			throw new NotFoundException("WAREHOUSE_NOT_FOUND: " + id);
		}
		archiveWarehouse.archive(warehouse);

	}

	@Override
	public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
		var existing = warehouseRepository.findByBusinessUnitCode(businessUnitCode);

		if (existing == null) {
			throw new NotFoundException("WAREHOUSE_NOT_FOUND: " + businessUnitCode);
		}

		var domain = toDomain(data);
		domain.businessUnitCode = businessUnitCode;

		replaceWarehouse.replace(domain);
		return toWarehouseResponse(domain);
	}

	private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomain(Warehouse data) {
		var w = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
		w.businessUnitCode = data.getBusinessUnitCode();
		w.location = data.getLocation();
		w.capacity = data.getCapacity();
		w.stock = data.getStock();
		return w;
	}

	private Warehouse toWarehouseResponse(
			com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
		var response = new Warehouse();
		response.setBusinessUnitCode(warehouse.businessUnitCode);
		response.setLocation(warehouse.location);
		response.setCapacity(warehouse.capacity);
		response.setStock(warehouse.stock);

		return response;
	}
}
