import React, { useContext } from 'react'
import PropTypes from 'prop-types'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'
import map from 'lodash/map'
import has from 'lodash/has'
import defaultTo from 'lodash/defaultTo'

import { closeOverlay, hidePrompt } from '../../ducks/overlays/store'
import { overlaysSelector } from '../../ducks/overlays/selectors'
import { FactoryContext } from '../../core/factory/context'
import { OVERLAYS } from '../../core/factory/factoryLevels'

const ModalMode = {
    MODAL: 'modal',
    DRAWER: 'drawer',
    DIALOG: 'dialog',
}

const PageComponent = {
    [ModalMode.MODAL]: 'Modal',
    [ModalMode.DRAWER]: 'Drawer',
    [ModalMode.DIALOG]: 'Dialog',
}

const prepareProps = (props, overlay = {}) => ({ ...props, ...overlay, ...defaultTo(overlay.props, {}) })

const renderOverlays = ({ overlays, ...rest }) => map(
    overlays,
    ({ mode, ...overlay }) => {
        if (!has(PageComponent, mode)) { return null }

        const { getComponent } = useContext(FactoryContext)
        const Overlay = getComponent(PageComponent[mode], OVERLAYS)

        return React.createElement(Overlay, prepareProps(rest, overlay))
    },
)

/**
 * Компонент, отображающий все оверлейные окна
 * @reactProps {object} overlays - Массив объектов (из Redux)
 * @example
 *  <OverlayPages/>
 */
function OverlayPages(props) {
    return <div className="n2o-overlay-pages">{renderOverlays(props)}</div>
}

const mapStateToProps = createStructuredSelector({
    overlays: state => overlaysSelector(state),
})

const mapDispatchToProps = dispatch => ({
    close: (name, prompt) => {
        dispatch(closeOverlay(name, prompt))
    },
    hidePrompt: (name) => {
        dispatch(hidePrompt(name))
    },
})

OverlayPages.propTypes = {
    // eslint-disable-next-line react/no-unused-prop-types
    overlays: PropTypes.array,
}

OverlayPages.defaultProps = {
    overlays: {},
}

export { OverlayPages }
export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(OverlayPages)
