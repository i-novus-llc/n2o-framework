interface IContentItem {
    id: string,
    group?: contentType
}

type contentType = IContentItem[]

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
