import { logger } from '../../../../utils/logger'

export function getParentPath(path: string) {
    if (!path || path === '/') { return '/' }

    let normalizedPath = path

    if (!normalizedPath.startsWith('/')) {
        logger.warn(`Base path should start with '/'. Got '${path}'`)
        normalizedPath = `/${normalizedPath}`
    }

    return normalizedPath.slice(0, normalizedPath.lastIndexOf('/') + 1)
}
