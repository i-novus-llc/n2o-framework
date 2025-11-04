import React, { useEffect, useMemo, useRef, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import { BodyCell, HeaderCell } from '../../../ducks/table/Table'

export type Props = {
    cells: {
        body: BodyCell[]
        header: HeaderCell[]
    }
    hasSelection: boolean
}

type Position = 'left' | 'right'
type Offset = { left: number, right: number }

const mapCells = <T extends BodyCell | HeaderCell>(
    cells: T[],
    cellsFixedPosition: Record<string, Position>,
    offsets: Record<string, Offset>,
): T[] => {
    const getOffset = (cell: T, position: Position): Offset => {
        if (offsets[cell.id]) { return offsets[cell.id] }
        if (cell.children) {
            const child = position === 'left'
                ? cell.children[0]
                : cell.children.at(-1)

            if (child) { return getOffset(child as T, position) }
        }

        return { left: 0, right: 0 }
    }

    const mapFn = (cell: T, parentPosition?: Position): T => {
        const position = parentPosition || cellsFixedPosition[cell.id]

        if (!position) { return cell }

        const { elementAttributes = {}, children } = cell
        const { className = '', style = {} } = elementAttributes
        const offset = getOffset(cell, position)

        return {
            ...cell,
            children: children?.map(cell => mapFn(cell as T, position)),
            elementAttributes: {
                ...elementAttributes,
                className: classNames(
                    className,
                    'sticky-cell',
                    `sticky-${position}`,
                ),
                style: {
                    ...style,
                    [position]: `${position === 'left' ? offset.left : offset.right}px`,
                },
            },
        }
    }

    return cells.map(cell => mapFn(cell))
}

export const useFixedCells = ({
    cells, hasSelection,
}: Props) => {
    const [offsets, setOffsets] = useState<null | Record<string, { left: number, right: number }>>(null)
    const { body, header } = cells
    const groupRef = useRef<HTMLTableColElement>(null)
    const cellsFixedPosition = useMemo(() => {
        const fixed: Record<string, Position> = {}

        const collectChildren = (cells?: HeaderCell[], parent?: Position) => {
            if (cells) {
                cells.forEach((cell) => {
                    const side = parent || cell.fixed

                    if (side) {
                        fixed[cell.id] = side

                        collectChildren(cell.children, side)
                    }
                })
            }
        }

        collectChildren(header)

        return fixed
    }, [header])
    const hasFixedLeft = header.some(cell => cell.fixed === 'left')
    const hasFixed = hasFixedLeft || !isEmpty(cellsFixedPosition)
    const colgroup = useMemo(() => (
        <colgroup ref={groupRef} key="colgroup">
            {hasSelection ? <col /> : null}
            {body.map(cell => (<col data-id={cell.id} />))}
        </colgroup>
    ), [body, hasSelection])
    const groupElement = groupRef.current

    useEffect(() => {
        if (!groupElement || !hasFixed) { return setOffsets(null) }

        const table = groupElement.offsetParent

        if (!table) { return setOffsets(null) }

        const update = () => {
            const cols = groupElement.querySelectorAll('col') ?? []
            const parentWidth = table.clientWidth || 0
            const offsets = Object.fromEntries(Array.from(cols).map((e) => {
                return [e.dataset.id, {
                    left: e.offsetLeft,
                    right: parentWidth - e.offsetLeft - e.clientWidth,
                }]
            }))

            setOffsets(offsets)
        }

        const resizeObserver = new ResizeObserver(update)

        resizeObserver.observe(table)
        update()

        // eslint-disable-next-line consistent-return
        return () => {
            resizeObserver.unobserve(table)
        }
    }, [groupElement, hasFixed])

    return useMemo(() => {
        if (!hasFixed) { return { cells, colgroup: null, hasFixedLeft: false } }
        if (!offsets) { return { cells, colgroup, hasFixedLeft } }

        const fixedCells: typeof cells = {
            body: mapCells(cells.body, cellsFixedPosition, offsets),
            header: mapCells(cells.header, cellsFixedPosition, offsets),
        }

        return { cells: fixedCells, colgroup, hasFixedLeft }
    }, [cells, cellsFixedPosition, colgroup, offsets, hasFixed, hasFixedLeft])
}
