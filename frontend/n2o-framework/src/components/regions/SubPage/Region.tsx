import React, { CSSProperties, useContext, useMemo } from 'react'
import { Switch, Route, matchPath } from 'react-router-dom'
import { useSelector } from 'react-redux'
import classNames from 'classnames'

import { Page } from '../../core/Page'
import { PageContext } from '../../core/router/context'
import { resolvePath } from '../../core/router/resolvePath'
import { useLocation } from '../../core/router/useLocation'
import { Redirect } from '../../core/router/Redirect'
import { makePageUrlByIdSelector } from '../../../ducks/pages/selectors'
import { EMPTY_ARRAY } from '../../../utils/emptyTypes'

/**
 * Регион, отображающий page по маске
 * RouteProps
 * @reactProps {string} id - Уникальный id страницы
 * @reactProps {string} route - Адрес или маска, по которой следует отображать страницу
 * @reactProps {string} url - Адресс для получения metadata
 */

interface RouteProps {
    id: string
    route: string
    url: string
    queryMapping?: {}
    pathMapping?: {}
}

export type Props = {
    routes: RouteProps[]
    defaultPageId?: string
    className?: string
    style?: CSSProperties
}

export function SubPage({
    className,
    defaultPageId,
    routes = EMPTY_ARRAY,
    style,
}: Props) {
    const location = useLocation()
    const { pageId: currentPageId } = useContext(PageContext)
    const baseUrl = useSelector(makePageUrlByIdSelector(currentPageId))
    const defaultPage = defaultPageId && routes.find(({ id }) => id === defaultPageId)

    const pages = useMemo(() => {
        /**
         * Сортировка элементов в порядке от самого длинного route к самому короткому
         * т.к. react-router рисует первый, а не наиболее подходящий
         */
        return [...routes]
            .sort((a, b) => b.route.length - a.route.length)
            .map(({ route, ...page }) => {
                const { id, url } = page
                const path = resolvePath(baseUrl, route)

                return (
                    <Route
                        key={id}
                        path={path}
                        render={() => (
                            <Page
                                {...page}
                                pageId={id}
                                pageUrl={url}
                                parentId={currentPageId}
                                baseUrl={path}
                                className="n2o-sub-page-body"
                            />
                        )}
                    />
                )
            })
    }, [routes, baseUrl, currentPageId])

    /*
     * скрываем весь регион, если базовый роут страницы не совпадает с адресом
     * т.к. реакция на смену роута происходит синхронно, а дестрой страницы в редаксе асинхронно
     * что приводит к рендеру дефолтного пейджа при переключении на совсем другую страницу и последующий запрос за метаданными
     */
    if (!matchPath(location.pathname, baseUrl)) { return null }

    return (
        <section className={classNames('n2o-subpage', className)} style={style}>
            <Switch location={location}>
                {pages}
                {
                    defaultPage
                        ? (
                            <Route
                                key="no-match"
                                render={() => (<Redirect to={resolvePath(baseUrl, defaultPage.route)} />)}
                            />
                        )
                        : null
                }
            </Switch>
        </section>
    )
}

SubPage.displayName = 'n2o/regions/SubPage'
