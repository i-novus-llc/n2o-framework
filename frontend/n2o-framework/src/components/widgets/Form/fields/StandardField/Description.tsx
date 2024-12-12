import React from 'react'

export interface Props {
    value?: string
}

/**
 * Описание поля
 * @example
 * <Description value='Введите номер телефона'/>
 */

export const Description = ({ value, ...props }: Props) => (
    <div {...props} style={{ fontSize: '0.8em' }}>{value}</div>
)

export default Description
