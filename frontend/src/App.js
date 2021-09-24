/* eslint-disable react/jsx-props-no-spreading */

import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'

import Login from './containers/Login'
import Home from './containers/Home'
import Signup from './containers/Signup'
import {
  DashboardSideNav,
  UserInfo,
  ListBookings,
  ViewBooking,
  ListAirlines,
  ListAirports,
  ListFlights
} from './containers/dashboard'
import FlightListings from './containers/FlightListings'
import CreateAirline from './containers/dashboard/CreateAirline'
import CreateAirport from './containers/dashboard/CreateAirport'

function App() {
  const queryClient = new QueryClient()

  return (
    <QueryClientProvider client={queryClient}>
      <main>
        <Switch>
          <Route exact path='/' render={() => <Home />} />
          <Route exact path='/login' render={() => <Login />} />
          <Route exact path='/signup' render={() => <Signup />} />
          {/* User Dashboard */}
          <Route
            exact
            path='/flight/results'
            render={() => <FlightListings />}
          />
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
          {/* Admin Dashboard */}
          <Route
            exact
            path='/dashboard/airlines'
            render={() => (
              <DashboardSideNav sectionOpened={<ListAirlines />} />
            )}
          />
          <Route
            exact
            path='/dashboard/airports'
            render={() => (
              <DashboardSideNav sectionOpened={<ListAirports />} />
            )}
          />
          <Route
            exact
            path='/dashboard/create-airport'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateAirport />} />
            )}
          />
          <Route
            exact
            path='/dashboard/flights'
            render={() => (
              <DashboardSideNav sectionOpened={<ListFlights />} />
            )}
          />
          <Route
            exact
            path='/dashboard/create-airline'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateAirline />} />
            )}
          />
        </Switch>
      </main>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
