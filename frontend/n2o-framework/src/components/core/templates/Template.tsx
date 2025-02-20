import React from 'react'

import { ConfigContainer, type ConfigContainerProps } from '../../../plugins/Menu/MenuContainer'

import { Page } from './Page'
import { type TemplateProps } from './types'

export function Template({ children }: TemplateProps) {
    return (
        <div className="application">
            <ConfigContainer render={(config: ConfigContainerProps) => <Page {...config}>{children}</Page>} />
        </div>
    )
}

export default Template
