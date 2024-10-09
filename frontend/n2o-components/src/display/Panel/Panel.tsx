import React, { ReactNode } from 'react'
// @ts-ignore import from js file
import { Panel as PanelBody } from 'rc-collapse'

export type Props = {
    className: string
    header: JSX.Element
    headerClass: string
    children: ReactNode
}

export function Panel({ children, className, header, headerClass, ...rest }: Props) {
    return (
        <PanelBody
            header={header}
            className={className}
            headerClass={headerClass}
            {...rest}
        >
            {children}
        </PanelBody>
    )
}
