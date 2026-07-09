import React, { ComponentType, PropsWithChildren } from 'react'
import { ConnectedRouter } from 'connected-react-router'

import history from '../../history'

import { RootPage } from './RootPage'
import { type TemplateProps } from './templates/types'

export default function Router({ defaultTemplate }: PropsWithChildren<{ defaultTemplate?: ComponentType<TemplateProps> }>) {
    return (
        <ConnectedRouter history={history}>
            <RootPage defaultTemplate={defaultTemplate} />
        </ConnectedRouter>
    )
}
