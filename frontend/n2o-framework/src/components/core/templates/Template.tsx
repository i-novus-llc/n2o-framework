import React from 'react'

import { ConfigContainer } from '../../../plugins/Menu/MenuContainer'

import { Page } from './Page'
import { type TemplateProps, type ConfigContainerProps } from './types'

export function Template({ children }: TemplateProps) {
    return (
        <div className="application">
            <ConfigContainer render={(config: ConfigContainerProps) => <Page {...config}>{children}</Page>} />
        </div>
    )
}

export default Template
