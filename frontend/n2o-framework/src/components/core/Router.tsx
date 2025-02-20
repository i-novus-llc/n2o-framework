import React, { ComponentType, ReactNode } from 'react'
import { Route, RouteComponentProps } from 'react-router-dom'
import { ConnectedRouter } from 'connected-react-router'

import history from '../../history'

import { RootPage } from './RootPage'
import { type TemplateProps } from './templates/types'

export default function Router({ defaultTemplate, children }: { defaultTemplate?: ComponentType<TemplateProps>, children?: ReactNode }) {
    return (
        <ConnectedRouter history={history}>
            <Route
                path="/:pageUrl*"
                render={(routeProps: RouteComponentProps) => (
                    <RootPage {...routeProps} defaultTemplate={defaultTemplate} />
                )}
            />
        </ConnectedRouter>
    )
}
