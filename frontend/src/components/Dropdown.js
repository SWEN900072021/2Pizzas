import React from 'react'
import { Menu } from '@headlessui/react'

const Dropdown = () => (
  <Menu>
    <Menu.Button classname='bg-yellow-500'>Best</Menu.Button>
    <Menu.Items>
      <Menu.Item>
        {({ active }) => (
          // eslint-disable-next-line jsx-a11y/anchor-has-content
          <a
            className={`${active && 'bg-blue-500'}`}
            href='/account-settings'
          />
        )}
      </Menu.Item>
      <Menu.Item>
        {({ active }) => (
          <a
            className={`${active && 'bg-blue-500'}`}
            href='/account-settings'
          >
            Documentation
          </a>
        )}
      </Menu.Item>
      <Menu.Item disabled>
        <span className='opacity-75'>
          Invite a friend (coming soon!)
        </span>
      </Menu.Item>
    </Menu.Items>
  </Menu>
)

export default Dropdown
