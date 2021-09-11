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
    originAirport: {},
    destinationAirport: {},
    setOriginAirport: (airport) =>
      set({ originAirport: { ...airport } }),
    setDestinationAirport: (airport) =>
      set({ destinationAirport: { ...airport } })
  }))
)

export default useStore
