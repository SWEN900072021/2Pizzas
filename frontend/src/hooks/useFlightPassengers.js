import { useQuery } from 'react-query'
import { FlightService } from '../api'

const useFlightPassengers = (token, flightId) =>
  useQuery({
    enabled: false,
    queryKey: ['passengers', flightId],
    queryFn: FlightService.getFlightPassengers(token, flightId)
  })

export default useFlightPassengers
