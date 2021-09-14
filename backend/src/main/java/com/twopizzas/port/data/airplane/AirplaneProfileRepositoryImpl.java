package com.twopizzas.port.data.airplane;

import com.twopizzas.port.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.domain.flight.AirplaneProfileRepository;

import java.util.List;

@Component
public class AirplaneProfileRepositoryImpl extends AbstractRepository<AirplaneProfile, AirplaneProfileSpecification, AirplaneProfileMapper> implements AirplaneProfileRepository {

    @Autowired
    public AirplaneProfileRepositoryImpl(AirplaneProfileMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<AirplaneProfile> findAllAirplanes() {
        return dataMapper.readAll(new AllAirplaneProfilesSpecification(dataMapper));
    }
}
