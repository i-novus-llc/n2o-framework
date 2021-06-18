import React from 'react'

import { FieldActionsPropTypes } from './FieldPropTypes'

/**
 * Компонент, содержащий экшены поля
 * @param  {array} actions - массив экшенов
 */
const FieldActions = ({ actions }) => (
    <div>
        {actions.map((action, i) => (
            // eslint-disable-next-line react/no-array-index-key
            <button type="button" key={i}>action.label</button>
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
