import React from 'react'
import { createRoot } from 'react-dom/client'
import N2O from 'n2o-framework'
import createFactoryConfig from "n2o-framework/lib/core/factory/createFactoryConfig"
import authProvider from "n2o-framework/lib/core/auth/authProvider"

import 'n2o-framework/dist/n2o.css'
import './index.css'

const config = {
  security: {
    authProvider,
    externalLoginUrl: '/'
  },
  messages: {
    timeout: {
      error: 0,
      success: 5000,
      warning: 0,
      info: 0,
    }
  }
}

const container = document.getElementById('n2o')

if (container) {
    const root = createRoot(container)
    root.render(<N2O {...createFactoryConfig(config)} />)
}
