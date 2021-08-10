import React from 'react'
import PropTypes from 'prop-types'
import omit from 'lodash/omit'
import get from 'lodash/get'
import classNames from 'classnames'

import { Factory } from '../../../core/factory/Factory'
import { CELLS } from '../../../core/factory/factoryLevels'
import propsResolver from '../../../utils/propsResolver'
/**
 * Строка карточки
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {object} model - Модель
 */
function TilesCell(props) {
    const { component, model, style, className } = props
    const resolvedProps = propsResolver(omit(component, ['src']), model)

    return (
        <div style={style}>
            <Factory
                src={get(component, 'src')}
                level={CELLS}
                model={model}
                {...resolvedProps}
                {...omit(props, ['component', 'model'])}
                className={classNames('n2o-cards__cell', resolvedProps.className, className)}
            />
        </div>
    )
}

TilesCell.propTypes = {
    className: PropTypes.string,
    style: PropTypes.string,
    model: PropTypes.object,
    component: PropTypes.object,
}

export default TilesCell
