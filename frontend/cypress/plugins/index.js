/// <reference types="cypress" />
// ***********************************************************
// This example plugins/index.js can be used to load plugins
//
// You can change the location of this file or turn off loading
// the plugins file with the 'pluginsFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/plugins-guide
// ***********************************************************

// This function is called when a project is opened or re-opened (e.g. due to
// the project's config changing)

/**
 * @type {Cypress.PluginConfig}
 */
// eslint-disable-next-line no-unused-vars

const fs = require('fs')
const path = require('path')

module.exports = (on, config) => {
  // `on` is used to hook into various events Cypress emits
  // `config` is the resolved Cypress config
  on('task', {
    setValue({ key, value }) {
      const filePath = path.join(
        process.cwd(),
        'cypress',
        'data.json'
      )

      if (fs.existsSync(filePath)) {
        const data = JSON.parse(fs.readFileSync(filePath))
        data[key] = value
        fs.writeFileSync(filePath, JSON.stringify(data))
      } else {
        fs.writeFileSync(filePath, JSON.stringify({ [key]: value }))
      }

      const data = JSON.parse(fs.readFileSync(filePath))
      return data[key]
    },

    getValue(key) {
      const filePath = path.join(
        process.cwd(),
        'cypress',
        'data.json'
      )

      if (fs.existsSync(filePath)) {
        const data = JSON.parse(fs.readFileSync(filePath))

        if (data[key] === undefined) {
          return null
        }

        return data[key]
      }
      return null
    },

    clearValues() {
      const filePath = path.join(
        process.cwd(),
        'cypress',
        'data.json'
      )

      if (fs.existsSync(filePath)) {
        fs.unlinkSync(filePath)
      }

      return null
    }
  })
}
