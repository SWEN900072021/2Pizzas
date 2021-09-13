import { React, useState, useEffect } from 'react'
import { v4 as uuid } from 'uuid'
import { useMediaQuery } from 'react-responsive'
import { AutoComplete } from 'antd'
import { FaPlaneArrival } from 'react-icons/fa'
import { arrayOf, shape, string } from 'prop-types'
import Search from '../components/Search'
import useStore from '../hooks/Store'

const DestinationSearch = ({ airports }) => {
  const isMobile = useMediaQuery({
    query: '(max-width: 640px)'
  })

  const [open, setOpen] = useState(false)
  const [loading, setLoading] = useState(false)

  const setDestinationAirport = useStore(
    (state) => state.setDestinationAirport
  )

  const destinationAirportSearchValue = useStore(
    (state) => state.destinationAirportSearchValue
  )
  const setDestinationAirportSearchValue = useStore(
    (state) => state.setDestinationAirportSearchValue
  )

  const updateDestinationAirport = (airport) => {
    if (airport !== null) {
      setDestinationAirportSearchValue(
        `(${airport.code}) ${airport.name}`
      )
      setDestinationAirport(airport)
      setOpen(false)
    } else {
      setDestinationAirportSearchValue('')
      setDestinationAirport({})
    }
  }
  const handleKeyUp = (e) => {
    if (e.key === 'Escape') {
      e.target.blur()
      setOpen(false)
    }
  }

  useEffect(() => {
    setLoading(false)
  }, [])

  return isMobile ? (
    <section className='flex flex-grow'>
      <button
        className='flex flex-grow'
        type='button'
        onClick={() => {
          setOpen(true)
        }}
      >
        <Search
          placeholder='Destination airport'
          Icon={<FaPlaneArrival />}
          value={destinationAirportSearchValue}
          loading={loading}
          handleKeyUp={handleKeyUp}
        />
      </button>
      {open && (
        <section className='top-0 left-0 absolute bg-white z-40 h-screen w-screen p-5 space-y-5'>
          <header className='flex justify-between'>
            <span className='font-bold'>From</span>
            <button
              type='button'
              className='underline text-purple-700'
              onClick={() => {
                setOpen(false)
              }}
            >
              Cancel
            </button>
          </header>
          <Search
            placeholder='Destination airport'
            Icon={<FaPlaneArrival />}
            value={destinationAirportSearchValue}
            loading={loading}
            handleChange={(e) => {
              setDestinationAirportSearchValue(e.target.value)
            }}
            handleKeyUp={handleKeyUp}
          />
          <div className='h-5/6 divide-y divide-gray-100 overflow-y-auto'>
            {airports.map((airport) => (
              <button
                key={uuid()}
                type='button'
                onClick={() => {
                  updateDestinationAirport(airport)
                }}
                className='group w-full flex flex-col align-top z-11 py-2 px-3 space-y-1 cursor-pointer hover:bg-gray-50 focus:bg-gray-50 focus:outline-none'
              >
                <span className='self-start text-sm font-semibold cursor-pointer group-hover:text-purple-600 group-focus:text-purple-600'>
                  {airport.name} ({airport.code})
                </span>
                <span className='self-start text-xs cursor-pointer'>
                  {airport.location}
                </span>
              </button>
            ))}
          </div>
        </section>
      )}
    </section>
  ) : (
    <div className='relative'>
      <AutoComplete
        onKeyDown={(e) => {
          if (e.key === 'Backspace') {
            updateDestinationAirport(null)
          }
        }}
        value={destinationAirportSearchValue}
        backfill
        dropdownMatchSelectWidth={350}
        options={airports.map((airport) => ({
          value: `(${airport.code}) ${airport.name}`,
          label: (
            <button
              type='button'
              onClick={() => {
                updateDestinationAirport(airport)
              }}
              className='group w-full flex flex-col align-top z-11 py-2 px-3 space-y-1 cursor-pointer focus:outline-none'
            >
              <span className='self-start text-sm font-semibold cursor-pointer group-hover:text-purple-600 group-focus:text-purple-600'>
                {airport.name} ({airport.code})
              </span>
              <span className='self-start text-xs cursor-pointer'>
                {airport.location}
              </span>
            </button>
          )
        }))}
      >
        <input
          placeholder='Destination airport'
          aria-label='search bar input'
          name='searchInput'
          type='text'
          autoComplete='off'
          className={`flex-grow
              border border-bg-grey rounded-lg 
              py-2 pl-9 pr:2
              md:py-3 md:pl-12 md:pr-3
              font-light tracking-wide text-gray-800 
              placeholder-gray-500 focus:placeholder-gray-400
              focus:outline-none focus:ring-2 focus:ring-purple-400`}
        />
      </AutoComplete>
      <div
        name='icon'
        className='absolute inset-y-0 left-2.5 flex justify-center items-center pl-1 md:pl-2 pointer-events-none'
      >
        <span className='focus:outline-none text-gray-400'>
          <FaPlaneArrival className='h-4 w-4' />
        </span>
      </div>
    </div>
  )
}

DestinationSearch.defaultProps = {
  airports: []
}

DestinationSearch.propTypes = {
  airports: arrayOf(
    shape({
      code: string.isRequired,
      name: string.isRequired,
      location: string.isRequired
    })
  )
}

export default DestinationSearch
