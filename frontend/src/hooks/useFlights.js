import { useQuery, useQueryClient } from 'react-query'
import { FlightService } from '../api'

const useFlights = (token) => {
  const queryClient = useQueryClient()

  return useQuery({
    // enabled: false,
    queryKey: ['flights'],
    queryFn: () => FlightService.getAllFlights(token),
    config: {
      onSuccess: (data) => {
        data.forEach((flight) => {
          queryClient.setQueryData(
            [
              'flights',
              {
                id: flight.id,
                origin: flight.origin,
                destination: flight.destination,
                departure: flight.departureLocal,
                arrival: flight.arrivalLocal,
                airline: flight.airline
              }
            ],
            flight
          )
        })
      }
    }
  })
}

export default useFlights
