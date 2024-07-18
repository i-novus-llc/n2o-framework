import React from 'react'
import PropTypes from 'prop-types'

// eslint-disable-next-line import/no-named-as-default
import SideBar from './SideBar'

/**
 * Компонент контейнер для {@link SideBar}
 */
export default class SidebarContainer extends React.Component {
    onMouseEnter = () => {
        const { defaultState, toggledState, onMouseEnter, controlled } = this.props
        const isStaticView = defaultState === toggledState

        if (isStaticView || !controlled) {
            return
        }

        if (typeof onMouseEnter === 'function') { onMouseEnter() }
    }

    onMouseLeave = () => {
        const { defaultState, toggledState, onMouseLeave, controlled } = this.props
        const isStaticView = defaultState === toggledState

        if (isStaticView || !controlled) {
            return
        }

        if (typeof onMouseLeave === 'function') { onMouseLeave() }
    }

    render() {
        const { defaultState, toggledState } = this.props
        const isStaticView = defaultState === toggledState

        return (
            <SideBar
                {...this.props}
                isStaticView={isStaticView}
                onMouseEnter={this.onMouseEnter}
                onMouseLeave={this.onMouseLeave}
            />
        )
    }
}

SidebarContainer.propTypes = {
    items: PropTypes.array,
    defaultState: PropTypes.string,
    toggledState: PropTypes.string,
    onMouseEnter: PropTypes.func,
    onMouseLeave: PropTypes.func,
    controlled: PropTypes.bool,

}

export const SimpleSidebar = SidebarContainer
