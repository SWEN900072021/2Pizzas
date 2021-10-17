import { bool, func, string } from 'prop-types'
import React from 'react'
// import { BsEye, BsEyeSlash } from 'react-icons/bs'

const TextField = ({
  datacy,
  value,
  name,
  label,
  placeholder,
  password,
  onChange,
  className,
  required
}) => (
  // const [show, setShow] = useState(!password)
  // const toggleShow = () => setShow(!show)
  <div className='relative'>
    <label htmlFor={label || placeholder} className='space-y-1'>
      <div className='font-medium'>{label}</div>
      <input
        data-cy={datacy}
        value={value}
        required={required}
        name={name || label || placeholder}
        onChange={onChange}
        type={!password ? 'text' : 'password'}
        id={label || placeholder}
        placeholder={placeholder || label}
        className={`${className} px-4 py-3 rounded-lg border border-bg-grey focus:outline-none focus:ring-2 focus:ring-yellow-600 focus:border-transparent`}
      />
      {/* <button
          type='button'
          className={`${
            (!password || !show) && 'hidden'
          } absolute top-1/4 left-48 bg-white z-40 text-black text-opacity-20`}
          onClick={toggleShow}
        >
          <BsEye className='w-5 h-5' />
        </button> */}
      {/* <button
          type='button'
          className={`${
            (!password || show) && 'hidden'
          } absolute top-1/4 left-48 bg-white z-40 text-black text-opacity-20`}
          onClick={toggleShow}
        >
          <BsEyeSlash className='w-5 h-5' />
        </button> */}
    </label>
  </div>
)

TextField.defaultProps = {
  datacy: 'text-field',
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
  datacy: string,
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
