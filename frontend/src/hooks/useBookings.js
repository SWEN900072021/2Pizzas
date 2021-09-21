import { useQuery, useQueryClient } from 'react-query'
import BookingService from '../api/BookingService'

const useBookings = (token) => {
  const queryClient = useQueryClient()

  return useQuery({
    queryKey: ['bookings'],
    queryFn: () => BookingService.getCustomerBookings(token),
    config: {
      onSuccess: (data) => {
        data.forEach((booking) => {
          queryClient.setQueryData(
            [
              'bookings',
              {
                id: booking.id,
                flight: booking.flight,
                returnFlight: booking.returnFlight,
                customer: booking.customer
              }
            ],
            booking
          )
        })
      }
    }
  })
}

export default useBookings
