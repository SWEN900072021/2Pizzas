import React from 'react'
import { number } from 'prop-types'
import Dropdown from '../common/Dropdown'

// eslint-disable-next-line no-unused-vars
const FlightFilter = ({ results }) => (
  <section className='flex justify-around item-center'>
    <div>4 results</div>
    <div className='flex-col justify-center item-center'>
      <div>Sort by</div>
      <Dropdown />
    </div>
  </section>
)

FlightFilter.propTypes = {
  results: number.isRequired
}

export default FlightFilter
