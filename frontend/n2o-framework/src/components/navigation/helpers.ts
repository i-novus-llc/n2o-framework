import { type Content } from './types'

export function hasVisibleContent(content: Content): boolean {
    return content.some(({ visible = true, content: nested }) => {
        if (!visible) { return false }

        if (nested) { return hasVisibleContent(nested) }

        return true
    })
}
