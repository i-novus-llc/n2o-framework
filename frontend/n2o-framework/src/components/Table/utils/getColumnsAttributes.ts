import classNames from 'classnames'
import { CSSProperties } from 'react'

import { HeaderCell } from '../../../ducks/table/Table'

export type ElementAttributes = {
    className?: string
    style?: CSSProperties
    fixed?: 'left' | 'right'
}

export type AttributesMap = Record<string, ElementAttributes>

export const getAttributesFromHeader = (cells: HeaderCell[], parentFix?: HeaderCell['fixed']) => {
    const attrsMap: AttributesMap = {}

    cells.forEach(({ children, elementAttributes, id, fixed, resizable }) => {
        if (children) {
            Object.assign(attrsMap, getAttributesFromHeader(children, parentFix || fixed))
        } else {
            const { style, className } = elementAttributes
            const width = elementAttributes.width ?? style?.width
            /*
            * для resizable колонок оставляем max-width как границу, в которой она может растягиваться
            * для остальных max указываем равным width(если он задан), иначе таблица может игнорировать просто width
            * и тянуть колонку как хочет относительно контента, а не обрезать/переносить текст в ней
            */
            const maxWidth = resizable
                ? style?.maxWidth
                : width ?? style?.maxWidth

            attrsMap[id] = {
                fixed,
                className: fixed ? classNames(`sticky-cell sticky-${fixed}`, className) : className,
                style: {
                    width,
                    minWidth: style?.minWidth ?? (resizable ? undefined : width),
                    maxWidth,
                },
            }
        }
    })

    return attrsMap
}
