import React from 'react'
import { bool, element, func, string } from 'prop-types'
import { HiSearch } from 'react-icons/hi'

const Search = ({
  value,
  datacy,
  placeholder,
  readOnly,
  className,
  StartIcon,
  EndIcon,
  handleChange,
  handleKeyUp,
  handleClick
}) => {
  const onChange = (e) => {
    handleChange(e)
  }

  const onKeyUp = (e) => {
    handleKeyUp(e)
  }

  const onClick = (e) => {
    handleClick(e)
  }

  return (
    <form className='relative flex flex-grow text-left text-gray-400 transition-colors'>
      <div
        name='start icon'
        className='absolute inset-y-0 left-2.5 flex justify-center items-center pl-1 md:pl-2 pointer-events-none'
      >
        <span className='focus:outline-none'>
          {StartIcon || <StartIcon className='w-5 h-5' />}
        </span>
      </div>
      <input
        data-cy={datacy}
        value={value}
        onChange={onChange}
        onKeyUp={onKeyUp}
        onClick={onClick}
        aria-label='search bar input'
        name='searchInput'
        type='text'
        autoComplete='off'
        placeholder={placeholder}
        readOnly={readOnly}
        className={`${className} w-full
            bg-white
            py-2 pl-9 pr:2
            md:py-3 md:pl-12 md:pr-3
            border border-bg-grey rounded-lg 
            font-light tracking-wide text-gray-800 
            placeholder-gray-500 focus:placeholder-gray-400
            focus:outline-none focus:ring-2 focus:ring-yellow-400`}
      />
      <div
        name='start icon'
        className='absolute inset-y-0 right-2.5 flex justify-center items-center pr-1 md:pr-2 pointer-events-none'
      >
        <span className='focus:outline-none'>
          {EndIcon || <EndIcon className='w-5 h-5' />}
        </span>
      </div>
    </form>
  )
}

Search.defaultProps = {
  readOnly: false,
  className: '',
  StartIcon: <HiSearch className='w-5 h-5' />,
  EndIcon: <></>,
  handleChange: () => {},
  handleKeyUp: () => {},
  handleClick: () => {},
  datacy: ''
}

Search.propTypes = {
  value: string.isRequired,
  placeholder: string.isRequired,
  readOnly: bool,
  className: string,
  StartIcon: element,
  EndIcon: element,
  handleChange: func,
  handleKeyUp: func,
  handleClick: func,
  datacy: string
}

export default Search
