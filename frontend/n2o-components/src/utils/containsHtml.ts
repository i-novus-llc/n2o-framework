export function containsHtml(str?: string | null | Node): boolean {
    if (typeof str !== 'string' || str.trim() === '') {
        return false
    }

    if (!str.includes('<')) {
        return false
    }

    try {
        const parser = new DOMParser()
        const doc = parser.parseFromString(str, 'text/html')

        const childNodes = Array.from(doc.body.childNodes)

        const significantNodes = childNodes.filter((node) => {
            if (node.nodeType === Node.ELEMENT_NODE) {
                return true
            }
            if (node.nodeType === Node.TEXT_NODE) {
                return node.textContent && node.textContent.trim() !== ''
            }

            return false
        })

        return significantNodes.length > 0
    } catch (e) {
        console.warn('Error parsing HTML string:', e)

        return false
    }
}
