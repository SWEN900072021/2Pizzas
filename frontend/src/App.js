import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'

import Login from './containers/Login'
import Home from './containers/Home'
import Signup from './containers/Signup'
import CreateBooking from './containers/CreateBooking'
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
  CreateFlight,
  EditFlight
} from './containers/dashboard'

import FlightListings from './containers/FlightListings'

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
            render={() => <FlightListings />}
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
            path='/dashboard/view/bookings/current'
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
            path='/dashboard/view/bookings/previous'
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
            path='/dashboard/view/bookings/:id'
            render={() => (
              <DashboardSideNav sectionOpened={<ViewBooking />} />
            )}
          />
          {/* Admin Dashboard */}
          <Route
            exact
            path='/dashboard/view/airlines'
            render={() => (
              <DashboardSideNav sectionOpened={<ListAirlines />} />
            )}
          />
          <Route
            exact
            path='/dashboard/view/airports'
            render={() => (
              <DashboardSideNav sectionOpened={<ListAirports />} />
            )}
          />
          <Route
            exact
            path='/dashboard/create/airports'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateAirport />} />
            )}
          />
          <Route
            exact
            path='/dashboard/create/airlines'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateAirline />} />
            )}
          />
          <Route
            exact
            path='/dashboard/view/flights'
            render={() => (
              <DashboardSideNav sectionOpened={<ListFlights />} />
            )}
          />
          <Route
            exact
            path='/dashboard/view/flights/:id'
            render={() => (
              <DashboardSideNav sectionOpened={<ViewFlight />} />
            )}
          />
          <Route
            exact
            path='/dashboard/create/flights'
            render={() => (
              <DashboardSideNav sectionOpened={<CreateFlight />} />
            )}
          />
          <Route
            exact
            path='/dashboard/edit/flights/:id'
            render={() => (
              <DashboardSideNav sectionOpened={<EditFlight />} />
            )}
          />
          <Route
            exact
            path='/booking/create'
            render={() => <CreateBooking />}
          />
        </Switch>
      </main>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
