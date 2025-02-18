import React, { ReactNode } from 'react'
// @ts-ignore import from js file
import { Panel as PanelBody } from 'rc-collapse'

import { Text } from '../../Typography/Text'

export type Props = {
    className: string
    header: JSX.Element
    headerClass: string
    children: ReactNode
}

export function Panel({ children, className, header, headerClass, ...rest }: Props) {
    return (
        <PanelBody
            header={<Text>{header}</Text>}
            className={className}
            headerClass={headerClass}
            {...rest}
        >
            {children}
        </PanelBody>
    )
}

Panel.displayName = '@n2o-components/display/Panel'
