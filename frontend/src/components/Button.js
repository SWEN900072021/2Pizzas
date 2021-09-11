import { bool, func, string } from 'prop-types'
import React from 'react'

const Button = ({ label, onClick, submit }) => (
  <div>
    <button
      type={submit ? 'submit' : 'button'}
      onClick={onClick}
      className='w-full px-4 py-3 rounded-lg text-white font-bold bg-purple-600 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-600 focus:ring-opacity-50 transition-colors'
    >
      {label}
    </button>
  </div>
)

Button.defaultProps = {
  submit: false
}

Button.propTypes = {
  label: string.isRequired,
  onClick: func.isRequired,
  submit: bool
}
export default Button
