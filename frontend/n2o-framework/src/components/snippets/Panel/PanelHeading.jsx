import React from 'react'
import PropTypes from 'prop-types'
import { CardHeader } from 'reactstrap'

/**
 * Компонент шапки для {@link Panel}
 * @reactProps {node} children - вставляемый внутрь PanelHeading элемент
 */
function PanelHeading({ children }) {
    return (
        <CardHeader className="panel-block-flex panel-block-flex panel-region-heading">
            {children}
        </CardHeader>
    )
}

PanelHeading.propTypes = {
    children: PropTypes.node,
}

export default PanelHeading
