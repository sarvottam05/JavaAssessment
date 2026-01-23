package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class CreateWarehouseUseCaseTest {
	
	@Inject
	  CreateWarehouseUseCase createWarehouseUseCase;

	  @Test
	  void shouldCreateWarehouseSuccessfully() {
	    Warehouse w = new Warehouse();
	    w.businessUnitCode = "WH-TEST-1";
	    w.location = "Bangalore";
	    w.capacity = 0;
	    w.stock = 0 ;

	    assertDoesNotThrow(() -> createWarehouseUseCase.create(w));
	  }
	
	@Test
	  void shouldFailWhenStockExceedsCapacity() {
	    Warehouse w = new Warehouse();
	    w.businessUnitCode = "WH-TEST-2";
	    w.location = "Bangalore";
	    w.capacity = 50;
	    w.stock = 100;

	    assertThrows(IllegalArgumentException.class,
	        () -> createWarehouseUseCase.create(w));
	}
}
