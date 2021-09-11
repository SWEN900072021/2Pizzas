import { bool, string } from 'prop-types'
import React from 'react'

const TextField = ({ label, placeholder, password }) => (
  <div>
    <input
      type={password ? 'password' : 'text'}
      name={label || placeholder}
      placeholder={placeholder}
      className='px-4 py-3 rounded-lg border border-bg-grey focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent'
    />
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
