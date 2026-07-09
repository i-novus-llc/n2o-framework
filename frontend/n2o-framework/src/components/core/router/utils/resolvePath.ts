import { getParentPath } from './getParentPath'
import { normalize } from './normalize'
import { isURL } from './isURL'

export function resolvePath(base: string, path: string) {
    if (isURL(path)) { return path }
    if (path.startsWith('/')) { return normalize(path) }

    return normalize(`${getParentPath(base)}${path}`)
}
