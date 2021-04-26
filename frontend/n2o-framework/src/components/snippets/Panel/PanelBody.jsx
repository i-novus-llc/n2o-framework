import React from 'react'
import PropTypes from 'prop-types'
import CardBody from 'reactstrap/lib/CardBody'
import TabContent from 'reactstrap/lib/TabContent'

/**
 * Компонент тела {@link Panel}
 * @reactProps {string} id - id для контейнера с табами
 * @reactProps {boolean} hasTabs - флаг наличия табов
 * @reactProps {string|number} activeKey - ключ активного таба
 * @reactProps {node} children - вставляемый внутрь PanelBody элемент
 */
function PanelBody({ id, activeKey, hasTabs, children }) {
    const tabContainer = (
        <TabContent id={id} activeTab={activeKey}>
            {children}
        </TabContent>
    )
    const element = () => (hasTabs ? tabContainer : children)

    return <CardBody>{element()}</CardBody>
}

PanelBody.propTypes = {
    id: PropTypes.string,
    hasTabs: PropTypes.bool,
    activeKey: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    children: PropTypes.node,
}

PanelBody.defaultProps = {
    hasTabs: false,
}

export default PanelBody
