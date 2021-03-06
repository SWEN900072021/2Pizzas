import create from 'zustand'
import { persist } from 'zustand/middleware'
import produce from 'immer'
import moment from 'moment'

// Copied from zustand GitHub docs to easily change nested state
const immer = (config) => (set, get, api) =>
  config(
    (partial, replace) => {
      const nextState =
        typeof partial === 'function' ? produce(partial) : partial
      return set(nextState, replace)
    },
    get,
    api
  )

// CONSTANTS

const ECONOMY = 'economy'
const BUSINESS = 'business'
const FIRST = 'first'
const AIRPORTS = [
  {
    code: 'LHR',
    name: 'London Heathrow',
    location: 'London'
  },
  {
    code: 'CDG',
    name: 'Charles De Gaulle',
    location: 'Paris'
  },
  {
    code: 'ORY',
    name: 'Paris Orly',
    location: 'Paris'
  },
  {
    code: 'JFK',
    name: 'John F Kennedy',
    location: 'New York'
  },
  {
    code: 'CGK',
    name: 'Soekarno-Hatta',
    location: 'Jakarta'
  },
  {
    code: 'SIN',
    name: 'Singapore Changi',
    location: 'Singapore'
  },
  {
    code: 'BKK',
    name: 'Suvarnabhumi',
    location: 'Bangkok'
  },
  {
    code: 'HKG',
    name: 'Hong Kong',
    location: 'Hong Kong'
  },
  {
    code: 'MNL',
    name: 'Minato',
    location: 'Tokyo'
  },
  {
    code: 'ICN',
    name: 'Incheon',
    location: 'Seoul'
  },
  {
    code: 'PUS',
    name: 'Busan',
    location: 'Busan'
  },
  {
    code: 'DPS',
    name: 'Denpasar',
    location: 'Bali'
  },
  {
    code: 'HKT',
    name: 'Phuket',
    location: 'Phuket'
  },
  {
    code: 'KUL',
    name: 'Kuala Lumpur',
    location: 'Kuala Lumpur'
  },
  {
    code: 'SGN',
    name: 'Tan Son Nhat',
    location: 'Ho Chi Minh'
  },
  {
    code: 'TPE',
    name: 'Taipei',
    location: 'Taipei'
  },
  {
    code: 'HND',
    name: 'Haneda',
    location: 'Tokyo'
  }
]

const RETURN_FLIGHTS = [
  {
    id: 5,
    airlineName: 'QANTAS',
    airlineCode: 'QA',
    origin: 'Townsville',
    destination: 'Melbourne',
    departure: '2020/10/10 21:00',
    arrival: '2020/10/11 00:00',
    stopovers: ['Perth', 'Darwin'],
    firstClassCost: 1000,
    businessClassCost: 500,
    economyClassCost: 100
  },
  {
    id: 6,
    airlineName: 'QANTAS',
    airlineCode: 'QA',
    origin: 'Sydney',
    destination: 'Melbourne',
    departure: '2020/10/10 21:00',
    arrival: '2020/10/11 00:00',
    stopovers: ['Perth', 'Darwin'],
    firstClassCost: 1000,
    businessClassCost: 500,
    economyClassCost: 100
  },
  {
    id: 7,
    airlineName: 'QANTAS',
    airlineCode: 'QA',
    origin: 'Sydney',
    destination: 'Melbourne',
    departure: '2020/10/10 21:00',
    arrival: '2020/10/11 00:00',
    stopovers: ['Perth', 'Darwin'],
    firstClassCost: 1000,
    businessClassCost: 500,
    economyClassCost: 100
  }
]

const ONE_WAY_FLIGHTS = [
  {
    id: 1,
    airlineName: 'QANTAS',
    airlineCode: 'QA',
    origin: 'Melbourne',
    destination: 'Sydney',
    departure: '2020/10/10 09:00',
    arrival: '2020/10/10 15:00',
    stopovers: ['Perth', 'Darwin'],
    firstClassCost: 1000,
    businessClassCost: 500,
    economyClassCost: 100
  },
  {
    id: 2,
    airlineName: 'QANTAS',
    airlineCode: 'QA',
    origin: 'Melbourne',
    destination: 'Sydney',
    departure: '2020/10/10 09:00',
    arrival: '2020/10/10 15:00',
    stopovers: ['Perth', 'Darwin', 'Brisbane'],
    firstClassCost: 1000,
    businessClassCost: 500,
    economyClassCost: 100
  },
  {
    id: 3,
    airlineName: 'QANTAS',
    airlineCode: 'QA',
    origin: 'Melbourne',
    destination: 'Sydney',
    departure: '2020/10/10 09:00',
    arrival: '2020/10/10 15:00',
    stopovers: ['Perth', 'Darwin'],
    firstClassCost: 1000,
    businessClassCost: 500,
    economyClassCost: 100
  },
  {
    id: 4,
    airlineName: 'QANTAS',
    airlineCode: 'QA',
    origin: 'Melbourne',
    destination: 'Sydney',
    departure: '2020/10/10 09:00',
    arrival: '2020/10/10 15:00',
    stopovers: ['Perth', 'Darwin'],
    firstClassCost: 1000,
    businessClassCost: 500,
    economyClassCost: 100
  }
]

// STORES

const useTestDataStore = create(
  immer(() => ({
    airports: AIRPORTS,
    returnFlights: RETURN_FLIGHTS,
    oneWayFlights: ONE_WAY_FLIGHTS
  }))
)

const useSessionStore = create(
  persist(
    immer((set) => ({
      token: null,
      setToken: (token) => set({ token }),
      user: null,
      setUser: (user) => set({ user }),
      resetSession: () =>
        set((state) => {
          state.setToken(null)
          state.setUser(null)
          return { token: null, user: null }
        })
    })),
    {
      name: 'session-store',
      getStorage: () => localStorage
    }
  )
)

const useBookingStore = create(
  persist(
    immer((set) => ({
      bookingId: null,
      setBookingId: (bookingId) => set({ bookingId }),
      isCreatingBooking: false,
      selectedOutboundFlight: null,
      selectedReturnFlight: null,
      setCreatingBooking: (isCreatingBooking) =>
        set({ isCreatingBooking }),
      setSelectedOutboundFlight: (selectedOutboundFlight) =>
        set({ selectedOutboundFlight }),
      setSelectedReturnFlight: (selectedReturnFlight) =>
        set({ selectedReturnFlight })
    })),
    {
      name: 'booking-store',
      getStorage: () => localStorage
    }
  )
)

const useFormStore = create(
  immer((set) => ({
    username: '',
    givenName: '',
    surname: '',
    email: '',
    setUsername: (username) => {
      set({ username })
    },
    setGivenName: (givenName) => {
      set({ givenName })
    },
    setSurname: (surname) => {
      set({ surname })
    },
    setEmail: (email) => {
      set({ email })
    }
  }))
)

const useFlightStore = create(
  persist(
    immer((set, get) => ({
      // Contains objects for origin and destination airports
      originAirport: {},
      destinationAirport: {},
      setOriginAirport: (airport) => {
        set({ originAirport: { ...airport } })
      },
      setDestinationAirport: (airport) => {
        set({ destinationAirport: { ...airport } })
      },
      // Contains strings for the value of the origin and destination airport input fields
      originAirportSearchValue: '',
      destinationAirportSearchValue: '',
      setOriginAirportSearchValue: (value) => {
        set({ originAirportSearchValue: value })
      },
      setDestinationAirportSearchValue: (value) => {
        set({ destinationAirportSearchValue: value })
      },

      isReturn: true,
      setReturn: (value) => {
        set({ isReturn: value })
      },

      // Contains string for cabin class type
      economyClass: ECONOMY,
      businessClass: BUSINESS,
      firstClass: FIRST,
      cabinClass: ECONOMY,
      setEconomyClass: () => {
        set({ cabinClass: ECONOMY })
      },
      setBusinessClass: () => {
        set({ cabinClass: BUSINESS })
      },
      setFirstClass: () => {
        set({ cabinClass: FIRST })
      },

      // Contains number of passenger
      passengerCount: 1,
      addPassenger: () => {
        set((state) => ({
          passengerCount: state.passengerCount + 1
        }))
      },
      removePassenger: () => {
        if (get().passengerCount > 1) {
          set((state) => ({
            passengerCount: state.passengerCount - 1
          }))
        }
      },
      departDate: moment(),
      returnDate: moment(),
      setDepartDate: (date) => {
        set(() => ({
          departDate: date
        }))
      },
      setReturnDate: (date) => {
        set(() => ({
          returnDate: date
        }))
      },
      outboundFlights: [],
      returnFlights: [],
      setOutboundFlights: (flights) => {
        set({ outboundFlights: flights })
      },
      setReturnFlights: (flights) => {
        set({ returnFlights: flights })
      },

      resetFlightSearch: () => {
        set({
          originAirport: {},
          destinationAirport: {},
          originAirportSearchValue: '',
          destinationAirportSearchValue: '',
          isReturn: true,
          passengerCount: 1,
          departDate: moment(),
          returnDate: moment(),
          outboundFlights: [],
          returnFlights: []
        })
      }
    })),
    {
      name: 'flight-store',
      getStorage: () => localStorage
    }
  )
)

export {
  useFlightStore,
  useSessionStore,
  useFormStore,
  useTestDataStore,
  useBookingStore
}
