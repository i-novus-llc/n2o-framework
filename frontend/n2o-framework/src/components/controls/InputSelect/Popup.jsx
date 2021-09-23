import React, { createRef } from 'react'
import PropTypes from 'prop-types'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import Dropdown from 'reactstrap/lib/Dropdown'
import classNames from 'classnames'
import { pure } from 'recompose'

/**
 * Компонент попапа для {@link InputSelect}
 * @reactProps {boolean} isExpanded - флаг видимости попапа
 * @reactProps {node} children - флаг видимости попапа
 * @reactProps {string} expandPopUp - флаг видимости попапа
 */

class Popup extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            direction: 'down',
        }

        this.popUpListRef = createRef()
    }

    componentDidUpdate(prevProps) {
        const { isExpanded } = this.props

        if (isExpanded && prevProps.isExpanded !== isExpanded) {
            this.updateDirection()
        }
    }

    updateDirection = () => {
        const { inputSelect } = this.props

        if (!inputSelect) {
            return null
        }

        const documentHeight = window.innerHeight ||
                document.documentElement.clientHeight ||
                document.body.clientHeight

        if (documentHeight - inputSelect.getBoundingClientRect().bottom < this.popUpListRef.current.offsetHeight) {
            this.setState({
                direction: 'up',
            })
        } else {
            this.setState({
                direction: 'down',
            })
        }

        return null
    }

    /**
     * Рендер
     */

    render() {
        const { isExpanded, children, expandPopUp, inputSelect } = this.props
        const { direction } = this.state

        const inputSelectHeight = inputSelect ? inputSelect.offsetHeight : null
        const style = inputSelectHeight && direction === 'up'
            ? { marginBottom: `${inputSelectHeight + 2}px` }
            : {}

        return (
            <Dropdown direction={direction}>
                <DropdownMenu
                    className={classNames(
                        'dropdown-menu',
                        'n2o-select-pop-up',
                        {
                            'd-block': isExpanded,
                            'd-none': !isExpanded,
                            expandPopUp,
                        },
                    )}
                    style={style}
                    flip
                >
                    <div ref={this.popUpListRef}>{children}</div>
                </DropdownMenu>
            </Dropdown>
        )
    }
}

Popup.propTypes = {
    isExpanded: PropTypes.bool.isRequired,
    children: PropTypes.node,
    expandPopUp: PropTypes.string,
    inputSelect: PropTypes.any,
}

export default pure(Popup)
