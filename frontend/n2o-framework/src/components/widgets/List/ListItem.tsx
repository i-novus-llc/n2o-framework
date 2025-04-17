import React, { useEffect, isValidElement } from 'react'
import classNames from 'classnames'

import { NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { HeaderWrapper } from './HeaderWrapper'
import { type ListItemProps } from './types'

/**
 * Компонент ListItem виджета ListWidget
 * @param leftTop - секция картинки
 * @param leftBottom - секция картинки
 * @param header - секция заголовка
 * @param subHeader - секция подзаголовка
 * @param body - секция тела
 * @param rightTop - секция справа сверху
 * @param rightBottom - секция справа снизу
 * @param extra - extra секция
 * @param selected - флаг активности строки
 * @param onClick - callback на клик по строке
 * @param divider - разделить между строками
 * @param style - стили
 * @param hasSelect
 * @param measure
 * @constructor
 */

export function ListItem({
    leftTop,
    leftBottom,
    header,
    subHeader,
    body,
    rightTop,
    rightBottom,
    extra,
    selected,
    onClick,
    divider,
    style,
    hasSelect,
    measure = NOOP_FUNCTION,
}: ListItemProps) {
    useEffect(() => {
        /* FIXME временный костыль до рефакторинга List widget.
            measure пересчитывает выстоту прокидывая style через CellMeasurer,
            style не может быть dependency useEffect уйдет в беск. цикл.
            Использование height не приносит результата, пересчет прерывается.
            Решение 1 рачсет с delay. При медленном рендере может несработать */
        setTimeout(() => measure(), 400)
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const renderImage = (image: ListItemProps['leftTop']) => {
        if (isValidElement(image)) { return image }

        return <img {...image} src={image?.src} alt={image?.alt || ''} />
    }

    return (
        <div
            onClick={onClick}
            style={style}
            className={classNames('n2o-widget-list-item', {
                'n2o-widget-list-item--active': hasSelect && selected,
                'n2o-widget-list-item--divider': divider,
            })}
        >
            <div className="n2o-widget-list-item-left-container">
                {leftTop && <div className="n2o-widget-list-item-left-top">{renderImage(leftTop)}</div>}
                {leftBottom && <div className="n2o-widget-list-item-left-bottom">{leftBottom}</div>}
            </div>
            <div className="n2o-widget-list-item-main-container">
                <div className="n2o-widget-list-item-header-row">
                    <HeaderWrapper isValid={isValidElement(header) || isValidElement(subHeader)}>
                        {header && <div className="n2o-widget-list-item-header">{header}</div>}
                        {subHeader && <div className="n2o-widget-list-item-subheader text-muted">{subHeader}</div>}
                    </HeaderWrapper>
                </div>
                {body && <div className="n2o-widget-list-item-body">{body}</div>}
            </div>
            <div className="n2o-widget-list-item-right-container">
                {rightTop && <div className="n2o-widget-list-item-right-top">{rightTop}</div>}
                {rightBottom && <div className="n2o-widget-list-item-right-bottom">{rightBottom}</div>}
            </div>
            <div className="n2o-widget-list-item-extra-container">
                {extra && <div className="n2o-widget-list-item-extra">{extra}</div>}
            </div>
        </div>
    )
}

export default ListItem
