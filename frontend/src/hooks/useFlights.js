import { useQuery, useQueryClient } from 'react-query'
import FlightSearchService from '../api/FlightSearchService'

const useFlights = ({ destination, origin, departDate, airline }) => {
  const queryClient = useQueryClient()

  return useQuery({
    enabled: false,
    queryKey: ['flights'],
    queryFn: () =>
      FlightSearchService.searchFlights({
        destination,
        origin,
        departingAfter: departDate.startOf('day').format(),
        departingBefore: departDate.endOf('day').format(),
        airline
      }),
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
                departure: flight.departure,
                arrival: flight.arrival,
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
