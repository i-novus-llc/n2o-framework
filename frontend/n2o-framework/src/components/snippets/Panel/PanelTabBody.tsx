import React from 'react'
import { TabPane } from 'reactstrap'
import { ReactNode } from 'react'

/**
 * Компонент тела таба для {@link Panel}
 * @reactProps {string|number} eventKey - идентификатор для таба
 * @reactProps {node} children - элемент вставляемый в PanelTabBody
 */
export function PanelTabBody({ eventKey, children }: { eventKey: string | number, children: ReactNode }) {
    return <TabPane tabId={eventKey}>{children}</TabPane>
}

export default PanelTabBody
