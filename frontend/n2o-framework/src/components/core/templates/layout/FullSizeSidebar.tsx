import React from 'react'
import classNames from 'classnames'
import { ScrollContainer } from '@i-novus/n2o-components/lib/layouts/ScrollContainer'

import { LayoutProps } from './types'

/**
 * Компонент с вёрсткой базового шаблона страницы боковой панелью на всю высоту экрана
 * @param header Шапка страницы
 * @param content Контент страницы
 * @param fixed Фиксированная ли навигация
 * @param sidebar Боковое меню
 * @param side Расположение бокового меню
 * @param footer Подвал страницы
 */
export function Layout({
    className,
    sidebar,
    fixed,
    header,
    children: content,
    footer,
}: LayoutProps) {
    const LayoutElement = fixed ? 'div' : ScrollContainer
    const ContainerElement = fixed ? ScrollContainer : 'div'

    return (
        <div className={className}>
            {sidebar}
            <LayoutElement className={classNames(' wq2s w-100 d-flex flex-column vh-100')}>
                {header}
                <ContainerElement className="application-body container-fluid">{content}</ContainerElement>
                {footer}
            </LayoutElement>
        </div>
    )
}
