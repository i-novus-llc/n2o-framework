import React from 'react'

import { Select } from './Select'
import { Total as TotalProps } from './constants'

export function Total(props: TotalProps) {
    const { total, title, className, onClick, visible } = props

    if (!visible) {
        return null
    }

    if (total) {
        return <section className={`${className}__text`}>{total}</section>
    }

    return <Select title={title} onClick={onClick} className={`${className}__button`} />
}
