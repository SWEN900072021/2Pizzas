import { QueryClient, useQuery } from 'react-query'
import AirlineService from '../api/AirlineService'

const useAirlines = () => {
  const queryClient = new QueryClient()

  return useQuery({
    enabled: false,
    queryKey: ['airlines'],
    queryFn: () => AirlineService.getAllAirlines(),
    config: {
      onSuccess: (data) => {
        data.forEach((airline) => {
          queryClient.setQueryData(['airlines', airline.id], airline)
        })
      }
    }
  })
}

export default useAirlines
