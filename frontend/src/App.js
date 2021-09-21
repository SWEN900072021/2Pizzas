import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'

import Login from './containers/Login'
import Home from './containers/Home'
import Signup from './containers/Signup'
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
        </Switch>
      </main>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
