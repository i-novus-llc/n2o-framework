import React, { Component } from 'react'
import PropTypes from 'prop-types'
import uniqueId from 'lodash/uniqueId'
import toString from 'lodash/toString'
import { isEqual } from 'lodash'

import { Group, RadioTypes } from '../Radio/Group'
import Spinner from '../../snippets/Spinner/InlineSpinner'
import withFetchData from '../withFetchData'

/**
 * Wrapper для радиогруппы
 * @reactProps {array} data - данные для чекбоксов
 * @reactProps {string} valueFieldId - ключ для value в data
 * @reactProps {string} labelFieldId - ключ для label в дата
 * @reactProps {string|number} value - выбранное значение
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {boolean} disabled - только для чтения
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {boolean} inline - флаг вывода в ряд
 * @reactProps {object} style - стили группы
 * @reactProps {string} className - класс группы
 * @reactProps {function} fetchData - функция для получения данных
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - размер
 * @reactProps {string} type - тип чекбокса
 */
class RadioGroupControl extends Component {
    constructor(props) {
        super(props)
        this.state = {
            groupName: uniqueId('n2o-radio-group-control'),
        }

        const { _fetchData, size, labelFieldId } = this.props

        _fetchData({
            size,
            [`sorting.${labelFieldId}`]: 'ASC',
        })
    }

    componentDidUpdate(prevProps) {
        const { _fetchData, size, labelFieldId } = this.props

        if (!isEqual(size, prevProps.size) || !isEqual(labelFieldId, prevProps.labelFieldId)) {
            _fetchData({
                size,
                [`sorting.${labelFieldId}`]: 'ASC',
            })
        }
    }

    changeHandler = (value) => {
        const { data, valueFieldId, onChange } = this.props
        const item = data.find(item => toString(item[valueFieldId]) === toString(value))

        return onChange(item)
    }

    static getDerivedStateFromProps({ data, valueFieldId, labelFieldId }) {
        return {
            options: data ? data.map(radio => ({
                value: radio[valueFieldId],
                label: radio[labelFieldId],
                disabled: radio.disabled,
            })) : [],
        }
    }

    render() {
        const {
            type,
            isLoading,
            inline,
            className,
            value,
            valueFieldId,
            disabled,
            visible,
            style,
        } = this.props
        const { groupName, options } = this.state

        if (isLoading) {
            return <Spinner />
        }

        return (
            <Group
                value={value?.[valueFieldId]}
                onChange={this.changeHandler}
                disabled={disabled}
                visible={visible}
                style={style}
                className={className}
                name={groupName}
                inline={inline}
                type={type}
                options={options}
            />
        )
    }
}

RadioGroupControl.propTypes = {
    data: PropTypes.array,
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
    inline: PropTypes.bool,
    onChange: PropTypes.func,
    disabled: PropTypes.bool,
    visible: PropTypes.bool,
    style: PropTypes.object,
    className: PropTypes.string,
    size: PropTypes.number,
    type: PropTypes.oneOf([RadioTypes.button, RadioTypes.input, RadioTypes.tabs]),
    isLoading: PropTypes.bool,
    _fetchData: PropTypes.func,
}

RadioGroupControl.defaultProps = {
    valueFieldId: 'id',
    labelFieldId: 'name',
    value: {},
    visible: true,
    type: RadioTypes.input,
    onChange: () => {},
    isLoading: false,
}

export default withFetchData(RadioGroupControl)
