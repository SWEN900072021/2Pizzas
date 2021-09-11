import { React, Fragment, useState, useEffect } from 'react'
import { v4 as uuid } from 'uuid'
import { useMediaQuery } from 'react-responsive'
import { Transition } from '@headlessui/react'
import { FaPlaneDeparture } from 'react-icons/fa'
import Search from '../components/Search'
import useStore from '../hooks/Store'

const airports = [
  {
    code: 'LAX',
    name: 'Los Angeles International',
    location: 'Los Angeles'
  },
  {
    code: 'JFK',
    name: 'John F. Kennedy',
    location: 'New York'
  },
  {
    code: 'JFK',
    name: 'John F. Kennedy',
    location: 'New York'
  },
  {
    code: 'JFK',
    name: 'John F. Kennedy',
    location: 'New York'
  },
  {
    code: 'JFK',
    name: 'John F. Kennedy',
    location: 'New York'
  }
]

const OriginSearch = () => {
  const isMobile = useMediaQuery({
    query: '(max-width: 640px)'
  })

  const [value, setValue] = useState('')
  const [open, setOpen] = useState(false)
  const [loading, setLoading] = useState(false)

  const setOriginAirport = useStore((state) => state.setOriginAirport)
  const updateOriginAirport = (airport) => {
    setValue(airport.name)
    setOriginAirport(airport)
    setOpen(false)
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
    <section>
      <button
        type='button'
        onClick={() => {
          setOpen(true)
        }}
      >
        <Search
          placeholder='Origin airport'
          Icon={<FaPlaneDeparture />}
          value={value}
          loading={loading}
          handleKeyUp={handleKeyUp}
        />
      </button>
      {open && (
        <div className='top-0 left-0 absolute bg-white z-40 h-screen w-screen p-5 space-y-5'>
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
          <main>
            <Search
              placeholder='Origin airport'
              Icon={<FaPlaneDeparture />}
              value={value}
              loading={loading}
              handleChange={(e) => {
                setValue(e.target.value)
              }}
              handleKeyUp={handleKeyUp}
            />
          </main>
        </div>
      )}
    </section>
  ) : (
    <section className='relative' onFocus={() => setOpen(true)}>
      <Search
        placeholder='Origin airport'
        Icon={<FaPlaneDeparture />}
        value={value}
        loading={loading}
        handleChange={(e) => {
          setValue(e.target.value)
        }}
        handleKeyUp={handleKeyUp}
      />
      <Transition
        as={Fragment}
        show={open}
        enter='transition ease-out duration-50'
        enterFrom='opacity-0 -translate-y-1'
        enterTo='opacity-100 translate-y-0'
      >
        <div className='absolute min-h-full max-h-80 bg-white shadow-md rounded border top-auto left-0 min-w-full w-56 z-30 mt-2.5 overflow-y-auto'>
          <div className='bg-white rounded w-full z-10 divide-y divide-gray-100'>
            {airports.map((airport) => (
              <button
                key={uuid()}
                type='button'
                onClick={() => {
                  updateOriginAirport(airport)
                }}
                className='group w-full flex flex-col align-top z-11 py-2 px-3 space-y-1 cursor-pointer hover:bg-gray-50'
              >
                <span className='self-start text-sm font-semibold cursor-pointer group-hover:text-purple-600'>
                  {airport.name} ({airport.code})
                </span>
                <span className='self-start text-xs cursor-pointer'>
                  {airport.location}
                </span>
              </button>
            ))}
          </div>
        </div>
      </Transition>
    </section>
  )
}

export default OriginSearch
