import { useQuery, useQueryClient } from 'react-query'
import { AirplaneProfileService } from '../api'

const useAirplaneProfiles = () => {
  const queryClient = useQueryClient()

  return useQuery({
    // enabled: false,
    queryKey: ['airplaneProfiles'],
    queryFn: () => AirplaneProfileService.getAllAirplaneProfiles(),
    config: {
      onSuccess: (data) => {
        data.forEach((airplaneProfile) => {
          queryClient.setQueryData(
            ['airplaneProfile', airplaneProfile.id],
            airplaneProfile
          )
        })
      }
    }
  })
}

export default useAirplaneProfiles
