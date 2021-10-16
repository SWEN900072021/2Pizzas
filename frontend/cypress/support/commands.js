// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

import '@testing-library/cypress/add-commands'
import 'cypress-localstorage-commands'
import 'cypress-wait-until'
import '@hon2a/cypress-antd/lib/register'
import moment from 'moment-timezone'

Cypress.Commands.add(
  'signup',
  (givenName, surname, email, username, password) => {
    cy.url()
      .then((url) => {
        if (!url.endsWith('/signup')) cy.visit('include', '/signup')
      })
      .then(() => {
        cy.get('[data-cy=given-name-input]').type(givenName)
        cy.get('[data-cy=surname-input]').type(surname)
        cy.get('[data-cy=email-input]').type(email)
        cy.get('[data-cy=username-input]').type(username)
        cy.get('[data-cy=password-input]').type(password)
        cy.get('[data-cy=signup-button]').click()
      })
  }
)

Cypress.Commands.add('login', (username, password) => {
  cy.url()
    .then((url) => {
      if (!url.endsWith('/login')) cy.visit('/login')
    })
    .then(() => {
      cy.get('[data-cy=username-input]').type(username)
      cy.get('[data-cy=password-input]').type(password)
      cy.get('[data-cy=login-button]').click()
    })
})

Cypress.Commands.add('logout', () => {
  cy.get('.ant-dropdown-trigger')
    .should('be.visible')
    .click()
    .then(() => {
      cy.get('[data-cy=logout-menu-button]').click()
    })
})

Cypress.Commands.add('getExistingReturnFlightDates', () => {
  cy.url().should('include', '/flight/results')

  cy.get('[data-cy=depart-date-picker-button]').as(
    'depart-date-button'
  )

  cy.get('[data-cy=return-date-picker-button]').as(
    'return-date-button'
  )

  // Departure: 2022/01/01
  cy.get('@depart-date-button')
    .click()
    .then(() => {
      cy.get('.ant-picker-year-btn').as('depart-year-button')
      cy.get('.ant-picker-header-super-next-btn').as(
        'depart-next-year-button'
      )
      cy.get('.ant-picker-header-super-prev-btn').as(
        'depart-prev-year-button'
      )

      cy.get('.ant-picker-month-btn')
        .click()
        .then(() => {
          cy.get('@depart-year-button')
            .invoke('text')
            .then((year) => {
              const targetYear = moment('2022', 'YYYY')
              const currentYear = moment(year, 'YYYY')
              let difference = targetYear.diff(currentYear, 'years')

              while (difference) {
                if (difference > 0) {
                  cy.get('@depart-next-year-button').click()
                  difference--
                } else {
                  cy.get('@depart-prev-year-button').click()
                  difference++
                }
              }
            })
        })
        .then(() => {
          cy.get('.ant-picker-cell-in-view')
            .contains('Jan')
            .click()
            .then(() => {
              cy.get('.ant-picker-cell-in-view')
                .eq(0)
                .click()
                .then(($el) => {
                  cy.wrap($el).should(
                    'have.attr',
                    'title',
                    '2022-01-01'
                  )

                  cy.get('@depart-date-button')
                    .invoke('text')
                    .then((text) => {
                      cy.wrap(text).should('eq', 'Sat, 01 Jan')
                    })
                })
            })
        })
    })

  // Return: 2022/01/02
  cy.get('@return-date-button')
    .click()
    .then(() => {
      cy.get('.ant-picker-dropdown')
        .eq(1)
        .find('.ant-picker-cell-in-view')
        .eq(1)
        .click()
        .then(($el) => {
          cy.wrap($el).should('have.attr', 'title', '2022-01-02')

          cy.get('@return-date-button')
            .invoke('text')
            .then((text) => {
              cy.wrap(text).should('eq', 'Sun, 02 Jan')
            })
        })
    })
})

Cypress.Commands.add('searchReturnFlights', () => {
  cy.visit('/')

  // Airport search: MEL -> SYD
  cy.get('[data-cy=origin-input]')
    .click()
    .then(() => {
      cy.get('[data-cy=origin-item]')
        .wait(1000)
        .contains('Tullamarine Airport (MEL)')
        .click({ force: true })
    })
  cy.get('[data-cy=destination-input]')
    .click()
    .then(() => {
      cy.get('[data-cy=destination-item]')
        .wait(1000)
        .contains('Sydney Airport (SYD)')
        .click({ force: true })
    })

  // Return Flight
  cy.get('[data-cy=return-button]').click()

  // Submit
  cy.get('[data-cy=submit-button]').click()
})
