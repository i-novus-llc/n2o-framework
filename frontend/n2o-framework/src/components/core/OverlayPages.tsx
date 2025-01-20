import React, { ComponentType, useContext } from 'react'
import { connect } from 'react-redux'
import map from 'lodash/map'
import defaultTo from 'lodash/defaultTo'
import get from 'lodash/get'
import { Dispatch } from 'redux'

import { closeOverlay, hidePrompt } from '../../ducks/overlays/store'
import { overlaysSelector } from '../../ducks/overlays/selectors'
import { FactoryContext } from '../../core/factory/context'
import { FactoryLevels } from '../../core/factory/factoryLevels'
import { type State } from '../../ducks/State'
import { type State as OverlaysState, type Overlay } from '../../ducks/overlays/Overlays'

export interface OverlayPagesProps {
    // eslint-disable-next-line react/no-unused-prop-types
    overlays: OverlaysState
}

type OverlayType = 'ConfirmDialog' | 'ConfirmPopover' | 'Modal' | 'Drawer' | 'Dialog'

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

const prepareProps = (props: Record<string, unknown>, overlay: Partial<Overlay> = {}) => ({
    ...props,
    ...overlay,
    ...defaultTo(overlay.props, {}),
})

const renderOverlays = ({ overlays = [], ...rest }: OverlayPagesProps) => map(
    overlays,
    ({ type, mode, ...overlay }) => {
        const overlayType = TYPES[type]

        if (!overlayType) { return null }

        const { getComponent } = useContext(FactoryContext)

        const Overlay = get(overlayType, mode) as OverlayType
        const Component = getComponent(Overlay, FactoryLevels.OVERLAYS) as ComponentType<Overlay>

        return React.createElement(Component, prepareProps(rest as Record<string, unknown>, overlay) as Overlay)
    },
)

/**
 * Компонент, отображающий все оверлейные окна
 * @reactProps {object} overlays - Массив объектов (из Redux)
 * @example
 *  <OverlayPages/>
 */
function OverlayPages(props: OverlayPagesProps) {
    return <div className="n2o-overlay-pages">{renderOverlays(props)}</div>
}

const mapStateToProps = (state: State) => ({ overlays: overlaysSelector(state) })

const mapDispatchToProps = (dispatch: Dispatch) => ({
    close: (name: string, prompt: boolean) => { dispatch(closeOverlay(name, prompt)) },
    hidePrompt: (name: string) => { dispatch(hidePrompt(name)) },
})

export { OverlayPages }
export default connect(mapStateToProps, mapDispatchToProps)(OverlayPages)
