import React, { Component } from 'react'
import PropTypes from 'prop-types'

import Pills from './Pills'
import { convertData, prepareValue } from './utils'

export class PillsContainer extends Component {
    constructor(props) {
        super(props)
        this.state = {
            value: [],
        }

        this.changeAndSetState = this.changeAndSetState.bind(this)
        this.handleOnClick = this.handleOnClick.bind(this)
    }

    static getDerivedStateFromState(nextProps, prevState) {
        if (nextProps.value !== prevState.value) {
            return {
                value: nextProps.value,
            }
        }

        return null
    }

    componentDidMount() {
        const { _fetchData } = this.props

        _fetchData()
    }

    changeAndSetState(value) {
        const { onChange } = this.props

        this.setState({ value })
        onChange(value)
    }

    handleOnClick(_, id) {
        const { value } = this.state
        const newValue = prepareValue(value, id, this.props)

        this.changeAndSetState(newValue)
    }

    render() {
        const { data, ...rest } = this.props
        const { value } = this.state

        return (
            <Pills
                data={convertData(value, data, rest)}
                onClick={this.handleOnClick}
            />
        )
    }
}

PillsContainer.propTypes = {
    /**
   * Ключ label в данных
   */
    labelFieldId: PropTypes.string,
    /**
   * Ключ value в данных
   */
    valueFieldId: PropTypes.string,
    /**
   * Данные
   */
    data: PropTypes.array,
    /**
   * Мульти выбор
   */
    multiSelect: PropTypes.bool,
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    _fetchData: PropTypes.func,
}

PillsContainer.defaultProps = {
    labelFieldId: 'label',
    valueFieldId: 'value',
    data: [],
    multiSelect: false,
    onChange: () => {},
    _fetchData: () => {},
}

export default PillsContainer
