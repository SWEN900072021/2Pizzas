import React from 'react'

import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'

import TextField from '../TextField'

describe('TextField', () => {
  let element

  const label = 'Username'
  const placeholder = 'Enter username'

  beforeEach(() => {
    render(<TextField label={label} placeholder={placeholder} />)
    element = screen.getByLabelText(label)
  })

  it('should render a text field', () => {
    expect(element).toBeInTheDocument()
  })

  it('should have the correct label', () => {
    const elementLabel = screen.getByText(label)
    expect(elementLabel).toBeInTheDocument()
  })

  it('should have the correct placeholder', () => {
    expect(element).toHaveAttribute('placeholder', placeholder)
  })

  it('should initially be empty', () => {
    expect(element).toHaveValue('')
  })

  it('should be focused when clicked', () => {
    userEvent.click(element)
    expect(element).toHaveFocus()
  })

  it('should contain keyboard input when typed', () => {
    userEvent.type(element, 'test')
    expect(element).toHaveValue('test')
  })
})
