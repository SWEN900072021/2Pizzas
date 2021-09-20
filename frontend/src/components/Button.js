import { bool, func, string } from 'prop-types'
import React from 'react'

const Button = ({ label, onClick, submit }) => (
  <div>
    <button
      type={submit ? 'submit' : 'button'}
      onClick={onClick}
      className='w-full px-4 py-3 font-bold text-white transition-colors bg-yellow-500 rounded-lg hover:bg-yellow-400 focus:outline-none focus:ring-2 focus:ring-yellow-500 focus:ring-opacity-50'
    >
      {label}
    </button>
  </div>
)

Button.defaultProps = {
  submit: false,
  onClick: () => {}
}

Button.propTypes = {
  label: string.isRequired,
  onClick: func,
  submit: bool
}
export default Button
