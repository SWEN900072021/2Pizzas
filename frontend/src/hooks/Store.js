import create from 'zustand'
import produce from 'immer'

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

// HELPER FUNCTIONS

const getLocalStorage = (key) =>
  JSON.parse(window.localStorage.getItem(key))
const setLocalStorage = (key, value) =>
  window.localStorage.setItem(key, JSON.stringify(value))

// STORES

const useTestDataStore = create(
  immer(() => ({
    airports: AIRPORTS
  }))
)

const useSessionStore = create(
  immer((set) => ({
    token: getLocalStorage('token') || null,
    setToken: (token) =>
      set(() => {
        setLocalStorage('token', token)
        return { token }
      }),
    username: getLocalStorage('username') || null,
    setSessionValue: (key, value) =>
      set(() => {
        setLocalStorage(key, value)
        return { [key]: value }
      }),
    resetSession: () =>
      set(() => {
        setLocalStorage('token', null)
        setLocalStorage('username', null)

        return { token: null, username: null }
      })
  }))
)

const useFormStore = create(
  immer((set) => ({
    username: '',
    password: '',
    givenName: '',
    surname: '',
    email: '',
    setUsername: (username) => {
      set({ username })
    },
    setPassword: (password) => {
      set({ password })
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

const useStore = create(
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

    // Contains string
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

    //
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
    }
  }))
)

export { useStore, useSessionStore, useFormStore, useTestDataStore }
