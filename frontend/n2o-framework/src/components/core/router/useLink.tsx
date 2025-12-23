import { useContext, MouseEvent, useCallback } from 'react'
import { push } from 'connected-react-router'
import { useDispatch, useStore } from 'react-redux'

import { getAnchorPage } from '../../../ducks/api/page/getAnchorPage'
import { setLocation } from '../../../ducks/pages/store'
import { type State as GlobalState } from '../../../ducks/State'

import { PageContext } from './context'
import { useLocation } from './useLocation'
import { LinkTarget } from './types'
import { resolvePath } from './resolvePath'

export type Props = {
    disabled?: boolean
    target: LinkTarget
    href?: string
}

const getURL = (href: string | undefined, target: LinkTarget) => {
    if (!href) { return undefined }
    if (href.startsWith('http://') || href.startsWith('https://')) { return href }

    if (target === LinkTarget.self) { return href }
    if (target === LinkTarget.blank) { return href }

    return `./#${resolvePath('', href)}`
}

export function useLink({
    disabled,
    target,
    href,
}: Props) {
    const { getState } = useStore()

    const dispatch = useDispatch()

    const { pageId } = useContext(PageContext)

    const { pathname } = useLocation()

    const url = disabled ? undefined : getURL(href, target)

    /** Учитывает роутинг sub-pages (прим. страницы в модальных окнах)*/
    const onClick = useCallback((event: MouseEvent) => {
        event.preventDefault()
        event.stopPropagation()

        if (!href || disabled) { return }

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

        dispatch(push(href))
    }, [disabled, dispatch, href, pageId, getState, url])

    return {
        active: href ? pathname.includes(href) : false,
        target: target === LinkTarget.blank ? target : undefined,
        url,
        onClick: target === LinkTarget.application ? onClick : undefined,
    }
}
