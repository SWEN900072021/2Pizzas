import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'

import Login from './containers/Login'
import Home from './containers/Home'

function App() {
  const queryClient = new QueryClient()

  return (
    <QueryClientProvider client={queryClient}>
      <main className='App'>
        <Switch>
          <Route exact path='/' render={() => <Login />} />
          <Route exact path='/home' render={() => <Home />} />
        </Switch>
      </main>
    </QueryClientProvider>
  )
}

export default App
