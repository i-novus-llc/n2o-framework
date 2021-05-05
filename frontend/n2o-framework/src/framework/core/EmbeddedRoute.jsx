import React from 'react'

import N2o from '../../N2o'
import Route from '../../components/core/Route'

import Page from './Page'

function EmbeddedRoute() {
    return (
        <N2o>
            <Route
                path="/:pageUrl*"
                render={routeProps => <Page {...routeProps} needMetadata rootPage />}
            />
        </N2o>
    )
}

export default EmbeddedRoute
