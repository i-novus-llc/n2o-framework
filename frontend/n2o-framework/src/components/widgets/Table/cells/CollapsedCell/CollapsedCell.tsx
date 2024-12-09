import React, { useCallback, useMemo, useState } from 'react'
import classNames from 'classnames'
import isString from 'lodash/isString'
import get from 'lodash/get'

import withTooltip from '../../withTooltip'
import { EMPTY_ARRAY } from '../../../../../utils/emptyTypes'
import propsResolver from '../../../../../utils/propsResolver'

import { type Props } from './types'

/**
 * CollapsedCell
 * @reactProps {object} model - модель
 * @reactProps {string} fieldKey - поле данных
 * @reactProps {string} color - цвет элементов
 * @reactProps {string} amountToGroup - количество элементов для группировки
 */

function CollapsedCellBody({
    model,
    fieldKey,
    labelFieldId,
    content,
    forwardedRef,
    color = 'secondary',
    amountToGroup = 3,
    visible = true,
    inline = true,
    separator = null,
}: Props) {
    const [isCollapsed, setIsCollapsed] = useState(true)

    const separatorAsHtml = useMemo(() => {
        if (separator) {
            return {
                __html: separator
                    .replaceAll(/(<([^>]+)>)/gi, '')
                    .replaceAll(/\r\n|\r|\n/g, '<br />')
                    .replaceAll(/ /g, '&nbsp;'),
            }
        }

        return null
    }, [separator])

    const toggleIsCollapsed = useCallback((event) => {
        event.stopPropagation()
        event.preventDefault()

        setIsCollapsed(state => !state)
    }, [])

    const { isButtonNeeded, items } = useMemo(() => {
        const data = model[fieldKey] || EMPTY_ARRAY

        return ({
            items: isCollapsed ? data.slice(0, amountToGroup) : data,
            isButtonNeeded: data.length > amountToGroup,
        })
    }, [fieldKey, model, amountToGroup, isCollapsed])

    const renderCell = useCallback((data) => {
        if (content) {
            const { component: Component, ...componentProps } = content
            const resolvedProps = propsResolver(componentProps, data)

            return (
                <Component {...resolvedProps} model={data}>
                    {isString(data) ? data : get(data, labelFieldId)}
                </Component>
            )
        }

        return (
            <span className={classNames('badge', `badge-${color}`)}>
                {isString(data) ? data : get(data, labelFieldId)}
            </span>
        )
    }, [content, color, labelFieldId])

    if (!visible) { return null }

    return (
        <div
            ref={forwardedRef}
            className={classNames('collapse-cell-content', { 'collapse-cell-content--inline': inline })}
        >
            {items.map((item, index, self) => (
                // eslint-disable-next-line react/no-array-index-key
                <div className="collapse-cell-data" key={String(index)}>
                    {renderCell(item)}
                    {separatorAsHtml && (index !== self.length - 1) && (
                        /* eslint-disable-next-line react/no-danger */
                        <span className="collapse-cell-data__separator" dangerouslySetInnerHTML={separatorAsHtml} />
                    )}
                </div>
            ))}

            {isButtonNeeded && (
                <button
                    type="button"
                    onClick={toggleIsCollapsed}
                    className="collapsed-cell-control link-button"
                >
                    {isCollapsed ? 'еще' : 'скрыть'}
                </button>
            )}
        </div>
    )
}

const CollapsedCell = withTooltip(CollapsedCellBody)

CollapsedCell.displayName = 'CollapsedCell'

export { CollapsedCell }
export default CollapsedCell
