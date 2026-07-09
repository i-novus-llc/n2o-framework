import { N2OLinkTarget } from '../types'

import { resolvePath } from './resolvePath'
import { isURL } from './isURL'

export const prepareLink = (href: string, target: N2OLinkTarget, base = '/'): { href: string, path?: string } => {
    if (isURL(href)) { return { href } }
    if (target === N2OLinkTarget.self) { return { href } }

    const path = resolvePath(base, href)

    return { href: `./#${path}`, path }
}
