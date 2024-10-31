import React, { useContext } from 'react'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'
import map from 'lodash/map'
import defaultTo from 'lodash/defaultTo'

import { closeOverlay, hidePrompt } from '../../ducks/overlays/store'
import { overlaysSelector } from '../../ducks/overlays/selectors'
import { FactoryContext } from '../../core/factory/context'
import { OVERLAYS } from '../../core/factory/factoryLevels'

/* TODO OverlaysRefactoring сделать 1 уровневым после перехода overlays на универсальный action insert */
const TYPES = {
    confirm: {
        modal: 'ConfirmDialog',
        popover: 'ConfirmPopover',
    },
    page: {
        modal: 'Modal',
        drawer: 'Drawer',
        dialog: 'Dialog',
    },
}

const prepareProps = (props, overlay = {}) => ({ ...props, ...overlay, ...defaultTo(overlay.props, {}) })

const renderOverlays = ({ overlays, ...rest }) => map(
    overlays,
    ({ type, mode, ...overlay }) => {
        const overlayType = TYPES[type]

        if (!overlayType) {
            return null
        }

        const { getComponent } = useContext(FactoryContext)

        const Overlay = overlayType[mode]
        const Component = getComponent(Overlay, OVERLAYS)

        return React.createElement(Component, prepareProps(rest, overlay))
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

OverlayPages.defaultProps = {
    overlays: {},
}

export { OverlayPages }
export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(OverlayPages)
