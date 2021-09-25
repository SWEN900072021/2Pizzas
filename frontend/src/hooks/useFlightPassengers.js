import { useQuery, useQueryClient } from 'react-query'
import { FlightService } from '../api'

const useFlightPassengers = (token, flightId) => {
  const queryClient = useQueryClient()
  return useQuery({
    // enabled: false,
    queryKey: ['passengers'],
    queryFn: () => FlightService.getFlightPassengers(token, flightId),
    config: {
      onSuccess: (data) => {
        data.forEach((passenger) => {
          queryClient.setQueryDate(['passengers'], {
            flightId,
            bookingId: passenger.bookingId
          })
        })
      }
    }
  })
}

export default useFlightPassengers
