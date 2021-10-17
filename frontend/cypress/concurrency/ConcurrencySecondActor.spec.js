import moment from 'moment-timezone'

context('Concurrency - Actor 2', () => {
  const customer = 'jane'
  const airline = 'qantas'
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

      cy.get('[data-cy=given-name]').type('Jane')
      cy.get('[data-cy=surname]').type('Tong')
      cy.get('[data-cy=passport-number]').type('B456')
      cy.get('[data-cy=nationality]').type('Singaporean')
      cy.get('[data-cy=dob-picker]')
        .click()
        .then(() => {
          cy.get('.ant-picker-cell').first().click()
        })
      cy.get('[data-cy=outbound-class-select]')
        .click()
        .then(() => {
          cy.get('[data-cy=outbound-economy-option]').click()
          cy.get('[data-cy=outbound-seat-select]')
            .click()
            .then(() => {
              cy.get('[data-cy=outbound-5A]').click()
            })
        })
      cy.get('[data-cy=return-class-select]')
        .click()
        .then(() => {
          cy.get('[data-cy=return-economy-option]').click()
          cy.get('[data-cy=return-seat-select]')
            .click()
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
              key: 'customerTwoUrl',
              value: url
            }).then(() => {
              if (url.endsWith('/booking/create')) {
                cy.get('[data-cy=error-text]').contains('Conflict')

                cy.waitUntil(() =>
                  cy.task('getValue', 'customerOneUrl')
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
                  cy.task('getValue', 'customerOneUrl')
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

    after(() => {
      cy.task('setValue', { key: 'customerTwoUrl', value: null })
    })
  })

  context('Create Flight with Invalid Airport and Airline', () => {
    before(() => {
      cy.login(airline, password)
      cy.task('clearValues')
    })

    it('fails to create a flight', () => {
      cy.waitUntil(() => cy.task('getValue', 'customerOneUrl'), {
        timeout: 120000,
        interval: 1000
      }).then((value) => {
        cy.wrap(value).should('include', '/dashboard/view/airports')
      })

      cy.get('[data-cy=create-flight-link]')
        .click()
        .then(() =>
          cy.url().then((url) =>
            cy.task('setValue', {
              key: 'customerTwoUrl',
              value: url
            })
          )
        )

      cy.fixture('createFlight').then((flight) => {
        cy.get('[data-cy=code-input]')
          .should('be.visible')
          .type(flight.code)

        cy.get('[data-cy=profile-select]')
          .should('be.visible')
          .click()
          .then(() => {
            cy.get(`[data-cy=${flight.airplaneProfile}]`).click()
          })

        cy.get('[data-cy=first-class-input]')
          .should('be.visible')
          .type(flight.firstClassCost)
        cy.get('[data-cy=business-class-input]')
          .should('be.visible')
          .type(flight.businessClassCost)
        cy.get('[data-cy=economy-class-input]')
          .should('be.visible')
          .type(flight.economyClassCost)

        cy.get('[data-cy=origin-select]')
          .should('be.visible')
          .click()
          .then(() => {
            cy.get(`[data-cy=origin-${flight.originCode}]`)
              .click()
              .then(() => {
                cy.get('[data-cy=departure-date-picker]')
                  .should('be.visible')
                  .click()
                  .then(() => {
                    cy.get('.ant-picker-cell-today')
                      .invoke('attr', 'title')
                      .then((title) => {
                        const date = moment(title, 'YYYY-MM-DD')
                        const nextDate = date.add(1, 'days')
                        cy.get(
                          `.ant-picker-cell[title=${nextDate.format(
                            'YYYY-MM-DD'
                          )}]`
                        ).click()
                      })
                      .then(() => {
                        cy.get('.ant-btn:visible')
                          .contains('Ok')
                          .click()
                      })
                  })
              })
          })
        cy.get('[data-cy=destination-select]')
          .should('be.visible')
          .click()
          .then(() => {
            cy.get(`[data-cy=destination-${flight.destinationCode}]`)
              .click()
              .then(() => {
                cy.get('[data-cy=arrival-date-picker]')
                  .should('be.visible')
                  .click()
                  .then(() => {
                    cy.get('.ant-picker-cell-today')
                      .last()
                      .invoke('attr', 'title')
                      .then((title) => {
                        const date = moment(title, 'YYYY-MM-DD')
                        const nextDate = date.add(2, 'days')
                        cy.get(
                          `.ant-picker-cell[title=${nextDate.format(
                            'YYYY-MM-DD'
                          )}]`
                        )
                          .last()
                          .click()
                      })
                      .then(() => {
                        cy.get('.ant-btn:visible')
                          .contains('Ok')
                          .click()
                      })
                  })
              })
          })

        cy.task('setValue', {
          key: 'formCompleted',
          value: true
        })

        cy.waitUntil(() => cy.task('getValue', 'airportDisabled'))
          .then((value) => cy.wrap(value).should('eq', true))
          .then(() => {
            cy.get('[data-cy=flight-submit-button]')
              .as('submit-button')
              .click()
              .then(() => {
                cy.get('p')
                  .contains('is in INACTIVE state')
                  .should('exist')
              })
              .then(() => {
                cy.task('setValue', {
                  key: 'flightFailed',
                  value: true
                }).then(() => {
                  cy.waitUntil(
                    () => cy.task('getValue', 'airlineDisabled'),
                    { timeout: 120000, interval: 1000 }
                  )
                    .then((value) =>
                      cy.wrap(value).should('eq', true)
                    )
                    .then(() => {
                      cy.get('@submit-button')
                        .click()
                        .then(() => {
                          cy.url().should('include', '/login')
                        })
                    })
                })
              })
          })
      })
    })
  })
})
