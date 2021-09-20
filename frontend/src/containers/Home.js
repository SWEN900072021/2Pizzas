import { React } from 'react'

// Containers and Components

import NavBar from '../components/NavBar'

// Hooks

// Assets
import landscapePicture from '../assets/home-landscape.png'
import FlightForm from './FlightForm'

const Home = () => (
  <main className='h-screen'>
    <NavBar />

    {/* -------------------------------------------------------------------------- */
    /*                                  Jumbotron                                 */
    /* -------------------------------------------------------------------------- */}
    <section className='flex flex-col items-center justify-start h-full'>
      <img
        draggable={false}
        src={landscapePicture}
        alt='Landscape with hot air balloons'
        className='object-cover 
  relative
 h-5/6 md:h-3/4 w-full
 object-left
 filter contrast-75'
      />
      <div
        className='self-center mx-6 
  absolute
 transform translate-y-36 md:translate-y-32
'
      >
        <h2 className='text-3xl font-bold text-center text-white select-none sm:text-left md:text-5xl'>
          Search hundreds of flights
        </h2>
      </div>
      {/* ------------------------------------------------------------------------- */
      /*                           Main Flight Search Form                          */
      /* -------------------------------------------------------------------------- */}
      <section className='absolute flex flex-col flex-wrap items-center justify-center flex-grow p-5 bg-yellow-50 mt-52 max-w-max'>
        <FlightForm />
      </section>
    </section>
  </main>
)

export default Home
