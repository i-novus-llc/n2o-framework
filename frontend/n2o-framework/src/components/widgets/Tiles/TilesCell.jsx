import React from 'react'
import omit from 'lodash/omit'
import get from 'lodash/get'
import classNames from 'classnames'

import { Factory } from '../../../core/factory/Factory'
import { CELLS } from '../../../core/factory/factoryLevels'
import { useResolved } from '../../../core/Expression/useResolver'
/**
 * Строка карточки
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {object} model - Модель
 */
function TilesCell(props) {
    const { component, model, style, className } = props
    const resolvedProps = useResolved(omit(component, ['src']), model)

    return (
        <div className="w-100 d-flex justify-content-center" style={style}>
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

export default TilesCell
