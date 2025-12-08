import { State as GlobalState } from '../../State'
import { makePageByIdSelector } from '../../pages/selectors'
import { resolvePath } from '../../../components/core/router/resolvePath'

export function getAnchorPage(url: string, state: GlobalState, pageId?: string): string | null {
    if (!pageId) { return null }

    const page = makePageByIdSelector(pageId)(state)

    if (!page || page.rootPage) { return null }
    if (page.parentId) { return getAnchorPage(url, state, page.parentId) }

    const isAnchor = url.startsWith(page.pageUrl) &&
        page.metadata?.routes?.subRoutes?.some(route => url.startsWith(
            resolvePath(page.pageUrl, route),
        ))

    return isAnchor ? pageId : null
}
