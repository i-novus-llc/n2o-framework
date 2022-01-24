import React from 'react'
import PropTypes from 'prop-types'
import { CardFooter } from 'reactstrap'

/**
 * Компонент подвала {@link Panel}
 * @reactProps {node} children - вставляемый внутрь PanelFooter элемент
 */
export function PanelFooter({ children }) {
    return <CardFooter className="panel-region-footer">{children}</CardFooter>
}

PanelFooter.propTypes = {
    children: PropTypes.node,
}

export default PanelFooter
