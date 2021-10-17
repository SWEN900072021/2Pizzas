import moment from 'moment-timezone'

context('Concurrency - Actor 1', () => {
  const customer = 'john'
  const admin = 'admin'
  const password = 'password'

  context('Create Booking with Same Seat', () => {
    before(() => {
      cy.task('clearValues')
      cy.searchReturnFlights()
      cy.getExistingReturnFlightDates()
    })

    it('books an existing flight', () => {
      cy.get('[data-cy=outbound-flight]')
        .should('not.have.length', 0)
        .first()
        .click()
      cy.get('[data-cy=return-flight]')
        .should('not.have.length', 0)
        .first()
        .click()
      cy.get('[data-cy=submit-button]').click()

      cy.get('[data-cy=username-input]').type(customer)
      cy.get('[data-cy=password-input]').type(password)
      cy.get('[data-cy=login-button]').click()

      cy.url().should('include', '/booking/create')
      cy.get('[data-cy=given-name]').type('John')
      cy.get('[data-cy=surname]').type('Smith')
      cy.get('[data-cy=passport-number]').type('A123')
      cy.get('[data-cy=nationality]').type('Australian')
      cy.get('[data-cy=dob-picker]')
        .click()
        .then(() => {
          cy.get('.ant-picker-cell').first().click()
        })
      cy.get('[data-cy=outbound-class-select]')
        .click()
        .wait(1000)
        .then(() => {
          cy.get('[data-cy=outbound-economy-option]').click()
          cy.get('[data-cy=outbound-seat-select]')
            .click()
            .wait(1000)
            .then(() => {
              cy.get('[data-cy=outbound-5A]').click()
            })
        })
      cy.get('[data-cy=return-class-select]')
        .click()
        .wait(1000)
        .then(() => {
          cy.get('[data-cy=return-economy-option]').click()
          cy.get('[data-cy=return-seat-select]')
            .click()
            .wait(1000)
            .then(() => {
              cy.get('[data-cy=return-5A]').click()
            })
        })
      cy.get('[data-cy=submit-button]')
        .click()
        .wait(10000)
        .then(() => {
          cy.url().then((url) => {
            cy.task('setValue', {
              key: 'customerOneUrl',
              value: url
            }).then(() => {
              if (url.endsWith('/booking/create')) {
                cy.get('[data-cy=error-text]').contains('Conflict')

                cy.waitUntil(() =>
                  cy.task('getValue', 'customerTwoUrl')
                ).then((value) =>
                  cy
                    .wrap(value)
                    .should(
                      'include',
                      '/dashboard/view/bookings/current'
                    )
                )
              } else if (
                url.endsWith('/dashboard/view/bookings/current')
              ) {
                cy.waitUntil(() =>
                  cy.task('getValue', 'customerTwoUrl')
                ).then((value) =>
                  cy.wrap(value).should('include', '/booking/create')
                )
              } else {
                throw new Error('Unexpected URL')
              }
            })
          })
        })
    })
  })

  context('Disable Airport and Airline', () => {
    before(() => {
      cy.login(admin, password)
    })

    it('disables an airport being used in flight creation', () => {
      cy.get('[data-cy=view-airports-link]')
        .click()
        .then(() => {
          cy.url().then((url) =>
            cy.task('setValue', {
              key: 'customerOneUrl',
              value: url
            })
          )
        })
        .then(() => {
          cy.waitUntil(() => cy.task('getValue', 'formCompleted'), {
            timeout: 120000,
            interval: 1000
          })
            .then((value) => {
              cy.wrap(value).should('eq', true)
            })
            .then(() => {
              cy.getTableRow(0)
                .find('[data-cy=disable-airport-button]')
                .click()
                .then(() => {
                  cy.task('setValue', {
                    key: 'airportDisabled',
                    value: true
                  })
                })
            })
            .waitUntil(() => cy.task('getValue', 'flightFailed'), {
              timeout: 120000,
              interval: 1000
            })
            .then((value) => {
              cy.wrap(value).should('eq', true)
            })
            .then(() => {
              cy.get('[data-cy=view-airlines-link]').click()
              cy.getTableRow(1)
                .find('[data-cy=disable-airline-button]')
                .click()
                .then(() => {
                  cy.task('setValue', {
                    key: 'airlineDisabled',
                    value: true
                  })
                })
            })
        })
    })
  })
})
