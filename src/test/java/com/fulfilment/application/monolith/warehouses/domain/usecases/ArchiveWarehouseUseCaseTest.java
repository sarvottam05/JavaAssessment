package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ArchiveWarehouseUseCaseTest {
	
	@Inject
	  ArchiveWarehouseUseCase archiveWarehouseUseCase;

	  @Test
	  void shouldArchiveWarehouse() {
	    Warehouse w = new Warehouse();
	    w.businessUnitCode = "WH-ARCH-1";
	    w.capacity = 100;
	    w.stock = 20;

	    archiveWarehouseUseCase.archive(w);
	    assertNotNull(w.archivedAt);
	  }
}
