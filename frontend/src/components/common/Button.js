import { bool, element, func, string } from 'prop-types'
import React from 'react'

const Button = ({
  datacy,
  label,
  onClick,
  submit,
  className,
  disabled
}) => (
  <div>
    <button
      data-cy={datacy}
      type={submit ? 'submit' : 'button'}
      onClick={onClick}
      disabled={disabled}
      className={`${className} w-full px-4 py-3  rounded-lg text-white font-bold ${
        disabled
          ? 'bg-gray-400 text-gray-100 cursor-not-allowed'
          : 'bg-yellow-500 hover:bg-yellow-600 border-2 border-yellow-500 focus:outline-none focus:ring-2 focus:ring-yellow-700 focus:ring-opacity-50 transition-colors'
      }`}
    >
      {label}
    </button>
  </div>
)

Button.defaultProps = {
  label: '',
  submit: false,
  onClick: () => {},
  className: '',
  disabled: false,
  datacy: 'button'
}

Button.propTypes = {
  datacy: string,
  label: string || element,
  onClick: func,
  submit: bool,
  className: string,
  disabled: bool
}
export default Button
