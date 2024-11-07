import React from 'react'
import classNames from 'classnames'

import { ScrollContainer } from '../../../snippets/ScrollContainer/ScrollContainer'

import { LayoutProps } from './types'

/**
 * Компонент с вёрсткой базового шаблона страницы с шапкой на всю ширину экрана
 * @param header Шапка страницы
 * @param content Контент страницы
 * @param fixed Фиксированная ли навигация
 * @param sidebar Боковое меню
 * @param side Расположение бокового меню
 * @param footer Подвал страницы
 */

export function Layout({
    className,
    header,
    side,
    children,
    fixed,
    sidebar,
    footer,
}: LayoutProps) {
    const LayoutElement = fixed ? 'div' : ScrollContainer
    const ContainerElement = fixed ? ScrollContainer : 'div'

    return (
        <LayoutElement className={className}>
            {header}
            <div className={classNames('w-100 d-flex flex-grow-1', {
                'flex-row': side === 'left',
                'flex-row-reverse': side === 'right',
                'overflow-auto': fixed,
            })}
            >
                {sidebar}
                <ContainerElement className="flex-grow-1 application-body container-fluid">
                    {children}
                </ContainerElement>
            </div>
            {footer}
        </LayoutElement>
    )
}
