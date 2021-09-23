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
  ListAirlines,
  ListAirports,
  ListFlights
} from './containers/dashboard'
import CreateFlight from './containers/dashboard/CreateFlight'

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
            path='/dashboard/flights'
            render={() => (
              <DashboardSideNav sectionOpened={<ListFlights />} />
            )}
          />
          <Route
            exact
            path='/dashboard/create-flight'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateFlight />} />
            )}
          />
        </Switch>
      </main>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
