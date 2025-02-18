import React from 'react'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

export interface Props {
    value?: string
}

/**
 * Описание поля
 * @example
 * <Description value='Введите номер телефона'/>
 */

export const Description = ({ value, ...props }: Props) => (
    <div {...props} style={{ fontSize: '0.8em' }}><Text>{value}</Text></div>
)

export default Description
