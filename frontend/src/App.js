import React from 'react'
import { Route, Switch } from 'react-router'
import { QueryClient, QueryClientProvider } from 'react-query'

import Login from './containers/Login'
import Home from './containers/Home'
import Signup from './containers/Signup'
import DashboardSideNav from './components/DashboardSideNav'

function App() {
  const queryClient = new QueryClient()

  return (
    <QueryClientProvider client={queryClient}>
      <main>
        <Switch>
          <Route exact path='/' render={() => <Home />} />
          <Route exact path='/login' render={() => <Login />} />
          <Route exact path='/signup' render={() => <Signup />} />
          <Route exact path='/dashboard' render={() => <DashboardSideNav sectionOpened='myInfo' />} />
          <Route exact path='/dashboard/myInfo' render={() => <DashboardSideNav sectionOpened='myInfo' />} />
          <Route exact path='/dashboard/currentBookings' render={() => <DashboardSideNav sectionOpened='currentBookings' />} />
          <Route exact path='/dashboard/previousBookings' render={() => <DashboardSideNav sectionOpened='previousBookings' />} />

          {/* <Route exact path='/dashboard/myInfo' render={() => <Sidebar content={myInfo}/> } /> */}

        </Switch>
      </main>
    </QueryClientProvider>
  )
}

export default App
