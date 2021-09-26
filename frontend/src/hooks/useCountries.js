import axios from 'axios'
import moment from 'moment-timezone'
import { useQuery } from 'react-query'

const useCountries = () =>
  useQuery({
    queryKey: ['countries'],
    queryFn: () => {
      const countriesIso = moment.tz.countries()
      const countries = []

      // console.log('Countries ISO:', countriesIso)

      countriesIso.forEach(async (countryIso) => {
        try {
          const { data } = await axios.get(
            `http://api.worldbank.org/v2/country/${countryIso}?format=json`
          )
          countries.push(data[1][0].name)
        } catch (error) {
          // console.log(error)
        }
      })

      // console.log('Countries fetched:', countries)
      return countries
    }
  })

export default useCountries
