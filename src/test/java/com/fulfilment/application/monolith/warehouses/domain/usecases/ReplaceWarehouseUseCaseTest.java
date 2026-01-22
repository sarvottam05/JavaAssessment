package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ReplaceWarehouseUseCaseTest {
	
	 @Inject
	  ReplaceWarehouseUseCase replaceWarehouseUseCase;

	  @Test
	  void shouldFailWhenCapacityLessThanStock() {
	    Warehouse w = new Warehouse();
	    w.businessUnitCode = "WH-REP-1";
	    w.capacity = 10;
	    w.stock = 50;

	    assertThrows(IllegalArgumentException.class,
	        () -> replaceWarehouseUseCase.replace(w));
	  }	
}
