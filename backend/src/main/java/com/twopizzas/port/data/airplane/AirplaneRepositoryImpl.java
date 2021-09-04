package com.twopizzas.port.data.airplane;

import com.twopizzas.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.AirplaneProfile;
import com.twopizzas.domain.AirplaneProfileRepository;
import com.twopizzas.domain.EntityId;

import java.util.List;

@Component
public class AirplaneRepositoryImpl extends AbstractRepository<Airplane, EntityId, AirplaneSpecification, AirplaneMapper> implements AirplaneRepository {

    @Autowired
    public AirplaneRepositoryImpl(AirplaneMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Airplane> findAllAirplanes() {
        return dataMapper.readAll(new AllAirplanesSpecification());
    }
}
