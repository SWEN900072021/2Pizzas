import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'

import Login from './containers/Login'
import Home from './containers/Home'
import Signup from './containers/Signup'
import UserDashboard from './containers/UserDashboard'

function App() {
  const queryClient = new QueryClient()

  return (
    <QueryClientProvider client={queryClient}>
      <main>
        <Switch>
          <Route exact path='/' render={() => <Home />} />
          <Route exact path='/login' render={() => <Login />} />
          <Route exact path='/signup' render={() => <Signup />} />
          <Route exact path='/dashboard' render={() => <UserDashboard />} />
        </Switch>
      </main>
    </QueryClientProvider>
  )
}

export default App
