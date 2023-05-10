import React from 'react'

import { Select } from './Select'
import { ITotal } from './constants'

export function Total(props: ITotal) {
    const { total, title, className, onClick, visible } = props

    if (!visible) {
        return null
    }

    if (total) {
        return <section className={`${className}__text`}>{total}</section>
    }

    return <Select title={title} onClick={onClick} className={`${className}__button`} />
}
