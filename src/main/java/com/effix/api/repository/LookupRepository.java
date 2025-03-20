package com.effix.api.repository;

import com.effix.api.enums.Country;
import com.effix.api.enums.LookupType;
import com.effix.api.enums.VehicleType;
import com.effix.api.model.entity.Lookup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LookupRepository extends CrudRepository<Lookup, Long> {
    List<Lookup> findByType(LookupType type);
    Lookup findByTypeAndName(LookupType type, String name);
    Lookup findByTypeAndNameAndVehicleType(LookupType type, String name, VehicleType vehicleType);
    Lookup findByTypeAndNameAndDescriptionAndVehicleType(LookupType type, String name, String description, VehicleType vehicleType);
    Lookup findByTypeAndNameAndCountry(LookupType type, String name, Country country);
    Lookup findByTypeAndNameAndDescription(LookupType type, String name, String description);
    List<Lookup> findByTypeAndVehicleType(LookupType type, VehicleType vehicleType);
}
