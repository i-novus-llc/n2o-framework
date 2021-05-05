import React from 'react'
import PropTypes from 'prop-types'
import omit from 'lodash/omit'
import get from 'lodash/get'

import Factory from '../../../core/factory/Factory'
import { CELLS } from '../../../core/factory/factoryLevels'
import propsResolver from '../../../utils/propsResolver'
/**
 * Строка карточки
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {object} model - Модель
 */
function TilesCell(props) {
    const { component, model } = props

    const getPassProps = () => omit(props, ['component', 'model'])

    return (
        <Factory
            src={get(component, 'src')}
            level={CELLS}
            model={model}
            {...propsResolver(omit(component, ['src']), model)}
            {...getPassProps()}
        />
    )
}

TilesCell.propTypes = {
    className: PropTypes.string,
    style: PropTypes.string,
    model: PropTypes.object,
    component: PropTypes.object,
}

export default TilesCell
