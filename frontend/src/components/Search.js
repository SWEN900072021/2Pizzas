import React from 'react'
import { bool, element, func, string } from 'prop-types'
import { HiSearch } from 'react-icons/hi'
import Spinner from './Spinner'

const Search = ({
  Icon,
  placeholder,
  value,
  loading,
  handleChange,
  handleKeyUp
}) => {
  const onChange = (e) => {
    handleChange(e)
  }

  const onKeyUp = (e) => {
    handleKeyUp(e)
  }

  return (
    <form className='w-full'>
      <div className='w-full relative text-left text-gray-300 focus-within:text-skin-muted transition-colors'>
        <div className='absolute inset-y-0 left-2.5 flex justify-center items-center pl-1 md:pl-2 pointer-events-none'>
          <button
            type='button'
            id='searchButton'
            className='focus:outline-none'
          >
            {Icon || <Icon className='h-5 w-5' />}
          </button>
        </div>
        <input
          aria-label='search bar input'
          id='searchInput'
          type='text'
          disabled={loading}
          value={value}
          onChange={onChange}
          onKeyUp={onKeyUp}
          autoComplete='off'
          placeholder={placeholder}
          className={`w-full
            border border-bg-grey rounded-lg 
            py-2 pl-9 pl:2
            md:py-4 md:pl-12 md:pr-4
            font-light tracking-wide text-gray-800 
            placeholder-gray-500 focus:placeholder-gray-400
            focus:outline-none focus:ring-2 focus:ring-purple-400`}
        />
        <div className='absolute inset-y-0 right-0 pr-7 flex items-center'>
          {loading && <Spinner size={6} />}
        </div>
      </div>
    </form>
  )
}

Search.defaultProps = {
  Icon: <HiSearch className='h-5 w-5' />,
  handleChange: () => {},
  handleKeyUp: () => {}
}

Search.propTypes = {
  Icon: element,
  placeholder: string.isRequired,
  value: string.isRequired,
  loading: bool.isRequired,
  handleChange: func,
  handleKeyUp: func
}

export default Search
