context('Signup', () => {
  before(() => {
    cy.visit('/signup')
  })

  beforeEach(() => {
    cy.get('[data-cy=given-name-input]').as('given-name-input')
    cy.get('[data-cy=surname-input]').as('surname-input')
    cy.get('[data-cy=email-input]').as('email-input')
    cy.get('[data-cy=username-input]').as('username-input')
    cy.get('[data-cy=password-input]').as('password-input')
    cy.get('[data-cy=error-text]', { force: true }).as('error-text')
    cy.get('[data-cy=signup-button]').as('signup-button')
    cy.get('[data-cy=login-link').as('login-link')
  })

  it('displays the signup page', () => {
    cy.get('[data-cy=signup-title]').should('have.text', 'Sign Up')

    cy.get('@given-name-input')
      .should('be.visible')
      .should('have.value', '')
      .should('have.attr', 'placeholder', 'Given Name')

    cy.get('@surname-input')
      .should('be.visible')
      .should('have.value', '')
      .should('have.attr', 'placeholder', 'Surname')

    cy.get('@email-input')
      .should('be.visible')
      .should('have.value', '')
      .should('have.attr', 'placeholder', 'Email')

    cy.get('@username-input')
      .should('be.visible')
      .should('have.value', '')
      .should('have.attr', 'placeholder', 'Username')

    cy.get('@password-input')
      .should('be.visible')
      .should('have.value', '')
      .should('have.attr', 'placeholder', 'Password')

    cy.get('@error-text').should('be.hidden')

    cy.get('@login-button').should('be.visible').contains('Sign Up')
    cy.get('@signup-link')
      .should('be.visible')
      .contains('Already have an account?')
  })

  context('Navigation', () => {
    it('navigates to Login when login link clicked', () => {
      cy.get('@login-link').click()
      cy.url().should('include', '/login')
    })

    it('navigates to Home for successful customer sign up', () => {
      cy.signup('Alex', 'Smith', 'alex@gmail.com', 'alex', 'password')
      cy.url().should('include', '/')
    })
  })
})
