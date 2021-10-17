import moment from 'moment-timezone'

context('FlightListings', () => {
  before(() => {
    cy.searchReturnFlights()
  })

  context('Flight Search Panel', () => {
    beforeEach(() => {
      cy.get('[data-cy=flight-form]').as('flight-form')

      cy.get('[data-cy=flight-form-toggle-button]').as(
        'toggle-button'
      )

      cy.get('[data-cy=origin-airport]').as('origin-airport')
      cy.get('[data-cy=destination-airport]').as(
        'destination-airport'
      )
      cy.get('[data-cy=depart-date-prev-button]').as(
        'depart-date-prev-button'
      )
      cy.get('[data-cy=depart-date-next-button]').as(
        'depart-date-next-button'
      )
      cy.get('[data-cy=depart-date-picker-button]').as(
        'depart-date-button'
      )
      cy.get('[data-cy=return-date-prev-button]').as(
        'return-date-prev-button'
      )
      cy.get('[data-cy=return-date-next-button]').as(
        'return-date-next-button'
      )
      cy.get('[data-cy=return-date-picker-button]').as(
        'return-date-button'
      )

      cy.get('[data-cy=submit-button]').as('submit-button')
    })

    it('hides flight form by default', () => {
      cy.get('@flight-form').should('not.be.visible')
    })

    it('displays selected airports by default', () => {
      cy.get('@origin-airport').contains('Melbourne (MEL)')
      cy.get('@destination-airport').contains('Sydney (SYD)')
    })

    it('toggles flight form visibility on button click', () => {
      cy.get('@toggle-button').click()
      cy.get('@flight-form').should('be.visible')
      cy.get('@toggle-button').click()
      cy.get('@flight-form').should('not.be.visible')
    })

    it('adds one day from date on right button click', () => {
      cy.get('@depart-date-button')
        .invoke('text')
        .then((text) => {
          const currentDepartDate = moment(text, 'ddd, DD MMM')
          cy.get('@depart-date-next-button')
            .click()
            .then(() => {
              cy.get('@depart-date-button')
                .invoke('text')
                .then((text) => {
                  const newDepartDate = moment(text, 'ddd, DD MMM')
                  cy.wrap(
                    newDepartDate.diff(currentDepartDate, 'days')
                  ).should('eq', 1)
                })
            })
        })

      cy.get('@return-date-button')
        .invoke('text')
        .then((text) => {
          const currentReturnDate = moment(text, 'ddd, DD MMM')
          cy.get('@return-date-next-button')
            .click()
            .then(() => {
              cy.get('@return-date-button')
                .invoke('text')
                .then((text) => {
                  const newReturnDate = moment(text, 'ddd, DD MMM')
                  cy.wrap(
                    newReturnDate.diff(currentReturnDate, 'days')
                  ).should('eq', 1)
                })
            })
        })
    })

    it('removes one day from date on left button click', () => {
      cy.get('@depart-date-button')
        .invoke('text')
        .then((text) => {
          const currentDepartDate = moment(text, 'ddd, DD MMM')

          cy.get('@depart-date-prev-button')
            .click()
            .then(() => {
              cy.get('@depart-date-button')
                .invoke('text')
                .then((text) => {
                  const newDepartDate = moment(text, 'ddd, DD MMM')
                  cy.wrap(
                    newDepartDate.diff(currentDepartDate, 'days')
                  ).should('eq', -1)
                })
            })
        })

      cy.get('@return-date-button')
        .invoke('text')
        .then((text) => {
          const currentReturnDate = moment(text, 'ddd, DD MMM')
          cy.get('@return-date-prev-button')
            .click()
            .then(() => {
              cy.get('@return-date-button')
                .invoke('text')
                .then((text) => {
                  const newReturnDate = moment(text, 'ddd, DD MMM')
                  cy.wrap(
                    newReturnDate.diff(currentReturnDate, 'days')
                  ).should('eq', -1)
                })
            })
        })
    })

    it('changes return date if depart date becomes after return date', () => {
      cy.get('@depart-date-button')
        .invoke('text')
        .then((text) => {
          const currentDepartDate = moment(text, 'ddd, DD MMM')
          cy.get('@return-date-button')
            .invoke('text')
            .then((text) => {
              const currentReturnDate = moment(text, 'ddd, DD MMM')
              let difference = currentReturnDate.diff(
                currentDepartDate,
                'days'
              )

              while (difference--) {
                cy.get('@depart-date-next-button').click()
              }
            })
        })
        .then(() => {
          cy.get('@return-date-button')
            .invoke('text')
            .then((text) => {
              const currentReturnDate = moment(text, 'ddd, DD MMM')
              cy.get('@depart-date-next-button')
                .click()
                .then(() => {
                  cy.get('@return-date-button')
                    .invoke('text')
                    .then((text) => {
                      const newReturnDate = moment(
                        text,
                        'ddd, DD MMM'
                      )
                      cy.wrap(
                        newReturnDate.diff(currentReturnDate, 'days')
                      ).should('eq', 1)
                    })
                })
            })
        })
    })

    it('changes depart date if return date becomes before depart date', () => {
      cy.get('@depart-date-button')
        .invoke('text')
        .then((text) => {
          const currentDepartDate = moment(text, 'ddd, DD MMM')
          cy.get('@return-date-button')
            .invoke('text')
            .then((text) => {
              const currentReturnDate = moment(text, 'ddd, DD MMM')
              let difference = currentReturnDate.diff(
                currentDepartDate,
                'days'
              )

              while (difference--) {
                cy.get('@depart-date-next-button').click()
              }
            })
        })
        .then(() => {
          cy.get('@depart-date-button')
            .invoke('text')
            .then((text) => {
              const currentDepartDate = moment(text, 'ddd, DD MMM')
              cy.get('@return-date-prev-button')
                .click()
                .then(() => {
                  cy.get('@depart-date-button')
                    .invoke('text')
                    .then((text) => {
                      const newDepartDate = moment(
                        text,
                        'ddd, DD MMM'
                      )
                      cy.wrap(
                        newDepartDate.diff(currentDepartDate, 'days')
                      ).should('eq', -1)
                    })
                })
            })
        })
    })

    it('cannot change date to before today', () => {
      cy.get('@depart-date-button')
        .invoke('text')
        .then((text) => {
          const currentDepartDate = moment(text, 'ddd, DD MMM')
          let difference = currentDepartDate.diff(moment(), 'days')
          while (difference--) {
            cy.get('@depart-date-prev-button').click()
          }
        })

      cy.get('@depart-date-prev-button').click()
      cy.get('@depart-date-button')
        .invoke('text')
        .then((text) => {
          const currentDepartDate = moment(text, 'ddd, DD MMM')
          cy.wrap(currentDepartDate.diff(moment(), 'days')).should(
            'eq',
            0
          )
        })

      cy.get('@return-date-button')
        .invoke('text')
        .then((text) => {
          const currentReturnDate = moment(text, 'ddd, DD MMM')
          let difference = currentReturnDate.diff(moment(), 'days')
          while (difference--) {
            cy.get('@return-date-prev-button').click()
          }
        })

      cy.get('@return-date-prev-button').click()
      cy.get('@return-date-button')
        .invoke('text')
        .then((text) => {
          const currentReturnDate = moment(text, 'ddd, DD MMM')
          cy.wrap(currentReturnDate.diff(moment(), 'days')).should(
            'eq',
            0
          )
        })
    })
  })

  context('Listings', () => {
    before(() => {
      cy.getExistingReturnFlightDates()
    })

    beforeEach(() => {
      cy.get('[data-cy=submit-button]').as('submit-button')
      cy.get('[data-cy=outbound-flight]').as('outbound-flights')
      cy.get('[data-cy=return-flight]').as('return-flights')
    })

    it('displays correct flight listings', () => {
      cy.get('@outbound-flights').should('have.length', 1)
      cy.get('@return-flights').should('have.length', 1)
    })
    it('cannot submit to create booking if no selected flights', () => {
      cy.get('@outbound-flights').each(($el) => {
        cy.wrap($el).should('not.have.class', 'selected')
      })
      cy.get('@return-flights').each(($el) => {
        cy.wrap($el).should('not.have.class', 'selected')
      })
      cy.get('@submit-button').should('be.disabled')
    })
    it('can click on unselected flight to mark it as selected', () => {
      cy.get('@outbound-flights')
        .filter(':not(.selected)')
        .should('not.have.length', 0)
        .first()
        .click()
        .should('have.class', 'selected')
      cy.get('@return-flights')
        .filter(':not(.selected)')
        .should('not.have.length', 0)
        .first()
        .click()
        .should('have.class', 'selected')
    })
    it('can click on selected flight to mark it as unselected', () => {
      cy.get('@outbound-flights')
        .filter('.selected')
        .should('have.length', 1)
        .click()
        .should('not.have.class', 'selected')
      cy.get('@return-flights')
        .filter('.selected')
        .should('have.length', 1)
        .click()
        .should('not.have.class', 'selected')
    })
  })

  context('Booking', () => {
    beforeEach(() => {
      cy.restoreLocalStorage()
      cy.searchReturnFlights()
      cy.getExistingReturnFlightDates()

      cy.get('[data-cy=submit-button]').as('submit-button')
      cy.get('[data-cy=outbound-flight]').as('outbound-flights')
      cy.get('[data-cy=return-flight]').as('return-flights')
    })

    it('follows correct flow when submit while not logged in: FlightListings -> Login -> CreateBooking', () => {
      cy.get('@outbound-flights').first().click()
      cy.get('@return-flights').first().click()
      cy.get('@submit-button').should('not.be.disabled').click()
      cy.url().should('include', '/login')
      cy.get('[data-cy=username-input]').type('john')
      cy.get('[data-cy=password-input]').type('password')
      cy.get('[data-cy=login-button]').click()
      cy.url().should('include', '/booking/create')
      cy.logout()
      cy.saveLocalStorage()
    })

    it('follows correct flow when submit while not logged in: FlightListings -> Login -> Signup -> CreateBooking', () => {
      cy.get('@outbound-flights').first().click()
      cy.get('@return-flights').first().click()
      cy.get('@submit-button').should('not.be.disabled').click()
      cy.url().should('include', '/login')
      cy.get('[data-cy=signup-link]').click()
      cy.signup('Dini', 'Putri', 'dini@gmail.com', 'dini', 'password')
      cy.url().should('include', '/booking/create')
      cy.logout()
      cy.saveLocalStorage()
    })

    it('follows correct flow when submit while logged in: FlightListings -> CreateBooking', () => {
      cy.login('john', 'password')
      cy.get('.ant-dropdown-trigger').should('be.visible')

      cy.searchReturnFlights()
      cy.getExistingReturnFlightDates()
      cy.get('@outbound-flights').first().click()
      cy.get('@return-flights').first().click()
      cy.get('@submit-button').click()

      cy.url().should('include', '/booking/create')
      cy.logout()
      cy.saveLocalStorage()
    })
  })
})
