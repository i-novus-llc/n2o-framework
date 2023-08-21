import { FETCH_TYPE } from '../../core/widget/const'

interface ContentItem {
    id: string,
    group?: contentType
}

type contentType = ContentItem[]

export function getFirstContentId(content: contentType = []): string | void {
    if (!content.length) {
        return undefined
    }

    const [first] = content

    const { group } = first

    if (group) {
        return getFirstContentId(group)
    }

    const { id } = first

    return id
}

export function getFetchOnInit(metaFetchOnInit: boolean, lazy: boolean, active: boolean) {
    if (!lazy || active) {
        return metaFetchOnInit
    }

    return false
}

export function getFetch(lazy: boolean, active: boolean) {
    if (!lazy) {
        return FETCH_TYPE.always
    }

    if (active) {
        return FETCH_TYPE.lazy
    }

    return FETCH_TYPE.never
}
