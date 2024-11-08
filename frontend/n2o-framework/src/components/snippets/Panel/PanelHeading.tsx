import React, { ReactNode } from 'react'
import { CardHeader } from 'reactstrap'

/**
 * Компонент шапки для {@link Panel}
 * @reactProps {node} children - вставляемый внутрь PanelHeading элемент
 */
function PanelHeading({ children }: { children: ReactNode }) {
    return <CardHeader className="panel-block-flex panel-block-flex panel-region-heading">{children}</CardHeader>
}

export default PanelHeading
