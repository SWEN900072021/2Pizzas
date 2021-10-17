context('Admin Dashboard', () => {
  const username = 'admin'
  const password = 'password'

  before(() => {
    cy.login(username, password)
  })

  beforeEach(() => {
    cy.get('[data-cy=my-info-link]').as('my-info-link')
    cy.get('[data-cy=view-airlines-link]').as('view-airlines-link')
    cy.get('[data-cy=view-airports-link]').as('view-airports-link')
    cy.get('[data-cy=add-airline-link]').as('add-airline-link')
    cy.get('[data-cy=add-airport-link]').as('add-airport-link')
  })

  it('displays admin /dashboard upon admin login', () => {
    cy.url().should('include', '/dashboard')
    cy.get('[data-cy=user-type]')
      .should('be.visible')
      .contains('Administrator')
    cy.get('[data-cy=user-name]')
      .should('be.visible')
      .contains(username)

    cy.get('@my-info-link').should('be.visible')
    cy.get('@view-airlines-link').should('be.visible')
    cy.get('@view-airports-link').should('be.visible')
    cy.get('@add-airline-link').should('be.visible')
    cy.get('@add-airport-link').should('be.visible')
  })

  context('Add Airline', () => {
    beforeEach(() => {
      cy.fixture('createAirline').as('airline')
      cy.get('@add-airline-link').click()
      cy.get('[data-cy=submit-button]').as('submit-button')
      cy.get('[data-cy=error-text]').as('error-text')
    })

    it('displays empty Create Airline form by default', () => {
      cy.get('@submit-button')
        .should('be.visible')
        .should('not.be.disabled')

      cy.get('@error-text').should('contain', '')

      cy.getFormInput()
        .should('have.length', 4)
        .each(($input, index) => {
          switch (index) {
            case 0:
              cy.get('[data-cy=name-input-label]').contains('Name')
              cy.wrap($input)
                .should(
                  'have.attr',
                  'placeholder',
                  'Enter airline name'
                )
                .should('be.empty')
              break
            case 1:
              cy.get('[data-cy=code-input-label]').contains('Code')
              cy.wrap($input)
                .should(
                  'have.attr',
                  'placeholder',
                  'Enter airline code'
                )
                .should('be.empty')
              break
            case 2:
              cy.get('[data-cy=username-input-label]').contains(
                'Username'
              )
              cy.wrap($input)
                .should('have.attr', 'placeholder', 'Enter username')
                .should('be.empty')
              break
            case 3:
              cy.get('[data-cy=password-input-label]').contains(
                'Password'
              )
              cy.wrap($input)
                .should('have.attr', 'placeholder', 'Enter password')
                .should('be.empty')
              break
          }
        })
    })

    it('displays error message when form is submitted without filling in all fields', () => {
      cy.get('@submit-button').click()
      cy.get('@error-text').should(
        'contain',
        'All fields are required'
      )
    })

    it('navigates to ViewAirlines with new airline upon successful creation', () => {
      cy.get('@view-airlines-link').click()

      cy.getTableRows()
        .its('length')
        .then((prevNumRows) => {
          cy.get('@add-airline-link').click()

          cy.getFormInput()
            .should('have.length', 4)
            .each(($input, index) => {
              switch (index) {
                case 0:
                  cy.wrap($input).type(airline.name)
                  break
                case 1:
                  cy.wrap($input).type(airline.code)
                  break
                case 2:
                  cy.wrap($input).type(airline.username)
                  break
                case 3:
                  cy.wrap($input).type(airline.password)
                  break
              }
            })

          cy.get('@submit-button').click()
          cy.url().should('include', '/view/airlines')

          cy.wait(1000)

          cy.getTableRows()
            .its('length')
            .then((currNumRows) => {
              cy.wrap(currNumRows - prevNumRows).should('eq', 1)
            })
        })
    })
  })

  context('View Airlines', () => {
    beforeEach(() => {
      cy.get('@view-airlines-link').click()
      cy.get('[data-cy=add-new-airline-button]').as(
        'add-new-airline-button'
      )
    })

    it('displays table of airlines', () => {
      cy.getTable().should('be.visible')
      cy.getTableColumnHeaders().should('have.length', 4)
      cy.expectTableSortedBy(0)
    })

    it('navigates to CreateAirline on Add New Airline button click', () => {
      cy.get('@add-new-airline-button').click()
      cy.url().should('include', '/create/airline')
    })

    it('can disable an active airline', () => {
      cy.getTableRows()
        .filter(':contains("INACTIVE")')
        .should('have.length', 0)
      cy.getTableRows()
        .filter(':contains("ACTIVE")')
        .first()
        .find('td')
        .last()
        .find('button')
        .click()
      cy.getTableRows()
        .filter(':contains("INACTIVE")')
        .should('have.length', 1)
    })

    it('can enable an inactive airline', () => {
      cy.getTableRows()
        .filter(':contains("INACTIVE")')
        .should('have.length', 1)
        .find('td')
        .last()
        .find('button')
        .click()
      cy.getTableRows()
        .filter(':contains("INACTIVE")')
        .should('have.length', 0)
    })
  })

  context('Add Airport', () => {
    beforeEach(() => {
      cy.fixture('createAirport').as('airport')
      cy.get('@add-airport-link').click()
      cy.get('[data-cy=submit-button]').as('submit-button')
      cy.get('[data-cy=error-text]').as('error-text')
    })

    it('displays empty Create Airport form by default', () => {
      cy.get('@submit-button')
        .should('be.visible')
        .should('not.be.disabled')

      cy.get('@error-text').should('contain', '')

      cy.getFormInput()
        .should('have.length', 3)
        .each(($input, index) => {
          switch (index) {
            case 0:
              cy.get('[data-cy=name-input-label]').contains('Name')
              cy.wrap($input)
                .should(
                  'have.attr',
                  'placeholder',
                  'Enter airport name'
                )
                .should('be.empty')
              break
            case 1:
              cy.get('[data-cy=code-input-label]').contains('Code')
              cy.wrap($input)
                .should(
                  'have.attr',
                  'placeholder',
                  'Enter airport code'
                )
                .should('be.empty')
              break
            case 2:
              cy.get('[data-cy=location-input-label]').contains(
                'Location'
              )
              cy.wrap($input)
                .should(
                  'have.attr',
                  'placeholder',
                  'Enter airport location'
                )
                .should('be.empty')
              break
          }
        })

      cy.getFormInput({ type: 'select' })
        .should('have.length', 1)
        .expectSelectPlaceholder('Select timezone')
    })

    it('displays error message when form is submitted without filling in all fields', () => {
      cy.get('@submit-button').click()
      cy.get('@error-text').should(
        'contain',
        'All fields are required'
      )
    })

    it('navigates to ViewAirports with new airport upon successful creation', () => {
      cy.get('@view-airports-link').click()

      cy.getTableRows()
        .its('length')
        .then((prevNumRows) => {
          cy.get('@add-airport-link').click()

          cy.getFormInput()
            .should('have.length', 3)
            .each(($input, index) => {
              switch (index) {
                case 0:
                  cy.wrap($input).type(airport.name)
                  break
                case 1:
                  cy.wrap($input).type(airport.code)
                  break
                case 2:
                  cy.wrap($input).type(airport.location)
                  break
              }
            })

          cy.getFormInput({ type: 'select' })
            .should('have.length', 1)
            .click()
            .then(() => {
              cy.chooseSelectDropdownOption(airport.timezone)
              cy.get('@submit-button').click()
              cy.url().should('include', '/view/airports')

              cy.wait(1000)

              cy.getTableRows()
                .its('length')
                .then((currNumRows) => {
                  cy.wrap(currNumRows - prevNumRows).should('eq', 1)
                })
            })
        })
    })
  })

  context('View Airports', () => {
    beforeEach(() => {
      cy.get('@view-airports-link').click()
      cy.get('[data-cy=add-new-airport-button]').as(
        'add-new-airport-button'
      )
    })

    it('displays table of airports', () => {
      cy.getTable().should('be.visible')
      cy.getTableColumnHeaders().should('have.length', 6)
      cy.expectTableSortedBy(0)
    })

    it('navigates to CreateAirport on Add New Airport button click', () => {
      cy.get('@add-new-airport-button').click()
      cy.url().should('include', '/create/airport')
    })

    it('can disable an active airport', () => {
      cy.getTableRows()
        .filter(':contains("INACTIVE")')
        .should('have.length', 0)
      cy.getTableRows()
        .filter(':contains("ACTIVE")')
        .first()
        .find('td')
        .last()
        .find('button')
        .click()
      cy.getTableRows()
        .filter(':contains("INACTIVE")')
        .should('have.length', 1)
    })

    it('can enable an inactive airport', () => {
      cy.getTableRows()
        .filter(':contains("INACTIVE")')
        .should('have.length', 1)
        .find('td')
        .last()
        .find('button')
        .click()
      cy.getTableRows()
        .filter(':contains("INACTIVE")')
        .should('have.length', 0)
    })
  })
})
