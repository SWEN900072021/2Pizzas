import { bool, string } from 'prop-types'
import React from 'react'

const TextField = ({ label, placeholder, password }) => (
  <div>
    <label htmlFor={label} className='space-y-1'>
      <div className='font-medium'>{label}</div>
      <input
        type={password ? 'password' : 'text'}
        id={label}
        name={label}
        placeholder={placeholder || label}
        className='px-4 py-3 rounded-lg border border-bg-grey focus:outline-none focus:ring-2 focus:ring-yellow-600 focus:border-transparent'
      />
    </label>
  </div>
)

TextField.defaultProps = {
  label: '',
  placeholder: '',
  password: false
}

TextField.propTypes = {
  label: string,
  placeholder: string,
  password: bool
}

export default TextField
