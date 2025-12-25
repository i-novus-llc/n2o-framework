import React, { RefObject, useCallback, useEffect, useLayoutEffect, useMemo, useRef, useState } from 'react'
import isEqual from 'lodash/isEqual'

import { BodyCell, HeaderCell } from '../../../ducks/table/Table'
import { mergeAttributes, mergeAttrToCells } from '../utils/mergeAttributes'
import { getAttributesFromHeader, AttributesMap } from '../utils/getColumnsAttributes'

const getAttr = (
    groupElement: HTMLTableColElement,
    initAttr: AttributesMap,
): AttributesMap => {
    const cols = Array.from(groupElement.querySelectorAll('col') ?? [])

    const nextAttrs = { ...initAttr }

    function collect(list: HTMLElement[], side: 'left' | 'right') {
        let temp = 0

        list.filter(el => (el.dataset.fixed === side)).forEach((el) => {
            const id = el.dataset.id as keyof typeof nextAttrs
            const attr = nextAttrs[id]

            if (attr) { nextAttrs[id] = mergeAttributes(attr, { style: { [side]: temp } }) }

            temp += el.clientWidth
        })
    }

    collect(cols, 'left')
    collect(cols.reverse(), 'right')

    return nextAttrs
}

/**
 * Обновление left/right позиционирования для фиксированных колонок
 */
const useFixed = (
    groupRef: RefObject<HTMLTableColElement>,
    initAttr: AttributesMap,
): AttributesMap => {
    const [attrMap, setAttr] = useState(initAttr)
    const widthRef = useRef(0)
    const initRef = useRef(initAttr)
    const groupElement = groupRef.current

    initRef.current = initAttr

    const update = useCallback(() => {
        const groupElement = groupRef.current

        if (!groupElement) { return }

        setAttr((attrMap) => {
            const nextAttrs = getAttr(groupElement, initRef.current)

            widthRef.current = groupElement.clientWidth || 0

            return (isEqual(attrMap, nextAttrs) ? attrMap : nextAttrs)
        })
    }, [groupRef])

    useLayoutEffect(update, [initAttr])
    useEffect(() => {
        if (!groupElement) { return }

        const resizeObserver = new ResizeObserver(() => {
            const groupWidth = groupElement.clientWidth || 0

            // ignore vertical resize
            if (widthRef.current !== groupWidth) { update() }
        })

        resizeObserver.observe(groupElement)

        // eslint-disable-next-line consistent-return
        return () => {
            resizeObserver.unobserve(groupElement)
        }
    }, [groupElement, update])

    return attrMap
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
    const groupRef = useRef<HTMLTableColElement>(null)

    const [colgroup, hattrMap] = useMemo(() => {
        const attrsMap = getAttributesFromHeader(header)

        const colgroup = (
            <colgroup ref={groupRef} key="colgroup">
                {hasSelection ? <col key="__selection_col__" data-fixed="left" /> : null}
                {Object.entries(attrsMap).map(([id, { fixed, style, className }]) => (
                    <col data-id={id} data-fixed={fixed} key={id} className={className} style={style} />
                ))}
            </colgroup>
        )

        return [colgroup, attrsMap]
    }, [header, hasSelection])

    const attrsMap = useFixed(groupRef, hattrMap)

    const fixedCells = useMemo(() => ({
        body: mergeAttrToCells(cells.body, attrsMap),
        header: mergeAttrToCells(cells.header, attrsMap),
    }), [cells, attrsMap])

    return { cells: fixedCells, colgroup }
}
