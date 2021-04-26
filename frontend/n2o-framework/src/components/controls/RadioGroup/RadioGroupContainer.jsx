import React from 'react'
import PropTypes from 'prop-types'

import Radio from '../Radio/RadioN2O'
import mapProp from '../../../utils/mapProp'

import RadioGroup from './RadioGroup'

/**
 * Контейнер для RadioGroup(пропсы прокидывает)
 * @reactProps {object} radioGroup - пропсы для RadioGroup
 * @reactProps {array} radios -  пропсы для Radio
 */
class RadioGroupContainer extends React.Component {
    /**
   * Базовый рендер
   * */
    render() {
        const { radioGroup, radios, ...props } = this.props
        return (
            <RadioGroup {...radioGroup} {...props}>
                {mapProp(radios).map(radio => (
                    <Radio {...radio} />
                ))}
            </RadioGroup>
        )
    }
}

RadioGroupContainer.propTypes = {
    radioGroup: PropTypes.object,
    radios: PropTypes.array,
}

RadioGroupContainer.defaultProps = {
    radios: [],
}

export default RadioGroupContainer
