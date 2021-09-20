import { bool, func, string } from 'prop-types'
import React from 'react'

const TextField = ({
  value,
  name,
  label,
  placeholder,
  password,
  onChange,
  className,
  required
}) => (
  <div>
    <label htmlFor={label || placeholder} className='space-y-1'>
      <div className='font-medium'>{label}</div>
      <input
        value={value}
        required={required}
        name={name || label || placeholder}
        onChange={onChange}
        type={password ? 'password' : 'text'}
        id={label || placeholder}
        placeholder={placeholder || label}
        className={`${className} px-4 py-3 rounded-lg border border-bg-grey focus:outline-none focus:ring-2 focus:ring-yellow-600 focus:border-transparent`}
      />
    </label>
  </div>
)

TextField.defaultProps = {
  value: '',
  name: '',
  label: '',
  placeholder: '',
  password: false,
  onChange: () => {},
  className: '',
  required: false
}

TextField.propTypes = {
  value: string,
  name: string,
  label: string,
  placeholder: string,
  password: bool,
  onChange: func,
  className: string,
  required: bool
}

export default TextField
