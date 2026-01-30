import { useContext, MouseEvent, useCallback, MouseEventHandler } from 'react'
import { push } from 'connected-react-router'
import { useDispatch, useSelector, useStore } from 'react-redux'

import { getAnchorPage } from '../../../ducks/api/page/getAnchorPage'
import { setLocation } from '../../../ducks/pages/store'
import { type State as GlobalState } from '../../../ducks/State'
import { makePageUrlByIdSelector } from '../../../ducks/pages/selectors'

import { PageContext } from './context'
import { useLocation } from './useLocation'
import { LinkTarget } from './types'
import { resolvePath } from './resolvePath'

export type Props = {
    disabled?: boolean
    target: LinkTarget
    href?: string
    onClick?: MouseEventHandler
}

const getURL = (href: string | undefined, target: LinkTarget, base = '') => {
    if (!href) { return undefined }
    if (href.startsWith('http://') || href.startsWith('https://')) { return href }

    if (target === LinkTarget.self) { return href }
    if (target === LinkTarget.blank) { return href }

    return `./#${resolvePath(base, href)}`
}

export function useLink({
    disabled,
    target,
    href,
    onClick,
}: Props) {
    const { getState } = useStore()
    const dispatch = useDispatch()
    const { pageId } = useContext(PageContext)
    const { pathname } = useLocation()
    const baseUrl = useSelector(makePageUrlByIdSelector(pageId)) || pathname

    const url = disabled ? undefined : getURL(href, target, baseUrl)

    /** Учитывает роутинг sub-pages (прим. страницы в модальных окнах)*/
    const onClickHandler = useCallback((event: MouseEvent) => {
        onClick?.(event)

        if (event.isDefaultPrevented()) { return }

        event.preventDefault()
        event.stopPropagation()

        if (!href || disabled || !url) { return }

        const state: GlobalState = getState()
        const anchorPageId = getAnchorPage(href, state, pageId)

        if (anchorPageId) {
            dispatch(setLocation(pageId, href))

            return
        }

        if (event.ctrlKey) {
            window.open(url, LinkTarget.blank)

            return
        }

        dispatch(push(url.replace(/^\.\/#/, '')))
    }, [disabled, dispatch, href, pageId, getState, url, onClick])

    return {
        active: href ? pathname.includes(href) : false,
        target: target === LinkTarget.blank ? target : undefined,
        url,
        onClick: target === LinkTarget.application ? onClickHandler : onClick,
    }
}
