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

// STORES

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

export { useStore, useFormStore }
