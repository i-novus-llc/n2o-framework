import React from 'react'
import classNames from 'classnames'

import { propTypes } from './propTypes'

/**
 * Компонент с вёрсткой базового шаблона страницы с шапкой на всю ширину экрана
 * @param [header] Шапка страницы
 * @param content Контент страницы
 * @param {boolean} fixed Фиксированная ли навигация
 * @param [sidebar] Боковое меню
 * @param {'left'|'right'} side Расположение бокового меню
 * @param [footer] Подвал страницы
 */
export function Layout({
    header,
    side,
    children: content,
    fixed,
    sidebar,
    footer,
}) {
    return (
        <>
            {header}
            <div className={classNames(
                'w-100 d-flex overflow-auto flex-grow-1',
                {
                    'flex-row': side === 'left',
                    'flex-row-reverse': side === 'right',
                },
            )}
            >
                {sidebar}
                <div className={classNames('w-100 d-flex flex-column', { 'application-body-container-fixed': fixed })}>
                    <div className="application-body container-fluid">{content}</div>
                </div>
            </div>
            {footer}
        </>
    )
}

Layout.propTypes = propTypes
