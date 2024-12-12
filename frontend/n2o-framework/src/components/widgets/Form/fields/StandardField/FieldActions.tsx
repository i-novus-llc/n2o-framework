import React from 'react'

export interface Props {
    actions?: Array<{ label: string }>
}

/**
 * Компонент, содержащий экшены поля
 */
export const FieldActions = ({ actions = [] }: Props) => (
    <div>
        {actions.map((action, i) => (
            // eslint-disable-next-line react/no-array-index-key
            <button type="button" key={i}>{action.label}</button>
        ))}
    </div>
)

export default FieldActions
