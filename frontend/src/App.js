/* eslint-disable react/jsx-props-no-spreading */

import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'

import Login from './containers/Login'
import Home from './containers/Home'
import Signup from './containers/Signup'
import DashboardSideNav from './components/DashboardSideNav'
import FlightListings from './containers/FlightListings'
import ListBookings from './containers/ListBookings'
import UserInfo from './containers/UserInfo'
import ViewBooking from './containers/ViewBooking'

function App() {
  const queryClient = new QueryClient()

  return (
    <QueryClientProvider client={queryClient}>
      <main>
        <Switch>
          <Route exact path='/' render={() => <Home />} />
          <Route exact path='/login' render={() => <Login />} />
          <Route exact path='/signup' render={() => <Signup />} />
          <Route
            exact
            path='/dashboard'
            render={() => (
              <DashboardSideNav sectionOpened={<UserInfo />} />
            )}
          />
          <Route
            exact
            path='/dashboard/current-bookings'
            render={() => (
              <DashboardSideNav
                sectionOpened={
                  <ListBookings bookingsStatus='current' />
                }
              />
            )}
          />
          <Route
            exact
            path='/dashboard/previous-bookings'
            render={() => (
              <DashboardSideNav
                sectionOpened={
                  <ListBookings bookingsStatus='previous' />
                }
              />
            )}
          />
          <Route
            exact
            path='/dashboard/bookings/:id'
            render={() => (
              <DashboardSideNav sectionOpened={<ViewBooking />} />
            )}
          />
          <Route
            exact
            path='/flight/results'
            render={() => <FlightListings />}
          />
        </Switch>
      </main>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
