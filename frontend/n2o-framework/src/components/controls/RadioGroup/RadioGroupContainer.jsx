import React from 'react'
import PropTypes from 'prop-types'

import mapProp from '../../../utils/mapProp'
import { Group as RadioGroup } from '../Radio/Group'

/**
 * Контейнер для RadioGroup(пропсы прокидывает)
 * @reactProps {object} radioGroup - пропсы для RadioGroup
 * @reactProps {array} radios -  пропсы для Radio
 * FIXME тут скорее всего упадёт из-за того что пропсы новые не передал правильно,
 *  но я не нашёл кто использует это и как, поэтому пока оставлю как есть %)
 */
function RadioGroupContainer({ radioGroup, radios, ...props }) {
    return (
        <RadioGroup
            {...radioGroup}
            {...props}
            options={mapProp(radios)}
        />
    )
}

RadioGroupContainer.propTypes = {
    radioGroup: PropTypes.object,
    radios: PropTypes.array,
}

RadioGroupContainer.defaultProps = {
    radios: [],
}

export default RadioGroupContainer
