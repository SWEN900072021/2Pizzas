/* eslint-disable react/jsx-props-no-spreading */

import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'

import Login from './containers/Login'
import Home from './containers/Home'
import Signup from './containers/Signup'
import DashboardSideNav from './containers/dashboard/DashboardSideNav'
import FlightListings from './containers/FlightListings'
import ListAirlines from './containers/dashboard/ListAirlines'
import ListAirports from './containers/dashboard/ListAirports'
import ListBookings from './containers/dashboard/ListBookings'
import ListFlights from './containers/dashboard/ListFlights'
import CreateAirline from './containers/dashboard/CreateAirline'
import CreateAirport from './containers/dashboard/CreateAirport'
import CreateFlight from './containers/dashboard/CreateFlight'
import UserInfo from './containers/dashboard/UserInfo'
import ViewBooking from './containers/dashboard/ViewBooking'

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
            path='/dashboard'
            render={() => (
              <DashboardSideNav sectionOpened={<UserInfo />} />
            )}
          />
          <Route
            exact
            path='/dashboard/currentBookings'
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
            path='/dashboard/previousBookings'
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
            render={(routeProps) => (
              <DashboardSideNav
                sectionOpened={<ViewBooking {...routeProps} />}
              />
            )}
          />
          {/* Admin Dashboard */}
          <Route
            exact
            path='/dashboard/manage/airlines'
            render={() => (
              <DashboardSideNav sectionOpened={<ListAirlines />} />
            )}
          />
          <Route
            exact
            path='/dashboard/manage/airports'
            render={() => (
              <DashboardSideNav sectionOpened={<ListAirports />} />
            )}
          />
          <Route
            exact
            path='/dashboard/manage/airports/create'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateAirport />} />
            )}
          />
          <Route
            exact
            path='/dashboard/manage/airlines/create'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateAirline />} />
            )}
          />
          {/* Airline Dashboard */}
          <Route
            exact
            path='/dashboard/manage/flights'
            render={() => (
              <DashboardSideNav sectionOpened={<ListFlights />} />
            )}
          />
          <Route
            exact
            path='/dashboard/manage/flights/create'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateFlight />} />
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
