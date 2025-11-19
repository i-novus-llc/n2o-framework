import classNames from 'classnames'
import { CSSProperties } from 'react'
import pick from 'lodash/pick'

import { BodyCell, HeaderCell } from '../../../ducks/table/Table'

type ElementAttributes = {
    className?: string
    style?: CSSProperties
}

type AttributesMap = Record<string, ElementAttributes>

type Position = 'left' | 'right'

export const mergeAttributes = <
    Target extends ElementAttributes,
    Ext extends ElementAttributes,
>(target: Target, ext: ElementAttributes) => {
    const { className = '', style = {} } = target

    return {
        ...target,
        ...ext,
        className: classNames(className, ext.className),
        style: {
            ...style,
            ...ext.style,
        },
    }
}

export const pickAttrByCell = <T extends BodyCell | HeaderCell>(
    attrsMap: AttributesMap,
    cell: T,
    fixed?: Position,
): ElementAttributes | null => {
    if (attrsMap[cell.id]) { return attrsMap[cell.id] }
    if (cell.children) {
        const position = fixed || (cell as HeaderCell).fixed

        if (!position) { return null }

        const child = position === 'left'
            ? cell.children[0]
            : cell.children.at(-1)

        if (!child) { return null }

        const attributes = pickAttrByCell(attrsMap, child as T, position)

        if (!attributes) { return null }

        return {
            className: attributes.className,
            style: pick(attributes.style, [position]),
        }
    }

    return null
}

export const mergeAttrToCells = <T extends BodyCell | HeaderCell>(
    cells: T[],
    attrsMap: AttributesMap,
): T[] => {
    const mapFn = (cell: T): T => {
        const column = pickAttrByCell(attrsMap, cell)
        const { elementAttributes = {}, children } = cell

        return {
            ...cell,
            children: children?.map(cell => mapFn(cell as T)),
            elementAttributes: column ? mergeAttributes(elementAttributes, column) : elementAttributes,
        }
    }

    return cells.map(cell => mapFn(cell))
}
