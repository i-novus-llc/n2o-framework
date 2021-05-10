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
    state = {
        dropdownOpen: false,
    }

    /**
   * смена видимости меню дропдауна
   */
    toggle = () => {
        const { dropdownOpen } = this.state

        this.setState({
            dropdownOpen: !dropdownOpen,
        })
    }

    /**
     * Базовый рендер
     */
    render() {
        const { color, title, size, children, disabled, id } = this.props
        const { dropdownOpen } = this.state

        return (
            <ButtonDropdown isOpen={dropdownOpen} toggle={this.toggle}>
                <DropdownToggle
                    caret
                    color={color}
                    size={size}
                    id={id}
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
