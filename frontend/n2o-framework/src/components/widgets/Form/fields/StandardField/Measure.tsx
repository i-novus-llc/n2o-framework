import React from 'react'

export interface Props {
    value?: string
}

/**
 * Компонент-размерность
 * @example
 * <Measure value="м"/>
 */
export const Measure = ({ value, ...props }: Props) => {
    if (!value) { return null }

    return <span style={{ marginLeft: 5 }} {...props}>{value}</span>
}

export default Measure
