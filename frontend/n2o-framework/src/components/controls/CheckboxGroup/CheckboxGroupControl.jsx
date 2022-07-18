import React from 'react'
import PropTypes from 'prop-types'
import { isNil } from 'lodash'

import withFetchData from '../withFetchData'
import { CheckboxN2OWrapped } from '../Checkbox/CheckboxN2O'
import CheckboxButton from '../Checkbox/CheckboxButton'
import Spinner from '../../snippets/Spinner/InlineSpinner'

import CheckboxGroup from './CheckboxGroup'

/**
 * Wrapper для группы чекбоксов
 * @reactProps {array} data - данные для чекбоксов
 * @reactProps {string} valueFieldId - ключ для value в data
 * @reactProps {string} labelFieldId - ключ для label в дата
 * @reactProps {string} enabledFieldId - ключ для enabled в data
 * @reactProps {array} value - выбранное значение
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
 * @reactProps {boolean} isLoading - флаг готовности
 */

export class CheckboxGroupControl extends React.Component {
    componentDidMount() {
        const { _fetchData, size, labelFieldId } = this.props

        _fetchData({
            size,
            [`sorting.${labelFieldId}`]: 'ASC',
        })
    }

    render() {
        const { data, labelFieldId, valueFieldId, enabledFieldId, type, isLoading } = this.props

        const checkboxTypes = {
            default: CheckboxN2OWrapped,
            n2o: CheckboxN2OWrapped,
            btn: CheckboxButton,
        }

        const CheckboxElement = checkboxTypes[type]

        return (
            <>
                {!isLoading && (
                    <CheckboxGroup {...this.props}>
                        {data &&
                            data.map((checkbox) => {
                                const isDisabled = isNil(checkbox[enabledFieldId])
                                    ? checkbox.disabled
                                    : !checkbox[enabledFieldId]

                                return (
                                    <CheckboxElement
                                        key={checkbox[valueFieldId]}
                                        value={checkbox}
                                        label={checkbox[labelFieldId]}
                                        disabled={isDisabled}
                                        checked={checkbox.checked}
                                    />
                                )
                            })}
                    </CheckboxGroup>
                )}
                {isLoading && <Spinner />}
            </>
        )
    }
}

CheckboxGroupControl.propTypes = {
    data: PropTypes.array,
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    enabledFieldId: PropTypes.string,
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
    onChange: PropTypes.func,
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    disabled: PropTypes.bool,
    visible: PropTypes.bool,
    inline: PropTypes.bool,
    style: PropTypes.object,
    className: PropTypes.string,
    fetchData: PropTypes.func,
    queryId: PropTypes.string,
    size: PropTypes.number.isRequired,
    type: PropTypes.oneOf(['default', 'n2o', 'btn']),
    isLoading: PropTypes.bool,
    _fetchData: PropTypes.func,
}

CheckboxGroupControl.defaultProps = {
    data: [],
    value: [],
    visible: true,
    type: 'default',
    onChange: () => {},
    onFocus: () => {},
    onBlur: () => {},
    isLoading: false,
    valueFieldId: 'id',
    labelFieldId: 'label',
    _fetchData: () => {},
}

export default withFetchData(CheckboxGroupControl)
