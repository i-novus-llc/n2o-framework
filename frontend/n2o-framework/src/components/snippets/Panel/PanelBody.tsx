import React, { ReactNode } from 'react'
import { CardBody, TabContent } from 'reactstrap'

export interface Props {
    children: ReactNode
    id: string
    activeKey: string
    hasTabs: boolean
}

/**
 * Компонент тела {@link Panel}
 * @reactProps {string} id - id для контейнера с табами
 * @reactProps {boolean} hasTabs - флаг наличия табов
 * @reactProps {string|number} activeKey - ключ активного таба
 * @reactProps {node} children - вставляемый внутрь PanelBody элемент
 */
export function PanelBody({ children, id, activeKey, hasTabs = false }: Props) {
    const tabContainer = <TabContent id={id} activeTab={activeKey}>{children}</TabContent>

    return <CardBody>{hasTabs ? tabContainer : children}</CardBody>
}

export default PanelBody
