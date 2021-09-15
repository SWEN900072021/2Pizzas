import { bool, func, string } from 'prop-types'
import React from 'react'

const TextField = ({
  value,
  label,
  placeholder,
  password,
  onChange
}) => (
  <div>
    <label htmlFor={label || placeholder} className='space-y-1'>
      <div className='font-medium'>{label}</div>
      <input
        value={value}
        onChange={onChange}
        type={password ? 'password' : 'text'}
        id={label || placeholder}
        name={label || placeholder}
        placeholder={placeholder || label}
        className='px-4 py-3 rounded-lg border border-bg-grey focus:outline-none focus:ring-2 focus:ring-yellow-600 focus:border-transparent'
      />
    </label>
  </div>
)

TextField.defaultProps = {
  value: '',
  label: '',
  placeholder: '',
  password: false,
  onChange: () => {}
}

TextField.propTypes = {
  value: string,
  label: string,
  placeholder: string,
  password: bool,
  onChange: func
}

export default TextField
