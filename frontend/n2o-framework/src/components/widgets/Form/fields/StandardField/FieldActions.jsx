import React from 'react'

import { FieldActionsPropTypes } from './FieldPropTypes'

/**
 * Компонент, содержащий экшены поля
 * @param  {array} actions - массив экшенов
 */
const FieldActions = ({ actions }) => (
    <div>
        {actions.map((action, i) => (
            <button key={i}>action.label</button>
        ))}
    </div>
)

FieldActions.propTypes = {
    actions: FieldActionsPropTypes,
}

FieldActions.defaultProps = {
    actions: [],
}

export default FieldActions
