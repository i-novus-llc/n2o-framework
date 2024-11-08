import React, { ReactNode } from 'react'
import { CardFooter } from 'reactstrap'

/**
 * Компонент подвала {@link Panel}
 * @reactProps {node} children - вставляемый внутрь PanelFooter элемент
 */
export function PanelFooter({ children }: { children: ReactNode }) {
    return <CardFooter className="panel-region-footer">{children}</CardFooter>
}

export default PanelFooter
