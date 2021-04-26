import React from 'react'
import PropTypes from 'prop-types'

import withFetchData from '../withFetchData.js'
import Radio from '../Radio/Radio'
import RadioN2O from '../Radio/RadioN2O'
import RadioButton from '../Radio/RadioButton'
import Spinner from '../../snippets/Spinner/InlineSpinner'

import RadioGroup from './RadioGroup'

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

class RadioGroupControl extends React.Component {
    componentDidMount() {
        this.props._fetchData({
            size: this.props.size,
            [`sorting.${this.props.labelFieldId}`]: 'ASC',
        })
    }

    /**
   * Рендер
   */

    render() {
        const { data, labelFieldId, type, isLoading } = this.props

        const radioTypes = {
            default: Radio,
            n2o: RadioN2O,
            btn: RadioButton,
        }

        const RadioElement = radioTypes[type]

        return (
            <>
                {!isLoading && (
                    <RadioGroup {...this.props}>
                        {data &&
              data.map(radio => (
                  <RadioElement
                      key={radio.id}
                      value={radio}
                      label={radio[labelFieldId]}
                      disabled={radio.disabled}
                      checked={radio.checked}
                  />
              ))}
                    </RadioGroup>
                )}
                {isLoading && <Spinner />}
            </>
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
    fetchData: PropTypes.func,
    size: PropTypes.number.isRequired,
    type: PropTypes.oneOf(['default', 'n2o', 'btn']),
    isLoading: PropTypes.bool,
}

RadioGroupControl.defaultProps = {
    valueFieldId: 'id',
    labelFieldId: 'name',
    value: {},
    visible: true,
    type: 'default',
    onChange: () => {},
    isLoading: false,
}

export default withFetchData(RadioGroupControl)
