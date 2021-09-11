import React, { useEffect, useState } from 'react'
import { element, string } from 'prop-types'
import { SearchIcon } from '@heroicons/react/outline'
import Spinner from './Spinner'

const Search = ({ Icon, placeholder }) => {
  const [value, setValue] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => {
    setValue(e.target.value)
  }

  useEffect(() => {
    setLoading(false)
  }, [])

  return (
    <form>
      <div className='relative text-left text-gray-300 focus-within:text-skin-muted transition-colors'>
        <div className='absolute inset-y-0 left-7 flex justify-center items-center pl-2 pointer-events-none'>
          <button type='button' id='searchButton' className='focus:outline-none'>
            {Icon || <Icon className='h-5 w-5' />}
          </button>
        </div>
        <input
          aria-label='search bar input'
          id='searchInput'
          type='text'
          disabled={loading}
          value={value}
          onChange={handleChange}
          autoComplete='off'
          placeholder={placeholder}
          className={`border border-bg-grey rounded-lg py-5 pl-20
            placeholder-gray-500 focus:placeholder-gray-400
            font-light tracking-wide text-gray-800 bg-gray-100
            focus:outline-none focus:ring-2 focus:ring-purple-400 focus:bg-gray-50`}
        />
        <div className='absolute inset-y-0 right-0 pr-7 flex items-center'>
          {loading && <Spinner size={6} />}
        </div>
      </div>
    </form>
  )
}

Search.defaultProps = {
  Icon: <SearchIcon className='h-5 w-5' />
}

Search.propTypes = {
  Icon: element,
  placeholder: string.isRequired
}

export default Search
