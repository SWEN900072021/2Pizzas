import React from 'react'
import { DatePicker as AntDatePicker } from 'antd'
import { BsChevronLeft, BsChevronRight } from 'react-icons/bs'

const DatePicker = (date, setDate) => (
  <div>
    <button type='button'>
      <BsChevronLeft />
    </button>
    <AntDatePicker
      allowClear={false}
      value={date}
      bordered={false}
      placeholder='Return date'
      onChange={(newDate) => {
        setDate(newDate)
      }}
    />
    <button type='button'>
      <BsChevronRight />
    </button>
  </div>
)

export default DatePicker
