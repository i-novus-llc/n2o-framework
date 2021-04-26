import React from 'react'
import PropTypes from 'prop-types'
import Input from 'reactstrap/lib/Input'

/**
 * N2OSelectInput
 * @reactProps {function} onChange - callback при изменение инпута
 * @reactProps {string} placeholder - placeholder
 * @reactProps {function} onSearch - callback при нажатии на кнопку поиска
 * @reactProps {string} value - значение инпута
 */

class N2OSelectInput extends React.Component {
    _handleKeyDown(event) {
        if (event.key === 'Enter') {
            this.props.onSearch()
        }
    }

    /**
   * Рендер
   */

    render() {
        return (
            <div className="N2O-select-dropdown-search">
                <Input
                    placeholder={this.props.placeholder}
                    type="text"
                    onChange={e => this.props.onChange(e.target.value)}
                    value={this.props.value}
                    onKeyDown={e => this._handleKeyDown(e)}
                />
                {/* <InputGroupAddon */}
                {/* addonType="append" */}
                {/* onClick={this.props.onSearch} */}
                {/* style={{ cursor: 'pointer' }} */}
                {/* > */}
                {/* <InputGroupText> */}
                {/* <i className="fa fa-search" /> */}
                {/* </InputGroupText> */}
                {/* </InputGroupAddon> */}
            </div>
        )
    }
}

N2OSelectInput.propTypes = {
    onChange: PropTypes.func,
    placeholder: PropTypes.string,
    onSearch: PropTypes.func,
    value: PropTypes.string,
}

export default N2OSelectInput
