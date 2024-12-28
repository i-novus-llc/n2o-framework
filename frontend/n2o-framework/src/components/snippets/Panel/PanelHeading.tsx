import React from 'react'
import { CardHeader } from 'reactstrap'

import { type CommonProps } from './types'

/**
 * Компонент шапки для {@link Panel}
 * @reactProps {node} children - вставляемый внутрь PanelHeading элемент
 */
export function PanelHeading({ children }: CommonProps) {
    return <CardHeader className="panel-block-flex panel-block-flex panel-region-heading">{children}</CardHeader>
}

export default PanelHeading
