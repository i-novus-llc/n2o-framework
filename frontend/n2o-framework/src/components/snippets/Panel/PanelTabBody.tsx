import React from 'react'
import { TabPane } from 'reactstrap'

import { type PanelTabBodyProps } from './types'

/**
 * Компонент тела таба для {@link Panel}
 * @reactProps {string|number} eventKey - идентификатор для таба
 * @reactProps {node} children - элемент вставляемый в PanelTabBody
 */
export function PanelTabBody({ eventKey, children }: PanelTabBodyProps) {
    return <TabPane tabId={eventKey}>{children}</TabPane>
}

export default PanelTabBody
