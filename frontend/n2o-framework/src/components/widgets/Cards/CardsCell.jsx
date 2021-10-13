import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import omit from 'lodash/omit'
import classNames from 'classnames'

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
    const { component, model, dispatch, onResolve } = props
    const resolvedProps = propsResolver({
        ...omit(props, ['model', 'dispatch', 'onResolve']),
        ...omit(component, ['src']),
    }, model)

    return (
        <Factory
            src={get(component, 'src')}
            level={CELLS}
            {...resolvedProps}
            model={model}
            dispatch={dispatch}
            onResolve={onResolve}
            className={classNames('n2o-cards__cell', resolvedProps.className)}
        />
    )
}

CardsCell.propTypes = {
    className: PropTypes.string,
    style: PropTypes.string,
    model: PropTypes.object,
    component: PropTypes.object,
    onResolve: PropTypes.func,
    dispatch: PropTypes.func,
}

export default CardsCell
