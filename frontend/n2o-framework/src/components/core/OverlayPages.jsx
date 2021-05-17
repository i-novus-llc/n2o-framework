import React from 'react'
import PropTypes from 'prop-types'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'
import map from 'lodash/map'
import has from 'lodash/has'
import defaultTo from 'lodash/defaultTo'

import { closeOverlay, hidePrompt } from '../../actions/overlays'
import { overlaysSelector } from '../../selectors/overlays'

// eslint-disable-next-line import/no-cycle
import DrawerPage from './DrawerPage'
import PageDialog from './PageDialog'
// eslint-disable-next-line import/no-cycle
import ModalPage from './ModalPage'

const ModalMode = {
    MODAL: 'modal',
    DRAWER: 'drawer',
    DIALOG: 'dialog',
}

const PageComponent = {
    [ModalMode.MODAL]: ModalPage,
    [ModalMode.DRAWER]: DrawerPage,
    [ModalMode.DIALOG]: PageDialog,
}

const prepareProps = (props, overlay = {}) => ({ ...props, ...overlay, ...defaultTo(overlay.props, {}) })

const renderOverlays = ({ overlays, ...rest }) => map(
    overlays,
    ({ mode, ...overlay }) => has(PageComponent, mode) &&
      React.createElement(PageComponent[mode], prepareProps(rest, overlay)),
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
