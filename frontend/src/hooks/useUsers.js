import { useQuery, useQueryClient } from 'react-query'
import UserService from '../api/UserService'

const useUsers = (token) => {
  const queryClient = useQueryClient()

  return useQuery({
    // enabled: false,
    queryKey: ['users'],
    queryFn: () => UserService.getAllUsers(token),
    config: {
      onSuccess: (data) => {
        data.forEach((user) => {
          queryClient.setQueryData(
            [
              'users',
              {
                id: user.id,
                type: user.type,
                username: user.returnFlight
              }
            ],
            user
          )
        })
      }
    }
  })
}

export default useUsers
