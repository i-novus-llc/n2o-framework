import { useContext, useEffect } from 'react'
import { useDispatch, useStore } from 'react-redux'
import { replace } from 'connected-react-router'

import { getAnchorPage } from '../../../ducks/api/page/getAnchorPage'
import { setLocation } from '../../../ducks/pages/store'

import { PageContext } from './context'

export type Props = {
    to: string
}

export function Redirect({ to }: Props) {
    const { getState } = useStore()

    const dispatch = useDispatch()

    const { pageId } = useContext(PageContext)

    useEffect(() => {
        const anchorPageId = getAnchorPage(to, getState(), pageId)

        if (anchorPageId) {
            dispatch(setLocation(pageId, to))

            return
        }

        dispatch(replace(to))
    }, [])

    return null
}
