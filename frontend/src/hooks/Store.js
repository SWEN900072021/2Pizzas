import create from 'zustand'
import produce from 'immer'

// Copied from zustand docs to easily change nested state
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

const useStore = create(
  immer((set) => ({
    // immer((set, get) => ({
    originAirport: {},
    destinationAirport: {},
    setOriginAirport: (airport) => {
      set({ originAirport: { ...airport } })
      // console.log('updated origin to:', get().originAirport)
    },
    setDestinationAirport: (airport) => {
      set({ destinationAirport: { ...airport } })
      // console.log('updated destination to:', get().destinationAirport)
    },
    originAirportSearchValue: '',
    destinationAirportSearchValue: '',
    setOriginAirportSearchValue: (value) => {
      set({ originAirportSearchValue: value })
      // console.log('update origin search value to:', value)
    },
    setDestinationAirportSearchValue: (value) => {
      set({ destinationAirportSearchValue: value })
      // console.log('update destination search value to:', value)
    }
  }))
)

export default useStore
