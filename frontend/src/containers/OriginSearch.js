import { React, useState, useEffect } from 'react'
import { v4 as uuid } from 'uuid'
import { AutoComplete } from 'antd'
import { FaPlaneDeparture } from 'react-icons/fa'
import { arrayOf, shape, string } from 'prop-types'
import Search from '../components/Search'
import { useStore } from '../hooks/Store'

const OriginSearch = ({ airports }) => {
  // Visibility state of dropdown
  const [open, setOpen] = useState(false)

  // Set the selected airport
  const setOriginAirport = useStore((state) => state.setOriginAirport)

  // Value of input element
  const originAirportSearchValue = useStore(
    (state) => state.originAirportSearchValue
  )
  const setOriginAirportSearchValue = useStore(
    (state) => state.setOriginAirportSearchValue
  )

  const updateOriginAirport = (airport) => {
    if (airport !== null) {
      setOriginAirportSearchValue(`(${airport.code}) ${airport.name}`)
      setOriginAirport(airport)
      setOpen(false)
    } else {
      setOriginAirportSearchValue('')
      setOriginAirport({})
    }
  }

  const handleKeyUp = (e) => {
    if (e.key === 'Escape') {
      e.target.blur()
      setOpen(false)
    }
  }

  // FIXME: Still buggy, still need to select all text in input every time input is focused
  const selectAllOnFocus = (e) => {
    e.target.select()
  }

  useEffect(() => {}, [])

  return (
    <section>
      {/* MOBILE */}
      <section className='flex flex-grow sm:hidden sm:flex-none'>
        {/* Input Field */}
        <Search
          placeholder='Origin airport'
          StartIcon={<FaPlaneDeparture />}
          value={originAirportSearchValue}
          handleKeyUp={handleKeyUp}
          handleClick={() => setOpen(true)}
        />
        {/* Airport Search and Select, which will be Full Screen in Mobile */}
        <section
          className={`${
            !open
              ? 'hidden'
              : 'fixed h-screen z-50 top-0 left-0 w-full bg-white p-5 space-y-5'
          } `}
        >
          <header className='flex justify-between'>
            <span className='font-bold'>From</span>
            <button
              type='button'
              className='underline text-yellow-700'
              onClick={() => {
                setOpen(false)
              }}
            >
              Cancel
            </button>
          </header>
          <Search
            placeholder='Origin airport'
            StartIcon={<FaPlaneDeparture />}
            value={originAirportSearchValue}
            handleChange={(e) => {
              setOriginAirportSearchValue(e.target.value)
            }}
            handleKeyUp={handleKeyUp}
          />
          {/* Airport Search Results */}
          <section className='h-5/6 divide-y divide-gray-100 overflow-y-auto'>
            {airports.map((airport) => (
              <button
                key={uuid()}
                type='button'
                onClick={() => {
                  updateOriginAirport(airport)
                }}
                className='group w-full flex flex-col align-top z-11 py-2 px-3 space-y-1 cursor-pointer hover:bg-gray-50 focus:bg-gray-50 focus:outline-none'
              >
                <span className='self-start text-sm font-semibold cursor-pointer group-hover:text-yellow-600 group-focus:text-yellow-600'>
                  {airport.name} ({airport.code})
                </span>
                <span className='self-start text-xs cursor-pointer'>
                  {airport.location}
                </span>
              </button>
            ))}
          </section>
        </section>
      </section>
      {/* TABLET AND LARGER DISPLAYS */}
      <section className='relative hidden sm:flex sm:flex-grow'>
        <AutoComplete
          // Need this width to fill the horizontal grid space completely
          style={{ width: '100%' }}
          onFocus={selectAllOnFocus}
          onKeyDown={(e) => {
            if (e.key === 'Backspace') {
              updateOriginAirport(null)
            }
          }}
          dropdownMatchSelectWidth={350}
          // Airport Search Results: Mapping a list of airports to a list of options
          options={airports.map((airport) => ({
            // Value: Will be shown as input value if this airport option is selected
            value: `(${airport.code}) ${airport.name}`,
            // Label: The visual representation of the airport option
            label: (
              <button
                type='button'
                onClick={() => {
                  updateOriginAirport(airport)
                }}
                className='group w-full flex flex-col align-top z-11 py-2 px-3 space-y-1 cursor-pointer focus:outline-none'
              >
                <span className='self-start text-sm font-semibold cursor-pointer group-hover:text-yellow-600 group-focus:text-yellow-600'>
                  {airport.name} ({airport.code})
                </span>
                <span className='self-start text-xs cursor-pointer'>
                  {airport.location}
                </span>
              </button>
            )
          }))}
        >
          {/* The anchor input element that the search results dropdown will anchor onto */}
          <input
            placeholder='Origin airport'
            aria-label='search bar input'
            name='searchInput'
            type='text'
            autoComplete='off'
            className={`flex-grow
                border border-bg-gray rounded-lg 
                py-2 pl-9 pr:2
                md:py-3 md:pl-12 md:pr-3
                font-light tracking-wide text-gray-800 
                placeholder-gray-500 focus:placeholder-gray-400
                focus:outline-none focus:ring-2 focus:ring-yellow-400`}
          />
        </AutoComplete>
        {/* Search input icon, adorning the left-hand side of searchbar */}
        <div
          name='icon'
          className='absolute inset-y-0 left-2.5 flex justify-center items-center pl-1 md:pl-2 pointer-events-none'
        >
          <span className='focus:outline-none text-gray-400'>
            <FaPlaneDeparture className='h-4 w-4' />
          </span>
        </div>
      </section>
    </section>
  )
}

OriginSearch.defaultProps = {
  airports: []
}

OriginSearch.propTypes = {
  airports: arrayOf(
    shape({
      code: string.isRequired,
      name: string.isRequired,
      location: string.isRequired
    })
  )
}

export default OriginSearch
