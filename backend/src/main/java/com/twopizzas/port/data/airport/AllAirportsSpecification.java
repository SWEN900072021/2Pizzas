package com.twopizzas.port.data.airport;

public class AllAirportsSpecification extends AirportSpecification {

    private static final String template =
            "SELECT * FROM airport;";

    private String location;

    protected AllAirportsSpecification(AirportMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    protected String getTemplate() {
        return template;
    }

    @Override
    protected Object[] getTemplateValues() {
        return new Object[]{
                location,
        };
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
