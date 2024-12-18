import React from 'react'
import { Route, RouteComponentProps } from 'react-router-dom'
import { ConnectedRouter } from 'connected-react-router'

import history from '../../history'

import RootPage from './RootPage'

export default function Router({ defaultTemplate }: { defaultTemplate: unknown }) {
    return (
        <ConnectedRouter history={history}>
            <Route
                path="/:pageUrl*"
                render={(routeProps: RouteComponentProps) => (
                    <RootPage
                        {...routeProps}
                        defaultTemplate={defaultTemplate}
                    />
                )}
            />
        </ConnectedRouter>
    )
}
