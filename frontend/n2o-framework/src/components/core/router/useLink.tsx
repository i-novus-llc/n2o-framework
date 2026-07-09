import { useContext, MouseEvent, useCallback, MouseEventHandler } from 'react'
import { push } from 'connected-react-router'
import { useDispatch, useSelector, useStore } from 'react-redux'

import { getAnchorPage } from '../../../ducks/api/page/getAnchorPage'
import { setLocation } from '../../../ducks/pages/store'
import { type State as GlobalState } from '../../../ducks/State'
import { makePageUrlByIdSelector } from '../../../ducks/pages/selectors'

import { PageContext } from './context'
import { useLocation } from './useLocation'
import { N2OLinkTarget } from './types'
import { prepareLink } from './utils/prepareLink'

export type Props = {
    disabled?: boolean
    target: N2OLinkTarget
    href?: string
    newWindow?: boolean
    onClick?: MouseEventHandler
}

export function useLink({
    disabled,
    target,
    href: link,
    newWindow,
    onClick,
}: Props) {
    const { getState } = useStore()
    const dispatch = useDispatch()
    const { pageId } = useContext(PageContext)
    const { pathname } = useLocation()
    const pageUrl = useSelector(makePageUrlByIdSelector(pageId))
    const baseUrl = pageUrl || pathname

    const { href, path } = disabled || !link
        ? { href: undefined, path: undefined }
        : prepareLink(link, target, baseUrl)

    /** Учитывает роутинг sub-pages (прим. страницы в модальных окнах)*/
    const onClickHandler = useCallback((event: MouseEvent) => {
        if (!href) { return }

        onClick?.(event)

        if (event.isDefaultPrevented()) { return }

        event.preventDefault()

        // Для sub-page внутри модальных окон игнорируем newWindow
        if (path) {
            const state: GlobalState = getState()
            const anchorPageId = getAnchorPage(path, state, pageId)

            if (anchorPageId) {
                dispatch(setLocation(pageId, path))

                return
            }
        }

        if (event.ctrlKey || newWindow) {
            window.open(href, '_blank' as const)

            return
        }

        if (!path) {
            // eslint-disable-next-line no-restricted-globals
            location.href = href

            return
        }

        dispatch(push(path))
    }, [dispatch, href, path, pageId, getState, onClick, newWindow])

    const normalize = (path: string) => path.replace(/\/$/, '')

    const normPath = normalize(path || '')
    const normPathname = normalize(pathname)

    const active = normPath && (normPathname === normPath || normPathname.startsWith(`${normPath}/`))

    return {
        active,
        target: newWindow ? '_blank' as const : undefined,
        url: href,
        onClick: onClickHandler,
    }
}
