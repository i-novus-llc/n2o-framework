import React from 'react'
import PropTypes from 'prop-types'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import classNames from 'classnames'
import { pure } from 'recompose'

/**
 * Компонент попапа для {@link InputSelect}
 * @reactProps {boolean} isExpanded - флаг видимости попапа
 * @reactProps {node} children - флаг видимости попапа
 * @reactProps {string} expandPopUp - флаг видимости попапа
 */

class Popup extends React.Component {
    displayTop() {
        const documentHeight = window.innerHeight ||
            document.documentElement.clientHeight ||
            document.body.clientHeight
        const { inputSelect } = this.props

        return (
            inputSelect &&
            documentHeight - inputSelect.getBoundingClientRect().bottom < this.popUp.offsetHeight
        )
    }

    /**
     * Рендер
     */
    render() {
        const { isExpanded, children, expandPopUp, flip } = this.props

        return (
            <DropdownMenu
                className={classNames('dropdown-menu', 'n2o-select-pop-up', {
                    'drop-up': this.displayTop(),
                    expandPopUp,
                })}
                flip={flip}
                ref={(el) => {
                    this.popUp = el
                }}
                style={{
                    display: isExpanded ? 'block' : 'none',
                }}
            >
                {children}
            </DropdownMenu>
        )
    }
}

Popup.propTypes = {
    isExpanded: PropTypes.bool.isRequired,
    children: PropTypes.node,
    expandPopUp: PropTypes.string,
    flip: PropTypes.bool,
    inputSelect: PropTypes.any,
}

export default pure(Popup)
