import React, { useContext, MouseEvent } from 'react'
import get from 'lodash/get'
import { Link } from '@i-novus/n2o-components/lib/navigation/Link'
import { ROOT_CLASS_NAME_PARAM } from '@i-novus/n2o-components/lib/navigation/helpers'
import classNames from 'classnames'
import { push } from 'connected-react-router'
import { useDispatch, useStore } from 'react-redux'
import { LinkTarget } from '@i-novus/n2o-components/lib/navigation/types'

import { useLink } from '../../core/router/useLink'
import { type Model } from '../../ducks/models/selectors'
import { getAnchorPage } from '../../ducks/api/page/getAnchorPage'
import { setLocation } from '../../ducks/pages/store'
import { type State as GlobalState } from '../../ducks/State'
import { PageContext } from '../core/router/context'
import { useLocation } from '../core/router/useLocation'

import { type NavigationLinkProps } from './types'

export function NavigationLink({
    model: modelPrefix,
    models,
    url: propsUrl,
    visible: propsVisible = true,
    enabled: propsEnabled = true,
    pathMapping,
    queryMapping,
    [ROOT_CLASS_NAME_PARAM]: rootClassName,
    className,
    target,
    ...rest
}: NavigationLinkProps) {
    const { getState } = useStore()
    const state: GlobalState = getState()

    const dispatch = useDispatch()

    const { pageId } = useContext(PageContext)

    const { pathname } = useLocation()
    const model = get(models, modelPrefix, {}) as Model

    const { url, visible, disabled } = useLink({
        url: propsUrl,
        pathMapping,
        queryMapping,
        model,
        visible: propsVisible,
        enabled: propsEnabled,
    })

    /** Учитывает роутинг sub-pages (прим. страницы в модальных окнах)*/
    const handleClick = (e: MouseEvent<HTMLElement>) => {
        e.preventDefault()

        if (!url) { return }

        const anchorPageId = getAnchorPage(url, state, pageId)

        if (anchorPageId) {
            dispatch(setLocation(pageId, url))

            return
        }

        if (e.ctrlKey) {
            window.open(url, LinkTarget.BLANK)

            return
        }

        dispatch(push(url))
    }

    if (!visible) { return null }

    return (
        <Link
            onClick={target === LinkTarget.APPLICATION ? handleClick : undefined}
            className={classNames(
                className,
                rootClassName,
                { active: url ? pathname.includes(url) : false },
            )}
            disabled={disabled}
            target={target}
            url={url}
            {...rest}
        />
    )
}
