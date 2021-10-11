context('Home', () => {
  before(() => {
    cy.visit('/')
  })

  context('Airport Search', () => {
    context('Origin Search', () => {
      beforeEach(() => {
        cy.get('[data-cy=origin-input]')
          .as('origin-input')
          .click()
          .waitUntil(() =>
            cy.get('.ant-select-dropdown').should('be.visible')
          )
        cy.get('[data-cy=origin-item]').as('origin-airports')
      })

      afterEach(() => {
        cy.get('@origin-input').focus()
        cy.get('@origin-input').blur()
      })

      it('displays 3 airport items by default', () => {
        cy.get('@origin-airports').should('have.length', 3)

        cy.get('[data-cy=origin-name]').each(($el, index) => {
          switch (index) {
            case 0:
              cy.wrap($el).should(
                'contain',
                'Tullamarine Airport (MEL)'
              )
              break
            case 1:
              cy.wrap($el).should('contain', 'Sydney Airport (SYD)')
              break
            case 2:
              cy.wrap($el).should('contain', 'Avalon Airport (AVV)')
              break
          }
        })

        cy.get('@origin-airports').each(($el, index) => {
          switch (index) {
            case 0:
              cy.wrap($el).should('contain', 'Melbourne')
              break
            case 1:
              cy.wrap($el).should('contain', 'Sydney')
              break
            case 2:
              cy.wrap($el).should('contain', 'Geelong')
              break
          }
        })
      })

      it('can select an airport item', () => {
        cy.get('@origin-airports').first().click()

        cy.get('@origin-input').should(
          'have.value',
          '(MEL) Tullamarine Airport'
        )
      })

      it('clears input on delete', () => {
        cy.get('@origin-airports', { timeout: 5000 }).first().click()
        cy.get('@origin-input').should(
          'have.value',
          '(MEL) Tullamarine Airport'
        )

        cy.get('@origin-input').click().type('{backspace}')
        cy.get('@origin-input').should('have.value', '')
      })
    })

    context('Destination Search', () => {
      beforeEach(() => {
        cy.get('[data-cy=destination-input]')
          .as('destination-input')
          .click()
          .waitUntil(() =>
            cy.get('.ant-select-dropdown').should('be.visible')
          )
        cy.get('[data-cy=destination-item]').as(
          'destination-airports'
        )
      })

      afterEach(() => {
        cy.get('@destination-input').focus()
        cy.get('@destination-input').blur()
      })

      it('displays 3 airport items by default', () => {
        cy.get('@destination-airports', { timeout: 5000 }).should(
          'have.length',
          3
        )

        cy.get('[data-cy=destination-name]').each(($el, index) => {
          switch (index) {
            case 0:
              cy.wrap($el).should(
                'contain',
                'Tullamarine Airport (MEL)'
              )
              break
            case 1:
              cy.wrap($el).should('contain', 'Sydney Airport (SYD)')
              break
            case 2:
              cy.wrap($el).should('contain', 'Avalon Airport (AVV)')
              break
          }
        })

        cy.get('[data-cy=destination-location]').each(
          ($el, index) => {
            switch (index) {
              case 0:
                cy.wrap($el).should('contain', 'Melbourne')
                break
              case 1:
                cy.wrap($el).should('contain', 'Sydney')
                break
              case 2:
                cy.wrap($el).should('contain', 'Geelong')
                break
            }
          }
        )
      })

      it('can select an airport item', () => {
        cy.get('@destination-airports').first().click()

        cy.get('@destination-input').should(
          'have.value',
          '(MEL) Tullamarine Airport'
        )
      })

      it('clears input on delete', () => {
        cy.get('@destination-airports').first().click()
        cy.get('@destination-input').should(
          'have.value',
          '(MEL) Tullamarine Airport'
        )

        cy.get('@destination-input').click().type('{backspace}')
        cy.get('@destination-input').should('have.value', '')
      })
    })
  })

  context('Date Pickers', () => {
    beforeEach(() => {
      cy.visit('')
      cy.get('.range-picker input')
        .as('range-date-pickers')
        .each(($input) => $input.val(''))
      cy.get('.date-pickers input')
        .as('date-pickers')
        .each(($input) => $input.val(''))
    })

    it('can choose two dates for return flights', () => {
      cy.get('[data-cy=return-button]').click()

      cy.get('@range-date-pickers').first().click()
      cy.get('.ant-picker-dropdown-range').should('be.visible')
      cy.get(
        'table.ant-picker-content > tbody > tr > td:not(.ant-picker-cell-today):not(.ant-picker-cell-disabled)'
      )
        .eq(0)
        .click()
        .then((clickedTd) => {
          cy.get('@range-date-pickers')
            .first()
            .should('have.value', clickedTd.prop('title'))
        })

      cy.get('@range-date-pickers').last().click()
      cy.get('.ant-picker-dropdown-range').should('be.visible')
      cy.get(
        'table.ant-picker-content > tbody > tr > td:not(.ant-picker-cell-today):not(.ant-picker-cell-disabled)'
      )
        .eq(1)
        .click()
        .then((clickedTd) => {
          cy.get('@range-date-pickers')
            .last()
            .should('have.value', clickedTd.prop('title'))
        })
    })

    it('can only choose one date for one-way flights', () => {
      cy.get('[data-cy=one-way-button]').click()

      cy.get('@date-pickers').first().click()
      cy.get('.ant-picker-dropdown').should('be.visible')
      cy.get(
        'table.ant-picker-content > tbody > tr > td:not(.ant-picker-cell-today):not(.ant-picker-cell-disabled)'
      )
        .eq(0)
        .click()
        .then((clickedTd) => {
          cy.get('@date-pickers')
            .first()
            .should('have.value', clickedTd.prop('title'))
        })

      cy.get('@date-pickers').last().should('be.disabled')
      cy.get('.ant-picker-dropdown').should('not.be.visible')
    })
  })

  context('Passenger Count', () => {
    beforeEach(() => {
      cy.get('[data-cy=passenger-count-input]')
        .as('passenger-count-input')
        .click()
      cy.get('[data-cy=passenger-count]').as('passenger-count')
      cy.get('[data-cy=add-passenger-button]').as(
        'add-passenger-button'
      )
      cy.get('[data-cy=remove-passenger-button]').as(
        'remove-passenger-button'
      )
    })

    afterEach(() => {
      cy.get('@passenger-count-input').click()
    })

    it('displays 1 passenger by default', () => {
      cy.get('@passenger-count-input').should(
        'have.value',
        '1 passenger(s)'
      )
      cy.get('@passenger-count').should('have.text', '1')
    })

    it('can increment passenger by 1', () => {
      cy.get('@passenger-count')
        .invoke('text')
        .then((text) => {
          const currentCount = parseInt(text)
          cy.get('@add-passenger-button').click()
          cy.get('@passenger-count-input').should(
            'have.value',
            `${currentCount + 1} passenger(s)`
          )
          cy.get('@passenger-count').should(
            'have.text',
            `${currentCount + 1}`
          )
        })
    })

    it('can decrement passenger by 1', () => {
      cy.get('@passenger-count')
        .invoke('text')
        .then((text) => {
          const currentCount = parseInt(text)
          cy.get('@remove-passenger-button').click()
          cy.get('@passenger-count-input').should(
            'have.value',
            `${currentCount - 1} passenger(s)`
          )
          cy.get('@passenger-count').should(
            'have.text',
            `${currentCount - 1}`
          )
        })
    })
  })

  context('Submit', () => {
    beforeEach(() => {
      cy.get('[data-cy=origin-input]').as('origin-input')
      cy.get('[data-cy=destination-input]').as('destination-input')
      cy.get('[data-cy=error-text]', { force: true }).as('error-text')
      cy.get('[data-cy=submit-button]').as('submit-button')
    })

    it('displays error if empty airport fields', () => {
      cy.get('@origin-input').should('have.value', '')
      cy.get('@destination-input').should('have.value', '')

      cy.get('@submit-button').click()

      cy.get('@error-text').should(
        'contain',
        'Airport fields are required.'
      )
    })

    it('displays error if airport fields are the same', () => {
      cy.get('@origin-input').click()
      cy.get('[data-cy=origin-item]').first().click()
      cy.get('@destination-input').click()
      cy.get('[data-cy=destination-item]').first().click()

      cy.get('@submit-button').click()
      cy.get('@error-text').should(
        'contain',
        'Must choose different origin and destination airports.'
      )
    })
  })
})
