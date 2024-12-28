import React from 'react'
import { CardBody, TabContent } from 'reactstrap'

import { type PanelBodyProps } from './types'

/**
 * Компонент тела {@link Panel}
 * @reactProps {string} id - id для контейнера с табами
 * @reactProps {boolean} hasTabs - флаг наличия табов
 * @reactProps {string|number} activeKey - ключ активного таба
 * @reactProps {node} children - вставляемый внутрь PanelBody элемент
 */
export function PanelBody({ children, id, activeKey, hasTabs = false }: PanelBodyProps) {
    if (hasTabs) {
        return <CardBody><TabContent id={id} activeTab={activeKey}>{children}</TabContent></CardBody>
    }

    return <CardBody>{children}</CardBody>
}

export default PanelBody
