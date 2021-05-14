import React from 'react'
import PropTypes from 'prop-types'
import omit from 'lodash/omit'
import get from 'lodash/get'
import pick from 'lodash/pick'

import { Factory } from '../../../core/factory/Factory'
import { CELLS } from '../../../core/factory/factoryLevels'
import propsResolver from '../../../utils/propsResolver'
/**
 * Строка Cards
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {object} model - Модель
 */
function CardsCell(props) {
    const { component, model } = props

    const getPassProps = () => ({
        ...omit(props, ['model', 'dispatch', 'onResolve']),
        ...omit(component, ['src']),
    })

    return (
        <Factory
            src={get(component, 'src')}
            level={CELLS}
            model={model}
            {...propsResolver(getPassProps(), model)}
            {...pick(props, ['dispatch', 'onResolve'])}
        />
    )
}

CardsCell.propTypes = {
    className: PropTypes.string,
    style: PropTypes.string,
    model: PropTypes.object,
    component: PropTypes.object,
}

export default CardsCell
