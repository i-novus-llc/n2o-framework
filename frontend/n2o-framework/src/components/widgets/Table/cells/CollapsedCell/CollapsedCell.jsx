import React, { useCallback, useMemo, useState } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import isString from 'lodash/isString'
import get from 'lodash/get'

import withTooltip from '../../withTooltip'
import { EMPTY_ARRAY } from '../../../../../utils/emptyTypes'
import propsResolver from '../../../../../utils/propsResolver'

/**
 * CollapsedCell
 * @reactProps {object} model - модель
 * @reactProps {string} fieldKey - поле данных
 * @reactProps {string} color - цвет элементов
 * @reactProps {string} amountToGroup - количество элементов для группировки
 */

function CollapsedCell(props) {
    const [isCollapsed, setIsCollapsed] = useState(true)

    const {
        model,
        fieldKey,
        color,
        amountToGroup,
        labelFieldId,
        content,
        separator = null,
        forwardedRef,
        visible,
        inline = true,
    } = props

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
            className={classNames(
                'collapse-cell-content',
                {
                    'collapse-cell-content--inline': inline,
                },
            )}
        >
            {items.map((item, index, self) => (
                <div className="collapse-cell-data" key={String(index)}>
                    {renderCell(item)}
                    {separatorAsHtml && (index !== self.length - 1) && (
                        /* eslint-disable-next-line react/no-danger */
                        <span className="collapse-cell-data__separator" dangerouslySetInnerHTML={separatorAsHtml} />
                    )}
                </div>
            ))}

            {isButtonNeeded ? (
                <button
                    type="button"
                    onClick={toggleIsCollapsed}
                    className="collapsed-cell-control link-button"
                >
                    {isCollapsed ? 'еще' : 'скрыть'}
                </button>
            ) : null}
        </div>
    )
}

CollapsedCell.propTypes = {
    /**
   * Модель даных
   */
    model: PropTypes.object.isRequired,
    /**
   * Ключ значения из модели
   */
    fieldKey: PropTypes.string.isRequired,
    /**
   * Цвет
   */
    color: PropTypes.string,
    /**
   * Количество элементов для группировки
   */
    amountToGroup: PropTypes.number,
    /**
   * Ключ label из модели
   */
    labelFieldId: PropTypes.string,
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
}

CollapsedCell.defaultProps = {
    amountToGroup: 3,
    color: 'secondary',
    visible: true,
}

CollapsedCell.displayName = 'CollapsedCell'

export default withTooltip(CollapsedCell)
