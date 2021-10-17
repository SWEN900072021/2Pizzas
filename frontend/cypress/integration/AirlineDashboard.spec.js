context('Airline Dashboard', () => {
  const username = 'qantas'
  const password = 'password'

  before(() => {
    cy.login(username, password)
  })

  beforeEach(() => {
    cy.get('[data-cy=my-info-link]').as('my-info-link')
    cy.get('[data-cy=view-flights-link]').as('view-flights-link')
    cy.get('[data-cy=create-flight-link]').as('create-flight-link')
  })

  it('displays airline /dashboard upon airline login', () => {
    cy.url().should('include', '/dashboard')
    cy.get('[data-cy=user-type]')
      .should('be.visible')
      .contains('Airline')
    cy.get('[data-cy=user-name]')
      .should('be.visible')
      .contains(username)

    cy.get('@my-info-link').should('be.visible')
    cy.get('@view-flights-link').should('be.visible')
    cy.get('@create-flight-link').should('be.visible')
  })

  context('Create Flight', () => {
    beforeEach(() => {
      cy.get('@create-flight-link').click()

      cy.getByPlaceholderText('Enter flight code').as('code-input')

      cy.getByPlaceholderText('Select an airplane profile').as(
        'profile-select'
      )
      cy.getByPlaceholderText('Enter cost for first class').as(
        'first-class-input'
      )
      cy.getByPlaceholderText('Enter cost for business class').as(
        'business-class-input'
      )
      cy.getByPlaceholderText('Enter cost for first class').as(
        'economy-class-input'
      )

      cy.getByPlaceholderText('Select origin airport').as(
        'origin-select'
      )
      cy.getByPlaceholderText('Departure date & time').as(
        'departure-date-picker'
      )

      cy.getByPlaceholderText('Select destination airport').as(
        'destination-select'
      )
      cy.getByPlaceholderText('Arrival date & time').as(
        'arrival-date-picker'
      )
    })

    it('displays empty Create Flight form by default', () => {
      cy.get('@code-input')
        .should('be.visible')
        .should('have.value', '')
      cy.get('@profile-select')
        .should('be.visible')
        .should('have.value', '')
      cy.get('@first-class-input')
        .should('be.visible')
        .should('have.value', '')
      cy.get('@business-class-input')
        .should('be.visible')
        .should('have.value', '')
      cy.get('@economy-class-input')
        .should('be.visible')
        .should('have.value', '')
      cy.get('@origin-select')
        .should('be.visible')
        .should('have.value', '')
      cy.get('@departure-date-picker')
        .should('be.visible')
        .should('have.value', '')
      cy.get('@destination-select')
        .should('be.visible')
        .should('have.value', '')
      cy.get('@arrival-date-picker')
        .should('be.visible')
        .should('have.value', '')
    })
  })

  context('View Flights', () => {
    beforeEach(() => {
      cy.get('@view-flights-link').click()
      cy.get('[data-cy=add-new-flight-button]').as(
        'add-new-flight-button'
      )
    })

    it('displays table of flights', () => {
      cy.getTable().should('be.visible')
      cy.getTableColumnHeaders().should('have.length', 11)
      cy.expectTableSortedBy(0)
    })

    context('View Flight', () => {
      it('navigates to ViewFlight when view button of flight clicked', () => {
        cy.getTableRows().first().find('td a').click()
        cy.url().should('include', '/dashboard/view/flights')
      })
    })
  })
})
