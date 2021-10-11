context('Login', () => {
  before(() => {
    cy.visit('/login')
  })

  beforeEach(() => {
    cy.get('[data-cy=username-input]').as('username-input')
    cy.get('[data-cy=password-input]').as('password-input')
    cy.get('[data-cy=error-text]', { force: true }).as('error-text')
    cy.get('[data-cy=login-button]').as('login-button')
    cy.get('[data-cy=signup-link').as('signup-link')
  })

  it('displays the login page', () => {
    cy.get('[data-cy=login-title]').should('have.text', 'Log In')
    cy.get('@username-input')
      .should('be.visible')
      .should('have.value', '')
      .should('have.attr', 'placeholder', 'Username')

    cy.get('@password-input')
      .should('be.visible')
      .should('have.value', '')
      .should('have.attr', 'placeholder', 'Password')

    cy.get('@error-text').should('be.hidden')

    cy.get('@login-button').should('be.visible').contains('Log In')
    cy.get('@signup-link')
      .should('be.visible')
      .contains("Don't have an account?")
  })

  it('displays an error if username and password combination does not exist', () => {
    cy.get('@username-input').type('wrong')
    cy.get('@password-input').type('wrong')
    cy.get('@login-button').click()
    cy.get('@error-text')
      .should('be.visible')
      .contains('Invalid username or password.')
  })

  context('Navigation', () => {
    afterEach(() => {
      localStorage.setItem('session-store', {
        state: { token: null, user: null },
        version: 0
      })
      cy.logout()
      cy.visit('/login')
    })

    it('navigates to Signup when signup link clicked', () => {
      cy.get('@signup-link').click()
      cy.url().should('include', '/signup')
    })

    it('navigates to Home for successful customer login', () => {
      cy.login('john', 'password')
      cy.url().should('include', '/')
    })

    it('navigates to Dashboard for successful admin login', () => {
      cy.login('admin', 'password')
      cy.url().should('include', '/dashboard')
    })

    it('navigates to Dashboard for successful airline login', () => {
      cy.login('john', 'password')
      cy.url().should('include', '/dashboard')
    })
  })
})
