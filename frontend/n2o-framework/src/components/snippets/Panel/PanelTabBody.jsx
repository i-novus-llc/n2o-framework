import React from 'react'
import PropTypes from 'prop-types'
import { TabPane } from 'reactstrap'

/**
 * Компонент тела таба для {@link Panel}
 * @reactProps {string|number} eventKey - идентификатор для таба
 * @reactProps {node} children - элемент вставляемый в PanelTabBody
 */
export function PanelTabBody({ eventKey, children }) {
    return <TabPane tabId={eventKey}>{children}</TabPane>
}

PanelTabBody.propTypes = {
    eventKey: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
        .isRequired,
    children: PropTypes.node,
}

export default PanelTabBody
