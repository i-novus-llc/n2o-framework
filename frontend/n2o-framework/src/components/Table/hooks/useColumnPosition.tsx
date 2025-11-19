import React, { RefObject, useEffect, useMemo, useRef, useState } from 'react'
import isEqual from 'lodash/isEqual'

import { BodyCell, HeaderCell } from '../../../ducks/table/Table'
import { mergeAttributes, mergeAttrToCells } from '../utils/mergeAttributes'
import { getAttributesFromHeader, AttributesMap } from '../utils/getColumnsAttributes'

/**
 * Обновление left/right позиционирования для фиксированных колонок
 */
const useFixed = <T extends AttributesMap>(
    groupRef: RefObject<HTMLTableColElement>,
    hasFixed: boolean,
    setAttrsMap: (arg: T | ((prev: T) => T)) => void,
) => {
    const groupElement = groupRef.current

    useEffect(() => {
        if (!groupElement || !hasFixed) { return }

        const update = () => {
            setAttrsMap((attrMap) => {
                const cols = groupElement.querySelectorAll('col') ?? []
                const groupWidth = groupElement.clientWidth || 0
                const nextAttrs = { ...attrMap }

                Array.from(cols).forEach((e) => {
                    const id = e.dataset.id as keyof typeof nextAttrs
                    const fixed = e.dataset.fixed as ('left' | 'right' | undefined)
                    const attr = nextAttrs[id]

                    if (!attr || !fixed) { return }

                    nextAttrs[id] = mergeAttributes(attr, { style: {
                        [fixed]: fixed === 'left'
                            ? e.offsetLeft
                            : groupWidth - e.offsetLeft - e.clientWidth,
                    } })
                })

                return isEqual(attrMap, nextAttrs) ? attrMap : nextAttrs
            })
        }

        const resizeObserver = new ResizeObserver(update)

        resizeObserver.observe(groupElement)
        update()

        // eslint-disable-next-line consistent-return
        return () => {
            resizeObserver.unobserve(groupElement)
        }
    }, [groupElement, hasFixed, setAttrsMap])
}

export type Props = {
    cells: {
        body: BodyCell[]
        header: HeaderCell[]
    }
    hasSelection: boolean
}

/**
 * Хук для прокидывания в колонки стилей, отвечающие за позиционирование и размеры, заданные в заголовках таблицы
 * Нужен т.к. табличная вёрстка не умеет нормально в ширины ячеек, заданных только в заголовках или colgroup
 * так же для position:sticky на ячейках колонки надо проставлять стиль в каждую отдельную ячейку
 */
export const useColumnPosition = ({ cells, hasSelection }: Props) => {
    const { header } = cells
    const [attrsMap, setAttrsMap] = useState(() => getAttributesFromHeader(header))
    const groupRef = useRef<HTMLTableColElement>(null)
    const colgroup = useMemo(() => (
        <colgroup ref={groupRef} key="colgroup">
            {hasSelection ? <col /> : null}
            {Object.entries(attrsMap).map(([id, { fixed }]) => (<col data-id={id} data-fixed={fixed} key={id} />))}
        </colgroup>
    ), [attrsMap, hasSelection])

    useFixed(
        groupRef,
        header.some(cell => cell.fixed),
        setAttrsMap,
    )

    useEffect(() => {
        const nextAttrs = getAttributesFromHeader(header)

        setAttrsMap(attrsMap => (isEqual(attrsMap, nextAttrs) ? attrsMap : nextAttrs))
    }, [header])

    return useMemo(() => {
        const fixedCells: typeof cells = {
            body: mergeAttrToCells(cells.body, attrsMap),
            header: mergeAttrToCells(cells.header, attrsMap),
        }

        return { cells: fixedCells, colgroup }
    }, [cells, attrsMap, colgroup])
}
