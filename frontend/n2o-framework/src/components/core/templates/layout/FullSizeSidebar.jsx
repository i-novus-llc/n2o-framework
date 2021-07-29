import React from 'react'
import classNames from 'classnames'

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
    return (
        <>
            {sidebar}
            <div className={classNames('w-100 d-flex flex-column', { 'vh-100': fixed })}>
                {header}
                <div className={classNames({ 'd-flex w-100 flex-grow-1': sidebar, 'application-body-container-fixed': fixed })}>
                    <div className="application-body container-fluid">{content}</div>
                </div>
                {footer}
            </div>
        </>
    )
}

Layout.propTypes = propTypes
