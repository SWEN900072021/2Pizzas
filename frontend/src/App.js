import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'

import Login from './containers/Login'
import Home from './containers/Home'
import Signup from './containers/Signup'
import FlightSearchResults from './containers/FlightSearchResults'
import {
  DashboardSideNav,
  UserInfo,
  ListBookings,
  ViewBooking,
  ViewFlight,
  ListAirlines,
  ListAirports,
  ListFlights,
  CreateAirline,
  CreateAirport,
  CreateFlight
} from './containers/dashboard'

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
            path='/flight/results'
            render={() => <FlightSearchResults />}
          />

          {/* All User Dashboards */}

          <Route
            exact
            path='/dashboard'
            render={() => (
              <DashboardSideNav sectionOpened={<UserInfo />} />
            )}
          />

          {/* Customer Dashboard */}
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
            path='/dashboard/flights/:id'
            render={() => (
              <DashboardSideNav sectionOpened={<ViewFlight />} />
            )}
          />
        </Switch>
      </main>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
