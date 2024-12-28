import React from 'react'
import { CardFooter } from 'reactstrap'

import { type CommonProps } from './types'

/**
 * Компонент подвала {@link Panel}
 * @reactProps {node} children - вставляемый внутрь PanelFooter элемент
 */
export function PanelFooter({ children }: CommonProps) {
    return <CardFooter className="panel-region-footer">{children}</CardFooter>
}

export default PanelFooter
