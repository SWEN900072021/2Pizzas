import { useQuery, useQueryClient } from 'react-query'
import AirportService from '../api/AirportService'

const useAirports = () => {
  const queryClient = useQueryClient()

  return useQuery({
    staleTime: 3 * 60 * 1000,
    queryKey: ['airports'],
    queryFn: () => AirportService.getAllAirports(),
    config: {
      onSuccess: (data) => {
        data.forEach((airport) => {
          queryClient.setQueryData(
            [
              'airports',
              {
                id: airport.id,
                code: airport.code,
                name: airport.name,
                location: airport.location
              }
            ],
            airport
          )
        })
      }
    }
  })
}

export default useAirports
