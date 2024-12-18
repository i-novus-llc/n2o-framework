import React, { useContext, useMemo } from 'react'
import { Switch, Route, matchPath } from 'react-router-dom'
import { useSelector } from 'react-redux'

import Page from '../../core/Page'
import { PageContext } from '../../core/router/context'
import { resolvePath } from '../../core/router/resolvePath'
import { useLocation } from '../../core/router/useLocation'
import { makePageUrlByIdSelector } from '../../../ducks/pages/selectors'

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
}

export function SubPage({
    routes = [],
    defaultPageId,
}: Props) {
    const location = useLocation()
    const { pageId: currentPageId } = useContext(PageContext)
    const baseUrl = useSelector(makePageUrlByIdSelector(currentPageId))
    const pages = useMemo(() => {
        /**
         * Сортировка элементов в порядке от самого длинного route к самому короткому
         * т.к. react-router рисует первый, а не наиболее подходящий
         */
        const pages = [...routes]
            .sort((a, b) => b.route.length - a.route.length)
            .map(({ route, ...page }) => {
                const { id, url } = page

                return (
                    <Route
                        key={id}
                        path={resolvePath(baseUrl, route)}
                        render={() => (
                            <Page
                                {...page}
                                pageId={id}
                                pageUrl={url}
                                parentId={currentPageId}
                                className="n2o-sub-page-body"
                            />
                        )}
                    />
                )
            })
        const defaultPage = defaultPageId && routes.find(({ id }) => id === defaultPageId)

        if (defaultPage) {
            const { id, url } = defaultPage

            pages.push(<Route
                key="no-match"
                render={() => (
                    <Page
                        {...defaultPage}
                        pageId={id}
                        pageUrl={url}
                        parentId={currentPageId}
                        className="n2o-sub-page-body"
                    />
                )}
            />)
        }

        return pages
    }, [routes, defaultPageId, baseUrl, currentPageId])

    /*
     * скрываем весь регион, если базовый роут страницы не совпадает с адресом
     * т.к. реакция на смену роута происходит синхронно, а дестрой страницы в редаксе асинхронно
     * что приводит к рендеру дефолтного пейджа при переключении на совсем другую страницу и последующий запрос за метаданными
     */
    if (!matchPath(location.pathname, baseUrl)) { return null }

    return <Switch location={location}>{pages}</Switch>
}
