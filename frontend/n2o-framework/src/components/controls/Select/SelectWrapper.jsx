import React from 'react'
import PropTypes from 'prop-types'

import withFetchData from '../withFetchData'
import Spinner from '../../snippets/Spinner/InlineSpinner'

import { Select } from './Select'
import { Option } from './Option'

/**
 * Wrapper для селекта
 * @reactProps {array} data - данные для чекбоксов
 * @reactProps {string} valueFieldId - ключ для value в data
 * @reactProps {string} labelFieldId - ключ для label в дата
 * @reactProps {string|number} value - выбранное значение
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {boolean} autoFocus - автофокус элемента
 * @reactProps {boolean} required - обязательность поля
 * @reactProps {string} heightSize - размер элемента
 * @reactProps {function} fetchData - функция для получения данных
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - размер
 * @reactProps {string} type - тип чекбокса
 */

function SelectWrapper(props) {
    const { data, valueFieldId, labelFieldId, isLoading } = props

    return (
        <>
            {!isLoading && (
                <Select {...props}>
                    {data.map(option => (
                        <Option
                            key={option.id}
                            value={option[valueFieldId]}
                            label={option[labelFieldId]}
                        />
                    ))}
                </Select>
            )}
            {isLoading && <Spinner />}
        </>
    )
}

SelectWrapper.propTypes = {
    data: PropTypes.array,
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    onChange: PropTypes.func,
    required: PropTypes.bool,
    autoFocus: PropTypes.bool,
    disabled: PropTypes.bool,
    visible: PropTypes.bool,
    heightSize: PropTypes.oneOf(['input-sm', 'input-lg', '']),
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    fetchData: PropTypes.func,
    queryId: PropTypes.string.isRequired,
    size: PropTypes.number.isRequired,
    isLoading: PropTypes.bool,
}

SelectWrapper.defaultProps = {
    value: '',
    visible: true,
    isLoading: false,
}

export default withFetchData(SelectWrapper)
