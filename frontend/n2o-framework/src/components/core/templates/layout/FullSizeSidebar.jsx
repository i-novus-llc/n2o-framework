import React from 'react'
import classNames from 'classnames'

import { ScrollContainer } from '../../../snippets/ScrollContainer/ScrollContainer'

import { propTypes } from './propTypes'

/**
 * Компонент с вёрсткой базового шаблона страницы боковой панелью на всю высоту экрана
 * @param [header] Шапка страницы
 * @param content Контент страницы
 * @param {boolean} fixed Фиксированная ли навигация
 * @param [sidebar] Боковое меню
 * @param {'left'|'right'} side Расположение бокового меню
 * @param [footer] Подвал страницы
 */
export function Layout({
    sidebar,
    fixed,
    header,
    children: content,
    footer,
}) {
    const LayoutElement = fixed ? 'div' : ScrollContainer
    const ContainerElement = fixed ? ScrollContainer : 'div'

    return (
        <div className="d-flex">
            {sidebar}
            <LayoutElement className={classNames(' wq2s w-100 d-flex flex-column vh-100')}>
                {header}
                <ContainerElement className="application-body container-fluid">{content}</ContainerElement>
                {footer}
            </LayoutElement>
        </div>
    )
}

Layout.propTypes = propTypes
