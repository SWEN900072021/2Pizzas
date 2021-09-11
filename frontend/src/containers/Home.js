import { React } from 'react'
import { FaExchangeAlt } from 'react-icons/fa'
import OriginSearch from './OriginSearch'
import DestinationSearch from './DestinationSearch'

const Home = () => (
  <main className='h-screen bg-purple-100 '>
    <section className='flex justify-center items-center flex-wrap gap-2'>
      <OriginSearch />
      <button
        type='button'
        className='rounded-3xl bg-purple-400 hover:bg-purple-500 transition-colors focus:outline-none focus:ring-2 focus:ring-purple-300 p-2'
      >
        <FaExchangeAlt className='w-5 h-5 text-white' />
      </button>
      <DestinationSearch />
    </section>
  </main>
)

export default Home
