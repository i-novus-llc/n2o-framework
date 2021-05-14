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
    handleKeyDown(event) {
        const { onSearch } = this.props

        if (event.key === 'Enter') {
            onSearch()
        }
    }

    render() {
        const { placeholder, onChange, value } = this.props

        return (
            <div className="N2O-select-dropdown-search">
                <Input
                    placeholder={placeholder}
                    type="text"
                    onChange={e => onChange(e.target.value)}
                    value={value}
                    onKeyDown={e => this.handleKeyDown(e)}
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
