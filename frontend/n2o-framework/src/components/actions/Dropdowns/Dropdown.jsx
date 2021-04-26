import React from 'react'
import PropTypes from 'prop-types'
import ButtonDropdown from 'reactstrap/lib/ButtonDropdown'
import DropdownToggle from 'reactstrap/lib/DropdownToggle'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'

/**
 * Дропдаун
 * @reactProps {string} id
 * @reactProps {node} children - элкменты меню дропдауна
 * @reactProps {string} color - цвет кнопки дропдауна
 * @reactProps {string} title - заголовок кнопки
 */
class Dropdown extends React.Component {
    constructor(props) {
        super(props)

        this.toggle = this.toggle.bind(this)
        this.state = {
            dropdownOpen: false,
        }
    }

    /**
   * смена видимости меню дропдауна
   */
    toggle(e) {
        this.setState({
            dropdownOpen: !this.state.dropdownOpen,
        })
    }

    /**
   * Базовый рендер
   */
    render() {
        const { color, title, size, children, disabled } = this.props

        return (
            <ButtonDropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
                <DropdownToggle
                    caret
                    color={color}
                    size={size}
                    id={this.props.id}
                    disabled={disabled}
                >
                    {title}
                </DropdownToggle>
                <DropdownMenu>{children}</DropdownMenu>
            </ButtonDropdown>
        )
    }
}

Dropdown.propTypes = {
    color: PropTypes.string,
    size: PropTypes.string,
    title: PropTypes.node,
    disabled: PropTypes.bool,
    id: PropTypes.string,
    children: PropTypes.node,
}

Dropdown.defaultProps = {
    disabled: false,
}

export default Dropdown
