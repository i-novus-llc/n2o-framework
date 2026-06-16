import React, { CSSProperties } from 'react'
import classNames from 'classnames'

import { type RowProps, COLUMNS, AlignItemsMap } from './types'

import '../../styles/components/Row.scss'

export function Row({
    id,
    children,
    className,
    style,
    columns = 12,
    wrap = true,
    align = 'top',
    justify = 'start',
}: RowProps) {
    const rowClasses = classNames(
        'layout-row',
        { 'nowrap': !wrap },
        className,
    )

    const styleProp: CSSProperties = {
        ...style,
        ...(columns != null && { [COLUMNS]: columns }),
        // css attr(data-*) подерживает только примитивные типы, поэтому приходится делать через стиль, а не data-*
        alignItems: AlignItemsMap[align],
        justifyContent: justify,
    }

    // data-атрибуты для пока используются только для тестов
    // TODO Выяснить актуальность этих тестов и удалить эти атрибуты, если они не нужны
    return (
        <div id={id} className={rowClasses} style={styleProp} data-align-items={align} data-justify-content={justify}>
            {children}
        </div>
    )
}
