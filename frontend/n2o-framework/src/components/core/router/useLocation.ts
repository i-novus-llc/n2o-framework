import { useContext } from 'react'
import { useLocation as useRouterLocation } from 'react-router-dom'
import { useSelector } from 'react-redux'

import { makeAnchorLocationByIdSelector } from '../../../ducks/pages/selectors'

import { PageContext } from './context'

export function useLocation() {
    const { pageId } = useContext(PageContext)
    const location = useSelector(makeAnchorLocationByIdSelector(pageId))
    const routerLocation = useRouterLocation()

    return location || routerLocation
}
