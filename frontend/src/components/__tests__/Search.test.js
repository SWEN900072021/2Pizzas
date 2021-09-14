import React from 'react'

import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'

import Search from '../Search'

describe('Search', () => {
  let element
  const value = ''
  const placeholder = 'Type to search'

  beforeEach(() => {
    render(
      <Search
        data-testid='search'
        value={value}
        placeholder={placeholder}
      />
    )
    element = screen.getByPlaceholderText(placeholder)
  })

  it('should be initially empty', () => {
    expect(element).toHaveValue('')
  })

  it('should have the correct placeholder', () => {
    expect(element).toHaveAttribute('placeholder', placeholder)
  })

  it('should be focused when clicked', () => {
    userEvent.click(element)
    expect(element).toHaveFocus()
  })
})
