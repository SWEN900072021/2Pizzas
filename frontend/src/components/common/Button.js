import { bool, element, func, string } from 'prop-types'
import React from 'react'

const Button = ({ label, onClick, submit, className }) => (
  <div>
    <button
      type={submit ? 'submit' : 'button'}
      onClick={onClick}
      className={`${className} w-full px-4 py-3 border-2 border-yellow-500 rounded-lg text-white font-bold bg-yellow-500 hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-700 focus:ring-opacity-50 transition-colors`}
    >
      {label}
    </button>
  </div>
)

Button.defaultProps = {
  label: '',
  submit: false,
  onClick: () => {},
  className: ''
}

Button.propTypes = {
  label: string || element,
  onClick: func,
  submit: bool,
  className: string
}
export default Button
